package club.orchid.dao

import club.orchid.domain.auth.Role
import club.orchid.domain.auth.User
import club.orchid.util.QueryUtils
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Repository

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 2/14/16 6:23 PM
 */
@Repository('userRepository')
class UserRepositoryDao extends PersistentRepositoryDao<User> implements UserRepository {
    private static final String USER_REQUEST = """select user.id, user.first_name, user.last_name, user.password, user.email,
    user.account_non_expired, user.account_non_locked, user.credentials_non_expired, user.enabled,
    group_concat(concat('ROLE_', upper(role.role_name))) roles
    from users user
    left join user_roles userrole on (userrole.user_id = user.id)
    left join roles role on (userrole.role_id = role.id)"""

    @Override
    Optional<User> get(long id) {
        try {
            return Optional.of(get([id:id]))
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
    long save(User user) {
        if (user.isPersistent()) {
            return update(user)
        } else {
            return create(user)
        }
    }

    @Override
    Collection<Role> getUserRoles(long userId) {
        return jdbcTemplate.query('''select r.id, concat('ROLE_', UPPER(r.role_name)) role_name from roles r
inner join user_roles ur on (r.id = ur.role_id)
inner join users u on (u.id = ur.user_id)
where u.id = :id and u.enabled = :enabled''', [id: userId, enabled: true], mapperHolder.getOrCreateMapper(Role))
    }
}
