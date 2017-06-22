/**
 * Created by xiyihai on 05/21/2017.
 */

require.config({
    paths:{
        'wh':'worker-header'
    }
});

require(['wh'], function (wh) {

    var url = location.search;
    var userID = url.split("?")[1].split("=")[1];

    $.ajax({
        type:'post',
        url:'loginInfoAction',
        data:{
            userID: userID,
            flag: "worker"
        },
        dataType:'json'
    }).then(function (data) {
        var worker = $.parseJSON(data);
        var personInfo = worker.personInfo;
        var taskInfo = worker.taskInfo;

        $("#id").html(personInfo.worker_id);
        $("#name").html(personInfo.name);
        $("#account").html('$'+personInfo.account);
        $("#email").html(personInfo.email);
        $("#password").html(personInfo.password);

        $("#quality").html(personInfo.quality+' (0~1)');
        $("#level").html(personInfo.level+'/10');
        $("#costtime").html(personInfo.average_costtime+'min');
        $("#di").html(personInfo.average_di+' (0~1)');
        $("#reward").html('$'+personInfo.average_reward);


        $("#state0").html(taskInfo.state0);
        $("#state2").html(taskInfo.state2);
        $("#testtask_number").html(taskInfo.testtask_number);
        $("#rtask_number").html(taskInfo.rtask_number);
    });

    wh.initHeader(userID);
});



