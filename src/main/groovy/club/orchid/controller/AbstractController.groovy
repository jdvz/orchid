package club.orchid.controller

import club.orchid.service.IUserService
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/3/16 12:09 AM
 */
class AbstractController {
    @Autowired
    protected IUserService userService
}
