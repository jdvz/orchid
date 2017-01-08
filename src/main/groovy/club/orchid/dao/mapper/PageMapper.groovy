package club.orchid.dao.mapper

import club.orchid.domain.cms.CmsPage
import club.orchid.domain.cms.Page
import club.orchid.strategy.PageContentStrategy
import org.springframework.stereotype.Component

import java.sql.ResultSet
import java.sql.SQLException

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/3/16 1:01 AM
 */
@Component
class PageMapper<T extends Page> extends RowPageMapper<T> {
    @Override
    T mapRow(ResultSet rs, int rowNum) throws SQLException {
        final String discriminator = rs.getString('discriminator')
        PageContentStrategy<T> pageContentStrategy = getStrategy(discriminator)

        if (!pageContentStrategy) {
            return null
        }

        final T page = pageContentStrategy.createPage()

        fillPage(rs, rowNum, page)

        page.setTemplate(rs.getString('template'))
        int mainPageId = rs.getInt('main_page_id')
        if (mainPageId > 0) {
            page.setMainPage(new CmsPage(id: mainPageId, lazy: true))
        }
        page.setDiscriminator(discriminator)
        page.setVersion(rs.getInt('version'))

        pageContentStrategy.extractContent(rs, rowNum, page)

        page
    }
}
