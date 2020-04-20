<#import "parts/common.ftlh" as c>

<@c.page>
<h5>Check to add  pupils</h5>
<form action="#" method="post">
    <#list users?ifExists as pupil>
    <div>
        <label><input type="checkbox" name="${pupil.id?ifExists}" >${pupil.username?ifExists}</label>
    </div>
    </#list>
    <input type="hidden" value="${user.id}" name="teacher">
    <input type="hidden" value="${_csrf.token}" name="_csrf">
    <button type="submit">Save</button>
</form>
<#list form?ifExists as k,v>
${k} - ${v}<br>
</#list>
</@c.page>