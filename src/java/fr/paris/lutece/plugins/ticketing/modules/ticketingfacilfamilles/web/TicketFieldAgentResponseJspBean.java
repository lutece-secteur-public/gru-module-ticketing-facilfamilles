/*
 * Copyright (c) 2002-2016, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.ticketing.modules.ticketingfacilfamilles.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.ticketing.business.ticket.Ticket;
import fr.paris.lutece.plugins.ticketing.business.ticket.TicketHome;
import fr.paris.lutece.plugins.ticketing.web.TicketingConstants;
import fr.paris.lutece.plugins.ticketing.web.user.UserFactory;
import fr.paris.lutece.plugins.ticketing.web.workflow.WorkflowCapableJspBean;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.config.TaskTicketEmailAgentConfig;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.demand.ITicketingEmailAgentMessageDAO;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.demand.TicketingEmailAgentMessage;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.history.ITicketEmailAgentHistoryDAO;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.history.TicketEmailAgentHistory;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.provider.TicketEmailAgentNotifyGruConstants;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.service.RequestAuthenticationService;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.service.task.TaskTicketEmailAgent;
import fr.paris.lutece.plugins.workflow.modules.upload.business.file.UploadFile;
import fr.paris.lutece.plugins.workflow.modules.upload.factory.FactoryDOA;
import fr.paris.lutece.plugins.workflow.modules.upload.services.download.DownloadFileService;
import fr.paris.lutece.plugins.workflow.utils.WorkflowUtils;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.resource.IResourceHistoryService;
import fr.paris.lutece.plugins.workflowcore.service.resource.ResourceHistoryService;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.web.constants.Messages;


/**
 * TicketFieldAgentResponse JSP Bean abstract class for JSP Bean
 */

/**
 * This class provides the user interface to manage Ticket features
 */
@Controller( controllerJsp = "TicketFieldAgentResponse.jsp", controllerPath = TicketingConstants.ADMIN_CONTROLLLER_PATH + "modules/ticketingfacilfamilles/", right = "TICKETING_ACTEUR_TERRAIN" )
public class TicketFieldAgentResponseJspBean extends WorkflowCapableJspBean
{
    // Right
    public static final String RIGHT_FIELD_AGENT = "TICKETING_ACTEUR_TERRAIN";
    private static final long serialVersionUID = 1L;

    ////////////////////////////////////////////////////////////////////////////
    // Constants

    // templates
    private static final String TEMPLATE_FIELD_AGENT_RESPONSE = "admin/plugins/ticketing/modules/ticketingfacilfamilles/ticket_field_agent_response.html";
    private static final String TEMPLATE_FIELD_AGENT_MESSAGE = "admin/plugins/ticketing/modules/ticketingfacilfamilles/ticket_field_agent_message.html";

    // Properties
    private static final String PROPERTY_PAGE_TITLE_FIELD_AGENT_RESPONSE = "module.workflow.ticketingfacilfamilles.fieldAgentResponse.pageTitle";
    private static final String PROPERTY_FIELD_AGENT_MESSAGE_OK = "module.workflow.ticketingfacilfamilles.fieldAgentResponse.message.ok";
    private static final String PROPERTY_FIELD_AGENT_MESSAGE_ALREADY_ANSWER = "module.workflow.ticketingfacilfamilles.fieldAgentResponse.message.already_answer";
    
    // Markers
    private static final String MARK_REFERENCE = "reference";
    private static final String MARK_AGENT_MESSAGE = "agent_message";
    private static final String MARK_ID_ACTION = "id_action";
	private static final String MARK_ID_TICKET = "id_ticket";
	private static final String MARK_LIST_FILE_UPLOAD = "list_file_uploaded";
    private static final String MARK_MAP_FILE_URL = "list_url";
    private static final String MARK_USER_FACTORY = "user_factory";
    private static final String MARK_USER_ADMIN = "user_admin";
    private static final String MARK_TASK_TICKET_EMAILAGENT_FORM = "task_ticket_emailagent_form";
    private static final String MARK_KEY_MESSAGE = "key_message";

    // Views
    private static final String VIEW_TICKET_FIELD_AGENT_RESPONSE = "fieldAgentReponse";

    // Other constants
    private boolean _bAvatarAvailable;

    /** DAO beans & service */
    private ITicketEmailAgentHistoryDAO _ticketEmailAgentHistoryDAO;
    private ITicketingEmailAgentMessageDAO _ticketingEmailAgentDemandDAO;
    private ITaskConfigService _taskTicketConfigService;    
    private IResourceHistoryService _resourceHistoryService;
    
    /**
	 * 
	 */
    public TicketFieldAgentResponseJspBean(  )
    {
	    super(  );
	    _ticketEmailAgentHistoryDAO = SpringContextService.getBean( ITicketEmailAgentHistoryDAO.BEAN_SERVICE );
	    _ticketingEmailAgentDemandDAO = SpringContextService.getBean( ITicketingEmailAgentMessageDAO.BEAN_SERVICE );
	    _taskTicketConfigService = SpringContextService.getBean( TaskTicketEmailAgent.BEAN_TICKET_CONFIG_SERVICE );
	    _resourceHistoryService = SpringContextService.getBean( ResourceHistoryService.BEAN_SERVICE );
	    _bAvatarAvailable = ( PluginService.getPlugin( TicketingConstants.PLUGIN_AVATAR ) != null );
    }

	/**
     * Returns the form using by field a to answer to agent
     *
     * @param request The Http request
     * @return the html code of the form
     */
    @View( value = VIEW_TICKET_FIELD_AGENT_RESPONSE, defaultView = true )
    public String getFieldAgentResponseView( HttpServletRequest request )
    {
        //Check sign request
        if ( !RequestAuthenticationService.getRequestAuthenticator(  ).isRequestAuthenticated( request ) )
        {
            return redirect( request,
                AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_STOP ) );
        }

        //retrieve objects
        String strIdEmailAgent = request.getParameter( TicketEmailAgentNotifyGruConstants.PARAMETER_ID_MESSAGE_AGENT );
        int nIdEmailAgent = -1;
        TicketingEmailAgentMessage emailAgentMessage = null;
        TicketEmailAgentHistory facilFamilesHistory = null;
        Ticket ticket = null;
        TaskTicketEmailAgentConfig config = null;
        List<UploadFile> listFileUpload = null;
        Map<String, Object> mapFileUrl = null;
        AdminUser userAdmin = null;
        boolean hasExcep = false;
        try
        {
	        nIdEmailAgent = Integer.parseInt( strIdEmailAgent );
	        emailAgentMessage = _ticketingEmailAgentDemandDAO.loadByIdMessageAgent( nIdEmailAgent );
	        //ticket status
	        if( ! _ticketingEmailAgentDemandDAO.isLastQuestion( emailAgentMessage.getIdTicket(  ), nIdEmailAgent ) || emailAgentMessage.getIsAnswered(  ) )
	        {
	        	return getMessagePage( PROPERTY_FIELD_AGENT_MESSAGE_ALREADY_ANSWER );
	        }
	        
	        List<TicketEmailAgentHistory> lstFFemailAgentHistory = _ticketEmailAgentHistoryDAO.loadByIdMessageAgent( nIdEmailAgent );
	        //The size has to be at 1 because it the action of answer a question, so we should only have the history line of the question for the nIdEmailAgent
	        if( lstFFemailAgentHistory.size(  ) == 1 )
	        {
	        	facilFamilesHistory = lstFFemailAgentHistory.get( 0 );
	        }
	        //if the size is not 1, facilFamilesHistory is null, so NPE will be throw
	        config = _taskTicketConfigService.findByPrimaryKey( facilFamilesHistory.getIdTask(  ) );
	        ticket = TicketHome.findByPrimaryKey( emailAgentMessage.getIdTicket(  ) );
	        listFileUpload = FactoryDOA.getUploadFileDAO(  ).load( facilFamilesHistory.getIdResourceHistory(  ), WorkflowUtils.getPlugin(  ) );
	        if( listFileUpload != null && !listFileUpload.isEmpty(  ) )
	        {
	        	mapFileUrl = new HashMap<String, Object>(  );
		        String strBaseUrl = AppPathService.getBaseUrl( request );
		        for ( int i = 0; i < listFileUpload.size(  ); i++ )
		        {
		        	mapFileUrl.put( Integer.toString( listFileUpload.get( i ).getIdUploadFile(  ) ),
		                DownloadFileService.getUrlDownloadFile( listFileUpload.get( i ).getIdFile(  ), strBaseUrl ) );
		        }
	        }
	        ResourceHistory resourceHistory = _resourceHistoryService.findByPrimaryKey( facilFamilesHistory.getIdResourceHistory(  ) );
	        userAdmin = AdminUserHome.findUserByLogin( resourceHistory.getUserAccessCode(  ) );
        }
        catch ( NumberFormatException e1 )
        {
        	hasExcep = true;
        }
        catch ( NullPointerException e2 )
        {
        	hasExcep = true;
        }
        if( hasExcep )
        {
        	return redirect( request, AdminMessageService.getMessageUrl( request, Messages.MESSAGE_INVALID_ENTRY, AdminMessage.TYPE_STOP ) );
        }
        
        Map<String, Object> model = getModel(  );
        model.put( MARK_REFERENCE, ticket.getReference(  ) );
        model.put( MARK_AGENT_MESSAGE, emailAgentMessage.getMessageQuestion(  ) );
        model.put( MARK_LIST_FILE_UPLOAD, listFileUpload );
        model.put( MARK_MAP_FILE_URL, mapFileUrl );
        model.put( MARK_USER_FACTORY, UserFactory.getInstance(  ) );
        model.put( TicketingConstants.MARK_AVATAR_AVAILABLE, _bAvatarAvailable );
        model.put( MARK_USER_ADMIN, userAdmin );
        model.put( MARK_TASK_TICKET_EMAILAGENT_FORM, WorkflowService.getInstance().getDisplayTasksForm( ticket.getId(  ), Ticket.TICKET_RESOURCE_TYPE, config.getIdFollowingAction(  ), request, getLocale(  ) ) );
        model.put( TicketingConstants.MARK_FORM_ACTION, getActionUrl( TicketingConstants.ACTION_DO_PROCESS_WORKFLOW_ACTION ) );
        model.put( MARK_ID_ACTION, config.getIdFollowingAction(  ) );
        model.put( MARK_ID_TICKET, ticket.getId(  ) );
        		

        return getPage( PROPERTY_PAGE_TITLE_FIELD_AGENT_RESPONSE, TEMPLATE_FIELD_AGENT_RESPONSE, model );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String redirectAfterWorkflowAction( HttpServletRequest request )
    {
        return getMessagePage( PROPERTY_FIELD_AGENT_MESSAGE_OK );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String redirectWorkflowActionCancelled( HttpServletRequest request )
    {
        return redirectView( request, VIEW_TICKET_FIELD_AGENT_RESPONSE );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String defaultRedirectWorkflowAction( HttpServletRequest request )
    {
        return redirectView( request, VIEW_TICKET_FIELD_AGENT_RESPONSE );
    }
    
    /**
     * call TEMPLATE_FIELD_AGENT_MESSAGE template for the given message key
     * @param strKeyMessage the key of the message
     * @return the content of the page
     */
    private String getMessagePage( String strKeyMessage )
    {
    	Map<String, Object> model = getModel(  );
    	model.put( MARK_KEY_MESSAGE, strKeyMessage );
        return getPage( PROPERTY_PAGE_TITLE_FIELD_AGENT_RESPONSE, TEMPLATE_FIELD_AGENT_MESSAGE, model );
    }

}
