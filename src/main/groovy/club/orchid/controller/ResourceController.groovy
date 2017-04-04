package club.orchid.controller

import club.orchid.domain.cms.Image
import club.orchid.service.IResourceService
import club.orchid.util.FrontendUtils
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpRequest
import org.springframework.http.MediaType
import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

import java.security.Principal

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/3/16 3:30 PM
 */
@Controller
@RequestMapping(value = '/images')
class ResourceController extends AbstractController {
    private static final Logger log = Logger.getLogger(ResourceController.class.name)

    @Value('${allow.images:jpeg,png}')
    String allowedMimeTypes

    @Autowired
    private IResourceService resourceService

    @Secured('ROLE_ADMIN')
    @RequestMapping(value = '/upload.html', method = RequestMethod.GET)
    public String uploadForm(final Model model, final Principal principal) {
        model.addAttribute('user', userService.loadUserByUsername(principal?.name))
        return 'images/upload'
    }

    @Secured('ROLE_ADMIN')
    @ResponseBody
    @RequestMapping(value = '/upload.html', method = RequestMethod.POST)
    public String handleUpload(@RequestParam(required = false) String name,
                               @RequestParam(required = false) String prettyUrl,
                               @RequestParam(value = "CKEditor", required = false) String ckEditor,
                               @RequestParam("upload") final MultipartFile file,
                               final Model model,
                               final RedirectAttributes redirectAttributes) {
        String message
        Image image = null
        if (!file.isEmpty()) {
            final String contentType = file.contentType
            if (contentType in allowedMimeTypes.split(/\s?,\s?/)) {
                name = name ?: FrontendUtils.createFileName(file.originalFilename)
                prettyUrl = prettyUrl ?: name
                image = resourceService.getOrCreateImage(name, prettyUrl, contentType)
                try {
                    resourceService.save(image, file)

                    message = "You successfully uploaded $name"
                }
                catch (Exception e) {
                    message = "You failed to upload $name: ${e.getMessage()}"
                }
            } else {
                message = "Content type $contentType not allowed"
            }
        }
        else {
            message = "You failed to upload $name because the file was empty"
        }
        if (ckEditor) {
/*
            model.addAttribute('errorMessage', message)
            model.addAttribute('uploadImage', FrontendUtils.createImageUrl(image))
            return 'system/responseUploadImage'
*/
            return [
                    'uploaded': 1,
                    'fileName': image.realName,
                    'url': "${image.realDir}"
            ]
        } else {
            if (message) {
                redirectAttributes.addFlashAttribute('message', message)
            }
            return "redirect:/images/upload.html"
        };
    }

    @Secured('ROLE_ADMIN')
    @RequestMapping(value = '/upload.html', method = RequestMethod.POST, headers = 'x-requested-with=XMLHttpRequest')
    @ResponseBody
    public HttpEntity handleUpload(@RequestParam(value = "CKEditor", required = true) String ckEditor, @RequestParam("upload") final MultipartFile file) {
        log.info("Loading image from ${ckEditor ?: 'unknown source'}")
        if (!file.isEmpty()) {
            final String contentType = file.contentType
            final String fileName = FrontendUtils.createFileName(file.originalFilename)
            if (contentType in allowedMimeTypes.split(/\s?,\s?/)) {
                Image image = resourceService.createImage(fileName, contentType)
                try {
                    resourceService.save(image, file)
                }
                catch (Exception e) {
                    return new HttpEntity(['status': 'fail', message: 'No file present'])
                }
            } else {
                return new HttpEntity([status: 'fail', message: "Content type $contentType not allowed"])
            }
        }
        else {
            return new HttpEntity([status: 'fail', message: "You failed to upload $name because the file was empty"])
        }

        return new HttpEntity(['status': 'ok'])
    }

    @RequestMapping(value = '/{uid}.{mime}', method = RequestMethod.GET)
    @ResponseBody
    public HttpEntity<Object> get(@PathVariable final String uid, @PathVariable final String mime, final RedirectAttributes redirectAttributes) {
        Image image = resourceService.getImageFullContentByUid(uid)
        if (image != null) {
            HttpHeaders headers = new HttpHeaders()
            headers.setContentType(MediaType.valueOf(image.mime))
            headers.setContentLength(image.bytes.length)
//            headers.set('Content-Disposition', "attachment; filename=\"${image.prettyUrl}.$mime\"")
            return new HttpEntity<>(image.bytes, headers)
        }
        return HttpEntity.EMPTY
    }


    @RequestMapping(value = '/browse.html', method = RequestMethod.GET)
    public String images(@RequestParam(value = "CKEditor", required = false) String ckEditor, final Model model, final RedirectAttributes redirectAttributes) {
        Collection<String> images = resourceService.getImageNames("")
        model.addAttribute('links', images)
        model.addAttribute('message', 'list')
        return "images/browse"
    }
}
