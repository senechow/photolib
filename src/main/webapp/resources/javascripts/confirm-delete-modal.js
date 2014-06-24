$('#confirmDelete').on('show.bs.modal', function(e) {
	$message = $(e.relatedTarget).attr('data-message');
	$(this).find('.modal-body p').text($message);
	$title = $(e.relatedTarget).attr('data-title');
	$(this).find('.modal-title').text($title);

	// Pass form reference to modal for submission on yes/ok
	var form = $(e.relatedTarget).closest('form');
	$(this).find('.modal-footer #confirm').data('form', form);
});

// Form confirm (yes/ok) handler, submits form
$('#confirmDelete').find('.modal-footer #confirm').on('click', function() {
	$(this).data('form').submit();
});

function centerModal() {
	$(this).css('display', 'block');
	var $dialog = $(this).find(".modal-dialog");
	var offset = ($(window).height() - $dialog.height()) / 2;
	// Center modal vertically in window
	$dialog.css("margin-top", offset);
}

$('.modal').on('show.bs.modal', centerModal);
$(window).on("resize", function() {
	$('.modal:visible').each(centerModal);
});
