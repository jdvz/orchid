package club.orchid.strategy

import club.orchid.anno.strategy.PageStrategy
import club.orchid.domain.cms.CmsPage
import club.orchid.domain.cms.ContentPage
import club.orchid.service.IPageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.sql.ResultSet

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/3/16 1:25 AM
 */
@Component
@PageStrategy(CmsPage.class)
class CmsPageContentStrategy extends PageContentStrategy<CmsPage> {
    @Autowired
    private IPageService pageService

    @Override
    void extractContent(ResultSet rs, int rowNum, CmsPage page) {
        def contentPageId = rs.getInt('content_page_id')
        if (contentPageId > 0) {
            page.setContentPage(new ContentPage(id: contentPageId))
            page.setCurrentCatalogId(contentPageId)
        }
        page.setContent(pageService.pageContent(page.id))
        page.setPageName('pages/page')
    }

    @Override
    CmsPage createPage() {
        new CmsPage()
    }

    @Override
    Class<CmsPage> getPageClass() {
        return CmsPage.class
    }
}
