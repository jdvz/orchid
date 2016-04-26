package club.orchid.domain.product

import club.orchid.anno.mapping.Primitive
import club.orchid.domain.BlossomStatus
import groovy.transform.Canonical

/**
 *
 * @author Dmitri Zaporozhtsev <dmitri.zaporozhtsev@novardis.com>
 * @link http://www.novardis.com/
 * @copyright 2016 NOVARDIS
 */
@Canonical
abstract class Product<T extends Product> extends Named<T> {
    @Primitive
    String description
    @Primitive
    int shootCount
    @Primitive
    BlossomStatus blossomStatus
    @Primitive
    boolean leaf
    @Primitive
    boolean enabled
}
