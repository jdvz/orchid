package club.orchid.dao

import club.orchid.domain.AbstractMappedPersistent
import club.orchid.service.impl.MainApplicationContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 2/14/16 5:59 PM
 */
@Component
class MapperHolder {
    private static final Map<Class<? extends AbstractMappedPersistent>, RowMapper> mappers = new HashMap<>()

    @Autowired
    private MainApplicationContext context

    public <T extends AbstractMappedPersistent<T>> RowMapper<T> getOrCreateMapper(Class<T> clazz) {
        final RowMapper<T> mapper = mappers.get(clazz)
        if (mapper) {
            return mapper
        } else {
            final Class<RowMapper<T>> pageMapperClazz = this.class.classLoader.loadClass("club.orchid.dao.mapper.${clazz.simpleName}Mapper", true )
            final RowMapper<T> newMapper = context.getContext().getBean(pageMapperClazz)
            mappers.put(clazz, newMapper)
            return newMapper
        }
    }
}
