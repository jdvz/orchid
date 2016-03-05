package club.orchid.strategy

import club.orchid.anno.strategy.PageStrategy
import club.orchid.domain.cms.ContentPage
import club.orchid.service.IPageService
import club.orchid.service.PageService
import club.orchid.web.forms.PageCommand
import org.springframework.beans.factory.annotation.Autowire
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.sql.ResultSet

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/3/16 1:30 AM
 */
@Component
@PageStrategy(ContentPage.class)
class ContentPageContentStrategy extends PageContentStrategy<ContentPage> {
    @Override
    void extractContent(ResultSet rs, int rowNum, ContentPage page) {
        page.setPages(pageService.chapters(page.id))
        page.setPageName('pages/content')
        page.setCurrentCatalogId(page.id)
    }

    @Override
    ContentPage createPage() {
        return new ContentPage(discriminator: ContentPage.class.simpleName)
    }

    @Override
    Class<ContentPage> getPageClass() {
        return ContentPage.class
    }

    @Override
    ContentPage savePage(ContentPage contentPage, PageCommand pageCommand) {
        return pageService.savePage(contentPage, pageCommand)
    }
}
