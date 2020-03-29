/**
 * 提交回复 调用ajax
 */
//这个是回复问题
function post() {
    let questionId = $("#question_id").val();
    let content = $("#comment_content").val();
    //1表示回复问题
    comment2target(questionId, 1, content)
}

//封装了一个发送评论的方法 无论是回复问题还是评论都是这个方法 都是存到数据库中
function comment2target(targetId, type, content) {

    if (!content) {
        alert("不能回复空内容")
        return;
    }
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json",
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        //返回的就是服务器的成功json
        success: function (response) {
            if (response.code == 200) {
                //直接刷新页面
                window.location.reload();

            } else {
                if (response.code == 2003) {
                    //弹出一个确认框，如果确认的话进入下一步
                    let confirm1 = confirm(response.message);
                    if (confirm1) {
                        //登陆 直接到github验证 就是我们的登陆逻辑
                        window.open("https://github.com/login/oauth/authorize?client_id=486d1fa1957053dd68b6&redirect_uri=http://localhost:8080/callback&scope=user&state=1");
                        //就是浏览器的存储方式 还是k v的方式存储到浏览器端 必须手动删除 可以再另外页面取出来
                        window.localStorage.setItem("closable", "true")
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

//回复评论
function comment(e) {
    //这是通过整个标签 获取到的id和内容 前面存到data中的 现在取出来
    let commentId = e.getAttribute("data-id");
    //通过id获取内容
    let content = $("#input-" + commentId).val();
    comment2target(commentId, 2, content);
}

/**
 * 展开二级评论
 * 这个e就是页面传入的整个span标签 也就是那个评论图标
 */
function collapseComment(e) {
    // debugger;
    // console.log(e);
    //这个获取的是一级评论的id
    let id = e.getAttribute("data-id");
    //取出这个id 这个是我们给二级评论命名的id
    let comments = $("#comment-" + id);
    //获取二级展开的状态
    let attribute = e.getAttribute("data-collapse");
    //如果存在 就表示已经打开了
    if (attribute) {
        //折叠二级评论
        comments.removeClass("in");
        //移除状态标记
        e.removeAttribute("data-collapse");
        //这是鼠标移开亦能选中 也就是蓝色选中样式
        e.classList.remove("active");
        //不存在就展示
    } else {
        var subCommentContainer = $("#comment-" + id);

        if (subCommentContainer.children().length != 1) {
            //让框显示出来
            comments.addClass("in");
            //二级显示后，标记评论状态是否展开后面可以判断关闭
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
            //重新加载
        } else {
            //请求展示的东西postman发送请求 返回的是一串json 用1级评论的id去找到它下面所有2级评论
            $.getJSON("/comment/" + id, function (data) {
                console.log(data);


                //遍历传回来的每一个数据中的data项
                $.each(data.data.reverse(), function (index, comment) {

                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object  img-rounded",
                        "src": comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                            "html": comment.content
                        }))
                        .append($("<div/>", {
                            "class": "menu"
                        }).append($("<span/>", {
                            "class": "pull-right",
                            "html": moment(comment.gmtCreate).format('YYYY-MM-DD')
                        })));

                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);


                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments",
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);
                });
                //让框显示出来
                comments.addClass("in");
                //二级显示后，标记评论状态是否展开后面可以判断关闭
                e.setAttribute("data-collapse", "in");
                e.classList.add("active");

            });
        }


    }
}
//将选中的标签加到输入框中
function selectTag(e) {
    let value = e.getAttribute("data-tag");
    let previous = $("#tag").val();
    //不等于-1表示已经存在这个值了

    if (previous.indexOf(value) == -1) {
        if (previous) {
            $("#tag").val(previous + ',' + value);
        } else {
            $("#tag").val(value);
        }
    }

}
//展示可选的标签模块
function showSelectTag() {
$("#select-tag").show();
}