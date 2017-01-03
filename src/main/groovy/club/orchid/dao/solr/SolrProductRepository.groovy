package club.orchid.dao.solr

import club.orchid.domain.product.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.solr.core.query.result.FacetPage
import org.springframework.data.solr.core.query.result.HighlightPage
import org.springframework.data.solr.repository.Boost
import org.springframework.data.solr.repository.Facet
import org.springframework.data.solr.repository.Highlight
import org.springframework.data.solr.repository.Query
import org.springframework.data.solr.repository.SolrCrudRepository

/**
 *
 * @author Dmitri Zaporozhtsev <dmitri.zaporozhtsev@novardis.com>
 * @link http://www.novardis.com/
 * @copyright 2016 NOVARDIS
 */
interface SolrProductRepository extends SolrCrudRepository<Product, String> {
    Page<Product> findByPopularity(Integer popularity, Pageable page)

    Page<Product> findByNameOrDescription(@Boost(2f) String name, String description, Pageable page)

    @Highlight
    HighlightPage<Product> findByNameIn(Collection<String> name, Page page)

    @Query(value = "name:?0")
    @Facet(fields = [ "cat" ], limit=20)
    FacetPage<Product> findByNameAndFacetOnCategory(String name, Pageable page)
}