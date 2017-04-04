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
import org.springframework.data.solr.server.support.EmbeddedSolrServerFactoryBean

/**
 *
 * @author Dmitri Zaporozhtsev <dmitri.zaporozhtsev@novardis.com>
 * @link http://www.novardis.com/
 * @copyright 2016 NOVARDIS
 */
@Configuration
@EnableSolrRepositories(basePackages=['club.orchid.dao.product'], multicoreSupport=true)
@Profile("embedded")
@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true)
@AutoConfigureAfter(WebSecurityConfigurer.class)
class EmbeddedSolrConfigurer {
    private static final Logger log = Logger.getLogger(EmbeddedSolrConfigurer.class.getName())

    @Value('${solr.home}')
    private String solrHome;

    @Bean
    public EmbeddedSolrServerFactoryBean solrServerFactoryBean() {
        log.info("init solr factory for path ${solrHome}")
        EmbeddedSolrServerFactoryBean factory = new EmbeddedSolrServerFactoryBean();

        factory.setSolrHome(solrHome);

        return factory;
    }

    @Bean
    public SolrTemplate solrTemplate() throws Exception {
        return new SolrTemplate(solrServerFactoryBean().getObject());
    }
}
