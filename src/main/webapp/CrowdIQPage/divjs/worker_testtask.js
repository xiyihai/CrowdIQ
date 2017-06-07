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
        url:'showAllTestTaskAction',
        data:{
            userID: userID
        },
        dataType:'json'
    }).then(function (data) {
        var testtasks = $.parseJSON(data);
        testtasks.forEach(function (d) {
            var testtask = new TestTask(d.testtask_id, d.state, d.worker_answer, d.iscorrect);

            var context = '<tr><td>' + testtask.testtask_id + '</td>' + '<td>' + testtask.state +
                '</td>'+'<td>' + testtask.worker_answer + '</td>'+'<td>' + testtask.iscorrect + '</td>'
                + lastsign + '</tr>';

            $("#testtask-tbody").append(context);
        });

        //这个是用来统计个数的函数，需要先加载，再调用
        $('#dataTables-example').DataTable({
            responsive: true
        });
    });


    var lastsign;

    //这样处理的好处在于，针对json数据中不存在的属性，这里可以预处理，d.iscorrect不影响操作
    function TestTask(testtask_id, state, worker_answer, iscorrect){
        this.testtask_id = testtask_id;
        if(state === 0){
            lastsign = '<td> <a href="forms.html"><i class="fa fa-pencil"></i>做题</a> </td>';
            this.state = "undo";
            this.worker_answer = null;
            this.iscorrect = null;
        }else {
            lastsign = '<td> <a href="forms.html"><i class="fa fa-check"></i>查看</a> </td>';
            this.state = "finished";
            this.worker_answer = worker_answer;
            if (iscorrect === 0){
                this.iscorrect = "false";
            }else this.iscorrect = "true";
        }
    }
});



