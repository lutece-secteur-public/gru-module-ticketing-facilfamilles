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
package fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.service.task;

import fr.paris.lutece.plugins.ticketing.business.ticket.Ticket;
import fr.paris.lutece.plugins.ticketing.business.ticket.TicketHome;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.cc.ITicketEmailAgentCcDAO;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.cc.TicketEmailAgentCc;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.config.MessageDirection;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.config.TaskTicketEmailAgentConfig;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.demand.ITicketingEmailAgentMessageDAO;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.demand.TicketingEmailAgentMessage;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.fieldagent.FieldAgentUser;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.fieldagent.IFieldAgentUserDAO;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.history.ITicketEmailAgentHistoryDAO;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.history.TicketEmailAgentHistory;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.recipient.ITicketEmailAgentRecipientDAO;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.recipient.TicketEmailAgentRecipient;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.resource.IResourceHistoryService;
import fr.paris.lutece.plugins.workflowcore.service.task.SimpleTask;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 */
public class TaskTicketEmailAgent extends SimpleTask
{
    // Beans
    public static final String BEAN_TICKET_CONFIG_SERVICE = "workflow-ticketingfacilfamilles.taskTicketEmailAgentConfigService";

    // Parameters
    public static final String PARAMETER_MESSAGE = "message";
    public static final String PARAMETER_EMAIL_RECIPIENTS = "email_recipients";
    public static final String PARAMETER_EMAIL_RECIPIENTS_CC = "email_recipients_cc";

    // Other constants
    public static final String UNDERSCORE = "_";
    public static final String SEMICOLON = ";";

    // Messages
    private static final String MESSAGE_TICKET = "module.workflow.ticketingfacilfamilles.task_ticket_emailagent.label";
    @Inject
    @Named( BEAN_TICKET_CONFIG_SERVICE )
    private ITaskConfigService _taskTicketConfigService;
    @Inject
    @Named( ITicketEmailAgentHistoryDAO.BEAN_SERVICE )
    private ITicketEmailAgentHistoryDAO _ticketEmailAgentHistoryDAO;
    @Inject
    @Named( ITicketEmailAgentRecipientDAO.BEAN_SERVICE )
    private ITicketEmailAgentRecipientDAO _ticketEmailAgentRecipientDAO;
    @Inject
    @Named( ITicketEmailAgentCcDAO.BEAN_SERVICE )
    private ITicketEmailAgentCcDAO _ticketEmailAgentCcDAO;
    @Inject
    @Named( ITicketingEmailAgentMessageDAO.BEAN_SERVICE )
    private ITicketingEmailAgentMessageDAO _ticketingEmailAgentDemandDAO;
    
    private IFieldAgentUserDAO _fieldAgentUserDAO = SpringContextService.getBean( IFieldAgentUserDAO.BEAN_SERVICE );

    /** The _resource history service. */
    @Inject
    private IResourceHistoryService _resourceHistoryService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void processTask( int nIdResourceHistory, HttpServletRequest request, Locale locale )
    {
        ResourceHistory resourceHistory = _resourceHistoryService.findByPrimaryKey( nIdResourceHistory );
        Ticket ticket = TicketHome.findByPrimaryKey( resourceHistory.getIdResource( ) );

        TaskTicketEmailAgentConfig config = _taskTicketConfigService.findByPrimaryKey( getId( ) );

        if ( config != null )
        {
            MessageDirection messageDirection = config.getMessageDirection( );

            if ( messageDirection == MessageDirection.AGENT_TO_TERRAIN )
            {
                processAgentTask( nIdResourceHistory, ticket, request, locale, config );
            }
            else
                if ( messageDirection == MessageDirection.TERRAIN_TO_AGENT )
                {
                    processTerrainTask( nIdResourceHistory, ticket, request, locale, config );
                }
                else
                {
                    processAgentRecontactTask( nIdResourceHistory, ticket, request, locale, config );
                }
        }
    }

    /**
     * Process agent to agent terrain task
     * 
     * @param nIdResourceHistory
     *            resourceHistory ID
     * @param ticket
     *            the current ticket
     * @param request
     *            HttpRequest from doAction
     * @param locale
     *            current Locale
     * @param config
     *            configuration of the current task
     */
    private void processAgentTask( int nIdResourceHistory, Ticket ticket, HttpServletRequest request, Locale locale, TaskTicketEmailAgentConfig config )
    {
        String strAgentMessage = request.getParameter( PARAMETER_MESSAGE + UNDERSCORE + getId( ) );
        String strEmailRecipients = request.getParameter( PARAMETER_EMAIL_RECIPIENTS + UNDERSCORE + getId( ) );
        String strEmailRecipientsCc = request.getParameter( PARAMETER_EMAIL_RECIPIENTS_CC + UNDERSCORE + getId( ) );

        // create demand item
        TicketingEmailAgentMessage emailAgentDemand = new TicketingEmailAgentMessage( );
        emailAgentDemand.setIdTicket( ticket.getId( ) );
        emailAgentDemand.setMessageQuestion( strAgentMessage );
        emailAgentDemand.setEmailRecipients( strEmailRecipients );
        emailAgentDemand.setEmailRecipientsCc( strEmailRecipientsCc );
        _ticketingEmailAgentDemandDAO.createQuestion( emailAgentDemand );

        // create resource item
        TicketEmailAgentHistory emailAgent = new TicketEmailAgentHistory( );
        emailAgent.setIdResourceHistory( nIdResourceHistory );
        emailAgent.setIdTask( getId( ) );
        emailAgent.setIdMessageAgent( emailAgentDemand.getIdMessageAgent( ) );
        _ticketEmailAgentHistoryDAO.insert( emailAgent );
        
        // create resource infos item
        String[] emailRecipients = null;
        if( strEmailRecipients != null && !strEmailRecipients.isEmpty() )
        {
        	emailRecipients = strEmailRecipients.split(SEMICOLON);
        	
        	for (int i = 0 ; i < emailRecipients.length ; i++)
            {
            	AdminUser user = AdminUserHome.findUserByLogin(AdminUserHome.findUserByEmail(emailRecipients[i]));
            	
            	TicketEmailAgentRecipient infosEmailAgent = new TicketEmailAgentRecipient( );
            	infosEmailAgent.setIdResourceHistory( nIdResourceHistory );
            	infosEmailAgent.setIdTask( getId( ) );
            	infosEmailAgent.setEmail(user.getEmail());
                
                List<FieldAgentUser> listUsers = _fieldAgentUserDAO.findFieldAgentUser( user.getLastName(), user.getEmail(), null );
                
                if(listUsers != null && listUsers.size() > 0)
                {
                	infosEmailAgent.setField( listUsers.iterator().next().getEntite() );
                }
                
            	infosEmailAgent.setName(user.getLastName());
            	infosEmailAgent.setFirstName(user.getFirstName());
            	_ticketEmailAgentRecipientDAO.insert(infosEmailAgent);
            }
        }
        
        String[] emailRecipientsCc = null;
        if( strEmailRecipientsCc != null && !strEmailRecipientsCc.isEmpty() ) 
        {
        	emailRecipientsCc = strEmailRecipientsCc.split(SEMICOLON);
        	for (int i = 0 ; i < emailRecipientsCc.length ; i++)
            {
            	TicketEmailAgentCc infosEmailAgent = new TicketEmailAgentCc( );
            	infosEmailAgent.setIdResourceHistory( nIdResourceHistory );
            	infosEmailAgent.setIdTask( getId( ) );
            	infosEmailAgent.setEmail(emailRecipientsCc[i]);
            	_ticketEmailAgentCcDAO.insert(infosEmailAgent);
            }
        }        
    }

    /**
     * Process agent to agent terrain task (recontact)
     * 
     * @param nIdResourceHistory
     *            resourceHistory ID
     * @param ticket
     *            the current ticket
     * @param request
     *            HttpRequest from doAction
     * @param locale
     *            current Locale
     * @param config
     *            configuration of the current task
     */
    private void processAgentRecontactTask( int nIdResourceHistory, Ticket ticket, HttpServletRequest request, Locale locale, TaskTicketEmailAgentConfig config )
    {
        String strAgentMessage = request.getParameter( PARAMETER_MESSAGE + UNDERSCORE + getId( ) );
        TicketingEmailAgentMessage firstEmailsAgentDemand = _ticketingEmailAgentDemandDAO.loadFirstByIdTicket( ticket.getId( ) );

        String strEmailRecipients = firstEmailsAgentDemand.getEmailRecipients( );
        String strEmailRecipientsCc = firstEmailsAgentDemand.getEmailRecipientsCc( );

        // create demand item
        TicketingEmailAgentMessage emailAgentDemand = new TicketingEmailAgentMessage( );
        emailAgentDemand.setIdTicket( ticket.getId( ) );
        emailAgentDemand.setMessageQuestion( strAgentMessage );
        emailAgentDemand.setEmailRecipients( strEmailRecipients );
        emailAgentDemand.setEmailRecipientsCc( strEmailRecipientsCc );
        _ticketingEmailAgentDemandDAO.createQuestion( emailAgentDemand );

        // create resource item
        TicketEmailAgentHistory emailAgent = new TicketEmailAgentHistory( );
        emailAgent.setIdResourceHistory( nIdResourceHistory );
        emailAgent.setIdTask( getId( ) );
        emailAgent.setIdMessageAgent( emailAgentDemand.getIdMessageAgent( ) );
        _ticketEmailAgentHistoryDAO.insert( emailAgent );
    }

    /**
     * Process agent terrain to agent task (response)
     * 
     * @param nIdResourceHistory
     *            resourceHistory ID
     * @param ticket
     *            the current ticket
     * @param request
     *            HttpRequest from doAction
     * @param locale
     *            current Locale
     * @param config
     *            configuration of the current task
     */
    private void processTerrainTask( int nIdResourceHistory, Ticket ticket, HttpServletRequest request, Locale locale, TaskTicketEmailAgentConfig config )
    {
        String strAgentMessage = request.getParameter( PARAMETER_MESSAGE + UNDERSCORE + getId( ) );

        // create demand item
        int nIdMessageAgent = _ticketingEmailAgentDemandDAO.addAnswer( ticket.getId( ), strAgentMessage );

        // Close all messages for this ticket
        _ticketingEmailAgentDemandDAO.closeMessagesByIdTicket( ticket.getId( ) );

        // create resource item
        TicketEmailAgentHistory emailAgent = new TicketEmailAgentHistory( );
        emailAgent.setIdResourceHistory( nIdResourceHistory );
        emailAgent.setIdTask( getId( ) );
        emailAgent.setIdMessageAgent( nIdMessageAgent );
        _ticketEmailAgentHistoryDAO.insert( emailAgent );

        // no email for this process
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle( Locale locale )
    {
        return I18nService.getLocalizedString( MESSAGE_TICKET, locale );
    }
}
