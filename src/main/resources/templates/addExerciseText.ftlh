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
                <input type="text" class="form-control ${(explanationError??)?string('is-invalid', '')}"
                       value="<#if exercise??>${exercise.explanation}</#if>" name="explanation" placeholder="Explanation" />
                <#if explanationError??>
                    <div class="invalid-feedback">
                        ${explanationError}
                    </div>
                </#if>
            </div>
             <div class="form-group">
                <input type="text" class="form-control ${(topicError??)?string('is-invalid', '')}"
                       value="<#if exercise??>${exercise.topic}</#if>" name="topic" placeholder="Enter topic" />
                <#if topicError??>
                    <div class="invalid-feedback">
                        ${topicError}
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
               <textarea  rows="10" cols="100" name="task" id = "task"> Enter exercise text</textarea>
                <#if taskError??>
                    <div class="invalid-feedback">
                        ${taskError}
                    </div>
                </#if>
            </div>
           
            
            <input type="hidden" name="_csrf" value="${_csrf.token}" />
            <div class="form-group">
                <button id="btnSubmit" type="submit"  class="btn btn-primary">Add exercise</button>
            </div>
        </form>
  </div>
  
</@c.page>
