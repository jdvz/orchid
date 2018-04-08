package club.orchid.config

import nz.net.ultraq.thymeleaf.LayoutDialect
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.ViewResolver
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.thymeleaf.TemplateEngine
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect
import org.thymeleaf.spring4.SpringTemplateEngine
import org.thymeleaf.spring4.view.ThymeleafViewResolver
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ITemplateResolver
import org.thymeleaf.templateresolver.ServletContextTemplateResolver

import javax.servlet.ServletContext
import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener
import javax.servlet.annotation.WebListener

/**
 * Created by zera on 4/7/18.
 */
@Configuration
@EnableWebMvc
@ComponentScan("club.orchid")
@WebListener
public class ThymeleafConfigurer implements ServletContextListener {
  private ApplicationContext applicationContext
  private ServletContext servletContext;
  private ITemplateResolver templateResolver;

  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext
  }

  @Bean
  public ViewResolver viewResolver() {
    ThymeleafViewResolver resolver = new ThymeleafViewResolver()
    resolver.setTemplateEngine(TemplateEngineUtil.getTemplateEngine(servletContext))
    resolver.setCharacterEncoding("UTF-8")
    resolver.setViewNames('*.html')
    resolver.setOrder(1)
    resolver
  }

  public TemplateEngine templateEngine(ServletContext servletContext) {
    SpringTemplateEngine engine = new SpringTemplateEngine()
//    engine.setEnableSpringELCompiler(true)
    engine.setTemplateResolver(templateResolver(servletContext))
    engine.addDialect(new LayoutDialect())
    engine.setAdditionalDialects(Collections.singleton(new SpringSecurityDialect()))
    engine
  }

  @Bean
  public ITemplateResolver templateResolver() {
    return templateResolver
  }

  protected ITemplateResolver templateResolver(ServletContext servletContext) {
    final ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext)
    templateResolver.prefix = '/WEB-INF/template/'
    templateResolver.suffix = '.html'
    templateResolver.templateMode = TemplateMode.HTML
    templateResolver.cacheable = false
    templateResolver.setCheckExistence(true)
    this.templateResolver = templateResolver
    templateResolver
  }

  @Override
  void contextInitialized(ServletContextEvent sce) {
    TemplateEngine engine = templateEngine(sce.getServletContext());
    this.servletContext = sce.getServletContext()
    TemplateEngineUtil.storeTemplateEngine(sce.getServletContext(), engine);
  }

  @Override
  void contextDestroyed(ServletContextEvent sce) {

  }
}
