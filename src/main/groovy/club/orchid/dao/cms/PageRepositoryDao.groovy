package club.orchid.dao.cms

import club.orchid.dao.PersistentRepositoryDao
import club.orchid.dao.mapper.PageMapper
import club.orchid.dao.mapper.RowPageMapper
import club.orchid.domain.cms.CmsPage
import club.orchid.domain.cms.Page
import club.orchid.web.forms.PageCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/3/16 12:33 AM
 */
@Repository('pageRepository')
class PageRepositoryDao extends PersistentRepositoryDao<Page> implements PageRepository {
    @Autowired
    private PageMapper pageMapper
    @Autowired
    private RowPageMapper rowPageMapper

    @Override
    List<CmsPage> pages() {
        return jdbcTemplate.query(
'''SELECT
entry.id,
entry.name,
entry.pretty_url,
entry.discriminator,
entry.version,
entry.enabled
FROM cms_entries entry
INNER JOIN pages page ON (entry.id = page.id)
INNER JOIN cms_pages cms_page ON (entry.id = cms_page.id)
WHERE entry.enabled = :enabled AND page.main_page_id IS NULL''', [enabled: true], rowPageMapper)
    }

    @Override
    List<CmsPage> pages(long mainPageId) {
        return jdbcTemplate.query(
"""SELECT
entry.id,
entry.name,
entry.pretty_url,
entry.discriminator,
entry.version,
entry.enabled,
coalesce(page.main_page_id, 0) main_page_id
FROM cms_entries entry
INNER JOIN pages page ON (entry.id = page.id)
INNER JOIN cms_pages cms_page ON (entry.id = cms_page.id)
WHERE entry.enabled = :enabled AND page.main_page_id = :main_page_id""", [enabled: true, main_page_id: mainPageId], rowPageMapper)
    }

    @Override
    Optional<CmsPage> page(String prettyUrl) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(
    '''SELECT
    entry.id,
    page.template,
    entry.name,
    entry.pretty_url,
    entry.discriminator,
    coalesce(page.main_page_id, 0) main_page_id,
    entry.version,
    entry.enabled
    FROM cms_entries entry
    INNER JOIN pages page ON (entry.id = page.id)
    INNER JOIN cms_pages cms_page ON (entry.id = cms_page.id)
    WHERE entry.enabled = :enabled AND entry.pretty_url = :pretty_url
    ''', [enabled: true, pretty_url: prettyUrl], mapperHolder.getOrCreateMapper(Page.class)))
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty()
        }
    }

    @Override
    def Optional<CmsPage> page(long pageId) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(
                    '''SELECT
    entry.id,
    page.template,
    entry.name,
    entry.pretty_url,
    entry.discriminator,
    coalesce(page.main_page_id, 0) main_page_id,
    entry.version,
    entry.enabled
    FROM cms_entries entry
    INNER JOIN pages page ON (entry.id = page.id)
    INNER JOIN cms_pages cms_page ON (entry.id = cms_page.id)
    WHERE entry.enabled = :enabled AND entry.id = :id
    ''', [enabled: true, id: pageId], mapperHolder.getOrCreateMapper(Page.class)))
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty()
        }
    }

    @Override
    String pageContentPath(long pageId) {
        try {
            return jdbcTemplate.queryForObject(
        '''SELECT content.path
        FROM cms_page_content content
        WHERE content.id = :id''', [id: pageId], String.class)
        } catch (EmptyResultDataAccessException e) {
            return null
        }
    }

    @Override
    CmsPage create(final PageCommand pageCommand) {
        final KeyHolder keyHolder = new GeneratedKeyHolder()

        jdbcTemplate.update('''INSERT INTO cms_entries(name, pretty_url, discriminator, version, enabled) 
VALUES (:name, :pretty_url, :discriminator, 1, :enabled)''',
                new MapSqlParameterSource([name: pageCommand.name, pretty_url: pageCommand.prettyUrl, discriminator: 'CmsPage', enabled: true]), keyHolder)
        long id = keyHolder.key?.longValue()
        jdbcTemplate.update('''INSERT INTO pages(id, main_page_id, template) VALUES (:id, :main_page_id, :template)''',
                new MapSqlParameterSource([id: id, main_page_id: pageCommand.mainPageId ?: null, template: pageCommand.template]))
        jdbcTemplate.update('''INSERT INTO cms_pages(id) VALUES (:id)''',
                new MapSqlParameterSource([id: id, content_page_id: pageCommand.mainPageId]))
        jdbcTemplate.update('''INSERT INTO cms_page_content(id, path) VALUES (:id, :path)''', [id: id, path: pageCommand.prettyUrl])

        return new CmsPage(id:id, name: pageCommand.name,
                prettyUrl: pageCommand.prettyUrl, discriminator: 'CmsPage',
                enabled: true, template: pageCommand.template,
                mainPage: new CmsPage(id: pageCommand.mainPageId),
                content: pageCommand.content
        )
    }

    @Override
    CmsPage update(final CmsPage cmsPage, final PageCommand pageCommand) {
        if (cmsPage.prettyUrl != pageCommand.prettyUrl || cmsPage.name != pageCommand.name) {
            jdbcTemplate.update('''UPDATE cms_entries SET name = :name, pretty_url = :pretty_url WHERE id = :id''',
                    new MapSqlParameterSource([name: pageCommand.name, pretty_url: pageCommand.prettyUrl, id: cmsPage.id]))
            cmsPage.name = pageCommand.name
        }
        if (cmsPage.prettyUrl != pageCommand.prettyUrl) {
            jdbcTemplate.update('''UPDATE cms_page_content SET path = :path WHERE id = :id''', [id: cmsPage.id, path: pageCommand.prettyUrl])
            cmsPage.prettyUrl = pageCommand.prettyUrl
        }
        if (cmsPage.template != pageCommand.template) {
            jdbcTemplate.update('''UPDATE pages SET template = :template WHERE id = :id''',
                    new MapSqlParameterSource([id: cmsPage.id, template: pageCommand.template]))
            cmsPage.template = pageCommand.template
        }
        if (cmsPage.mainPage?.id != pageCommand.mainPageId && pageCommand.mainPageId > 0) {
            jdbcTemplate.update('''UPDATE pages SET main_page_id = :main_page_id WHERE id = :id''',
                    new MapSqlParameterSource([id: cmsPage.id, main_page_id: pageCommand.mainPageId]))
            cmsPage.mainPage = new CmsPage(id: pageCommand.mainPageId, lazy: true)
        }
        return cmsPage // update
    }
}
