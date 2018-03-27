$(document).ready(function () {
    window.mobilecheck = function() {
        var check = false;
        (function(a){if(/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce|xda|xiino/i.test(a)||/1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(a.substr(0,4))) check = true;})(navigator.userAgent||navigator.vendor||window.opera);
        return check;
    };

    var startDate;
    var endDate;

    var calendar = $('#calendar').fullCalendar({
        defaultView: Cookies.get('fullCalendarCurrentView') || "month",
        defaultDate: Cookies.get('fullCalendarCurrentDate') || null,

        businessHours: {
            // days of week. an array of zero-based day of week integers (0=Sunday)
            dow: [ 1, 2, 3, 4, 5],

            start: '9:00',
            end: '18:00',
        },
        height: 800,
        aspectRatio: 1,
        editable: true,
        selectable: true,
        resizable: true,
        themeSystem: 'bootstrap3',
        handleWindowResize: true,
        eventLimit: true,
        nowIndicator: true,
        timeFormat: 'H:mm',
        slotLabelFormat: 'H:mm',
        columnHeaderFormat: 'dddd DD/M',
        allDayText: 'Whole day',
        views: {
            month: {
                eventLimit: 4
            }
        },
        customButtons: {
            screenshotButton: {
                text: 'Screenshot',
                click: function() {
                    // window.print();
                    html2canvas(document.getElementById('calendarContainer')).then(function(canvas) {
                        $('#screenshotModal').modal('toggle');
                        $('#screenshotModal #screenshotModalBody').append(canvas);
                    });
                }
            },
            printButton: {
                text: 'Print',
                click: function() {
                    window.print();
                }
            }
        },
        eventColor: '#5C6BC0',
        firstDay: 1,
        googleCalendarApiKey: 'AIzaSyB3TNtPD1CNpwIZW2W2Yqx2LRXBkskgIKs',
        eventSources: [
            {
                url : '/api/event/all'
            },
            {
                googleCalendarId: 'nl.be#holiday@group.v.calendar.google.com',
                className: 'gcal-event',
                color: '#00E676',
                editable: false
            }

        ],

        viewRender: function(view) {
            Cookies.set('fullCalendarCurrentView', view.name, {path: ''});
            Cookies.set('fullCalendarCurrentDate', view.intervalStart.format(), {path: ''});
        },

        eventClick:  function(event, jsEvent, view) {
            if (event.url) {
                return false;
            }

            endtime = $.fullCalendar.moment(event.end).format('h:mm');
            starttime = $.fullCalendar.moment(event.start).format('dddd, MMMM Do YYYY, h:mm');

            start = moment(event.start).format();
            end = moment(event.end).format();

            var mywhen = starttime + ' - ' + endtime;
            $('#modalEditTitle').val(event.title);
            $('#modalEditDesc').val(event.description)
            $('#eventEditID').val(event.id);
            $('#eventEditColour').val(event.backgroundColor);

            $('#editEventModal #startEditTime').val(start);
            $('#editEventModal #endEditTime').val(end);
            $('#editEventModal #editWhen').text(mywhen);

            $('#editEventModal').modal('toggle');
            $("#editEventModal").on('shown.bs.modal', function() {
                $('#modalEditTitle').focus();
            });

            console.log(event.title.substr(0,6));
            if (event.title.substr(0,6) == "To-do:") {
                $('#modalEditTitle').attr("disabled", "disabled");
                $('#modalEditDesc').attr("disabled", "disabled");
                $('#eventEditColour').attr("disabled", "disabled");
                $('#editCancelButton').text("Close");
            } else {
                $('#modalEditTitle').removeAttr('disabled');
                $('#modalEditDesc').removeAttr('disabled');
                $('#eventEditColour').removeAttr('disabled');
                $('#editCancelButton').text("Cancel");
            }

            $('#submitEditButton').on('click', function (e) {
                e.preventDefault();
                $("#editEventModal").modal('hide');
                var eventID = $('#eventEditID').val();
                var start = $('#startEditTime').attr("value");
                var end = $('#endEditTime').attr("value");
                var colour = $('#eventEditColour').val();
                var title = $('#modalEditTitle').val();
                var description = $('#modalEditDesc').val();
                event.id = eventID;
                event.title = title;
                event.description = description;
                event.backgroundColor = colour;
                event.borderColor = colour;
                if (event.title.length == 0 ) {
                    event.title="Event";
                }
                $.ajax({
                    url: 'calendar-updateevent',
                    data: {title: title, start: start, end: end, id: eventID, colour: colour, desc: description},
                    type: "GET",
                    success: function () {
                        $('#calendar').fullCalendar('refetchEventSources', '/api/event/all' );
                    }
                });

            })
        },
        select: function(start, end, jsEvent) {
            var endtime = $.fullCalendar.moment(end).format('dddd, DD/MM/YYYY h:mm');
            var starttime = $.fullCalendar.moment(start).format('dddd, DD/MM/YYYY h:mm'); // dddd, Do MMMM YYYY h:mm
            startDate = start;
            endDate = end;
            var mywhen = starttime + ' - ' + endtime;

            $('#createEventModal #when').text(mywhen);
            $('#createEventModal').modal('toggle');
            $(".fc-highlight").css("background", "#2196F3");

            $("#createEventModal").on('shown.bs.modal', function() {
                $('#theTitle').focus();
            });
        },
        eventDrop: function(event, delta){
            $.ajax({
                url: '/calendar-update',
                data: {title: event.title, start: moment(event.start).format(), end: moment(event.end).format(), id: event.id},
                type: "GET"
            });
        },
        eventResize: function(event) {
            $.ajax({
                url: '/calendar-update',
                data: {title: event.title, start: moment(event.start).format(), end: moment(event.end).format(), id: event.id},
                type: "GET"
            });
        },
        eventRender: function(event, element){
            element.popover({
                animation:true,
                html: true,
                delay: 800,
                content: event.description,
                trigger: 'hover'
            });
            /*element.tooltip({
                placement: "right",
                title: event.description,
                delay: 500,
            })*/
        },
        header: {
            left: 'prev, next today',
            center: 'title',
            right: 'month, agendaWeek, agendaDay, listWeek, printButton, screenshotButton'
        },
        bootstrapGlyphicons: {
            printButton: 'glyphicon-print',
            screenshotButton: 'glyphicon-camera'
        }
    })

    $(document).keyup(function (event) {
        if (event.keyCode === 13)  {
            if ($('#createEventModal').hasClass('in')) {
                $("#submitButton").click();
            } else if ($('#editEventModal').hasClass('in')) {
                $("#submitEditButton").click();
            }
        }
    })

    $('#submitButton').on('click', function (e) {
        $("#createEventModal").modal('hide');

        var data_row = {
            'start': moment(startDate).format(),
            'end': moment(endDate).format(),
            'title': $('#theTitle').val(),
            'description': $('#theDescription').val(),
            'colour': $('#theColor').val()
        }

        if ($('#theTitle').val().trim().length > 0) {
            $('#theTitle').val("");
            $('#theDescription').val("");
            $('#theColor').val("");

            $.ajax({
                url: 'calendar-addevent',
                data: data_row,
                type: "GET",
                cache: false,
                success: function () {
                    $('#calendar').fullCalendar('refetchEventSources', '/api/event/all' );
                }
            });
        } else {
            emptyTitleNotification();
        }



    });

    function emptyTitleNotification() {
        $.notify({
            icon: 'fas fa-exclamation-triangle marginright',
            message: 'Title cannot be empty'
        },{
            type: 'danger',
            newest_on_top: true,
            allow_dismiss: true,
            placement: {
                from: "bottom",
                align: "right"
            },
            delay: 2000,
            animate: {
                enter: 'animated bounceIn',
                exit: 'animated bounceOut'
            }
        });
    }

    $('#deleteButton').on('click', function(e){
        // We don't want this to act as a link so cancel the link action
        e.preventDefault();
        doDelete();
    });

    function doDelete(view){
        $("#editEventModal").modal('hide');
        var eventID = $('#eventEditID').val();
        $.ajaxSetup ({
            cache: false
        });
        $.ajax({
            url: 'calendar-delete',
            data: {id: eventID},
            type: "GET"
        });
        calendar.fullCalendar('removeEvents',eventID);
    }
});