package club.orchid.dao.mapper

import club.orchid.domain.BlossomStatus
import club.orchid.domain.product.Category
import club.orchid.domain.product.Hybrid
import club.orchid.domain.product.Orchid
import club.orchid.domain.product.Product
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component

import java.sql.ResultSet
import java.sql.SQLException

/**
 *
 * @author Dmitri Zaporozhtsev <dmitri.zaporozhtsev@novardis.com>
 * @link http://www.novardis.com/
 * @copyright 2016 NOVARDIS
 */
@Component
class ProductMapper<T extends Product<T>> implements RowMapper<T> {
    @Override
    T mapRow(ResultSet rs, int rowNum) throws SQLException {
        final String discriminator = rs.getString('discriminator')
        T product
        switch (discriminator) {
            case 'orchid':
                product = new Orchid()

                def categoryId = rs.getInt('category_id')
                if (categoryId > 0) {
                    product.setCategory(new Category(id: categoryId, name: rs.getString('category_name')))
                }
                break
            case 'hybrid':
                product = new Hybrid()
                break
            default:
                throw new UnsupportedOperationException("unknown discriminator '$discriminator' for product")
        }
        product.setDescription(rs.getString('description'))
        product.setShootCount(rs.getInt('shoot_count'))
        product.setBlossomStatus(BlossomStatus.valueOf(rs.getString('blossom_status')))
        product.setLeaf(rs.getBoolean('leaf'))
        return product
    }
}
