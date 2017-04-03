package club.orchid.service.impl

import club.orchid.dao.config.ServiceDao
import club.orchid.service.IConfigService
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 1/8/17 10:04 PM
 */
@Service
class ConfigService implements IConfigService {
    private static final Logger log = Logger.getLogger(ConfigService.class.name)

    @Autowired
    ServiceDao serviceDao

    String getString(final String key, final String defaultValue) {
        return serviceDao.getAs(key, String.class, defaultValue)
    }

    @Override
    int getInt(String key, int defaultValue) {
        return serviceDao.getAs(key, Integer.class, defaultValue)
    }

    @Override
    int updateInt(String key, int value) {
        serviceDao.update(key, Integer.class, value)
    }
}
