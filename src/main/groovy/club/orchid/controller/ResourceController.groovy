package club.orchid.controller

import club.orchid.domain.cms.Image
import club.orchid.service.IResourceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.FileCopyUtils
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes

import javax.annotation.Resource
import java.security.Principal

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/3/16 3:30 PM
 */
@Controller
@RequestMapping(value = '/images')
class ResourceController extends AbstractController {
    @Value('${path.images:/opt/resources/images}')
    String pathImages
    @Value('${allow.images:jpeg,png}')
    String allowedMimeTypes

    @Autowired
    private IResourceService resourceService

    @RequestMapping(value = '/upload.html', method = RequestMethod.GET)
    public String uploadForm(final Model model, final Principal principal) {
        model.addAttribute('user', userService.loadUserByUsername(principal?.name))
        return 'images/upload'
    }

    @RequestMapping(value = '/upload.html', method = RequestMethod.POST)
    public String handleUpload(@RequestParam(required = false) String name,
                               @RequestParam(required = false) String prettyUrl,
                               @RequestParam("file") final MultipartFile file,
                               final RedirectAttributes redirectAttributes) {
        if (!file.isEmpty()) {
            final String contentType = file.contentType
            if (contentType in allowedMimeTypes.split(/\s?,\s?/)) {
                name = name ?: file.name
                prettyUrl = prettyUrl ?: name
                Image image = resourceService.getOrCreateImage(name, prettyUrl)
                try {
                    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File("$pathImages/$image.realName")));
                    FileCopyUtils.copy(file.getInputStream(), stream);
                    stream.close();
                    image.mime = file.contentType
                    resourceService.save(image)
                    redirectAttributes.addFlashAttribute("message", "You successfully uploaded $name");
                }
                catch (Exception e) {
                    redirectAttributes.addFlashAttribute("message", "You failed to upload $name: ${e.getMessage()}");
                }
            } else {
                redirectAttributes.addFlashAttribute("message", "Content type $contentType not allowed");
            }
        }
        else {
            redirectAttributes.addFlashAttribute("message", "You failed to upload $name because the file was empty");
        }

        return "redirect:/images/upload.html";
    }

    @RequestMapping(value = '/{uid}.{mime}', method = RequestMethod.GET)
    @ResponseBody
    public HttpEntity<byte[]> get(@PathVariable final String uid, @PathVariable final String mime, final RedirectAttributes redirectAttributes) {
        final Optional<Image> optImage = resourceService.getImageByUid(uid)
        if (optImage.isPresent()) {
            final Image image = optImage.get()
            File file = new File("$pathImages/$image.realName")
            if (file.exists()) {
                HttpHeaders headers = new HttpHeaders()
                headers.setContentType(MediaType.valueOf(image.mime))
                headers.setContentLength(file.length())
                return new HttpEntity<>(file.bytes, headers)
            }
        }
        return HttpEntity.EMPTY
    }
}
