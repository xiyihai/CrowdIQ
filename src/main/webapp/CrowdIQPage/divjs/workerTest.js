/**
 * Created by xiyihai on 05/21/2017.
 */

require.config({
    paths:{


    }
});

require([], function () {

    $.getJSON("../divjson/WorkerTest.json").then(
        function (data) {
            var task = data;
            var sqlTarget = task.sqlTarget;
            var questionD = task.questionDescribe;
            var showing_contents = task.showing_contents;
            var candidateItems = task.candidateItems;

            sqlTarget.forEach(function (d) {
                $("#sqlTarget").append('<td>'+ d +'</td>>');
            });

            $("#questionDescribe").append('<td colspan="'+ sqlTarget.length +'">'+ questionD +'</td>>');

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
                            $("#showing-others").append('<td>'+ d +'</td>');
                        }
                    }
                }


            });

            candidateItems.forEach(function (d,j) {
                var options = "<td> <div class='form-group'>";
                d.forEach(function (di,i) {
                    options = options+"<div class='radio'> <label> <input type='radio' name='optionsRadios"+ j + "' id='option"+i+"' value='"+di+"'>"+
                        di+"</label> </div>";
                });

                $("#candidateItems").append(options);
            });


            //这个是用来统计个数的函数，需要先加载，再调用
            $('#dataTables-example').DataTable({
                responsive: true
            });
        }
    );

});


