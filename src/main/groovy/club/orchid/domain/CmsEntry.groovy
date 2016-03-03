package club.orchid.domain

import club.orchid.anno.mapping.Primitive
import groovy.transform.Canonical

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/3/16 3:33 PM
 */
@Canonical
abstract class CmsEntry <T extends CmsEntry<T>> extends AbstractMappedPersistent <T> {
    @Primitive
    String name
    @Primitive
    String prettyUrl
    @Primitive
    String discriminator
    @Primitive
    boolean enabled
    @Primitive
    String tagline

    long currentCatalogId
    abstract String getContent()
}
