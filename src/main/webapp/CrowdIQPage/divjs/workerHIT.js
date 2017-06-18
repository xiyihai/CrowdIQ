/**
 * Created by xiyihai on 05/21/2017.
 */

require.config({
    paths:{


    }
});

require([], function () {



    var url = location.search;
    var userID = url.split("?")[1].split("&")[0].split("=")[1];
    var taskID = url.split("?")[1].split("&")[1].split("=")[1];

    var answerLength;

    $.ajax({
        type:'post',
        url:'showTaskAction',
        data:{
            userID: userID,
            taskID: taskID,
            flag: "worker"
        },
        dataType:'json'
    }).then(function (data) {

            var task = $.parseJSON($.parseJSON(data).content);
            var sqlTargets = task.sqlTargets;
            var questionD = task.questionDescribe;
            var showing_contents = task.showing_contents;
            var candidateItems = task.candidateItems;

            answerLength = sqlTargets.length;
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
                if (subattribures[0] === "headers"){
                    if (typeof (subattribures[1]) == "undefined"){
                        //这样就是整个headers
                        var array = JSON.parse(content);
                        array.forEach(function (di) {
                            $("#showing-headers").append('<th>'+di+'</th>');
                        });
                    }else {
                        //一个表头
                        for (var i=0;i<subattribures[1];i++){
                            $("#showing-headers").append('<th></th>');
                        }
                        $("#showing-headers").append('<th>'+content+'</th>');

                    }
                }else {
                    if (subattribures[0] === "rows"){
                        if (typeof (subattribures[1]) == "undefined"){
                            //整个二维数组
                            var doublearray = JSON.parse(content);
                            doublearray.forEach(function (di) {
                                var c = '<tr>';
                                di.forEach(function (dii) {
                                    c = c+'<td>'+dii+'</td>';
                                });
                                c = c+'</tr>';
                                $("#showing-tbody").append(c);
                            });
                        }else{
                            if (typeof (subattribures[2]) == "undefined"){
                                //一维数组
                                var array = JSON.parse(content);
                                var c = '<tr>';
                                array.forEach(function (di) {
                                    c = c+'<td>'+di+'</td>';
                                });
                                c = c+'</tr>';

                                $("#showing-tbody").append(c);

                            }else {
                                //单个字符串
                                $("#showing-tbody").append('<tr><td>'+content+'</td></tr>');
                            }
                        }
                    }else{
                        if (subattribures[0] === "columns"){
                            if (typeof (subattribures[1]) == "undefined"){
                                //整个二维数组
                                var doublearray = JSON.parse(content);
                                //先转化成rows再写方便些
                                var rows = new Array(doublearray[0].length);
                                for (var i=0;i<rows.length;i++){
                                    rows[i] = new Array(doublearray[0].length);
                                }

                                doublearray.forEach(function (d,i) {
                                    d.forEach(function (di,j) {
                                        rows[j][i] = di;
                                    });
                                });

                                rows.forEach(function (di) {
                                    var c = '<tr>';
                                    di.forEach(function (dii) {
                                        c = c+'<td>'+dii+'</td>';
                                    });
                                    c = c+'</tr>';
                                    $("#showing-tbody").append(c);
                                });

                            }else{
                                if (typeof (subattribures[2]) == "undefined"){
                                    //一维数组
                                    var array = JSON.parse(content);

                                    array.forEach(function (di) {
                                        var c = '<tr>';
                                        for(var i=0;i<subattribures[1];i++){
                                            c = c+'<td></td>';
                                        }
                                        c = c+'<td>'+di+'</td></tr>';
                                        $("#showing-tbody").append(c);
                                    });
                                }else {
                                    //单个字符串
                                    $("#showing-tbody").append('<tr><td>'+content+'</td></tr>');
                                }
                            }
                        }else {
                            //除了上面三种情况的
                            $("#showing-others").append('<span>' + d + '</span>');
                        }
                    }
                }

            });

            var candidateLength = 0;
            if (candidateItems!=null) {
                var count = 0;
                candidateLength = candidateItems.length;
                candidateItems.forEach(function (d, j) {
                    var options = "<div class='form-group'>";
                    d.forEach(function (di, i) {
                        if (di.indexOf(":") !== -1) {
                            di = di.split(":")[0];
                        }
                        options = options + "<div class='radio'> <label> <input type='radio' name='optionsRadios" + j + "' id='option" + i + "' value='" + di + "'>" +
                            di + "</label> </div>";
                        count = i;
                    });
                    count = count + 1;
                    options = options + "<div class='radio'> <label> " +
                        "<input type='radio' name='optionsRadios" + j + "' id='option" + count + "' value='' checked>" + "" +
                        "<input type='text' id='customfill"+j+"' placeholder='fill in the blanks'></label> </div>";
                    options = options + "</div>";

                    $("#candidateItems").append(options);
                });
            }

            for (var i=0;i<sqlTargets.length-candidateLength;i++){
                var options = "<div class='form-group'> <div class='radio'> <label> " +
                    "<input type='radio' name='optionsRadios"+(candidateLength+i)+"' id='option"+(candidateLength+i)+"' value='' checked>"+
                    "<input type='text' id='customfill"+(candidateLength+i)+"' placeholder='fill in the blanks'></label> </div> </div>";
                $("#candidateItems").append(options);
            }

            //这个是用来统计个数的函数，需要先加载，再调用
            $('#dataTables-example').DataTable({
                responsive: true
            });
    }).then(function () {
        //用来绑定提交答案按钮

        $("#submitanswer").bind("click", function () {

            var answerArray = new Array();

            for (var j=0;j<answerLength;j++){
                var radios = document.getElementsByName("optionsRadios"+j);
                var answer;
                for (var i=0;i<radios.length;i++){
                    if (radios[i].checked){
                        if (radios[i].value == ""){
                            answer = $("#"+"customfill"+j).val();
                        }else {
                            answer = radios[i].value;
                        }
                        answerArray[j] = answer;
                    }
                }
            }
            $.ajax({
                type:'post',
                url:'finishTaskAction',
                data:{
                    userID: userID,
                    taskID: taskID,
                    answers: JSON.stringify(answerArray)
                },
                dataType:'json'
            }).then(function () {
                alert("success");
            })
        })

    });


});



