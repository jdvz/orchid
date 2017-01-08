package club.orchid.dao.config

import club.orchid.domain.AbstractMappedPersistent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

import javax.sql.DataSource

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 1/8/17 10:17 PM
 */
@Repository('serviceDao')
class ServiceRepositoryDao implements ServiceDao {
    protected NamedParameterJdbcTemplate jdbcTemplate

    @Autowired
    public setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource)
    }

    @Override
    def <T> T getAs(String key, Class<T> clazz, T defaultValue) {
        try {
            final T object = jdbcTemplate.queryForObject("select value from configuration where name = :name", [name: key], clazz)
            return object
        } catch (EmptyResultDataAccessException e) {
            jdbcTemplate.update('''insert into configuration(name, value) values(:name, :value)''', [name: key, value: defaultValue])
            return defaultValue
        }
    }

    @Override
    def <T> T update(String key, Class<T> clazz, T value) {
        try {
            final T object = jdbcTemplate.queryForObject("select value from configuration where name = :key", [name: key], clazz)
            jdbcTemplate.update('''update configuration set value = :value where name = :key''', [name: key, value: value])
        } catch (EmptyResultDataAccessException e) {
            jdbcTemplate.update('''insert into configuration(name, value) values(:name, :value)''', [name: key, value: value])
        }
        return value
    }
}
