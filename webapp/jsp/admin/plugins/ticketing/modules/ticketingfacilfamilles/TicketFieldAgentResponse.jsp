<jsp:useBean id="ticketfieldagentresponse" scope="session" class="fr.paris.lutece.plugins.ticketing.modules.ticketingfacilfamilles.web.TicketFieldAgentResponseJspBean" />

<% String strContent = ticketfieldagentresponse.processController ( request , response ); %>

<%@ page errorPage="../../../../ErrorPage.jsp" %>
<jsp:include page="../../../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../../../AdminFooter.jsp" %>
