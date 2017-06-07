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
        url:'showDBTableAction',
        data:{
            userID: userID
        },
        dataType:'json'
    }).then(function (data) {
    //$.getJSON("../divjson/tables.json").then(function (data) {

        var tables = $.parseJSON(data);
        //var tables = data;
        tables.forEach(function (d, i) {
            var table = new Table(d,i);

            var context = "<tr><td id='tablename"+ i +"'>" + table.tablename + "</td>" + "<td>" + table.state +
                "</td>"+ lastsign + "</tr>";

            $("#tables-tbody").append(context);
        });

        //这个是用来统计个数的函数，需要先加载，再调用
        $('#dataTables-example').DataTable({
            responsive: true
        });
    }).then(function () {

        $("a[id^='show']").bind("click", function () {
            var number = $(this).attr('id').substring(4, 6);
            var tablename = $("#" + "tablename" + number).html();
            $.ajax({
                type: 'post',
                url: 'readDBTableAction',
                data: {
                    userID: userID,
                    tablename: tablename
                },
                dataType: 'json'
            }).then(function (data) {
                //这里展示JSONTree树
                console.log($.parseJSON(data));
            });
        });


        $("a[id^='quality']").bind("click", function () {
            var number = $(this).attr('id').substring(7, 9);
            var tablename = $("#" + "tablename" + number).html();
            $.ajax({
                type: 'post',
                url: 'inspectionAction',
                data: {
                    userID: userID,
                    tablename: tablename
                },
                dataType: 'json'
            }).then(function (d) {
                var data = $.parseJSON(d);
                var dataformat = data.dataformat;
                var datamiss = data.datamiss;
                var redundancyRow = data.redundancyRow;
                var headersmiss = data.headersmiss;

                dataformat.forEach(function (d) {
                    $("#dataformat").children().each(function (d, i) {
                        if (d !== 0){
                            $(i).remove();
                        }
                    });
                    $("#dataformat").append('<td>'+d+'</td>');
                });
                datamiss.forEach(function (d) {
                    $("#datamiss").children().each(function (d, i) {
                        if (d !== 0){
                            $(i).remove();
                        }
                    });
                    $("#datamiss").append('<td>'+d+'</td>');
                });
                redundancyRow.forEach(function (d) {
                    $("#redundancyRow").children().each(function (d, i) {
                        if (d !== 0){
                            $(i).remove();
                        }
                    });
                    $("#redundancyRow").append('<td>'+d+'</td>');
                });
                headersmiss.forEach(function (d) {
                    $("#headersmiss").children().each(function (d, i) {
                        if (d !== 0){
                            $(i).remove();
                        }
                    });

                    $("#headersmiss").append('<td>'+d+'</td>');
                });
            });
        });

        $("a[id^='download']").bind("click", function () {
            var number = $(this).attr('id').substring(8, 10);
            var tablename = $("#" + "tablename" + number).html();
            $.ajax({
                type: 'post',
                url: 'downloadTableAction',
                data: {
                    userID: userID,
                    tablename: tablename
                },
                dataType: 'json'
            }).then(function () {
                //下载表格
            });
        });

        $("a[id^='delete']").bind("click", function () {
            var number = $(this).attr('id').substring(6, 8);
            var tablename = $("#" + "tablename" + number).html();
            $.ajax({
                type: 'post',
                url: 'deleteTableAction',
                data: {
                    userID: userID,
                    tablename: tablename
                },
                dataType: 'json'
            }).then(function () {
                //删除表格
            });
        });

        $("a[id^='design']").bind("click", function () {
            var number = $(this).attr('id').substring(6, 8);
            var tablename = $("#" + "tablename" + number).html();
            window.location.href = "requesterHITDesign.html?userID="+userID+"&tablename="+tablename;
        });

    });


    var lastsign;

    //这样处理的好处在于，针对json数据中不存在的属性，这里可以预处理，d.iscorrect不影响操作
    function Table(tablename, i){
        var info = tablename.split(":");
        this.tablename = info[0];
        if(parseInt(info[1]) === 0){
            lastsign = "<td class='option-button'>" +
                "<a href='#' id='design"+ i +"'><i class='fa fa-pencil'></i>design</a>" +
                "<a href='#' id='quality"+ i +"'><i class='fa fa-check-square'></i>quality test</a> " +
                "<a href='#' id='show" +i+ "'><i class='fa fa-search-plus'></i>show</a></td>";
            this.state = "processing";
        }else {
            lastsign = "<td class='option-button'> " +
                "<a href='#' id='download"+ i +"'><i class='fa fa-cloud-download'></i>download</a>" +
                "<a href='#' id='delete"+ i +"'><i class='fa fa-trash-o'></i>delete</a> " +
                "<a href='#' id='show" +i+ "'><i class='fa fa-search-plus'></i>show</a></td>";
            this.state = "finished";
        }
    }

});


