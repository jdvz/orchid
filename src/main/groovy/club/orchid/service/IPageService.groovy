package club.orchid.service

import club.orchid.domain.cms.CmsPage
import club.orchid.domain.cms.MultiCmsPage
import club.orchid.domain.cms.Page

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/3/16 12:31 AM
 */
interface IPageService {
    List<Page> contentpages()
    List<Page> pages()
    List<Page> subpages(final long mainPageId)
    List<Page> chapters(final long contentPageId)
    public <T extends Page<T>> Optional<T> page(String prettyUrl)
    Optional<MultiCmsPage> page(String prettyUrl, final int page)
    String pageContent(long pageId)
    Page save(CmsPage cmsPage)
}
