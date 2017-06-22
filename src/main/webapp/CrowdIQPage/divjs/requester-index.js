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
        $("#password").html(personInfo.password);

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

    //绑定上传按钮
    $("#uploadFunc").bind("click", function () {

        //先获取是哪个类型
        var filetype = $("input[name='filetype']:checked").val();

        $.ajaxFileUpload
        (
            {
                type: 'post',
                url:'uploadFileAction',//用于文件上传的服务器端请求地址
                data:{
                  userID: userID,
                  filetype: filetype
                },
                secureuri:false,//一般设置为false
                fileElementId:'uploadfile',//文件上传空间的id属性  <input type="file" id="file" name="file" />
                dataType: 'json',//返回值类型 一般设置为json
                success: function ()  //服务器成功响应处理函数
                {
                   alert("success!");
                },
                error: function ()//服务器响应失败处理函数
                {
                    alert("failed!");
                }
            }
        )

    });

    rh.initHeader(userID);

});



