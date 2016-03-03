package club.orchid.domain

import club.orchid.anno.mapping.Id
import groovy.transform.Canonical
/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 2/14/16 4:27 PM
 */
@Canonical
abstract class AbstractMappedPersistent<T extends AbstractMappedPersistent> extends AbstractMapper {
    @Id
    long id
    boolean lazy

    boolean isPersistent() { id > 0 }
}
