define([], function () {

    function initHeader(userID){
        //公共部分
        $("a[name='showID']").append("ID: "+userID);

        $("a[name='alltask']").bind("click", function () {
            window.location.href = "requester_alltask.html?userID="+userID;
        });

        $("a[name='tables']").bind("click", function () {
            window.location.href = "requester_tables.html?userID="+userID;
        });

        $("#homepage").bind("click", function () {
            window.location.href = "index_requester.html?userID="+userID;
        });

        $.ajax({
            type:'post',
            url:'showAllRTaskAction',
            data:{
                userID: userID
            },
            dataType:'json'
        }).then(function (data) {
            var tasks = $.parseJSON(data);
            var count=0;
            tasks.forEach(function (d, i) {
                //界面只展示4个值
                if (count<4){

                    if (d.state === 5 || d.state === 1) {

                        var taskID = d.task_id;
                        var process = (((d.hastaken_number / d.worker_number).toFixed(2) * 0.5) + ((d.hasanswer_number / d.worker_number).toFixed(2) * 0.5)) * 100;
                        //这是需要展示的数据
                        $("#" + count + "task").html("TaskID:" + taskID);
                        $("#" + count + "taskState").html(process + "% Complete");
                        if (process <= 20) {
                            $("#" + count + "taskValue").attr("class", "progress-bar progress-bar-success");
                            $("#" + count + "taskValue2").html(process + "% Complete (success)");
                        } else if (process <= 40) {
                            $("#" + count + "taskValue").attr("class", "progress-bar progress-bar-info");
                            $("#" + count + "taskValue2").html(process + "% Complete (info)");
                        } else if (process <= 60) {
                            $("#" + count + "taskValue").attr("class", "progress-bar progress-bar-warning");
                            $("#" + count + "taskValue2").html(process + "% Complete (warning)");
                        } else if (process <= 80) {
                            $("#" + count + "taskValue").attr("class", "progress-bar progress-bar-danger");
                            $("#" + count + "taskValue2").html(process + "% Complete (danger)");
                        } else {
                            $("#" + count + "taskValue").attr("class", "progress-bar progress-bar");
                            $("#" + count + "taskValue2").html(process + "% Complete");
                        }
                        $("#" + count + "taskValue").attr("aria-valuenow", process).attr("style", "width: " + process + "%");

                        count++;
                    }
                }
            })
        });

        $.ajax({
            type:'post',
            url:'showLastestMAction',
            data:{
                userID: userID
            },
            dataType:'json'
        }).then(function (data) {
            var messages = $.parseJSON(data);
            messages.forEach(function (d, i) {
                var message = new Message(d.taskID, d.flag, d.workerLevel, d.time);
                var classI;
                if (d.flag === "take"){
                    classI = "fa fa-comment fa-fw";
                }else {
                    classI = "fa fa-tasks fa-fw";
                }

                $("#messages").append("<li> <a href='#'> <div> <i class='"+classI+"'></i> " +
                    "<span > taskID:"+message.taskID+"</span> "+ message.flag+" by level"+ d.workerLevel+" <span class='pull-right text-muted small'>" +
                    message.time+"</span> </div> </a> </li> <li class='divider'></li>");
            })
        });

        function Message(taskID, flag, workerLevel, time){
            this.taskID = taskID;
            this.flag = flag;
            this.workerLevel = workerLevel;

            if (time/(60)<60){
                this.time = (time/60).toFixed(0) + "minutes ago";
            }else if (time/(60*60)<24){
                this.time = (time/(60*60)).toFixed(0) + "hours ago";
            }else{
                this.time = (time/(60*60*24)).toFixed(0) + "days ago";
            }
        }
    }

    return {
        initHeader:initHeader
    }
});
