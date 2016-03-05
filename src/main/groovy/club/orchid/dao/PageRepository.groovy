package club.orchid.dao

import club.orchid.domain.cms.CmsPage
import club.orchid.web.forms.PageCommand
/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/3/16 12:33 AM
 */
interface PageRepository {
    List<CmsPage> pages()
    List<CmsPage> pages(long mainPageId)

    Optional<CmsPage> page(final String prettyUrl)
    Optional<CmsPage> page(final long pageId)
    String pageContentPath(final long pageId)

    CmsPage create(final PageCommand pageCommand)
    CmsPage update(final CmsPage cmsPage, final PageCommand pageCommand)
}