<#include "security.ftlh">
<#import "pager.ftlh" as p>
<div class="card-columns" id = "exercises-list">

    <#list page.content as exercise>
        <div class="card my-3">
            <div class="m-2">
                <a href="/exercise/${exercise.getType()}/${exercise.id}">${exercise.title}</a><br/>
            </div>
            <div class="card-footer text-muted">
                <a href="/user-exercises/${exercise.author.id}">${exercise.authorName}</a>
                <#if exercise.author.id == currentUserId>
                    <a class="btn btn-primary" href="/editExerciseText/${exercise.id}">
                        Edit
                    </a>
                    <a class="btn btn-primary" href="/deleteExercise/${exercise.id}">
                        Delete
                    </a>
                </#if>
            
            </div>
        </div>
    <#else>
        No Execises
    </#list>
</div>
<@p.pager url page />