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
    var userID = url.split("?")[1].split("&")[0].split("=")[1];
    var taskID = url.split("?")[1].split("&")[1].split("=")[1];
    var done = url.split("?")[1].split("&")[2].split("=")[1];

    var flag;
    if (done === "0"){
        flag = "requester";
    }else {
        flag = "worker";
    }
    $.ajax({
        type:'post',
        url:'showTaskAction',
        data:{
            userID: userID,
            taskID: taskID,
            flag: flag
        },
        dataType:'json'
    }).then(function (data) {

        var task = $.parseJSON($.parseJSON(data).content);

        var sqlTargets = task.sqlTargets;
        var questionD = task.questionDescribe;
        var showing_contents = task.showing_contents;
        var candidateItems = task.candidateItems;
        var submitAnswer = task.submitAnswer;

        if (sqlTargets != null){
            sqlTargets.forEach(function (d) {
                $("#sqlTargets").append('<span>'+d+'</span>');
            });
        }

        $("#questionDescribe").html(questionD);

        showing_contents.forEach(function (d) {
            //需要判断开头是什么，再决定放在表格的哪里,顺便还能知道展示内容是什么，这里解析和后台java是一样的
            //标识符区分：  headers , headers[1] , rows[2] columns[2][3] ，在对应地方展示后面的内容
            var ahead = d.split(':')[0];
            var content = d.split(':')[1];
            var subattribures = ahead.split('-');
            if (subattribures[0] === "headers") {
                if (typeof (subattribures[1]) == "undefined") {
                    //这样就是整个headers
                    var array = JSON.parse(content);
                    array.forEach(function (di) {
                        $("#showing-headers").append('<th>' + di + '</th>');
                    });
                } else {
                    //一个表头
                    for (var i = 0; i < subattribures[1]; i++) {
                        $("#showing-headers").append('<th></th>');
                    }
                    $("#showing-headers").append('<th>' + content + '</th>');

                }
            } else {
                if (subattribures[0] === "rows") {
                    if (typeof (subattribures[1]) == "undefined") {
                        //整个二维数组
                        var doublearray = JSON.parse(content);
                        doublearray.forEach(function (di) {
                            var c = '<tr>';
                            di.forEach(function (dii) {
                                c = c + '<td>' + dii + '</td>';
                            });
                            c = c + '</tr>';
                            $("#showing-tbody").append(c);
                        });
                    } else {
                        if (typeof (subattribures[2]) == "undefined") {
                            //一维数组
                            var array = JSON.parse(content);
                            var c = '<tr>';
                            array.forEach(function (di) {
                                c = c + '<td>' + di + '</td>';
                            });
                            c = c + '</tr>';

                            $("#showing-tbody").append(c);

                        } else {
                            //单个字符串
                            $("#showing-tbody").append('<tr><td>' + content + '</td></tr>');
                        }
                    }
                } else {
                    if (subattribures[0] === "columns") {
                        if (typeof (subattribures[1]) == "undefined") {
                            //整个二维数组
                            var doublearray = JSON.parse(content);
                            //先转化成rows再写方便些
                            var rows = new Array(doublearray[0].length);
                            for (var i = 0; i < rows.length; i++) {
                                rows[i] = new Array(doublearray[0].length);
                            }

                            doublearray.forEach(function (d, i) {
                                d.forEach(function (di, j) {
                                    rows[j][i] = di;
                                });
                            });

                            rows.forEach(function (di) {
                                var c = '<tr>';
                                di.forEach(function (dii) {
                                    c = c + '<td>' + dii + '</td>';
                                });
                                c = c + '</tr>';
                                $("#showing-tbody").append(c);
                            });

                        } else {
                            if (typeof (subattribures[2]) == "undefined") {
                                //一维数组
                                var array = JSON.parse(content);

                                array.forEach(function (di) {
                                    var c = '<tr>';
                                    for (var i = 0; i < subattribures[1]; i++) {
                                        c = c + '<td></td>';
                                    }
                                    c = c + '<td>' + di + '</td></tr>';
                                    $("#showing-tbody").append(c);
                                });
                            } else {
                                //单个字符串
                                $("#showing-tbody").append('<tr><td>' + content + '</td></tr>');
                            }
                        }
                    } else {
                        //除了上面三种情况的
                        $("#showing-others").append('<span>' + d + '</span>');
                    }
                }
            }


        });


        if (candidateItems != null){
            candidateItems.forEach(function (d) {
                var options = "<ul class='top-k'>";
                d.forEach(function (di) {
                    options = options+"<li>"+di+"</li>";
                });
                $("#candidateItems").append(options+"</ul>");
            });
        }

        if (submitAnswer!=null){
            submitAnswer.forEach(function (d) {
                $("#submitAnswer").append("<span>"+d+"</span>");
            });
        }


        //这个是用来统计个数的函数，需要先加载，再调用
        $('#dataTables-example').DataTable({
            responsive: true
        });

        }
    );

    wh.initHeader(userID);
});



