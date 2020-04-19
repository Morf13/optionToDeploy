<#if result??>
<#if result lt 60>
<div class="alert alert-danger" role="alert">
  <h4 class="alert-heading">You should try again</h4>
  <p>You result is ${result}</p>
  <hr>
  <div class="progress">
  <div class="progress-bar" role="progressbar" aria-valuenow="0" style="width: ${result}%" aria-valuemin="0" aria-valuemax="100"></div>
</div>
</div>
</#if>
<#if result gt 60 && result lt 90>
<div class="alert alert-warning" role="alert">
  <h4 class="alert-heading">It is ok</h4>
  <p>You result is ${result}</p>
  <hr>
  <div class="progress">
  <div class="progress-bar" role="progressbar" style="width: ${result}%" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100"></div>
</div>
</div>
</#if>
<#if result gt 89>
<div class="alert alert-success" role="alert">
  <h4 class="alert-heading">Well done!</h4>
  <p>You result is ${result}</p>
  <hr>
  <div class="progress">
  <div class="progress-bar" role="progressbar" aria-valuenow="0" style="width: ${result}%" aria-valuemin="0" aria-valuemax="100"></div>
</div>
</div>
</#if>
</#if>