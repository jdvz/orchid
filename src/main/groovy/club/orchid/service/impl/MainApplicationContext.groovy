package club.orchid.service.impl

import club.orchid.anno.strategy.PageStrategy
import club.orchid.domain.cms.Page
import club.orchid.strategy.PageContentStrategy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.stereotype.Component
/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/3/16 10:36 AM
 */
@Component
@Qualifier('context')
class MainApplicationContext {
    @Autowired
    @Qualifier('applicationContext')
    ApplicationContext context

    public <T extends Page<T>, S extends PageContentStrategy<T>> S getPageContentStrategy(String discriminator) {
        Map<String, Object> beans = context.getBeansWithAnnotation(PageStrategy.class)

        def bean = beans.values().find {
            AnnotationUtils.findAnnotation(it.getClass(), PageStrategy.class)?.value()?.simpleName?.equals(discriminator)
        }
        return bean == null ? null : bean as S
    }
}
