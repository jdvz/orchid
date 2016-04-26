package club.orchid.config

//import org.apache.solr.client.solrj.SolrServer
//import org.apache.solr.client.solrj.impl.HttpSolrServer
import org.springframework.beans.factory.annotation.Autowired
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
@Profile("standalone")
@PropertySource("classpath:application.properties")
@AutoConfigureAfter(WebSecurityConfigurer.class)
class StandaloneSolrConfigurer {
    static final String SOLR_HOST = 'solr.host';
    public static final String SOLR_HOME = 'solr.home'

    @Autowired
    private Environment environment;

/*
    @Bean
    public HttpSolrServerFactoryBean solrServerFactoryBean() {
        HttpSolrServerFactoryBean factory = new HttpSolrServerFactoryBean();

        factory.setUrl(environment.getRequiredProperty("solr.host"));

        return factory;
    }

    @Bean
    public SolrTemplate solrTemplate() throws Exception {
        return new SolrTemplate(solrServerFactoryBean().getObject());
    }
*/
}
