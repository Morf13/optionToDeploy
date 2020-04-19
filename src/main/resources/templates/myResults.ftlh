<#import "parts/common.ftlh" as c>

<@c.page>
<table class="table">
  <thead>
    <tr>
      <th scope="col">Exercise</th>
      <th scope="col">Score</th>
      <th scope="col">Date</th>
    </tr>
  </thead>
  <tbody>
  <#list results as result>
    <tr>
      <td>${result.exerciseTitle}</td>
      <td>${result.score}</td>
      <td>${result.modtime}</td>
    </tr>
   </#list> 
  </tbody>
</table>
</@c.page>