package club.orchid.dao

import club.orchid.domain.AbstractMappedPersistent
import club.orchid.util.QueryUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder

import javax.sql.DataSource

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 2/14/16 4:34 PM
 */
abstract class PersistentRepositoryDao <T extends AbstractMappedPersistent<T>> implements PersistentRepository<T> {
    protected NamedParameterJdbcTemplate jdbcTemplate

    @Autowired
    protected MapperHolder mapperHolder

    @Autowired
    public setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource)
    }

    @Override
    public Optional<T> get(final long id, final Class<T> clazz) {
        try {
            return Optional.of(jdbcTemplate.queryForObject("select * from ${QueryUtils.generateQualifiedSqlName(clazz)} where id = :id",
                    [id: id], mapperHolder.getOrCreateMapper(clazz)))
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty()
        }
    }

    @Override
    Optional<T> get(Map<String, Object> params, Class<T> clazz) {
        try {
            return Optional.of(jdbcTemplate.queryForObject("select * from ${QueryUtils.generateQualifiedSqlName(clazz)} where ${QueryUtils.createWhereString(params)}",
                    params, mapperHolder.getOrCreateMapper(clazz)))
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty()
        }
    }

    protected long create(final T obj) {
        final KeyHolder keyHolder = new GeneratedKeyHolder()

        jdbcTemplate.update(QueryUtils.createInsertString(obj.class), new MapSqlParameterSource(QueryUtils.createPersistParams(obj)), keyHolder)
        return keyHolder.key?.longValue()
    }

    protected long update(final T obj) {
        jdbcTemplate.update(QueryUtils.createUpdateString(obj.class), new MapSqlParameterSource(QueryUtils.createPersistParams(obj)))
        return obj.id
    }
}
