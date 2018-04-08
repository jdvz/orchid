package club.orchid.config

import groovy.util.logging.Log
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import org.thymeleaf.spring4.SpringTemplateEngine

import javax.servlet.annotation.WebListener

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 2/14/16 12:56 PM
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = ['club.orchids'])
@ConditionalOnClass(SpringTemplateEngine.class)
@WebListener
@Log
class WebConfigurer extends WebMvcConfigurerAdapter {

    @Override
    void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler('/resources/**')
                .addResourceLocations('/resources/')
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
        resourceBundleMessageSource.setBasename('base');
        resourceBundleMessageSource.setDefaultEncoding('UTF-8')
        resourceBundleMessageSource.setBasename("message");
        return resourceBundleMessageSource;
    }
}
