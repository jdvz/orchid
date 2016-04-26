package club.orchid.domain.product

import club.orchid.anno.mapping.ManyToOne
import groovy.transform.Canonical

/**
 *
 * @author Dmitri Zaporozhtsev <dmitri.zaporozhtsev@novardis.com>
 * @link http://www.novardis.com/
 * @copyright 2016 NOVARDIS
 */
@Canonical
class Orchid extends Product<Orchid> {
    @ManyToOne('category_id')
    Category category
}
