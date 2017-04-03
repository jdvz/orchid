package club.orchid.dao.product

import club.orchid.domain.product.Hybrid
import club.orchid.domain.product.IProduct
import club.orchid.domain.product.Orchid
import club.orchid.domain.product.Product
import club.orchid.domain.product.SolrProduct
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.solr.core.SolrTemplate
import org.springframework.data.solr.core.query.Criteria
import org.springframework.data.solr.core.query.PartialUpdate
import org.springframework.data.solr.core.query.SimpleQuery
import org.springframework.data.solr.repository.SolrCrudRepository
import org.springframework.stereotype.Repository

import java.lang.reflect.Field

/**
 * @author Dmitri Zaporozhtsev <dmitri.zera@gmail.com>
 */
@Repository('productSolrRepository')
class ProductRepositorySolrDao implements ProductRepository {
    public static final Logger log = Logger.getLogger(ProductRepositorySolrDao.class.getName())
    @Autowired
    SolrTemplate solrTemplate

    @Override
    List<Product> products() {
        return null
    }

    @Override
    IProduct update(IProduct product) {
        final PartialUpdate update = new PartialUpdate("id", product.getId())
        product.class.declaredFields.each { Field f ->
            if (f.isAnnotationPresent(org.apache.solr.client.solrj.beans.Field.class)) {
                update.add(f.name, product.("get${f.name.capitalize()}"))
            }
        }
        solrTemplate.saveBean(update)
        solrTemplate.commit()
        return product
    }

    @Override
    IProduct get(HashMap<String, Object> map) {
        if (!map) {
            log.error("criteria not set")
            return null
        }
        final Criteria conditions = new Criteria()
        map.each { k, v ->
            conditions.or(new Criteria(k).is(v))
        }
        final SimpleQuery simpleQuery = new SimpleQuery(conditions)
        return solrTemplate.queryForObject(simpleQuery, SolrProduct.class)
    }

    @Override
    IProduct get(IProduct example) {
        return null
    }
}
