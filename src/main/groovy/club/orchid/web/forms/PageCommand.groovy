package club.orchid.web.forms

import groovy.transform.Canonical

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/3/16 11:13 PM
 */
@Canonical
class PageCommand {
    String prettyUrl
    String template
    String type
    String content
}
