package club.orchid.domain.cms

import club.orchid.anno.mapping.Primitive
import club.orchid.domain.AbstractMappedPersistent
import groovy.transform.Canonical
/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/3/16 11:03 PM
 */
@Canonical
class CmsPageContent extends AbstractMappedPersistent<CmsPageContent> {
    @Primitive
    String path
}
