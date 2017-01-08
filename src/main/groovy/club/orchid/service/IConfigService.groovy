package club.orchid.service

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 1/8/17 9:24 PM
 */
interface IConfigService {
    String getString(final String key, final String defaultValue)
    int getInt(final String key, final int defaultValue)
    int updateInt(String key, int value)
}