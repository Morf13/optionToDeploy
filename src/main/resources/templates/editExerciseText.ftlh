<#import "parts/common.ftlh" as c>

<@c.page>
    <div class="form-group mt-3">
        <form method="post"  enctype="multipart/form-data">
            <div class="form-group">
                <input type="text" class="form-control ${(titleError??)?string('is-invalid', '')}"
                       value="<#if exercise??>${exercise.title}</#if>" name="title" placeholder="Exercise title" />
                <#if titleError??>
                    <div class="invalid-feedback">
                        ${titleError}
                    </div>
                </#if>
            </div>
      
            <div class="form-group">
               <p><textarea rows="10" cols="100" name="task" id = "task"><#if exercise??>${exercise.task}</#if></textarea></p>
                
            </div>
           
            
            <input type="hidden" name="_csrf" value="${_csrf.token}" />
            <div class="form-group">
                <button id="btnSubmit" type="submit"  class="btn btn-primary">Save exercise</button>
            </div>
        </form>
  </div>
  
</@c.page>
