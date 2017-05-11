<%-- Used for retrocompatibility with the old url links which are pointed to a Jsp which have been moved to the module-workflow-ticketing --%>
<%@page import="fr.paris.lutece.portal.service.util.AppPathService"%>
<%
	String strIdExternalUser = request.getParameter( "id_message_agent" );
	String strTimestamp = request.getParameter( "timestamp" );
	String strSignature = request.getParameter( "signature" );
	String baseUrl = AppPathService.getBaseUrl( request );
	String fullUrl = baseUrl + "jsp/admin/plugins/workflow/modules/ticketing/TicketExternalUserResponse.jsp" + "?id_message_external_user=" + strIdExternalUser + "&signature=" + strSignature + "&timestamp=" + strTimestamp;
	response.sendRedirect( fullUrl );
%>

<%@ page errorPage="../../../../ErrorPage.jsp" %>
<jsp:include page="../../../../AdminHeader.jsp" />
<%@ include file="../../../../AdminFooter.jsp" %>