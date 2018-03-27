$(document).ready(function () {
    var messagesCount = 0;
    var url = "/api/user/newmessages";
    $.ajax({
        type:"get",
        url:url,
        dataType: 'text',
        success:function(data)
        {
            messagesCount = data;
        }
    });

    setInterval(function()
    {
        $.ajax({
            type:"get",
            url:"/api/user/newmessages",
            dataType: 'text',
            success:function(data) {
                var newMessagesCount = data;
                if (messagesCount < newMessagesCount) {
                    messagesCount = newMessagesCount;
                    newMessageNotification()
                    if (isNaN(parseInt($('#newMessageCountNavigation').text())) ) {
                        $('#notification').append("<li><a href='/messages' data-toggle='tooltip' data-placement='bottom' th:title='1 unread message'><span class='fa fa-envelope margindown fa-lg faa-horizontal animated'></span><span id='newMessageCountNavigation' class='badge'>1</span> </li>");
                        $('#hasNoMessages').html("You have <strong>new</strong> messages!");
                    } else {
                        $('#newMessageCountNavigation').text(parseInt($('#newMessageCountNavigation').text())+1);
                    }
                    $('#newMessageCountWidget').text(parseInt($('#newMessageCountWidget').text())+1);
                }
            }
        });
    }, 5000);
})

function newMessageNotification() {
    $.notify({
        icon: 'fas fa-envelope marginright',
        message: 'You have a new message!'
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