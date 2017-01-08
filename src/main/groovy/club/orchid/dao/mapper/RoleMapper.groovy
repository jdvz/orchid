package club.orchid.dao.mapper

import club.orchid.domain.auth.Role
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
class RoleMapper implements RowMapper<Role> {
    @Override
    Role mapRow(ResultSet rs, int rowNum) throws SQLException {
        Role role = new Role()
        role.setId(rs.getInt('id'))
        role.setRoleName(rs.getString('role_name'))
        role.setVersion(rs.getInt('version'))
        return role
    }

}
