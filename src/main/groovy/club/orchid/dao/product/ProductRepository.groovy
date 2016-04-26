package club.orchid.dao.product

import club.orchid.domain.product.Hybrid
import club.orchid.domain.product.Orchid
import club.orchid.domain.product.Product

/**
 *
 * @author Dmitri Zaporozhtsev <dmitri.zaporozhtsev@novardis.com>
 * @link http://www.novardis.com/
 * @copyright 2016 NOVARDIS
 */
interface ProductRepository {
    List<Product> products()

    Orchid create(Orchid product)
    Hybrid create(Hybrid product)
}