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
        url:'showTakenTaskAction',
        data:{
            userID: userID
        },
        dataType:'json'
    }).then(function (data) {
            var takentasks = $.parseJSON(data);
            takentasks.forEach(function (d, i) {

                var takentask = new TakenTask(d.task_id, d.state, d.deadline, d.each_reward, d.di, d.taken_time
                , d.finish_time, d.get_reward, i);

                var context = "<tr><td id='taskID"+i+"'>" + takentask.task_id + "</td>" + "<td>" + takentask.state +
                    "</td>"+"<td>" + takentask.deadline + "</td>"+"<td>" + takentask.each_reward + "</td>"
                    +"<td>" + takentask.di + "</td>"+"<td>" + takentask.taken_time + "</td>"
                    +"<td>" + takentask.finish_time + "</td>"+"<td>" + takentask.get_reward + "</td>"
                    + lastsign + "</tr>";

                $("#takentask-tbody").append(context);
            });


        }
    ).then(function () {

        $("a[id^='delete']").bind("click", function () {
            var number = $(this).attr('id').substring(6);
            var taskID = $("#" + "taskID" + number).html();
            $.ajax({
                type:'post',
                url:'deleteTaskAction',
                data:{
                    userID: userID,
                    taskID: taskID,
                    flag: "worker"
                },
                dataType:'json'
            }).then(function () {
                alert("success");
            }, function () {
                alert("failed");
            });
        });

        $("a[id^='do']").bind("click", function () {
            var number = $(this).attr('id').substring(2);
            var taskID = $("#" + "taskID" + number).html();
            window.location.href = "workerHIT.html?userID="+userID+"&taskID="+taskID;
        });

        $("a[id^='preview']").bind("click", function () {
            var number = $(this).attr('id').substring(7);
            var taskID = $("#" + "taskID" + number).html();
            window.location.href = "workerHITOverview.html?userID="+userID+"&taskID="+taskID+"&done=1";
        });
        //这个是用来统计个数的函数，需要先加载，再调用
        $('#dataTables-example').DataTable({
            responsive: true
        });
    });


    var lastsign;

    //这样处理的好处在于，针对json数据中不存在的属性，这里可以预处理，d.iscorrect不影响操作
    function TakenTask(task_id, state, deadline, each_reward, di, taken_time, finish_time, get_reward, i){
        this.task_id = task_id;
        this.deadline = deadline;
        this.each_reward = each_reward;
        this.di = di;
        this.taken_time = taken_time;
        if(state === 0){
            lastsign = "<td> <a href='#' id='do"+i+"'><i class='fa fa-pencil'></i>work</a> </td>";
            this.state = "undo";
            this.finish_time = null;
            this.get_reward = null;
        }
        if (state === 2){
            lastsign = "<td> <a href='#' id='preview"+i+"'><i class='fa fa-check'></i>check</a>" +
                " <a href='#' id='delete"+i+"'><i class='fa fa-ban'></i>delete</a> </td>";
            this.state = "finished";
            this.finish_time = finish_time;
            this.get_reward = get_reward;
        }
        if(state === 4){
            lastsign = "<td> <a href='#' id='delete"+i+"'><i class='fa fa-ban'></i>delete</a> </td>";
            this.state = "expired";
            this.finish_time = null;
            this.get_reward = null;
        }
    }


    wh.initHeader(userID);

});



