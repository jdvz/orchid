package club.orchid.domain

import club.orchid.anno.mapping.Persistent

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/2/16 10:47 AM
 */
abstract class AbstractMapper implements Serializable {
    private static final long serialVersionUID = 1l

    def findPersistentProperties() {
        this.properties.findAll { prop ->
            this.getClass().declaredFields.find { it.name == prop.key && Persistent.class in it.declaredAnnotations*.annotationType() }
        }
    }
}
