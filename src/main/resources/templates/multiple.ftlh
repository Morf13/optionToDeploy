<#import "parts/common.ftlh" as c>
<@c.page>
<#include "parts/result.ftlh">
<#assign numberSentence = 1>



<form method="POST">
<h3>${exerciseTitle}</h3>
<br>
 <#list exercise as k>
 <p>${numberSentence}. ${k.task}
 <#assign numberAnswer = 1>
 <#list k.answers as a>
  <div>
    <input type="checkbox" id="" name="${numberSentence}/${numberAnswer}" value="${a}">
    <label for="answer">${a}</label>
  </div>
  <#assign numberAnswer += 1>
  </#list>
  <br>
  <#assign numberSentence += 1>
  </#list>
   <br>
  <div>
    <button type="submit">Check</button>
  </div>
 
  <input type="hidden" name="_csrf" value="${_csrf.token}" />
</form>
</@c.page>


