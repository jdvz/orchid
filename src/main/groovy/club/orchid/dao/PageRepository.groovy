package club.orchid.dao

import club.orchid.domain.cms.CmsPage
import club.orchid.domain.cms.CmsPageContent
import club.orchid.domain.cms.ContentPage
import club.orchid.domain.cms.MultiCmsPage
import club.orchid.domain.cms.Page
import club.orchid.web.forms.PageCommand

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/3/16 12:33 AM
 */
interface PageRepository {
    List<Page> pages()
    List<ContentPage> contentpages()
    List<Page> subpages(long mainPageId)
    List<Page> chapters(long contentPageId)
    Optional<Page> page(String prettyUrl)
    Optional<MultiCmsPage> page(String prettyUrl, int num)

    List<String> pageContent(long pageId)

    CmsPage create(final PageCommand pageCommand)
    CmsPage update(final CmsPage cmsPage, final PageCommand pageCommand)
    CmsPage create(final List<CmsPageContent> cmsPageContent, final CmsPage page)
    CmsPage update(final List<CmsPageContent> cmsPageContent, final CmsPage page)
}