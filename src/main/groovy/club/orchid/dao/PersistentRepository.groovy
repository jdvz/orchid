package club.orchid.dao

import club.orchid.domain.AbstractMappedPersistent

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 2/14/16 5:46 PM
 */
interface PersistentRepository<T extends AbstractMappedPersistent<T>> {
    public Optional<T> get(final long id, Class<T> clazz)
    public Optional<T> get(final Map<String, Object> params, Class<T> clazz)
}