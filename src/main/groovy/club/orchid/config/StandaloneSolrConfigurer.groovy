package club.orchid.config

import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.data.solr.core.SolrTemplate
import org.springframework.data.solr.repository.config.EnableSolrRepositories
import org.springframework.data.solr.server.support.HttpSolrClientFactoryBean
/**
 *c
 * @author Dmitri Zaporozhtsev <dmitri.zaporozhtsev@novardis.com>
 * @link http://www.novardis.com/
 * @copyright 2016 NOVARDIS
 */
@Configuration
@EnableSolrRepositories(basePackages = ['club.orchid.dao.product'], multicoreSupport = true)
@Profile("standalone")
@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true)
//@AutoConfigureAfter(WebSecurityConfigurer.class)
class StandaloneSolrConfigurer {
    private static final Logger log = Logger.getLogger(StandaloneSolrConfigurer.class.getName())

    @Value('${solr.host}')
    private String solrHost;

    @Bean
    public HttpSolrClientFactoryBean solrServerFactoryBean() {
        log.info("init solr factory for path ${solrHost}")
        HttpSolrClientFactoryBean factory = new HttpSolrClientFactoryBean();

        factory.setUrl(solrHost);

        return factory;
    }

    @Bean
    public SolrTemplate solrTemplate() throws Exception {
        return new SolrTemplate(solrServerFactoryBean().getObject());
    }
}
