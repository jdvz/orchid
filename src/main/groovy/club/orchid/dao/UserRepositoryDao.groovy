package club.orchid.dao

import club.orchid.domain.auth.Role
import club.orchid.domain.auth.User
import club.orchid.domain.auth.UserRole
import club.orchid.util.AuthenticationUtils
import club.orchid.util.QueryUtils
import club.orchid.web.forms.UserObject
import org.apache.log4j.Logger
import org.codehaus.groovy.runtime.InvokerHelper
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 2/14/16 6:23 PM
 */
@Repository('userRepository')
class UserRepositoryDao extends PersistentRepositoryDao<User> implements UserRepository {
    private static final Logger log = Logger.getLogger(UserRepositoryDao.class.name)

    private static final String USER_REQUEST = """select user.id, user.first_name, user.last_name, user.password, user.email,
    user.account_non_expired, user.account_non_locked, user.credentials_non_expired, user.enabled,
    group_concat(concat('ROLE_', upper(role.role_name))) roles
    from users user
    left join user_roles userrole on (userrole.user_id = user.id)
    left join roles role on (userrole.role_id = role.id)"""

    @Override
    Optional<User> get(long id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject("""${USER_REQUEST}
    where user.id = :id and user.enabled = :enabled
    group by user.id""",
                    [id: id, enabled: true], mapperHolder.getOrCreateMapper(User.class)))
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty()
        }
    }

    Optional<User> get(Map<String, Object> params) {
        params += [enabled: true]
        try {
            return Optional.of(jdbcTemplate.queryForObject("""${USER_REQUEST}
    where ${QueryUtils.createWhereString(params)}
    group by user.id""",
                    params, mapperHolder.getOrCreateMapper(User.class)))
        } catch (EmptyResultDataAccessException e) {
            Optional.empty()
        }
    }

    @Override
    Collection<User> list() {
        return jdbcTemplate.query(
"""${USER_REQUEST}
group by user.id""", mapperHolder.getOrCreateMapper(User))
    }

    @Override
    List<Role> roles() {
        return jdbcTemplate.query('''select r.id, concat('ROLE_', UPPER(r.role_name)) role_name from roles r''', mapperHolder.getOrCreateMapper(Role.class))
    }

    @Override
    User save(User user, UserObject userObject) {
        def properties = [
                id: userObject.userId, first_name: userObject.firstName, last_name: userObject.lastName, email: userObject.email,
                account_non_expired: userObject.accountNonExpired, account_non_locked: userObject.accountNonLocked,
                credentials_non_expired: userObject.accountNonExpired, enabled: userObject.enabled
        ]
        if (userObject.password) {
            properties.password = userObject.password
        }
        if (user.isPersistent()) {
            jdbcTemplate.update(
"""UPDATE users SET first_name = :first_name, last_name = :last_name, email = :email, ${userObject.password ? 'password = :password,' : ''}
account_non_expired = :account_non_expired, account_non_locked = :account_non_locked, credentials_non_expired = :credentials_non_expired, enabled = :enabled
WHERE id = :id""", properties)
        } else {
            final KeyHolder keyHolder = new GeneratedKeyHolder()
            jdbcTemplate.update(
                    '''INSERT INTO users(first_name, last_name, email, password, account_non_expired, account_non_locked, credentials_non_expired, enabled)
VALUES (:first_name, :last_name, :email, :password, :account_non_expired, :account_non_locked, :credentials_non_expired, :enabled)''',
                    new MapSqlParameterSource(properties), keyHolder)
            user.id = keyHolder.key?.longValue()
        }
        use(InvokerHelper) {
            user.setProperties(userObject.properties)
        }
        List<Role> allRoles = jdbcTemplate.query('''SELECT role.id, concat('ROLE_', UPPER(role.role_name)) role_name FROM roles role''',
                [user_id: user.id], mapperHolder.getOrCreateMapper(Role))
        List<Long> userRoles = jdbcTemplate.queryForList('''SELECT role_id FROM user_roles WHERE user_id = :userId''', [userId: userObject.userId], Long.class)
        allRoles.each { Role role ->
            if (userObject.allRoles.containsKey(role)) {
                if (!userRoles.remove(role.id)) {
                    jdbcTemplate.update('''INSERT INTO user_roles(user_id, role_id) VALUES (:user_id, :role_id)''', [user_id: user.id, role_id: role.id])
                    user.clearAuthorization = true
                }
            } else if (userRoles.remove(role.id)) {
                jdbcTemplate.update('''DELETE FROM user_roles WHERE user_id = :user_id AND role_id = :role_id''', [user_id: user.id, role_id: role.id])
                user.clearAuthorization = true
            }
        }
        return user
    }

    @Override
    Collection<Role> getUserRoles(long userId) {
        return jdbcTemplate.query('''select r.id, concat('ROLE_', UPPER(r.role_name)) role_name from roles r
inner join user_roles ur on (r.id = ur.role_id)
inner join users u on (u.id = ur.user_id)
where u.id = :id and u.enabled = :enabled''', [id: userId, enabled: true], mapperHolder.getOrCreateMapper(Role))
    }
}
