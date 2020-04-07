<#assign pageList = groupBy("Year","${pages}")/>
<#list pageList?keys as key>
    <#assign pages = pageList[key]/>
    <#list pages as page>
        <p>${page.permLink}</p>
    </#list>
</#list>