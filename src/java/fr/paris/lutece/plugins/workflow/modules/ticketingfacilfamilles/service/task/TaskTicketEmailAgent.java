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
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.config.MessageDirection;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.config.TaskTicketEmailAgentConfig;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.demand.ITicketingEmailAgentMessageDAO;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.demand.TicketingEmailAgentMessage;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.history.ITicketEmailAgentHistoryDAO;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.history.TicketEmailAgentHistory;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.resource.IResourceHistoryService;
import fr.paris.lutece.plugins.workflowcore.service.task.SimpleTask;
import fr.paris.lutece.portal.service.i18n.I18nService;

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
    public static final String PARAMETER_EMAIL = "email";

    // Other constants
    public static final String UNDERSCORE = "_";

    // Messages
    private static final String MESSAGE_TICKET = "module.workflow.ticketingfacilfamilles.task_ticket_emailagent.label";
    @Inject
    @Named( BEAN_TICKET_CONFIG_SERVICE )
    private ITaskConfigService _taskTicketConfigService;
    @Inject
    @Named( ITicketEmailAgentHistoryDAO.BEAN_SERVICE )
    private ITicketEmailAgentHistoryDAO _ticketEmailAgentHistoryDAO;
    @Inject
    @Named( ITicketingEmailAgentMessageDAO.BEAN_SERVICE )
    private ITicketingEmailAgentMessageDAO _ticketingEmailAgentDemandDAO;

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
            {
                processTerrainTask( nIdResourceHistory, ticket, request, locale, config );
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
        String strEmail = request.getParameter( PARAMETER_EMAIL + UNDERSCORE + getId( ) );

        // create demand item
        TicketingEmailAgentMessage emailAgentDemand = new TicketingEmailAgentMessage( );
        emailAgentDemand.setIdTicket( ticket.getId( ) );
        emailAgentDemand.setMessageQuestion( strAgentMessage );
        emailAgentDemand.setEmailAgent( strEmail );
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
