package club.orchid.dao

import club.orchid.domain.cms.Image
import club.orchid.util.QueryUtils
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/3/16 5:25 PM
 */
@Repository('resourceDao')
class ResourceRepositoryDao extends PersistentRepositoryDao<Image> implements ResourceDao {
    @Override
    Optional<Image> getImageByNameAndPrettyUrl(String name, String prettyUrl) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(
    '''SELECT
    entry.id,
    entry.name,
    entry.pretty_url,
    entry.discriminator,
    entry.enabled,
    image.real_name,
    image.mime
    FROM cms_entries entry
    INNER JOIN images image ON (entry.id = image.id)
    WHERE entry.enabled = :enabled AND (:pretty_url IS NOT NULL OR entry.name = :name)
    AND (:pretty_url IS NULL OR entry.pretty_url = :pretty_url)''', [enabled: true, name: name, pretty_url: prettyUrl], mapperHolder.getOrCreateMapper(Image.class)))
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty()
        }
    }

    @Override
    Optional<Image> getImageByNameOrPrettyUrl(String uid) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(
                    '''SELECT
    entry.id,
    entry.name,
    entry.pretty_url,
    entry.discriminator,
    entry.enabled,
    image.real_name,
    image.mime
    FROM cms_entries entry
    INNER JOIN images image ON (entry.id = image.id)
    WHERE entry.enabled = :enabled AND (entry.name = :name OR entry.pretty_url = :name)''',
                    [enabled: true, name: uid], mapperHolder.getOrCreateMapper(Image.class)))
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty()
        }
    }

    @Override
    Image save(Image image) {
        if (image.isPersistent()) {
            jdbcTemplate.update('''
UPDATE cms_entries entry
SET entry.name = :name,
entry.pretty_url = :pretty_url,
entry.enabled = :enabled
WHERE entry.id = :id''', [name: image.name, pretty_url: image.prettyUrl, enabled: true, id: image.id])
            jdbcTemplate.update('''
UPDATE images image
SET image.real_name = :name,
image.mime = :mime
WHERE image.id = :id''', [name: image.realName, mime: image.mime, id: image.id])
        } else {
            final KeyHolder keyHolder = new GeneratedKeyHolder()

            jdbcTemplate.update('''INSERT INTO cms_entries(name, pretty_url, discriminator, enabled) VALUES (:name, :pretty_url, :discriminator, :enabled)''',
                    new MapSqlParameterSource([name: image.name, pretty_url: image.prettyUrl, discriminator: 'Image', enabled: true]), keyHolder)
            image.id = keyHolder.key?.longValue()
            jdbcTemplate.update('''INSERT INTO images(id, real_name, mime) VALUES (:id, :name, :mime)''',
                    new MapSqlParameterSource([id: image.id, name: image.realName, mime: image.mime]))
        }
        return image
    }
}
