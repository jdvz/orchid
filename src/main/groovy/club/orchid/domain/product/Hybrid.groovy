package club.orchid.domain.product

import club.orchid.anno.mapping.OneToMany

/**
 *
 * @author Dmitri Zaporozhtsev <dmitri.zaporozhtsev@novardis.com>
 * @link http://www.novardis.com/
 * @copyright 2016 NOVARDIS
 */
class Hybrid extends Product<Hybrid> {
    @OneToMany('hybrid_id')
    List<Hybrid2CategoryRelation> categoryRelations
}
