package club.orchid.controller

import club.orchid.domain.auth.User
import club.orchid.service.IUserService
import club.orchid.web.forms.UserObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes

import javax.validation.Valid
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
        model.addAttribute('user', userService.loadUserByUsername(principal?.name))
        return 'club'
    }

    @Secured(['ROLE_ADMIN'])
    @RequestMapping(value = '/users.html', method = RequestMethod.GET)
    public String users(final Model model) {
        model.addAttribute('users', userService.getAllUsers())
        return 'club/users'
    }

    @Secured(['ROLE_ADMIN'])
    @RequestMapping(value = '/edit.html', method = RequestMethod.GET)
    public String edit(@RequestParam final Long id, final Model model) {
        final User user = userService.getUserById(id).orElseThrow({
            throw new IllegalArgumentException("User ${id} not found")
        })

        def object = new UserObject(user)
        object.initRoles(userService.roles())
        model.addAttribute('userObject', object)

        return 'club/edit'
    }

    @Secured(['ROLE_ADMIN'])
    @RequestMapping(value = '/create.html', method = RequestMethod.GET)
    public String create(final Model model) {
        def object = new UserObject()
        object.initRoles(userService.roles())
        model.addAttribute('userObject', object)
        return 'club/edit'
    }

    @Secured('ROLE_ADMIN')
    @RequestMapping(value = '/edit.html', method = RequestMethod.POST)
    public String save(@Valid final UserObject userObject, final RedirectAttributes redirectAttributes) {
        if (userObject.password && userObject.password != userObject.passwordRepeated) {
            redirectAttributes.addAttribute('message', 'Password doesn\'t match')
            return userObject.userId > 0 ? "redirect:/club/edit.html?id=${userObject.userId}" : "redirect:/club/create.html"
        }
        userService.save(userObject)
        return "redirect:/club/users.html"
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String error(final Model model, final Exception e) {
        model.addAttribute('message', e.message)
        return '404'
    }



}
