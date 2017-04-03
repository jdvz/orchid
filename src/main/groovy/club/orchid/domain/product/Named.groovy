package club.orchid.domain.product

import club.orchid.anno.mapping.Primitive
import club.orchid.domain.AbstractMappedPersistent
import groovy.transform.Canonical

/**
 *
 * @author Dmitri Zaporozhtsev <dmitri.zaporozhtsev@novardis.com>
 * @link http://www.novardis.com/
 * @copyright 2016 NOVARDIS
 */
@Canonical
abstract class Named <T extends Named<T>> extends AbstractMappedPersistent<T> {
    @Primitive
    String name
}
