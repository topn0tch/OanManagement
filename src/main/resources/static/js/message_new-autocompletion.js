$(document).ready(function () {
    var options = {

        url: "http://localhost:8080/api/users",

        getValue: "username",

        list: {
            match: {
                enabled: true
            },
            maxNumberOfElements: 4,
            showAnimation: {
                type: "fade", //normal|slide|fade
                time: 400,
                callback: function() {}
            },

            cssClasses: "form-control",

            hideAnimation: {
                type: "slide", //normal|slide|fade
                time: 400,
                callback: function() {}
            }
        },
    };

    $("#reciepent").easyAutocomplete(options);
})