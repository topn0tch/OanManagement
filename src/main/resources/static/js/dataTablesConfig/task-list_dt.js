$(document).ready(function() {
    var table = $('#completedTaskList').DataTable( {
            "columnDefs": [
                { "type": "date", "targets": 1 }
            ],
            "order": [ 1, 'asc' ],
            language: {
                searchPlaceholder: "Search task"
            }
        }

    );
    $('#completedTaskList').on( 'click', '#deleteCompletedTask', function () {
        var task_id = $(this).attr('value');

        $.ajax({
            url: 'task-delete',
            data: {id: task_id},
            type: 'GET'
        })
        table
            .row( $(this).parents('tr') )
            .remove()
            .draw();
        deleteTaskNotification();
    } );

    $('#TodoList').on( 'click', '#deleteTask', function () {
        var task_id = $(this).attr('value');

        $.ajax({
            url: 'task-delete',
            data: {id: task_id},
            type: 'GET'
        })
        $(this).closest('tr').remove();
        deleteTaskNotification();
    } );
    
    function deleteTaskNotification() {
        $.notify({
            icon: 'fas fa-trash-alt marginright',
            message: 'Task has been deleted'
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