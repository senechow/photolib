<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!-- Modal Dialog -->
<div class="modal fade" id="confirmDelete">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="modal-title">Delete Content</h4>
			</div>
			<div class="modal-body">
				<p>Are you sure you want to delete this?</p>
			</div>
			<div class="modal-footer">
				<button type="submit" class="btn btn-danger" id="confirm">Delete</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
			</div>
		</div>
	</div>
</div>