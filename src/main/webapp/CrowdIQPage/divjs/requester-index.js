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
        url:'loginInfoAction',
        data:{
            userID: userID,
            flag: "requester"
        },
        dataType:'json'
    }).then(function (data) {

        var requester = $.parseJSON(data);
        var personInfo = requester.personInfo;
        var taskInfo = requester.taskInfo;
        var datasource = requester.datasource;

        $("#id").html(personInfo.requester_id);
        $("#name").html(personInfo.name);
        $("#account").html('$'+personInfo.account);
        $("#email").html(personInfo.email);

        $("#state0").html(taskInfo.state0);
        $("#state1").html(taskInfo.state1);
        $("#state2").html(taskInfo.state2);
        $("#table-number").html(taskInfo.table_number);

        datasource.algorithms.forEach(function (d) {
            $("#algorithms").append('<a href="#" class="list-group-item"> <i class="algorithm_name"></i>'+
                d+ '</a>');
        });
        datasource.tablelists.forEach(function (d) {
            $("#tablelists").append('<a href="#" class="list-group-item"> <i class="algorithm_name"></i>'+
                d+ '</a>');
        });

    });

    $("a[name='alltask']").bind("click", function () {
        window.location.href = "requester_alltask.html?userID="+userID;
    });

    $("a[name='tables']").bind("click", function () {
        window.location.href = "requester_tables.html?userID="+userID;
    });


    //绑定上传按钮
    $("#uploadFunc").bind("click", function () {
        console.log(2333);
        $.ajaxFileUpload
        (
            {
                type: 'post',
                url:'readUploadTableAction',//用于文件上传的服务器端请求地址
                secureuri:false,//一般设置为false
                fileElementId:'uploadfile',//文件上传空间的id属性  <input type="file" id="file" name="file" />
                dataType: 'json',//返回值类型 一般设置为json
                success: function (data, status)  //服务器成功响应处理函数
                {
                    console.log(22);
                    alert(data.message);//从服务器返回的json中取出message中的数据,其中message为在struts2中action中定义的成员变量

                    if(typeof(data.error) != 'undefined')
                    {
                        if(data.error != '')
                        {
                            alert(data.error);
                        }else
                        {
                            alert(data.message);
                        }
                    }
                },
                error: function (data, status, e)//服务器响应失败处理函数
                {
                    alert(e);
                }
            }
        )

    });
});



