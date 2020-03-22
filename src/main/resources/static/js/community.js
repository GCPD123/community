function post() {
    let questionId = $("#question_id").val();
    let content = $("#comment_content").val();
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json",
        data: JSON.stringify({
            "parentId": questionId,
            "content": content,
            "type": 1
        }),
        //返回的就是服务器的成功json
        success: function (response) {
            if (response.code == 200) {

                $("#comment_section").hide();
            } else {
                if (response.code == 2003) {
                    //弹出一个确认框，如果确认的话进入下一步
                    let confirm1 = confirm(response.message);
                    if(confirm1){
                        //登陆 直接到github验证 就是我们的登陆逻辑
                        window.open("https://github.com/login/oauth/authorize?client_id=486d1fa1957053dd68b6&redirect_uri=http://localhost:8080/callback&scope=user&state=1");
                        //就是浏览器的存储方式 还是k v的方式存储到浏览器端 必须手动删除 可以再另外页面取出来
                        window.localStorage.setItem("closable","true")
                    }
                } else {

                    alert(response.message);
                }
            }
            console.log(response);
        },
        dataType: "json"
    });
    // console.log(question);
    // console.log(content);
}