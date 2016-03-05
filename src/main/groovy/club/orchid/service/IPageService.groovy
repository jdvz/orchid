package club.orchid.service

import club.orchid.domain.cms.CmsPage
import club.orchid.web.forms.PageCommand
/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/3/16 12:31 AM
 */
interface IPageService {
    List<CmsPage> pages()
    List<CmsPage> pages(final long mainPageId)
    Optional<CmsPage> page(String prettyUrl)
    Optional<CmsPage> page(long pageId)
    String pageContent(long pageId)
    CmsPage save(PageCommand pageCommand)

    List<String> allowedTemplates()
    List<String> allowedTypes()
    List<CmsPage> allowedPages()
}
