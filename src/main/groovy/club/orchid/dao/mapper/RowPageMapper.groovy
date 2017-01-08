package club.orchid.dao.mapper

import club.orchid.domain.cms.CmsPage
import club.orchid.domain.cms.Page
import club.orchid.service.MainApplicationContext
import club.orchid.strategy.PageContentStrategy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component

import java.sql.ResultSet
import java.sql.SQLException

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/3/16 12:52 PM
 */
@Component
class RowPageMapper <T extends Page> implements RowMapper<T> {
    @Autowired
    protected MainApplicationContext context

    @Override
    T mapRow(ResultSet rs, int rowNum) throws SQLException {
        final String discriminator = rs.getString('discriminator')
        T page = getStrategy(discriminator).createPage()
        page.setDiscriminator(discriminator)
        page.setVersion(rs.getInt('version'))
        fillPage(rs, rowNum, page)
        return page
    }

    protected static void fillPage(ResultSet rs, int rowNum, T page) {
        page.setId(rs.getInt('id'))
        page.setEnabled(rs.getBoolean('enabled'))
        page.setName(rs.getString('name'))
        page.setPrettyUrl(rs.getString('pretty_url'))
    }

    protected PageContentStrategy<T> getStrategy(final String discriminator) {
        return context.getPageContentStrategy(discriminator)
    }
}
