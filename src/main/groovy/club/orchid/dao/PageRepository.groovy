package club.orchid.dao

import club.orchid.domain.cms.ContentPage
import club.orchid.domain.cms.MultiCmsPage
import club.orchid.domain.cms.Page

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
}