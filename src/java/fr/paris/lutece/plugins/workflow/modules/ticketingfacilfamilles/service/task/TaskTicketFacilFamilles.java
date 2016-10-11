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

import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.config.MessageDirection;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.config.TaskTicketFacilFamillesConfig;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.history.ITicketFacilFamillesHistoryDAO;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.history.TicketFacilFamillesHistory;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.task.SimpleTask;
import fr.paris.lutece.portal.service.i18n.I18nService;

import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;

import javax.servlet.http.HttpServletRequest;


/**
 *
 */
public class TaskTicketFacilFamilles extends SimpleTask
{
    // Beans
    public static final String BEAN_TICKET_CONFIG_SERVICE = "workflow-ticketingfacilfamilles.taskTicketFacilFamillesConfigService";

    // Parameters
    private static final String PARAMETER_MESSAGE = "message";
    private static final String PARAMETER_EMAIL = "email";

    // Messages
    private static final String MESSAGE_TICKET = "module.workflow.ticketingfacilfamilles.task_ticket_facilfamilles.label";

    // Other constants
    private static final String UNDERSCORE = "_";
    @Inject
    @Named( BEAN_TICKET_CONFIG_SERVICE )
    private ITaskConfigService _taskTicketConfigService;
    @Inject
    @Named( ITicketFacilFamillesHistoryDAO.BEAN_SERVICE )
    private ITicketFacilFamillesHistoryDAO _ticketFacilFamillesHistoryDAO;

    /**
     * {@inheritDoc}
     */
    @Override
    public void processTask( int nIdResourceHistory, HttpServletRequest request, Locale locale )
    {
        TaskTicketFacilFamillesConfig config = _taskTicketConfigService.findByPrimaryKey( getId(  ) );

        if ( config != null )
        {
            MessageDirection messageDirection = config.getMessageDirection(  );

            if ( messageDirection == MessageDirection.AGENT_TO_TERRAIN )
            {
                processAgentTask( nIdResourceHistory, request, locale, config );
            }
            else
            {
                processTerrainTask( nIdResourceHistory, request, locale, config );
            }
        }
    }

    /**
     * Process agent to agent terrain task
     * @param nIdResourceHistory resourceHistory ID
     * @param request HttpRequest from doAction
     * @param locale current Locale
     * @param config configuration of the current task
     */
    private void processAgentTask( int nIdResourceHistory, HttpServletRequest request, Locale locale,
        TaskTicketFacilFamillesConfig config )
    {
        String strAgentMessage = request.getParameter( PARAMETER_MESSAGE + UNDERSCORE + getId(  ) );
        String strEmail = request.getParameter( PARAMETER_EMAIL + UNDERSCORE + getId(  ) );

        //create resource item
        TicketFacilFamillesHistory emailAgent = new TicketFacilFamillesHistory(  );
        emailAgent.setIdResourceHistory( nIdResourceHistory );
        emailAgent.setIdTask( getId(  ) );
        emailAgent.setMessage( strAgentMessage );
        emailAgent.setEmailAgent( strEmail );
        _ticketFacilFamillesHistoryDAO.insert( emailAgent );
    }

    /**
     * Process agent terrain to agent task (response)
     * @param nIdResourceHistory resourceHistory ID
     * @param request HttpRequest from doAction
     * @param locale current Locale
     * @param config configuration of the current task
     */
    private void processTerrainTask( int nIdResourceHistory, HttpServletRequest request, Locale locale,
        TaskTicketFacilFamillesConfig config )
    {
        String strAgentMessage = request.getParameter( PARAMETER_MESSAGE + UNDERSCORE + getId(  ) );

        //create resource item
        TicketFacilFamillesHistory emailAgent = new TicketFacilFamillesHistory(  );
        emailAgent.setIdResourceHistory( nIdResourceHistory );
        emailAgent.setIdTask( getId(  ) );
        emailAgent.setMessage( strAgentMessage );
        emailAgent.setEmailAgent( null );
        _ticketFacilFamillesHistoryDAO.insert( emailAgent );

        //no email for this process
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
