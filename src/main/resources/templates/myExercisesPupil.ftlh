<#import "parts/common.ftlh" as c>

<@c.page>
<div class="card-columns" id = "exercises-list">

    <#list exercises as exercise>
        <div class="card my-3">
            <div class="m-2">
                <a href="/exercise/${exercise.getType()}/${exercise.id}">${exercise.title}</a><br/>
            </div>
        </div>
    <#else>
        No Execises
    </#list>
</div>
</@c.page>