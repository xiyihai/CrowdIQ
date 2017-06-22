
define([], function () {

    function initHeader(userID) {
        //公共部分
        $("a[name='showID']").append("ID: " + userID);

        $("#homepage").bind("click", function () {
            window.location.href = "index_worker.html?userID=" + userID;
        });

        $("a[name='takentask']").bind("click", function () {
            window.location.href = "worker_takentask.html?userID="+userID;
        });

        $("a[name='recommendtask']").bind("click", function () {
            window.location.href = "worker_recommendtask.html?userID="+userID;
        });

        $("a[name='testtask']").bind("click", function () {
            window.location.href = "worker_testtask.html?userID="+userID;
        });
    }
    return {
        initHeader:initHeader
    }
});
