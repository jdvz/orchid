package club.orchid.web.forms

import club.orchid.domain.cms.ContentPage
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
    long contentPageId

    List<String> templates
    List<String> types
    List<ContentPage> contentPages
}
