package club.orchid.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

import javax.sql.DataSource
/**
 * Created by zera on 2/14/16.
 */
@Configuration
@ComponentScan(basePackages = ['club.orchids'])
@EnableConfigurationProperties
public class JdbcConfigurer {
    Logger log = LoggerFactory.getLogger(JdbcConfigurer.class)

    @Bean
    @ConfigurationProperties(prefix = 'spring.datasource')
    public DataSource dataSource() {
        DataSourceBuilder.create().build()
    }

/*
    @Bean
    public NamedParameterJdbcTemplate jdbcTemplate() {
        log.info "create jdbc"
        new NamedParameterJdbcTemplate(dataSource())
    }
*/
}
