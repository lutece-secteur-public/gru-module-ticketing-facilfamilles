<%-- Used for retrocompatibility with the old url links which are pointed to a Jsp which have been moved to the module-workflow-ticketing --%>
<%
	String strIdExternalUser = request.getParameter( "id_message_agent" );
	String strTimestamp = request.getParameter( "timestamp" );
	String strSignature = request.getParameter( "signature" );
	String strUrl = request.getRequestURL( ).toString( );
	String strNewPath = strUrl.replace( "plugins/ticketing/modules/ticketingfacilfamilles/TicketFieldAgentResponse.jsp", "plugins/workflow/modules/ticketing/TicketExternalUserResponse.jsp" );
	String fullUrl = strNewPath + "?id_message_external_user=" + strIdExternalUser + "&signature=" + strSignature + "&timestamp=" + strTimestamp;
	response.sendRedirect( fullUrl );
%>

<%@ page errorPage="../../../../ErrorPage.jsp" %>
<jsp:include page="../../../../AdminHeader.jsp" />
<%@ include file="../../../../AdminFooter.jsp" %>