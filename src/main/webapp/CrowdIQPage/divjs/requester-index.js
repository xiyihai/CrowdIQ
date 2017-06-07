/**
 * Created by xiyihai on 05/21/2017.
 */

require.config({
    paths:{


    }
});

require([], function () {

    var url = location.search;
    var userID = url.split("?")[1].split("=")[1];

    $.ajax({
        type:'post',
        url:'loginInfoAction',
        data:{
            userID: userID,
            flag: "requester"
        },
        dataType:'json'
    }).then(function (data) {

        var requester = $.parseJSON(data);
        var personInfo = requester.personInfo;
        var taskInfo = requester.taskInfo;
        var datasource = requester.datasource;

        $("#id").html(personInfo.requester_id);
        $("#name").html(personInfo.name);
        $("#account").html('$'+personInfo.account);
        $("#email").html(personInfo.email);

        $("#state0").html(taskInfo.state0);
        $("#state1").html(taskInfo.state1);
        $("#state2").html(taskInfo.state2);
        $("#table-number").html(taskInfo.table_number);

        datasource.algorithms.forEach(function (d) {
            $("#algorithms").append('<a href="#" class="list-group-item"> <i class="algorithm_name"></i>'+
                d+ '</a>');
        });
        datasource.tablelists.forEach(function (d) {
            $("#tablelists").append('<a href="#" class="list-group-item"> <i class="algorithm_name"></i>'+
                d+ '</a>');
        });

    });

    $("a[name='alltask']").bind("click", function () {
        window.location.href = "requester_alltask.html?userID="+userID;
    });

    $("a[name='tables']").bind("click", function () {
        window.location.href = "requester_tables.html?userID="+userID;
    });
});



