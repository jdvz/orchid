package club.orchid.domain.cms

import club.orchid.anno.mapping.OneToOne
import club.orchid.anno.mapping.Primitive

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/3/16 12:19 AM
 */
class MultiCmsPage extends CmsPage {
    @OneToOne('next_page_id')
    MultiCmsPage nextPage
    @Primitive
    int pageNum
}
