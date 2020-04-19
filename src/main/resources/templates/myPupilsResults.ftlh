<#import "parts/common.ftlh" as c>

<@c.page>
<#include "parts/security.ftlh">

<#import "parts/pager.ftlh" as p>

<table class="table">
  <thead>
    <tr>
     <th scope="col">Pupil</th>
      <th scope="col">Exercise</th>
      <th scope="col">Score</th>
      <th scope="col">Date</th>
    </tr>
  </thead>
  <tbody>
  <@p.pager url page />
  <#list page.content as result>
    <tr>
    <td>${result.username}</td>
      <td>${result.exerciseTitle}</td>
      <td>${result.score}</td>
      <td>${result.modtime}</td>
    </tr>
   </#list> 
  </tbody>
</table>
</@c.page>