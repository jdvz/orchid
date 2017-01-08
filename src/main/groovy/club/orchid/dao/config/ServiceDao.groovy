package club.orchid.dao.config

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 1/8/17 10:10 PM
 */
interface ServiceDao {
    public <T> T getAs(String key, Class<T> clazz, T defaultValue)

    public <T> T update(String key, Class<T> clazz, T value)
}