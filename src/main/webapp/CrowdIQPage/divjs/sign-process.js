/**
 * Created by xiyihai on 05/21/2017.
 */

require.config({
   paths:{


   }
});

require([], function () {

    //用来判断是否通过了校验
    var flag = [];

    //这样使得只校验，不提交表单
    $.validator.setDefaults({
        debug: true
    });

    $("#username").validate({
        rules: {
            add_username: {
                required: true,
                rangelength: [2,12]
            }
        },
        success: function () {
            flag[0] = 1;
        }
    });
    $("#email").validate({
        rules: {
            add_email: {
                required: true,
                email: true
            }
        },
        success: function () {
            flag[1] = 1;
        }
    });
    $("#password").validate({
        rules: {
            add_password: {
                required: true,
                rangelength: [6,12]
            }
        },
        success: function () {
            flag[2] = 1;
        }
    });
    $("#cpassword").validate({
        rules: {
            add_cpassword: {
                required: true,
                equalTo: "#add_password"
            }
        },
        success: function () {
            flag[3] = 1;
        }
    });
    $("#account").validate({
        rules: {
            add_account: {
                required: true,
                range:[0,10000]
            }
        },
        success: function () {
            flag[4] = 1;
        }
    });


    $("#sign_up").bind("click", function () {

        var num = 0;
        flag.forEach(function(d,i){
            if (d===1){
                num++;
            }
        });

        //通过判断警告标签是否出现，结合之前必须通过标签审查，两者协作
        var label = 0;
        $("label").each(function () {
            if ($(this).html() != ""){
                label++;
            }
        });

        if (num === 5 && label === 7){
            sign_up();
        }else {
            alert("请按规范注册信息!");
        }
    });

    $("#sign_in").bind("click", function () {
        if (flag[1] === 1 && flag[2] === 1){
            var label = 0;
            $("label").each(function () {
                if ($(this).html() != ""){
                    label++;
                }
            });
            if (label === 4){
                sign_in();
            }else {
                alert("Please input the right information!");
            }
        }else {
            alert("Please input the right information!");
        }
    });

    function sign_in(){
        var person = {};
        person.email = $("#add_email").val();
        person.password = $("#add_password").val();
        person.flag = $("input[name='category']:checked").val();

        $.ajax({
            type:'post',
            url:'loginAction',
            data:{
                informationJSON:JSON.stringify(person)
            },
            dataType:'json'
        }).then(function (data) {
            userID = $.parseJSON(data);
            if (person.flag === "requester"){
                window.location.href = "index_requester.html?userID="+userID;
            }else{
                window.location.href = "index_worker.html?userID="+userID;
            }
        }, function () {
            alert("failed!");
        })
    }

    function sign_up(){
        var person = new Person($("#add_username").val(), $("#add_email").val(), $("#add_password").val(),
            $("#add_account").val(), $("input[name='category']:checked").val());
        $.ajax({
            type:'post',
            url:'registerAction',
            data:{
                informationJSON:JSON.stringify(person)
            },
            dataType:'json'
        }).then(function () {
            alert("success！");
        }, function () {
            alert("failed");
        })
    }



    function Person(name, email, password, account, flag){
        this.name = name;
        this.email = email;
        this.password = password;
        this.account = account;
        this.flag = flag;
    }

});



