<#macro head>
    <meta name="baidu-site-verification" content="jZRrwlvEpq" />
    <meta name="google-site-verification" content="OcQLxKrUrj5A9hpAdCQuoY-K7f5Ja4LUYt35Kxdwq_E" />
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="description" content="${site.title}的博客" />
    <meta name="keywords" content="${site.title}的博客" />
    <meta name="generator" content="Tale" />
    <meta name="renderer" content="webkit">
    <meta http-equiv="x-dns-prefetch-control" content="on" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta charset="UTF-8">
    <link rel="shortcut icon" href="/static/images/favicon.ico">
    <link href="https://cdn.staticfile.org/amazeui/2.7.2/css/amazeui.min.css" rel="stylesheet">
    <!--<link rel="stylesheet" href="https://hemm.site/customui.min.css">-->
    <link rel="stylesheet" href="/static/css/customui.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.css" rel="stylesheet">
    <!-- CodeHighlight -->
    <style>
        .hljs{display:block;overflow-x:auto;padding:0.5em;color:#383a42;background:#fafafa}.hljs-comment,.hljs-quote{color:#a0a1a7;}.hljs-doctag,.hljs-keyword,.hljs-formula{color:#a626a4}.hljs-section,.hljs-name,.hljs-selector-tag,.hljs-deletion,.hljs-subst{color:#e45649}.hljs-literal{color:#0184bb}.hljs-string,.hljs-regexp,.hljs-addition,.hljs-attribute,.hljs-meta-string{color:#50a14f}.hljs-built_in,.hljs-class .hljs-title{color:#c18401}.hljs-attr,.hljs-variable,.hljs-template-variable,.hljs-type,.hljs-selector-class,.hljs-selector-attr,.hljs-selector-pseudo,.hljs-number{color:#986801}.hljs-symbol,.hljs-bullet,.hljs-link,.hljs-meta,.hljs-selector-id,.hljs-title{color:#4078f2}.hljs-emphasis{font-style:italic}.hljs-strong{font-weight:bold}.hljs-link{text-decoration:underline}
    </style>
</#macro>
<#macro header>
    <header class="intro-header" style="background-image: url('/static/images/snowgitlabk.jpg')">
        <div class="container">
            <div class="row">
                <div class="col-lg-8 col-lg-offset-2 col-md-10 col-md-offset-1">
                    <div class="site-heading">
                        <h1>${title}</h1>
                        <span class="subheading">这是一个小标题</span>
                    </div>
                </div>
            </div>
        </div>
    </header>
</#macro>
<#macro footer>
    <footer class="blog-footer">
        <div class="blog-text-center">© 2019 <a href="/">{{title}}</a> 由
            <a href="https://github.com/gohugoio/hugo" target="_blank">Hugo</a>
            强力驱动 Theme is
            <a href="https://github.com/Heemooo/amaze" target="_blank">Amaze made by heemooo</a>
        </div>
    </footer>
    <script src="https://cdn.staticfile.org/jquery/3.3.1/jquery.slim.min.js"></script>
    <script src="https://cdn.staticfile.org/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://cdn.staticfile.org/highlight.js/9.12.0/highlight.min.js"></script>
    <script>hljs.initHighlightingOnLoad();</script>
    <script>
        var $body=document.body;var $toggle=document.querySelector(".navbar-toggle");var $navbar=document.querySelector("#huxblog_navbar");var $collapse=document.querySelector(".navbar-collapse");var __HuxNav__={close:function(){$navbar.className=" ";setTimeout(function(){if($navbar.className.indexOf("in")<0){$collapse.style.height="0px"}},400)},open:function(){$collapse.style.height="auto";$navbar.className+=" in"}};$toggle.addEventListener("click",function(a){if($navbar.className.indexOf("in")>0){__HuxNav__.close()}else{__HuxNav__.open()}});document.addEventListener("click",function(a){if(a.target==$toggle){return}if(a.target.className=="icon-bar"){return}__HuxNav__.close()});jQuery(document).ready(function(c){var d=1170;if(c(window).width()>d){var b=c(".navbar-custom").height(),a=c(".intro-header .container").height();c(window).on("scroll",{previousTop:0},function(){var e=c(window).scrollTop(),f=c(".side-catalog");if(e<this.previousTop){if(e>0&&c(".navbar-custom").hasClass("is-fixed")){c(".navbar-custom").addClass("is-visible")}else{c(".navbar-custom").removeClass("is-visible is-fixed")}}else{c(".navbar-custom").removeClass("is-visible");if(e>b&&!c(".navbar-custom").hasClass("is-fixed")){c(".navbar-custom").addClass("is-fixed")}}this.previousTop=e;f.show();if(e>(a+41)){f.addClass("fixed")}else{f.removeClass("fixed")}})}});
    </script>
    <script>
        $(document).ready(function() {
            $('pre code').each(function(i, block) {
                hljs.highlightBlock(block)
            })
            $('table').addClass('am-table')
        });
    </script>
</#macro>
<#macro nav>
    <nav class="navbar navbar-default navbar-custom navbar-fixed-top">
        <div class="container-fluid">
            <div class="navbar-header page-scroll">
                <button type="button" class="navbar-toggle">
                    <span class="sr-only"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="/">{{title}}的博客</a>
            </div>
            <div id="huxblog_navbar">
                <div class="navbar-collapse">
                    <ul class="nav navbar-nav navbar-right">
                        <li class="menu-item">
                            <a class="menu-item-link" href="https://heemooo.gitee.io/">首页</a>
                        </li>
                        <li class="menu-item">
                            <a class="menu-item-link" href="https://heemooo.gitee.io/post.html">归档</a>
                        </li>
                        <li class="menu-item">
                            <a class="menu-item-link" href="https://heemooo.gitee.io/categories.html">分类</a>
                        </li>
                        <li class="menu-item">
                            <a class="menu-item-link" href="https://heemooo.gitee.io/tags.html">标签</a>
                        </li>
                        <li class="menu-item">
                            <a class="menu-item-link" href="https://heemooo.gitee.io/about.html">关于</a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </nav>
</#macro>