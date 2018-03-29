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
	console.log("Loading page");
	$("#content").children().remove();
	$.getJSON("/service", function (data) {
		$.each(data, function (k, v) {
			if (k === "services") {
				$.each(v, function(key, val) {
				    $("<tr><td>" + val.id + "</td><td>" + val.name + "</td><td>" + val.url + "</td>" +
			            "<td>" + val.status + "</td>" +
			            "<td>" +
			            "<button class='delbtn' " +
			            "data-name='" + val.name + "' " +
			            "data-url='" + val.url + "' " +
			            "data-id='" + val.id + "' " +
			            "data-status='" + val.status + "'>Delete</button>" +
			            "</td>" +
			            "</tr>").appendTo("#content");
				})
			}
		})
	});
	initCallbacks();
}

function initCallbacks() {
    $("#content").unbind().on("click", ".delbtn", function() {
        var id = $(this).data("id");
        console.log("Pressing delete button: "+id);
        remove(id);
    });
    
    $("#add-btn").unbind().click(function() {
    	var name = $("#input-name").val();
    	var url = $("#input-url").val();
    	$("#input-name").val('');
    	$("#input-url").val('');
    	console.log("Pressing add button: "+name+", "+url);
    	create(name, url);
    });
}
