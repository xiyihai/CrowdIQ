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
        url:'showRecommendTaskAction',
        data:{
            userID: userID
        },
        dataType:'json'
    }).then(function (data) {
        var rtasks = $.parseJSON(data);
        rtasks.forEach(function (d, i) {
            var rtask = new RTask(d.taskID, d.taken_deadline, d.final_deadline, d.wbase, d.di, d.work_state);

            var context = "<tr><td id='taskID"+i+"'>" + rtask.taskID + "</td>" + "<td>" + rtask.taken_deadline +
                "</td>"+"<td>" + rtask.final_deadline + "</td>"+"<td>" + rtask.wbase + "</td>"+
                "<td>" + rtask.di + "</td>" + "<td>" + rtask.work_state + "</td>"+
                "<td> <a href='#' id='taken"+i+"'><i class='fa fa-check'></i>take</a> " +
                "<a href='#' id='preview"+i+"'><i class='fa fa-hand-o-up'></i>preview</a> </td> </tr>";

            $("#recommendtask-tbody").append(context);
        });


    }).then(function () {

        $("a[id^='preview']").bind("click", function () {
            var number = $(this).attr('id').substring(7);
            var taskID = $("#" + "taskID" + number).html();
            window.location.href = "workerHITOverview.html?userID="+userID+"&taskID="+taskID+"&done=0";
        });

        $("a[id^='taken']").bind("click", function () {
            var number = $(this).attr('id').substring(5);
            var taskID = $("#" + "taskID" + number).html();
            $.ajax({
                type:'post',
                url:'takeTaskAction',
                data:{
                    userID: userID,
                    taskID: taskID
                },
                dataType:'json'
            }).then(function () {
                alert("success");
            }, function () {
                alert("failed");
            });
        });

        //这个是用来统计个数的函数，需要先加载，再调用
        $('#dataTables-example').DataTable({
            responsive: true
        });
    });

    function RTask(taskID, taken_deadline, final_deadline, wbase, di, work_state){
        this.taskID = taskID;
        this.taken_deadline = taken_deadline;
        this.final_deadline = final_deadline;
        this.wbase = '$'+wbase;
        this.di = di;
        this.work_state = work_state;
    }

    wh.initHeader(userID);
});



