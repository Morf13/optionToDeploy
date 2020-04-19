<#import "parts/common.ftlh" as c>
<@c.page>
<#include "parts/result.ftlh">
<#assign numberSentence = 1>



<form method="POST">
<h3>${exerciseTitle}</h3>
<br>
 <#list exercise as k>
 <p>${numberSentence}. ${k.taskLeft} <input type="text"  name="${numberSentence}" placeholder="your answer" /> ${k.taskRight}
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


