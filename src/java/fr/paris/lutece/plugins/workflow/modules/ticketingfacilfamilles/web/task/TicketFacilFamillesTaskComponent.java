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
package fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.web.task;

import fr.paris.lutece.plugins.ticketing.web.util.ModelUtils;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.config.MessageDirection;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.config.TaskTicketFacilFamillesConfig;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.service.task.TaskTicketFacilFamilles;
import fr.paris.lutece.plugins.workflowcore.business.config.ITaskConfig;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.plugins.workflowcore.web.task.SimpleTaskComponent;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import javax.servlet.http.HttpServletRequest;


/**
 *
 */
public class TicketFacilFamillesTaskComponent extends SimpleTaskComponent
{
    // TEMPLATES
    private static final String TEMPLATE_TASK_TICKET_CONFIG = "admin/plugins/workflow/modules/ticketingfacilfamilles/task_ticket_facilfamilles_config.html";
    private static final String TEMPLATE_TASK_TICKET_FORM = "admin/plugins/workflow/modules/ticketingfacilfamilles/task_ticket_facilfamilles_form.html";

    // Marks
    private static final String MARK_CONFIG = "config";
    private static final String MARK_MESSAGE_DIRECTIONS_LIST = "message_directions_list";
    private static final String MARK_MESSAGE_DIRECTION = "message_direction";

    // Parameters
    private static final String PARAMETER_MESSAGE_DIRECTION = "message_direction";

    //TOBEDELETED
    private static final String TMP_TASK_INFO = "EmptyTaskInformation";
    @Inject
    @Named( TaskTicketFacilFamilles.BEAN_TICKET_CONFIG_SERVICE )
    private ITaskConfigService _taskTicketConfigService;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayTaskInformation( int nIdHistory, HttpServletRequest request, Locale locale, ITask task )
    {
        //String strTaskInformation = StringUtils.EMPTY;
        String strTaskInformation = TMP_TASK_INFO;

        TaskTicketFacilFamillesConfig config = _taskTicketConfigService.findByPrimaryKey( task.getId(  ) );
        strTaskInformation = strTaskInformation + config.getMessageDirection(  ).name(  );

        return strTaskInformation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayTaskForm( int nIdResource, String strResourceType, HttpServletRequest request,
        Locale locale, ITask task )
    {
        TaskTicketFacilFamillesConfig config = this.getTaskConfigService(  ).findByPrimaryKey( task.getId(  ) );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_CONFIG, config );

        ModelUtils.storeRichText( request, model );
        ModelUtils.storeUserSignature( request, model );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_TICKET_FORM, locale, model );

        return template.getHtml(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTaskInformationXml( int nIdHistory, HttpServletRequest request, Locale locale, ITask task )
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String validateConfig( ITaskConfig config, HttpServletRequest request )
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayConfigForm( HttpServletRequest request, Locale locale, ITask task )
    {
        Map<String, Object> model = new HashMap<String, Object>(  );
        TaskTicketFacilFamillesConfig config = this.getTaskConfigService(  ).findByPrimaryKey( task.getId(  ) );

        ReferenceList listMessageDirections = MessageDirection.getReferenceList( locale );

        model.put( MARK_MESSAGE_DIRECTIONS_LIST, listMessageDirections );

        if ( config != null )
        {
            model.put( MARK_MESSAGE_DIRECTION, config.getMessageDirection(  ).ordinal(  ) );
        }
        else
        {
            model.put( MARK_MESSAGE_DIRECTION, MessageDirection.AGENT_TO_TERRAIN );
        }

        ModelUtils.storeRichText( request, model );

        model.put( MARK_CONFIG, config );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_TICKET_CONFIG, locale, model );

        return template.getHtml(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String doSaveConfig( HttpServletRequest request, Locale locale, ITask task )
    {
        int nMessageDirectionId = Integer.parseInt( request.getParameter( PARAMETER_MESSAGE_DIRECTION ) );

        TaskTicketFacilFamillesConfig config = this.getTaskConfigService(  ).findByPrimaryKey( task.getId(  ) );
        Boolean bConfigToCreate = false;

        if ( config == null )
        {
            config = new TaskTicketFacilFamillesConfig(  );
            config.setIdTask( task.getId(  ) );
            bConfigToCreate = true;
        }

        config.setMessageDirection( MessageDirection.valueOf( nMessageDirectionId ) );

        if ( bConfigToCreate )
        {
            this.getTaskConfigService(  ).create( config );
        }
        else
        {
            this.getTaskConfigService(  ).update( config );
        }

        return null;
    }
}
