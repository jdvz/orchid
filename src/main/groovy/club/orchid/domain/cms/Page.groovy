package club.orchid.domain.cms

import club.orchid.anno.mapping.ManyToOne
import club.orchid.anno.mapping.Primitive
import club.orchid.domain.AbstractMappedPersistent
import club.orchid.domain.CmsEntry
import groovy.transform.Canonical

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/1/16 6:55 PM
 */
@Canonical
abstract class Page<T extends Page> extends CmsEntry<T> {
    @Primitive
    String template
    @ManyToOne("main_page_id")
    Page mainPage

    String pageName
    List<? extends Page> pages
}
