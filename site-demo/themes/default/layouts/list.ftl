<#--<#assign dataile = bored.groupByYear()/>
<#list dataile?keys as key>
    <#assign css = dataile[key]/>
    <h1>${key}</h1>
    <#list css as s>
        <a href="${s.permLink}">${s.title}:${s.date}</a><br>
    </#list>
</#list>-->
<#list pagination.data as page>
    <a href="${page.permLink}">${page.title}:${page.date}</a><br>
</#list>
<#if pagination.hasPrev>
    <a href="${pagination.prev}">上一页</a><br>
</#if>
<#if pagination.hasNext>
    <a href="${pagination.next}">下一页</a><br>
</#if>
<p>当前第:${pagination.current}页</p>
<p>总共:${pagination.pageCount}页</p>