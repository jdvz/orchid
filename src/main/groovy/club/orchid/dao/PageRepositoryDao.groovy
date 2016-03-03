package club.orchid.dao

import club.orchid.dao.mapper.PageMapper
import club.orchid.dao.mapper.RowPageMapper
import club.orchid.domain.cms.ContentPage
import club.orchid.domain.cms.MultiCmsPage
import club.orchid.domain.cms.Page
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
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
    List<String> pageContent(long pageId) {
        return jdbcTemplate.queryForList(
    '''SELECT content.content
    FROM cms_page_content content
    WHERE content.page_id = :id ORDER BY content.content_order''', [id: pageId], String.class)
    }
}
