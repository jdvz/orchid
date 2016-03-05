package club.orchid.service

import club.orchid.domain.cms.CmsPage
import club.orchid.domain.cms.ContentPage
import club.orchid.domain.cms.MultiCmsPage
import club.orchid.domain.cms.Page
import club.orchid.web.forms.PageCommand

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/3/16 12:31 AM
 */
interface IPageService {
    List<ContentPage> contentpages()
    List<Page> pages()
    List<Page> subpages(final long mainPageId)
    List<Page> chapters(final long contentPageId)
    public <T extends Page<T>> Optional<T> page(String prettyUrl)
    Optional<MultiCmsPage> page(String prettyUrl, final int page)
    public <T extends Page<T>> Optional<T> page(long pageId)
    String pageContent(long pageId)
    public <T extends Page<T>> T save(PageCommand pageCommand)
    CmsPage savePage(final CmsPage cmsPage, final PageCommand pageCommand)
    ContentPage savePage(final ContentPage contentPage, final PageCommand pageCommand)

    List<String> allowedTemplates()
    List<String> allowedTypes()
}
