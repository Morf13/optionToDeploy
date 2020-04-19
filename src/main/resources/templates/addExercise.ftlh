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
             <label for="types">Choose exercise type</label>

				<select id="types" name = "type" class="form-control ${(typeOfTaskError??)?string('is-invalid', '')}">
				<option value="">none</option>
				  <option value="mchoice">Multiple Choice</option>
				  <option value="gap">Gap Exercise</option>
				</select>
				<#if typeOfTaskError??>
                    <div class="invalid-feedback">
                        ${typeOfTaskError}
                    </div>
                </#if>
                </div>
            <div class="form-group">
                <div class="custom-file">
                    <input type="file" name="file" id="file" value="chosen" onchange="Filevalidation()" accept=".xls, .xlsx"class="form-control ${(fileNameError??)?string('is-invalid', '')}" />
                    <#if fileNameError??>
                    <div class="invalid-feedback">
                        ${fileNameError}
                    </div>
                </#if>
                  
                </div>
                
            </div>
           
            
            <input type="hidden" name="_csrf" value="${_csrf.token}" />
            <div class="form-group">
                <button disabled id="btnSubmit" type="submit"  class="btn btn-primary">Add exercise</button>
            </div>
        </form>
  </div>
  <script> 
    Filevalidation = () => { 
    	$('#btnSubmit').attr("disabled", false);
    }
        
</script> 
</@c.page>
