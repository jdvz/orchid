package club.orchid.dao.mapper

import club.orchid.domain.auth.Role
import club.orchid.domain.auth.UserRole
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
class UserRoleMapper implements RowMapper<UserRole> {
    @Override
    UserRole mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserRole role = new UserRole()
        role.setUserId(rs.getInt('user_id'))
        role.setRoleId(rs.getInt('role_id'))
        return role
    }

}
