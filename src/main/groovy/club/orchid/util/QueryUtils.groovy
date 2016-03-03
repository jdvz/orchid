package club.orchid.util

import club.orchid.anno.mapping.Persistent
import club.orchid.domain.AbstractMappedPersistent
import org.springframework.core.annotation.AnnotationUtils

import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 2/14/16 6:11 PM
 */
class QueryUtils {
    private QueryUtils() {
    }

    public static <T extends AbstractMappedPersistent<T>> String generateQualifiedSqlName(Class<T> clazz) {
        return "${generateQualifiedSqlName(clazz.simpleName)}s"
    }

    public static String generateQualifiedSqlName(String name) {
        return "${name.replaceAll(/([^\*])([A-Z])/, '$1_$2').toLowerCase()}"
    }

    public static String generateQualifiedSqlName(Persistent anno, String name) {
        return anno == null ? null : anno.value() ?: "${name.replaceAll(/([^\*])([A-Z])/, '$1_$2').toLowerCase()}"
    }

    private static <T extends AbstractMappedPersistent<T>> List<Field> getDeclaredFields(Class<T> clazz, List<Field> fields)  {
        fields.addAll(clazz.getDeclaredFields())
        if (clazz.is(AbstractMappedPersistent)) {
            return fields
        } else {
            return getDeclaredFields(clazz.superclass, fields)
        }
    }

    private static <T extends AbstractMappedPersistent<T>> ArrayList<String> getAnnotatedFields(Class<T> clazz) {
        getDeclaredFields(clazz, [])
                .collect { it -> generateQualifiedSqlName(AnnotationUtils.findAnnotation(it, Persistent.class), it.name) }
                .findAll { it }
    }

    public static <T extends AbstractMappedPersistent<T>> String createInsertString(Class<T> clazz) {
        List<String> declaredFields = getAnnotatedFields(clazz)
        """insert into ${generateQualifiedSqlName(clazz)}(${declaredFields.join(',')})
    values (${declaredFields.collect { ":$it" }.join(',')})"""
    }

    public static final String createUpdateString(Class<? extends AbstractMappedPersistent> clazz) {
        List<String> declaredFields = getAnnotatedFields(clazz)
        """update ${generateQualifiedSqlName(clazz)} set
    ${declaredFields.collect { "$it = :$it" }.join(' ')})"""
    }

    public static String createWhereString(Map<String, Object> params) {
        """${params.collect { k, v -> "$k = :$k" }.join(' AND ')}"""
    }

    @Deprecated
    private static boolean checkAssessibility(Method it) {
//        it.name.startsWith('get') && Modifier.isPublic(it.modifiers) && !Modifier.isStatic(it.modifiers)
        it.name.startsWith('get') && !it.name.startsWith('getId') && Modifier.PUBLIC == it.modifiers
    }

    public static <T extends AbstractMappedPersistent<T>> Map createPersistParams(T obj) {
        getDeclaredFields(obj.class, []).collectEntries { [(generateQualifiedSqlName(AnnotationUtils.findAnnotation(it, Persistent.class), it.name)): obj[it.name]] }
    }
}
