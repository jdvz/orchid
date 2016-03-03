package club.orchid.dao.mapper

import club.orchid.domain.cms.Image
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component

import java.sql.ResultSet
import java.sql.SQLException

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/3/16 5:30 PM
 */
@Component
class ImageMapper implements RowMapper<Image> {
    @Override
    Image mapRow(ResultSet rs, int rowNum) throws SQLException {
        final Image image = new Image()
        image.setId(rs.getInt('id'))
        image.setName(rs.getString('name'))
        image.setPrettyUrl(rs.getString('pretty_url'))
        image.setDiscriminator('Image')
        image.setEnabled(true)
        image.setRealName(rs.getString('real_name'))
        image.setMime(rs.getString('mime'))
        image
    }
}
