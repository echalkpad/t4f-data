<%--
  Licensed to the AOS Community (AOS) under one or more
  contributor license agreements.  See the NOTICE file
  distributed with this work for additional information
  regarding copyright ownership.  The AOS licenses this file
  to you under the Apache License, Version 2.0 (the 
  "License"); you may not use this file except in compliance
  with the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
  software distributed under the License is distributed on an
  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  KIND, either express or implied.  See the License for the
  specific language governing permissions and limitations
  under the License.
--%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
<head>
<title>JMS Message Sender</title>
<script language="javascript">
hex = 0
function fadeOutText() { 
  if(hex < 255) { 
    hex += 11; 
    document.getElementById("message").style.color="rgb(" + hex + "," + hex + "," + hex + ")";
    setTimeout("fadeOutText()",40); 
  }
  else
    hex = 0 
}
</script>
</head>
<body onload="fadeOutText()">

<h3>Send a JMS Message</h3>
<form:form action="send.html" modelAttribute="jmsMessageBean">
    <table>
      <tr>
        <td align="center" colspan="2">&nbsp;
	    <c:if test="${not empty successfulSend}">
	        <div id="message">${successfulSend}</div>
	    </c:if>
        </td>
      </tr>
      <tr>
        <td align="right">
	      <b>Reply To:</b>
        </td>
        <td>
          <form:input path="replyTo" />
        </td>
      </tr>
      <tr>
        <td align="right">
	      <b>Time To Live:</b>
        </td>
        <td>
          <form:input path="timeToLive" />
        </td>
      </tr>
      <tr>
        <td align="right">
	      <b>Persistent:</b>
        </td>
        <td>
          <form:checkbox path="persistent" />
        </td>
      </tr>
      <tr>
        <td align="right">
	      <b>Message:</b>
        </td>
        <td>
          <form:textarea rows="10" cols="30" path="messagePayload" />
        </td>
      </tr>
      <tr>
        <td colspan="2">
          <input type="submit" value="Send Message" />
        </td>
      </tr>
    </table>
</form:form>
</body>
</html>
