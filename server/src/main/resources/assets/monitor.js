$(function () {
    load();
});

function create(name, url) {
	$.post("/service", JSON.stringify({name: name, url: url}), function () {
        load();
    }, "json");
}

function remove(id) {
	$.ajax({
        method: "DELETE",
        url: "/service/" + id
    }).done(function () {
        load();
    });
}

function load() {
	$("#content").children().remove();
	$.getJSON("/service", function (data) {
		$.each(data, function (key, val) {
			$("<tr><td>" + val.id + "</td><td>" + val.name + "</td><td>" + val.url + "</td>" +
	            "<td>" + val.status + "</td>" +
	            "<td>" +
	            "<button data-action='delete' class='btn delete-btn' " +
	            "data-name='" + val.name + "' " +
	            "data-url='" + val.url + "' " +
	            "data-id='" + val.id + "' " +
	            "data-status='" + val.status + "'></button>" +
	            "</td>" +
	            "</tr>").appendTo("#content");
		}
	}
	initCallbacks();
}

function initCallbacks() {
    $(".delete-btn").unbind().click(function() {
       var id = $(this).data("id");
       remove(id);
    });
    
    $("#add-btn").unbind().click(function() {
    	var name = $("#input-name").val();
    	var url = $("#input-url").val();
    	create(name, url);
    });
}