package club.orchid.domain

import club.orchid.anno.mapping.Persistent

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/2/16 10:47 AM
 */
abstract class AbstractPersistent<T extends AbstractPersistent> implements Serializable {
    private static final long serialVersionUID = 1l
    def storage = [:]

    boolean lazy

    def methodMissing(String name, def args) {
        return null
    }

    def propertyMissing(String name) {
        storage[name]
    }

    def propertyMissing(String name, def value) {
        storage[name] = value
    }

    boolean isLazy() { return lazy }
}
