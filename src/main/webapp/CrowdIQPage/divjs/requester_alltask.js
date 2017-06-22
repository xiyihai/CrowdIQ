/**
 * Created by xiyihai on 05/21/2017.
 */

require.config({
    paths:{
        'rh':'requester-header'
    }
});

require(['rh'], function (rh) {


    var url = location.search;
    var userID = url.split("?")[1].split("=")[1];

    $.ajax({
        type:'post',
        url:'showAllRTaskAction',
        data:{
            userID: userID
        },
        dataType:'json'
    }).then(function (data) {
        var tasks = $.parseJSON(data);
        tasks.forEach(function (d, i) {
            var task = new RequesterTask(d.task_id, d.state, d.deadline, d.hastaken_number, d.hasanswer_number,
                d.worker_number, d.each_reward, d.predict_cost, d.haspaid_cost, d.difficult_degree, i);

            var context = "<tr><td id='taskID"+ i +"'>" + task.task_id + "</td>" + "<td>" + task.state +
                "</td>"+"<td>" + task.hastaken_number + "</td>"+
                "<td>" + task.hasanswer_number + "</td>" + "<td>" + task.worker_number + "</td>"+
                "<td>" + task.deadline + "</td>"+
                "<td>" + task.each_reward + "</td>" + "<td>" + task.predict_cost + "</td>"+
                "<td>" + task.haspaid_cost + "</td>" + "<td>" + task.difficult_degree + "</td>"+
                lastsign+"</tr>>";

            $("#requestertask-tbody").append(context);
        });

        //这个是用来统计个数的函数，需要先加载，再调用
        $('#dataTables-example').DataTable({
            responsive: true
        });
    }).then(function () {
       //用于绑定按钮作用

        $("a[id^='show']").bind("click", function () {
            var number = $(this).attr('id').substring(4);
            var taskID = $("#" + "taskID" + number).html();
            window.location.href = "requesterHITOverview.html?userID="+userID+"&taskID="+taskID+"&flag=requester";
        });

        $("a[id^='delete']").bind("click", function () {
            var number = $(this).attr('id').substring(6);
            var taskID = $("#" + "taskID" + number).html();
            $.ajax({
                type:'post',
                url:'deleteTaskAction',
                data:{
                    userID: userID,
                    taskID: taskID,
                    flag: "requester"
                },
                dataType:'json'
            }).then(function (data) {
                //删除所在的那一行
                alert("success");
                $("#"+"taskID"+number).parent().remove();
            })
        });

        $("a[id^='pause']").bind("click", function () {
            var number = $(this).attr('id').substring(5);
            var taskID = $("#" + "taskID" + number).html();
            $.ajax({
                type:'post',
                url:'pauseTaskAction',
                data:{
                    userID: userID,
                    taskID: taskID
                },
                dataType:'json'
            }).then(function (data) {
                alert("success");
                window.location.reload();
            }, function () {
                alert("无法暂停");
            })
        });

        $("a[id^='publish']").bind("click", function () {
            var number = $(this).attr('id').substring(7);
            var taskID = $("#" + "taskID" + number).html();
            $.ajax({
                type:'post',
                url:'publishTaskAction',
                data:{
                    userID: userID,
                    taskID: taskID
                },
                dataType:'json'
            }).then(function (data) {
                alert("success");
                window.location.reload();
            }, function () {
                alert("无法发布");
            })
        });

        $("a[id^='edit']").bind("click", function () {
            var number = $(this).attr('id').substring(4);
            var taskID = $("#" + "taskID" + number).html();
            window.location.href = "requesterHITRevise.html?userID="+userID+"&taskID="+taskID;
        });

    });
    var lastsign;

    function RequesterTask(task_id, state, deadline, hastaken_number, hasanswer_number, worker_number, each_reward, predict_cost, haspaid_cost, difficult_degree, i){
        this.task_id = task_id;
        if (state === 0){
            lastsign = "<td> <a href='#' id='publish"+i+"'><i class='fa fa-check'></i>发布</a> <a href='#' id='delete"+i+"'><i class='fa fa-ban'></i>删除</a> <a href='#' id='edit"+i+"'><i class='fa fa-pencil'></i>修改</a> <a href='#' id='show"+i+"'><i class='fa fa-hand-o-up'></i>查看</a> </td>";
            this.state = "待发布";
        }
        if (state === 1){
            lastsign = "<td> <a href='#' id='pause"+i+"'><i class='fa fa-power-off'></i>暂停</a> <a href='#' id='show"+i+"'><i class='fa fa-hand-o-up'></i>查看</a></td>";
            this.state = "发布中";
        }
        if (state === 2){
            lastsign = "<td> <a href='#' id='delete"+i+"'><i class='fa fa-ban'></i>删除</a> <a href='#' id='show"+i+"'><i class='fa fa-hand-o-up'></i>查看</a> </td>";
            this.state = "已完成";
        }
        if (state === 3){
            lastsign = "<td> <a href='#' id='publish"+i+"'><i class='fa fa-check'></i>发布</a> <a href='#' id='edit"+i+"'><i class='fa fa-pencil'></i>修改</a> <a href='#' id='show"+i+"'><i class='fa fa-hand-o-up'></i>查看</a> </td>";
            this.state = "暂停中";
        }
        if (state === 4){
            lastsign = "<td> <a href='#' id='delete"+i+"'><i class='fa fa-ban'></i>删除</a><a href='#' id='show"+i+"'><i class='fa fa-hand-o-up'></i>查看</a></td>";
            this.state = "已过期";
        }
        if (state === 5){
            lastsign = "<td> <a href='#' id='show"+i+"'><i class='fa fa-hand-o-up'></i>查看</a></td>";
            this.state = "等待中";
        }
        this.deadline = deadline;
        this.hastaken_number = hastaken_number;
        this.hasanswer_number = hasanswer_number;
        this.worker_number = worker_number;
        this.each_reward = '$'+each_reward;
        this.predict_cost = '$'+predict_cost;
        this.haspaid_cost = '$'+haspaid_cost;
        this.difficult_degree = difficult_degree;
    }



    rh.initHeader(userID);

});



