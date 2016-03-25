<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Add Business</title>
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/style.css"/>
" />
<script type="text/javascript" src="<c:url value="/resources/js/jquery.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/jquery.validate.js"/>"></script>
<script>
		$(document).ready(function() {
			$("#businessForm").validate();
			$("#submit").click(function(){
				$("#businessForm").submit();
			});
			
				jQuery.validator.addMethod("phoneUS", function(phone_number, element) {
	   				    phone_number = phone_number.replace(/\s+/g, ""); 
						return this.optional(element) || phone_number.length > 9 &&
			            phone_number.match(/^(1-?)?(\([2-9]\d{2}\)|[2-9]\d{2})-?[2-9]\d{2}-?\d{4}$/);
	             }, "Please specify a valid phone number");
				
					$.validator.addMethod("noSpecialChars", function(value, element) {
					      return this.optional(element) || /^[a-z0-9\_]+$/i.test(value);
					  }, "Only letters, numbers and underscores are allowed");
					
					$.validator.addMethod("alpha", function(value, element) {
						return this.optional(element) || value == value.match(/^[a-zA-Z ]+$/);
						},"Only letters Allowed.");

		});
</script>
</head>
<body>

<!-- Include -->
<jsp:include page="/resources/include/header.jsp" />

<!--header-->
<div id="main">
  <div id="create_user">
    <h4>Add New business Account</h4>
    <form:form method="post" action="add" commandName="business" class="cssform" id="businessForm">
      <p>
        <form:label path="name" class="addname" >
          <spring:message code="label.business.name" /><em class="star"> *</em>
        </form:label>
        <form:input path="name" class="feedback-textfield required alpha" />
      </p>
      <p>
        <form:label path="address" class="addname" >
          <spring:message code="label.business.address" /><em class="star"> *</em>
        </form:label>
        <form:input path="address" class="feedback-textfield required" />
      </p>
      <p>
        <form:label path="description" class="addname" >
          <spring:message code="label.business.description" /><em class="star"> *</em>
        </form:label>
        <form:input path="description" class="feedback-textfield required" />
      </p>
      <p>
        <form:label path="namePrimaryContact" class="addname" >
          <spring:message code="label.business.namePrimaryContact" /><em class="star"> *</em>
        </form:label>
        <form:input path="namePrimaryContact" class="feedback-textfield required alpha" />
      </p>
      <p>
        <form:label path="phonePrimaryContact" class="addname" >
          <spring:message code="label.business.phonePrimaryContact" /><em class="star"> *</em>
        </form:label>
        <form:input path="phonePrimaryContact" class="feedback-textfield required number" minlength="9" maxlength="15" />
      </p>
      <p>
        <form:label path="emailPrimaryContact" class="addname" >
          <spring:message code="label.business.emailPrimaryContact"/><em class="star"> *</em>
        </form:label>
        <form:input path="emailPrimaryContact" class="feedback-textfield required email" />
      </p>
      <p>
        <label class="addname" >Account Types</label>
        <form:select path="accountType" class="select">
        	<form:option value="" label="Select Account type..." />
        	<form:options items="${account_types}"/>
        </form:select>
      </p>
      <p>
        <input type="submit" value='<spring:message code="label.add" />' class="subname" />
<input type="button" onClick="return confirmSubmit()" value="<spring:message code="label.cancel" />" class="subname1" />

 </p>
    </form:form>
    <script>
    function confirmSubmit()
    {
    var agree=confirm("Are you sure you dont want to add?");
    if (agree)
    	window.location = '<c:url value="/businesses/"/>';
    else
    	return false ;
    }
   </script>
    <div class="clear_both"></div>
  </div>
</div>
</body>
</html>
