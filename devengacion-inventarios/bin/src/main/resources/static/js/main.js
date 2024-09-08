(function() {
	"use strict";

	var treeviewMenu = $('.app-menu');

	// Toggle Sidebar
	$('[data-toggle="sidebar"]').click(function(event) {
		event.preventDefault();
		$('.app').toggleClass('sidenav-toggled');
	});

	// Activate sidebar treeview toggle
	$("[data-toggle='treeview']").click(function(event) {
		event.preventDefault();
		if (!$(this).parent().hasClass('is-expanded')) {
			treeviewMenu.find("[data-toggle='treeview']").parent().removeClass('is-expanded');
		}
		$(this).parent().toggleClass('is-expanded');
	});

	// Set initial active toggle
	$("[data-toggle='treeview.'].is-expanded").parent().toggleClass('is-expanded');

	//Activate bootstrip tooltips
	$("[data-toggle='tooltip']").tooltip();

})();
function controlTag(e) {
	tecla = (document.all) ? e.keyCode : e.which;
	if (tecla == 8) return true;
	else if (tecla == 0 || tecla == 9) return true;
	patron = /[0-9\s]/;
	n = String.fromCharCode(tecla);
	return patron.test(n);
}

function testText(txtString) {
	let stringText = new RegExp(/^[a-zA-ZÑñÁáÉéÍíÓóÚúÜü\s]+$/);
	if (stringText.test(txtString)) {
		return true;
	} else {
		return false;
	}
}
function testPrice(price) {
	let stringPrice = new RegExp(/^\d*(\.\d{1})?\d{0,1}$/);
	if (stringPrice.test(price)) {
		return true;
	} else {
		return false;
	}
}
function testEntero(intCant) {
	let intCantidad = new RegExp(/^([0-9])*$/);
	if (intCantidad.test(intCant)) {
		return true;
	} else {
		return false;
	}
}
function testQuantity(intCant) {
	let intQuantity = new RegExp(/^([0-9])*$/);
	if (intQuantity.test(intCant) && intCant > 0) {
		return true;
	} else {
		return false;
	}
}
function fntEmailValidate(email) {
	var stringEmail = new RegExp(/^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})$/);
	if (stringEmail.test(email) == false) {
		return false;
	} else {
		return true;
	}
}

function fntValidText() {
	let validText = document.querySelectorAll(".validText");
	validText.forEach(function(validText) {
		validText.addEventListener('keyup', function() {
			let inputValue = this.value;
			if (!testText(inputValue)) {
				this.classList.add('is-invalid');
			} else {
				this.classList.remove('is-invalid');
			}
		});
	});
}

function fntValidNumber() {
	let validNumber = document.querySelectorAll(".validNumber");
	validNumber.forEach(function(validNumber) {
		validNumber.addEventListener('keyup', function() {
			let inputValue = this.value;
			if (!testEntero(inputValue)) {
				this.classList.add('is-invalid');
			} else {
				this.classList.remove('is-invalid');
			}
		});
	});
}

function fntValidEmail() {
	let validEmail = document.querySelectorAll(".validEmail");
	validEmail.forEach(function(validEmail) {
		validEmail.addEventListener('keyup', function() {
			let inputValue = this.value;
			if (!fntEmailValidate(inputValue)) {
				this.classList.add('is-invalid');
			} else {
				this.classList.remove('is-invalid');
			}
		});
	});
}
function fntValidPrice() {
	let validPrice = document.querySelectorAll(".validPrice");
	validPrice.forEach(function(validPrice) {
		validPrice.addEventListener('keyup', function() {
			let inputValue = this.value;
			if (!testPrice(inputValue)) {
				this.classList.add('is-invalid');
			} else {
				this.classList.remove('is-invalid');
			}
		});
	});
}
function fntValidQuantity() {
	let validQuantity = document.querySelectorAll(".validQuantity");
	validQuantity.forEach(function(validQuantity) {
		validQuantity.addEventListener('keyup', function() {
			let inputValue = this.value;
			if (!testQuantity(inputValue)) {
				this.classList.add('is-invalid');
			} else {
				this.classList.remove('is-invalid');
			}
		});
	});
}

window.addEventListener('load', function() {
	fntValidText();
	fntValidEmail();
	fntValidNumber();
	fntValidPrice();
	fntValidQuantity();
}, false);

document.addEventListener('DOMContentLoaded', function() {
	$('#sampleTable').DataTable({
		"aProcessing": true,
		"aServerSide": true,
		'dom': 'lBfrtip',
		'buttons': [{
			"extend": "excelHtml5",
			"text": "<i class='fas fa-file-excel'></i> Excel",
			"titleAttr": "Esportar a Excel",
			"className": "btn btn-success"
		}, {
			"extend": "pdfHtml5",
			"text": "<i class='fas fa-file-pdf'></i> PDF",
			"titleAttr": "Esportar a PDF",
			"className": "btn btn-danger"
		}
		],
		"resonsieve": "true",
		"bDestroy": true,
		"iDisplayLength": 10,
		"order": [[0, "desc"]]
	});
});
$('#tableUsuarios').DataTable();
$('#tableRoles').DataTable();
