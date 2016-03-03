package club.orchid.strategy

import club.orchid.anno.strategy.PageStrategy
import club.orchid.domain.cms.ContentPage
import club.orchid.domain.cms.MultiCmsPage
import org.springframework.stereotype.Component

import java.sql.ResultSet

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/3/16 10:29 AM
 */
@Component
@PageStrategy(MultiCmsPage.class)
class MultiCmsPageContentStrategy extends PageContentStrategy<MultiCmsPage> {
    @Override
    void extractContent(ResultSet rs, int rowNum, MultiCmsPage page) {
        page.setContent(rs.getString('content'))
        def contentPageId = rs.getInt('content_page_id')
        if (contentPageId > 0) {
            page.setContentPage(new ContentPage(id: contentPageId, lazy: true))
        }
        def nextPageId = rs.getInt('next_page_id')
        if (nextPageId > 0) {
            page.setNextPage(new MultiCmsPage(id: nextPageId, lazy: true))
        }
        page.setPageNum(rs.getInt('page_num'))
        page.setPageName('pages/multipage')
    }

    @Override
    MultiCmsPage createPage() {
        return new MultiCmsPage()
    }

    @Override
    Class<MultiCmsPage> getPageClass() {
        return MultiCmsPage.class
    }
}
