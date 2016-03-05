package club.orchid.dao

import club.orchid.dao.mapper.PageMapper
import club.orchid.dao.mapper.RowPageMapper
import club.orchid.domain.cms.CmsPage
import club.orchid.domain.cms.ContentPage
import club.orchid.domain.cms.MultiCmsPage
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
    List<Page> pages() {
        return jdbcTemplate.query(
'''SELECT
entry.id,
entry.name,
entry.pretty_url,
entry.discriminator,
entry.enabled
FROM cms_entries entry
INNER JOIN pages page ON (entry.id = page.id)
WHERE entry.enabled = :enabled''', [enabled: true], rowPageMapper)
    }

    @Override
    List<ContentPage> contentpages() {
        return jdbcTemplate.query(
'''SELECT
entry.id,
entry.name,
entry.pretty_url,
entry.discriminator,
entry.enabled
FROM cms_entries entry
INNER JOIN pages page ON (entry.id = page.id)
INNER JOIN content_pages content ON (entry.id = content.id)
WHERE entry.enabled = :enabled''', [enabled: true], rowPageMapper
        )
    }

    @Override
    List<Page> subpages(long mainPageId) {
        return jdbcTemplate.query(
"""SELECT
entry.id,
entry.name,
entry.pretty_url,
entry.discriminator,
entry.enabled,
coalesce(page.main_page_id, 0) main_page_id
FROM cms_entries entry
INNER JOIN pages page ON (entry.id = page.id)
WHERE entry.enabled = :enabled AND page.main_page_id = :main_page_id""", [enabled: true, main_page_id: mainPageId], rowPageMapper)
    }

    @Override
    List<Page> chapters(long contentPageId) {
        return jdbcTemplate.query(
"""SELECT
entry.id,
entry.name,
entry.pretty_url,
entry.discriminator,
coalesce(page.main_page_id, 0) main_page_id,
cms_page.content_page_id,
entry.enabled
FROM cms_entries entry
INNER JOIN pages page ON (entry.id = page.id)
INNER JOIN cms_pages cms_page ON (entry.id = cms_page.id)
WHERE entry.enabled = :enabled AND cms_page.content_page_id = :content_page_id""", [enabled: true, content_page_id: contentPageId], rowPageMapper)
    }

    @Override
    Optional<Page> page(String prettyUrl) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(
    '''SELECT
    entry.id,
    page.template,
    entry.name,
    entry.pretty_url,
    entry.discriminator,
    coalesce(page.main_page_id, 0) main_page_id,
    coalesce(multi_page.next_page_id, 0) next_page_id,
    coalesce(cms_page.content_page_id, 0) content_page_id,
    entry.enabled
    FROM cms_entries entry
    INNER JOIN pages page ON (entry.id = page.id)
    LEFT JOIN cms_pages cms_page ON (entry.id = cms_page.id)
    LEFT JOIN content_pages content_page ON (entry.id = content_page.id)
    LEFT JOIN multi_cms_pages multi_page ON (entry.id = multi_page.id)
    WHERE entry.enabled = :enabled AND entry.pretty_url = :pretty_url
    ''', [enabled: true, pretty_url: prettyUrl], mapperHolder.getOrCreateMapper(Page.class)))
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty()
        }
    }

    @Override
    def <T extends Page> Optional<T> page(long pageId) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(
                    '''SELECT
    entry.id,
    page.template,
    entry.name,
    entry.pretty_url,
    entry.discriminator,
    coalesce(page.main_page_id, 0) main_page_id,
    coalesce(multi_page.next_page_id, 0) next_page_id,
    coalesce(cms_page.content_page_id, 0) content_page_id,
    entry.enabled
    FROM cms_entries entry
    INNER JOIN pages page ON (entry.id = page.id)
    LEFT JOIN cms_pages cms_page ON (entry.id = cms_page.id)
    LEFT JOIN content_pages content_page ON (entry.id = content_page.id)
    LEFT JOIN multi_cms_pages multi_page ON (entry.id = multi_page.id)
    WHERE entry.enabled = :enabled AND entry.id = :id
    ''', [enabled: true, id: pageId], mapperHolder.getOrCreateMapper(Page.class)))
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty()
        }
    }

    @Override
    Optional<MultiCmsPage> page(String prettyUrl, int num) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(
    '''SELECT
    entry.id,
    page.template,
    entry.name,
    entry.pretty_url,
    entry.discriminator,
    coalesce(page.main_page_id, 0) main_page_id,
    coalesce(multi_page.next_page_id, 0) next_page_id,
    coalesce(cms_page.content_page_id, 0) content_page_id,
    coalesce(multi_page.page_num, 0) page_num,
    entry.enabled
    FROM cms_entries entry
    INNER JOIN pages page ON (entry.id = page.id)
    INNER JOIN cms_pages cms_page ON (entry.id = cms_page.id)
    INNER JOIN multi_cms_pages multi_page ON (entry.id = multi_page.id)
    WHERE entry.enabled = :enabled AND entry.pretty_url = :pretty_url AND multi_page.page_num = :page_num
    ''', [enabled: true, pretty_url: prettyUrl, page_num: num], mapperHolder.getOrCreateMapper(Page.class)))
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
    CmsPage create(CmsPage cmsPage, final PageCommand pageCommand) {
        final KeyHolder keyHolder = new GeneratedKeyHolder()

        jdbcTemplate.update('''INSERT INTO cms_entries(name, pretty_url, discriminator, enabled) VALUES (:name, :pretty_url, :discriminator, :enabled)''',
                new MapSqlParameterSource([name: pageCommand.name, pretty_url: pageCommand.prettyUrl, discriminator: 'CmsPage', enabled: true]), keyHolder)
        long id = keyHolder.key?.longValue()
        jdbcTemplate.update('''INSERT INTO pages(id, template) VALUES (:id, :template)''',
                new MapSqlParameterSource([id: id, template: pageCommand.template]))
        jdbcTemplate.update('''INSERT INTO cms_pages(id, content_page_id) VALUES (:id, :content_page_id)''',
                new MapSqlParameterSource([id: id, content_page_id: pageCommand.contentPageId]))
        jdbcTemplate.update('''INSERT INTO cms_page_content(id, path) VALUES (:id, :path)''', [id: id, path: pageCommand.prettyUrl])

        return new CmsPage(id:id, name: pageCommand.name,
                prettyUrl: pageCommand.prettyUrl, discriminator: 'CmsPage',
                enabled: true, template: pageCommand.template,
                contentPage: new ContentPage(id: pageCommand.contentPageId),
                content: pageCommand.content
        )
    }

    @Override
    ContentPage create(ContentPage cmsPage, final PageCommand pageCommand) {
        final KeyHolder keyHolder = new GeneratedKeyHolder()

        jdbcTemplate.update('''INSERT INTO cms_entries(name, pretty_url, discriminator, enabled) VALUES (:name, :pretty_url, :discriminator, :enabled)''',
                new MapSqlParameterSource([name: pageCommand.name, pretty_url: pageCommand.prettyUrl, discriminator: 'ContentPage', enabled: true]), keyHolder)
        long id = keyHolder.key?.longValue()
        jdbcTemplate.update('''INSERT INTO pages(id, template) VALUES (:id, :template)''',
                new MapSqlParameterSource([id: id, template: pageCommand.template]))
        jdbcTemplate.update('''INSERT INTO content_pages(id) VALUES (:id)''',
                new MapSqlParameterSource([id: id]))

        return new ContentPage(id:id, name: pageCommand.name,
                prettyUrl: pageCommand.prettyUrl, discriminator: 'CmsPage',
                enabled: true, template: pageCommand.template
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
        if (cmsPage.contentPage?.id != pageCommand.contentPageId && pageCommand.contentPageId > 0) {
            jdbcTemplate.update('''UPDATE cms_pages SET content_page_id = :content_page_id WHERE id = :id''',
                    new MapSqlParameterSource([id: cmsPage.id, content_page_id: cmsPage.contentPage?.id]))
            cmsPage.contentPage = new ContentPage(id: pageCommand.contentPageId, lazy: true)
        }
        return cmsPage // update
    }

    @Override
    ContentPage update(final ContentPage contentPage, final PageCommand pageCommand) {
        if (contentPage.prettyUrl != pageCommand.prettyUrl || contentPage.name != pageCommand.name) {
            jdbcTemplate.update('''UPDATE cms_entries SET name = :name, pretty_url = :pretty_url WHERE id = :id''',
                    new MapSqlParameterSource([name: pageCommand.name, pretty_url: pageCommand.prettyUrl, id: contentPage.id]))
            contentPage.name = pageCommand.name
        }
        if (contentPage.prettyUrl != pageCommand.prettyUrl) {
            jdbcTemplate.update('''UPDATE cms_page_content SET path = :path WHERE id = :id''', [id: contentPage.id, path: pageCommand.prettyUrl])
            contentPage.prettyUrl = pageCommand.prettyUrl
        }
        if (contentPage.template != pageCommand.template) {
            jdbcTemplate.update('''UPDATE pages SET template = :template WHERE id = :id''',
                    new MapSqlParameterSource([id: contentPage.id, template: pageCommand.template]))
            contentPage.template = pageCommand.template
        }
        return contentPage // update
    }
}
