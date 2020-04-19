<#import "parts/common.ftlh" as c>

<@c.page>

<form action="#" method="post">
   
    
  <div class="container">
  <div class="row">
    <div class="col-sm">
      My pupils
       <#list pupils?ifExists as pupil>
    <div>
        <label><input type="checkbox" name="pupil${pupil.id?ifExists}" >${pupil.username?ifExists}</label>
    </div>
    </#list>
    </div>
    <div class="col-sm">
      My exercises
       <#list exercises?ifExists as exercise>
    <div>
        <label><input type="checkbox" name="exercise${exercise.id?ifExists}" >${exercise.title?ifExists}</label>
    </div>
    </#list>
    </div>
   
  </div>
</div>
 <input type="hidden" value="${user.id}" name="teacher">
    <input type="hidden" value="${_csrf.token}" name="_csrf">
    <button type="submit">Save</button>
</form>
</@c.page>