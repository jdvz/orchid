package club.orchid.dao.product

import club.orchid.dao.PersistentRepositoryDao
import club.orchid.dao.mapper.PageMapper
import club.orchid.dao.mapper.ProductMapper
import club.orchid.dao.mapper.RowPageMapper
import club.orchid.domain.product.Hybrid
import club.orchid.domain.product.Orchid
import club.orchid.domain.product.Product
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository

/**
 *
 * @author Dmitri Zaporozhtsev <dmitri.zaporozhtsev@novardis.com>
 * @link http://www.novardis.com/
 * @copyright 2016 NOVARDIS
 */
@Repository('productRepository')
class ProductRepositoryDao extends PersistentRepositoryDao<Product> implements ProductRepository {
    @Autowired
    private PageMapper pageMapper
    @Autowired
    private ProductMapper productMapper

    public List<Product> products() {
        return jdbcTemplate.query('''
SELECT
    product.id id,
    product.name name,
    product.description description,
    product.shoot_count shoot_count,
    product.blossom_status blossom_status,
    product.leaf leaf,
    coalesce(orchid.category_id) category_id,
    coalesce(category.name) category_name,
    CASE WHEN orchid.id IS NOT NULL THEN 'orchid' WHEN hybrid.id IS NOT NULL THEN 'hybrid' ELSE 'unknown' END as discriminator
FROM products product
LEFT JOIN orchids orchid ON (orchid.id = product.id)
LEFT JOIN hybrids hybrid ON (hybrid.id = product.id)
LEFT JOIN categories category ON (category.id = orchid.category_id)
WHERE product.enabled = :enabled
''', [enabled: true],
                productMapper)
    }

    @Override
    public Orchid create(Orchid product) {
        final KeyHolder keyHolder = new GeneratedKeyHolder()
        jdbcTemplate.update('''
INSERT INTO products(name, description, shoot_count, blossom_status, leaf)
VALUES (:name, :description, :shoot_count, :blossom_status, :leaf''',
                [name: product.name, description: product.description, shoot_count: product.shootCount, blossom_status: product.blossomStatus, leaf: product.leaf])
        long id = keyHolder.key?.longValue()
        jdbcTemplate.update('''
INSERT INTO orchids(id, category_id) VALUES(:id, :category_id),
''', [id: id, category_id: product.category?.id])
        return product
    }

    @Override
    public Hybrid create(Hybrid product) {
        final KeyHolder keyHolder = new GeneratedKeyHolder()
        jdbcTemplate.update('''
INSERT INTO products(name, description, shoot_count, blossom_status, leaf)
VALUES (:name, :description, :shoot_count, :blossom_status, :leaf''',
                [name: product.name, description: product.description, shoot_count: product.shootCount, blossom_status: product.blossomStatus, leaf: product.leaf])
        long id = keyHolder.key?.longValue()
        jdbcTemplate.update('''
INSERT INTO hybrids(id) VALUES(:id),
''', [id: id])
        return product
    }
}
