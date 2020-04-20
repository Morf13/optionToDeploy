<#import "parts/common.ftlh" as c>
<#include "parts/security.ftlh">
<@c.page>

<#if user??>
  <div>Hello, ${name} </div>
<#else>
  <div>Hello, Guest </div>
  <a href = "/login"> Please log in </a>
</#if>


</@c.page>