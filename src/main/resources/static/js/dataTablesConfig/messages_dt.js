$(document).ready(function() {
    var table = $('#messagesTable').DataTable( {
            "columnDefs": [
                { "type": "date", "targets": 2 }
            ],
            "order": [ 3, 'asc' ],

            language: {
                searchPlaceholder: "Search message"
            }
        }
    )
        .on( 'click', '#deleteMessage', function () {
            var message_id = $(this).attr('value');

            $.ajax({
                url: 'message-delete',
                data: {id: message_id},
                type: 'GET'
            })
            table
                .row( $(this).parents('tr') )
                .remove()
                .draw();
            deleteMessageNotification();
        });

    function deleteMessageNotification() {
        $.notify({
            icon: 'fas fa-trash-alt marginright',
            message: 'Message has been deleted'
        },{
            type: 'success',
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
} );