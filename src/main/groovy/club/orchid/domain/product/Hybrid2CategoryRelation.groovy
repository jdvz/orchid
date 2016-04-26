package club.orchid.domain.product

import club.orchid.anno.mapping.Id

/**
 *
 * @author Dmitri Zaporozhtsev <dmitri.zaporozhtsev@novardis.com>
 * @link http://www.novardis.com/
 * @copyright 2016 NOVARDIS
 */
class Hybrid2CategoryRelation {
    @Id('hybrid_id')
    long hybridId
    @Id('category_id')
    long categoryId
}
