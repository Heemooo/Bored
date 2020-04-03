<#assign dataile = bored.groupByYear()/>
<#list dataile?keys as key>
    <#assign css = dataile[key]/>
    <#list css as s>
        <h1>${s.url}</h1>
    </#list>
</#list>