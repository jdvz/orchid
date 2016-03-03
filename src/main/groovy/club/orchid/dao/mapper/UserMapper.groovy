package club.orchid.dao.mapper

import club.orchid.domain.auth.User
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component

import java.sql.ResultSet
import java.sql.SQLException

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 2/14/16 5:58 PM
 */
@Component
class UserMapper implements RowMapper<User> {
    @Override
    User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User()
        user.setId(rs.getInt('id'))
        user.setFirstName(rs.getString('first_name'))
        user.setLastName(rs.getString('last_name'))
        user.setPassword(rs.getString('password'))
        user.setEmail(rs.getString('email'))
        user.setAccountNonExpired(rs.getBoolean('account_non_expired'))
        user.setAccountNonLocked(rs.getBoolean('account_non_locked'))
        user.setCredentialsNonExpired(rs.getBoolean('credentials_non_expired'))
        user.setEnabled(rs.getBoolean('enabled'))
        user.setRoles(rs.getString('roles'))
        return user
    }

}
