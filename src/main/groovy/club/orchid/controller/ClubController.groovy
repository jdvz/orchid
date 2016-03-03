package club.orchid.controller

import club.orchid.service.IUserService
import club.orchid.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

import java.security.Principal

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/2/16 5:21 PM
 */
@Controller
@RequestMapping('/club')
@Secured(['ROLE_USER', 'ROLE_ADMIN'])
class ClubController {
    @Autowired
    IUserService userService

    @RequestMapping(value = '/index.html', method = RequestMethod.GET)
    public String club(final Model model, final Principal principal) {
        model.addAttribute('DECORATOR_TITLE', 'Home page')
        model.addAttribute('CONTENT_TITLE', 'Main content')
        return 'club'
    }

    @Secured(['ROLE_ADMIN'])
    @RequestMapping(value = '/users.html', method = RequestMethod.GET)
    public String users(final Model model) {
        model.addAttribute('DECORATOR_TITLE', 'Home page')
        model.addAttribute('CONTENT_TITLE', 'Main content')
        model.addAttribute('users', userService.allUsers)
        return 'users'
    }


}
