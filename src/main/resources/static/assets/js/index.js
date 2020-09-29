layui.use(['jquery','element', 'layer'], function () {

    var $ = layui.jquery;
    var element = layui.element;
    var layer = layui.layer;

    ////加载时执行
    layer.ready(function(){
        //加载主页
        active.tabAdd("topage?url=home.html", "station_home","主页");
        active.tabChange("station_home");
        inspectMessage();
    });




    ////全屏事件
    $(document).on('click', "[data-toggle='fullscreen']", function () {
        var doc = document.documentElement;
        if ($(document.body).hasClass("full-screen")) {
            $('#logo-fullscreen-i').attr('class','layui-icon layui-icon-screen-full');
            $(document.body).removeClass("full-screen");
            document.exitFullscreen ? document.exitFullscreen() : document.mozCancelFullScreen ? document.mozCancelFullScreen() : document.webkitExitFullscreen && document.webkitExitFullscreen();
        } else {
            $('#logo-fullscreen-i').attr('class','layui-icon layui-icon-screen-restore');
            $(document.body).addClass("full-screen");
            doc.requestFullscreen ? doc.requestFullscreen() : doc.mozRequestFullScreen ? doc.mozRequestFullScreen() : doc.webkitRequestFullscreen ? doc.webkitRequestFullscreen() : doc.msRequestFullscreen && doc.msRequestFullscreen();
        }
    });


    ////菜单栏左缩进
    var isShow = true;  //定义一个标志位
    $('.kit-side-fold').click(function(){
        //选择出所有的span，并判断是不是hidden
        $('.layui-nav-item span').each(function(){
            if($(this).is(':hidden')){
                $(this).show();
            }else{
                $(this).hide();
            }
        });
        //判断isshow的状态
        if(isShow){
            $('.layui-side.layui-bg-black').width(60); //设置宽度
            $('.layui-logo.logo-div').width(60); //设置宽度
            var b=$(".logo-left").offset().left;
            b=b-140;
            $(".logo-left").animate({left:b+'px'});
            $('#logo-left-i').attr('class','layui-icon layui-icon-spread-left');
            $('.kit-side-fold i').css('margin-right', '70%');  //修改图标的位置
            $('.layui-logo.logo-div img').css('margin-right', '70%');
            //将footer和body的宽度修改
            $('.layui-body').css('left', 60+'px');
            $('.layui-footer').css('left', 60+'px');
            //将二级导航栏隐藏
            $('dd span').each(function(){
                $(this).hide();
            });
            $('.logo-cite').hide();
            //修改标志位
            isShow =false;
        }else{
            $('.layui-side.layui-bg-black').width(200);
            $('.layui-logo.logo-div').width(200); //设置宽度
            var b=$(".logo-left").offset().left;
            b=b+140;
            $(".logo-left").animate({left:b+'px'});
            $('#logo-left-i').attr('class','layui-icon layui-icon-shrink-right');
            $('.kit-side-fold i').css('margin-right', '10%');
            $('.layui-logo.logo-div img').css('margin-right', '10%');
            $('.layui-body').css('left', 200+'px');
            $('.layui-footer').css('left', 200+'px');
            $('dd span').each(function(){
                $(this).show();
            });
            $('.logo-cite').show();
            isShow =true;
        }
    });

    ////左侧菜单关联右侧菜单，菜单选中右击事件
    //触发事件
    window.active = {
        //在这里给active绑定几项事件，后面可通过active调用这些事件
        tabAdd: function(url,id,name) {
            //新增一个Tab项 传入三个参数，分别对应其标题，tab页面的地址，还有一个规定的id，是标签中data-id的属性值
            //关于tabAdd的方法所传入的参数可看layui的开发文档中基础方法部分
            element.tabAdd('main-tab', {
                title: name,
                content: '<iframe data-frameid="'+id+'" scrolling="auto" frameborder="0" src="'+url+'" style="width:100%;height:99%;"></iframe>',
                id: id //规定好的id
            })
            CustomRightClick(id); //给tab绑定右击事件
            FrameWH();  //计算ifram层的大小
        },
        tabChange: function(id) {
            //切换到指定Tab项
            element.tabChange('main-tab', id); //根据传入的id传入到指定的tab项
        },
        tabDelete: function (id) {
            element.tabDelete("main-tab", id);//删除
        }
        , tabDeleteAll: function (ids) {//删除所有
            $.each(ids, function (i,item) {
                element.tabDelete("main-tab", item); //ids是一个数组，里面存放了多个id，调用tabDelete方法分别删除
            })
        }
    };

    //当点击有site-main-active属性的标签时，即左侧菜单栏中内容 ，触发点击事件
    $('.site-main-active').on('click', function() {
        var dataid = $(this);
        openTab(dataid);

    });

    window.openTab = function(dataid){
        //这时会判断右侧.layui-tab-title属性下的有lay-id属性的li的数目，即已经打开的tab项数目
        if ($(".layui-tab-title li[lay-id]").length <= 0) {
            //如果比零小，则直接打开新的tab项
            active.tabAdd(dataid.attr("data-url"), dataid.attr("data-id"),dataid.attr("data-title"));
        } else {
            //否则判断该tab项是否以及存在

            var isData = false; //初始化一个标志，为false说明未打开该tab项 为true则说明已有
            $.each($(".layui-tab-title li[lay-id]"), function () {
                //如果点击左侧菜单栏所传入的id 在右侧tab项中的lay-id属性可以找到，则说明该tab项已经打开
                if ($(this).attr("lay-id") == dataid.attr("data-id")) {
                    isData = true;
                }
            });
            if (isData == false) {
                //标志为false 新增一个tab项
                active.tabAdd(dataid.attr("data-url"), dataid.attr("data-id"),dataid.attr("data-title"));
            }
        }
        //最后不管是否新增tab，最后都转到要打开的选项页面上
        active.tabChange(dataid.attr("data-id"));
    }

    window.openTabByBo = function(bo){
        //这时会判断右侧.layui-tab-title属性下的有lay-id属性的li的数目，即已经打开的tab项数目
        if ($(".layui-tab-title li[lay-id]").length <= 0) {
            //如果比零小，则直接打开新的tab项
            active.tabAdd(bo.dataurl, bo.dataid,bo.datatitle);
        } else {
            //否则判断该tab项是否以及存在

            var isData = false; //初始化一个标志，为false说明未打开该tab项 为true则说明已有
            $.each($(".layui-tab-title li[lay-id]"), function () {
                //如果点击左侧菜单栏所传入的id 在右侧tab项中的lay-id属性可以找到，则说明该tab项已经打开
                if ($(this).attr("lay-id") == bo.dataid) {
                    isData = true;
                }
            });
            if (isData == false) {
                //标志为false 新增一个tab项
                active.tabAdd(bo.dataurl, bo.dataid,bo.datatitle);
            }
        }
        //最后不管是否新增tab，最后都转到要打开的选项页面上
        active.tabChange(bo.dataid);
    }

    function CustomRightClick(id) {
        //取消右键  rightmenu属性开始是隐藏的 ，当右击的时候显示，左击的时候隐藏
        $('.layui-tab-title li:last').on('contextmenu', function () { return false; })
        $('.layui-tab-title,.layui-tab-title li:last').click(function () {
            $('.rightmenu').hide();
        });
        //桌面点击右击
        $('.layui-tab-title li:last').on('contextmenu', function (e) {
            var popupmenu = $(".rightmenu");
            popupmenu.find("li").attr("data-id",id); //在右键菜单中的标签绑定id属性

            //判断右侧菜单的位置
            /* l = ($(document).width() - e.clientX) < popupmenu.width() ? (e.clientX - popupmenu.width()) : e.clientX;
             t = ($(document).height() - e.clientY) < popupmenu.height() ? (e.clientY - popupmenu.height()) : e.clientY;*/
            popupmenu.css({left: e.pageX-190, top: e.pageYOffset-50 }).show(); //进行绝对定位
            //alert("右键菜单")
            return false;
        });
    }

    $(".rightmenu li").click(function () {
        // debugger
        // var popupmenu = $(".rightmenu");
        // popupmenu.find("li").attr("data-id",id)
        //console.log($(this).attr("data-id"));

        //右键菜单中的选项被点击之后，判断type的类型，决定关闭所有还是关闭当前。
        if ($(this).attr("data-type") == "closethis") {
            //如果关闭当前，即根据显示右键菜单时所绑定的id，执行tabDelete
            active.tabDelete($(this).attr("data-id"));
        } else if ($(this).attr("data-type") == "closeall") {
            var tabtitle = $(".layui-tab-title li");
            var ids = new Array();
            $.each(tabtitle, function (i) {
                ids[i] = $(this).attr("lay-id");
            });
            //如果关闭所有 ，即将所有的lay-id放进数组，执行tabDeleteAll
            active.tabDeleteAll(ids);
        } else if ($(this).attr("data-type") == "refreshthis"){
            var iframes = $(".layui-tab-content").find("iframe");
            var refreshid = $(this).attr("data-id");
            $.each(iframes,function (){
                var frameid = $(this).attr("data-frameid");
                if(refreshid==frameid){
                    $(this).attr('src', $(this).attr('src'));
                }
            });
        } else if($(this).attr("data-type") == "closeothers"){
            var otherid = $(this).attr("data-id");
            var tabtitle = $(".layui-tab-title li");
            var ids = new Array();
            $.each(tabtitle, function (i) {
                if(otherid!=$(this).attr("lay-id")){
                    ids[i] = $(this).attr("lay-id");
                }
            });
            //关闭其他
            active.tabDeleteAll(ids);
        }

        $('.rightmenu').hide(); //最后再隐藏右键菜单
    });

    function FrameWH() {
        var h = $(window).height() -41- 10 - 60 -10-44 -10;
        $("iframe").css("height",h+"px");
    }

    $(window).resize(function () {
        FrameWH();
    });

    //密码修改
    $('.edit-password').on('click', function () {
        layer.open({
            type : 2,
            maxmin : true,
            title : '密码修改',
            area : [ '300px', '300px' ],
            fix : false,
            content : '/topage?url=user/index/edit-password.html'
        });

    });

    //查看并添加信息红点
    function inspectMessage(){
        $.ajax({
            url:'/message/inspectMessage',
            type:'post',
            data:{},
            success:function(result){
                if(result.state == 1){
                    if(result.data>0){
                        $("#message-notice-index").append('<span class="layui-badge-dot"></span>');
                    }
                }else{
                }
            },
        });
    }


    $('.message-notice').on('click', function () {
        layer.open({
            type : 2,
            maxmin : true,
            title : '信息查看',
            area : [ '601px', '500px' ],
            fix : false,
            content : '/topage?url=user/index/read-message.html'
        });

    });

});