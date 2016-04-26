package club.orchid.domain.product

import groovy.transform.Canonical

/**
 *
 * @author Dmitri Zaporozhtsev <dmitri.zaporozhtsev@novardis.com>
 * @link http://www.novardis.com/
 * @copyright 2016 NOVARDIS
 */
@Canonical
class Category extends Named<Category> {
    Category supercategory
}
