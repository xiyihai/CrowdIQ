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
        url:'showAllTestTaskAction',
        data:{
            userID: userID
        },
        dataType:'json'
    }).then(function (data) {
        var testtasks = $.parseJSON(data);
        testtasks.forEach(function (d, i) {
            var testtask = new TestTask(d.testtask_id, d.state, d.worker_answer, d.iscorrect, i);

            var context = "<tr><td id='taskID"+i+"'>" + testtask.testtask_id + "</td>" + "<td>" + testtask.state +
                "</td>"+"<td>" + testtask.worker_answer + "</td>"+"<td>" + testtask.iscorrect + "</td>"
                + lastsign + "</tr>";

            $("#testtask-tbody").append(context);
        });


    }).then(function () {

        $("a[id^='do']").bind("click", function () {
            var number = $(this).attr('id').substring(2);
            var taskID = $("#" + "taskID" + number).html();
            window.location.href = "workerTest.html?userID="+userID+"&taskID="+taskID;
        });

        $("a[id^='show']").bind("click", function () {
            var number = $(this).attr('id').substring(4);
            var taskID = $("#" + "taskID" + number).html();
            window.location.href = "workerTestResult.html?userID="+userID+"&taskID="+taskID;
        });

        //这个是用来统计个数的函数，需要先加载，再调用
        $('#dataTables-example').DataTable({
            responsive: true
        });
    });


    var lastsign;

    //这样处理的好处在于，针对json数据中不存在的属性，这里可以预处理，d.iscorrect不影响操作
    function TestTask(testtask_id, state, worker_answer, iscorrect, i){
        this.testtask_id = testtask_id;
        if(state === 0){
            lastsign = "<td> <a href='#' id='do"+i+"'><i class='fa fa-pencil'></i>work</a> </td>";
            this.state = "undo";
            this.worker_answer = null;
            this.iscorrect = null;
        }else {
            lastsign = "<td> <a href='#' id='show"+i+"'><i class='fa fa-check'></i>check</a> </td>";
            this.state = "finished";
            this.worker_answer = worker_answer;
            if (iscorrect === 0){
                this.iscorrect = "false";
            }else this.iscorrect = "true";
        }
    }


    wh.initHeader(userID);

});



