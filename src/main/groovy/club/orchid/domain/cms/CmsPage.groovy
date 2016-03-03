package club.orchid.domain.cms

import club.orchid.anno.mapping.ManyToOne
import club.orchid.anno.mapping.Primitive
import groovy.transform.Canonical

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/1/16 6:55 PM
 */
@Canonical
class CmsPage extends Page<CmsPage> {
    String content
    @ManyToOne('content_page_id')
    ContentPage contentPage
}
