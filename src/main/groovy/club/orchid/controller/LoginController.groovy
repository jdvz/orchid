package club.orchid.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 2/14/16 9:41 PM
 */
@Controller
class LoginController {
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView getLoginPage(@RequestParam Optional<String> error) {
        return new ModelAndView("login", "error", error);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView getLogoutPage(@RequestParam Optional<String> error) {
        return new ModelAndView("login", "error", error);
    }
}
