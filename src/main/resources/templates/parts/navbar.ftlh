<#include "security.ftlh">
<#import "login.ftlh" as l>

<nav class="navbar navbar-expand-lg navbar-light bg-light">
  <a class="navbar-brand" href="/">Option</a>
  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
    <span class="navbar-toggler-icon"></span>
  </button>

  <div class="collapse navbar-collapse" id="navbarSupportedContent">
    <ul class="navbar-nav mr-auto">
      <li class="nav-item">
        <a class="nav-link" href="/">Home</a>
      </li>
      <#if isAdmin>
      <li class="nav-item">
        <a class="nav-link" href="/user">User List</a>
      </li>
      </#if>
      <#if isTeacher>
      <li class="nav-item">
        <a class="nav-link" href="/teacher-pupils/${currentUserId}">My pupils</a>
      </li>
       <li class="nav-item">
        <a class="nav-link" href="/teacher/add-pupils/${currentUserId}">Add Pupils</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="/addExerciseText">Add Exercise</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="/teacher/results/${currentUserId}">My pupils results</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="/user-exercises/${currentUserId}">My Exercises</a>
      </li>
      </#if>
       <#if !isTeacher>
      <li class="nav-item">
        <a class="nav-link" href="/main">All Exercises</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="/pupil/exercises/${currentUserId}">My Exercises</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="/pupil/results/${currentUserId}">My results</a>
      </li>
      
     
      
      
      </#if>
      </ul>
      <div class="navbar-text mr-3">${name}</div>
      <#if user??><div><@l.logout /></div></#if>
      </div>
      </nav>