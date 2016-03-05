package club.orchid.web.forms

import club.orchid.domain.cms.CmsPage
import groovy.transform.Canonical
/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/3/16 11:13 PM
 */
@Canonical
class PageCommand {
    long pageId
    String name
    String prettyUrl
    String template
    String type
    String content
    long mainPageId

    List<String> templates
    List<String> types
    List<CmsPage> contentPages
}
