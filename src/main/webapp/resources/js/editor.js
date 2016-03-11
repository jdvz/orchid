function loadCkEditor() {
    CKEDITOR.replace('editor1', {
        filebrowserBrowseUrl: '/images/browse.html',
        filebrowserUploadUrl: '/images/upload.html'
    });


    // Integrate Rails CSRF token into file upload dialogs (link, image, attachment and flash)

//            updateEditor();
//            type.addEventListener('change', updateEditor);
}
function updateEditor() {
    var editor = document.getElementById('editor');
    var type = document.getElementById('type');
    if (type.value == 'CmsPage') {
        editor.classList.remove('hidden');
    } else {
        editor.classList.add('hidden');
    }
}
