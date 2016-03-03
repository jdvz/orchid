package club.orchid.config

import groovy.text.TemplateEngine
import nz.net.ultraq.thymeleaf.LayoutDialect
import org.slf4j.LoggerFactory
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.web.servlet.ViewResolver
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import org.springframework.web.servlet.view.InternalResourceViewResolver
import org.thymeleaf.spring4.SpringTemplateEngine
import org.thymeleaf.spring4.view.ThymeleafViewResolver
import org.thymeleaf.templateresolver.ServletContextTemplateResolver
import org.thymeleaf.templateresolver.TemplateResolver

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 2/14/16 12:56 PM
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = ['club.orchids'])
//@EnableConfigurationProperties(ThymeleafProperties.class)
@ConditionalOnClass(SpringTemplateEngine.class)
//@AutoConfigureAfter(WebMvcAutoConfiguration.class)
class WebConfigurer extends WebMvcConfigurerAdapter {
    public static final Logger logger = LoggerFactory.getLogger(WebConfigurer.class)

    @Override
    void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler('/resources/**')
                .addResourceLocations('/resources/')
    }

    @Bean
    public ServletContextTemplateResolver templateResolver() {
        final ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver()
        templateResolver.prefix = '/WEB-INF/template/'
        templateResolver.suffix = '.html'
        templateResolver.templateMode = 'HTML5'
        templateResolver.cacheable = false
        templateResolver
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        def templateEngine = new SpringTemplateEngine()
        templateEngine.setTemplateResolver(templateResolver())
        templateEngine.addDialect(new LayoutDialect())
        return templateEngine
    }

    @Bean
    public ThymeleafViewResolver viewResolver() {
        final ThymeleafViewResolver resolver = new ThymeleafViewResolver()
        resolver.setTemplateEngine(templateEngine())
        resolver.setOrder(1)
        resolver.setViewNames('*.html')
        return resolver
    }

    @Bean
    public SessionLocaleResolver sessionLocaleResolver() {
        final SessionLocaleResolver resolver = new SessionLocaleResolver()
        resolver.setDefaultLocale(Locale.ENGLISH)
        resolver
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
        resourceBundleMessageSource.setBasename("base");
        return resourceBundleMessageSource;
    }
}
