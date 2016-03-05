package club.orchid.strategy

import club.orchid.anno.strategy.PageStrategy
import club.orchid.domain.cms.CmsPage
import club.orchid.web.forms.PageCommand
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
    @Override
    void extractContent(ResultSet rs, int rowNum, CmsPage page) {
        page.setContent(pageService.pageContent(page.id))
        page.pages = pageService.pages(page.id)
        page.setPageName('pages/page')
    }

    @Override
    CmsPage createPage() {
        new CmsPage(discriminator: CmsPage.class.simpleName)
    }

    @Override
    Class<CmsPage> getPageClass() {
        return CmsPage.class
    }

    @Override
    def CmsPage savePage(CmsPage page, PageCommand pageCommand) {
        return pageService.savePage(page, pageCommand)
    }
}
