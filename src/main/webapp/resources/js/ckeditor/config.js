/**
 * @license Copyright (c) 2003-2016, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here.
	// For complete reference see:
	// http://docs.ckeditor.com/#!/api/CKEDITOR.config

	// The toolbar groups arrangement, optimized for two toolbar rows.
	config.toolbarGroups = [
		{ name: 'clipboard',   groups: [ 'clipboard', 'undo' ] },
		{ name: 'editing',     groups: [ 'find', 'selection', 'spellchecker' ] },
		{ name: 'links' },
		{ name: 'insert' },
		{ name: 'forms' },
		{ name: 'tools' },
		{ name: 'document',	   groups: [ 'mode', 'document', 'doctools' ] },
		{ name: 'others' },
		'/',
		{ name: 'basicstyles', groups: [ 'basicstyles', 'cleanup' ] },
		{ name: 'paragraph',   groups: [ 'list', 'indent', 'blocks', 'align', 'bidi' ] },
		{ name: 'styles' },
		{ name: 'colors' },
		{ name: 'about' }
	];

	// Remove some buttons provided by the standard plugins, which are
	// not needed in the Standard(s) toolbar.
	config.removeButtons = 'Underline,Subscript,Superscript';

	// Set the most common block elements.
	config.format_tags = 'p;h1;h2;h3;pre';

	// Simplify the dialog windows.
	config.removeDialogTabs = 'image:advanced;link:advanced';

	config.filebrowserParams = function() {
		var csrf_token, csrf_param, meta;
		var metas = document.getElementsByTagName('meta');
		var	params = new Object();

		for ( var i = 0 ; i < metas.length ; i++ ){
			meta = metas[i];

			switch(meta.name) {
				case "_csrf_token":
					csrf_token = meta.content;
					break;
				case "_csrf_param":
					csrf_param = meta.content;
					break;
				default:
					continue;
			}
		}

		if (csrf_param !== undefined && csrf_token !== undefined) {
			params[csrf_param] = csrf_token;
		}

		return params;
	};

	config.addQueryString = function( url, params ){
		var queryString = [];

		if ( !params ) {
			return url;
		} else {
			for ( var i in params )
				queryString.push( i + "=" + encodeURIComponent( params[ i ] ) );
		}

		return url + ( ( url.indexOf( "?" ) != -1 ) ? "&" : "?" ) + queryString.join( "&" );
	};
	CKEDITOR.on( 'dialogDefinition', function( ev ){
		// Take the dialog name and its definition from the event data.
		var dialogName = ev.data.name;
		var dialogDefinition = ev.data.definition;
		var content, upload;

		if (CKEDITOR.tools.indexOf(['link', 'image', 'attachment', 'flash'], dialogName) > -1) {
			content = (dialogDefinition.getContents('Upload') || dialogDefinition.getContents('upload'));
			upload = (content == null ? null : content.get('upload'));

			if (upload && upload.filebrowser && upload.filebrowser['params'] === undefined) {
				upload.filebrowser['params'] = config.filebrowserParams();
				upload.action = config.addQueryString(upload.action, upload.filebrowser['params']);
			}
		}
	});

};
