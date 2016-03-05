package club.orchid.dao

import club.orchid.domain.cms.CmsPage
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
    public <T extends Page> Optional<T> page(long pageId)
    Optional<MultiCmsPage> page(String prettyUrl, int num)

    String pageContentPath(long pageId)

    CmsPage create(final CmsPage cmsPage, final PageCommand pageCommand)
    ContentPage create(final ContentPage cmsPage, final PageCommand pageCommand)
    CmsPage update(final CmsPage cmsPage, final PageCommand pageCommand)
    ContentPage update(final ContentPage contentPage, final PageCommand pageCommand)
}