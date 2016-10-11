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
package fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.provider;

import fr.paris.lutece.plugins.ticketing.business.ticket.Ticket;
import fr.paris.lutece.plugins.ticketing.business.ticket.TicketHome;
import fr.paris.lutece.plugins.workflow.modules.notifygru.service.AbstractServiceProvider;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.history.ITicketFacilFamillesHistoryDAO;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.history.TicketFacilFamillesHistory;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.service.resource.IResourceHistoryService;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;


/**
 *
 */
public class TicketFacilFamillesNotifyGruProvider extends AbstractServiceProvider
{
    /** The Constant FORM_HELPER. */
    private static final String FORM_HELPER = "admin/plugins/workflow/modules/ticketingfacilfamilles/form_helper.html";
    private static final String LIST_ENTRIES = "list_entries";

    /** Parameter for response url */
    private static final String RESPONSE_URL = "module.workflow.ticketingfacilfamilles.task_ticket_facilfamilles.url_response";

    /** The _resource history service. */
    @Inject
    private IResourceHistoryService _resourceHistoryService;

    /** The TicketFacilFamillesHistory DAO. */
    @Inject
    @Named( ITicketFacilFamillesHistoryDAO.BEAN_SERVICE )
    private ITicketFacilFamillesHistoryDAO _ticketFacilFamillesHistoryDAO;

    /**
     * Get ticket fot the given id resource
     *
     * @param nIdResourceHistory the n id resource history
     * @return the ticket
     */
    private Ticket getTicket( int nIdResourceHistory )
    {
        ResourceHistory resourceHistory = _resourceHistoryService.findByPrimaryKey( nIdResourceHistory );
        Ticket ticket = TicketHome.findByPrimaryKey( resourceHistory.getIdResource(  ) );

        if ( ticket == null )
        {
            throw new AppException( "No ticket found for resource history Id : " + nIdResourceHistory );
        }

        return ticket;
    }

    /**
     * Get ticketFacilFamillesHistory fot the given id resource
     *
     * @param nIdResourceHistory the n id resource history
     * @return the ticket
     */
    private TicketFacilFamillesHistory getTicketFacilFamillesHistory( int nIdResourceHistory )
    {
        TicketFacilFamillesHistory ticketFacilFamillesHistory = _ticketFacilFamillesHistoryDAO.loadByIdHistory( nIdResourceHistory );

        if ( ticketFacilFamillesHistory == null )
        {
            throw new AppException( "No ticketFacilFamillesHistory found for resource history Id : " +
                nIdResourceHistory );
        }

        return ticketFacilFamillesHistory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserGuid( int nIdResourceHistory )
    {
        return StringUtils.EMPTY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserEmail( int nIdResourceHistory )
    {
        return getTicketFacilFamillesHistory( nIdResourceHistory ).getEmailAgent(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getOptionalDemandId( int nIdResourceHistory )
    {
        return getTicket( nIdResourceHistory ).getId(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDemandReference( int nIdResourceHistory )
    {
        return getTicket( nIdResourceHistory ).getReference(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCustomerId( int nIdResourceHistory )
    {
        return TicketFacilFamillesNotifyGruConstants.STR_NO_CUSTOMER_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getOptionalDemandIdType( int nIdResourceHistory )
    {
        //NOT USED FOR THE MOMENT
        return TicketFacilFamillesNotifyGruConstants.EMPTY_OPTIONAL_DEMAND_ID_TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getOptionalMobilePhoneNumber( int nIdResourceHistory )
    {
        return StringUtils.EMPTY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getInfosHelp( Locale local )
    {
        List<String> lstEntries = new ArrayList<String>(  );
        Map<String, Object> model = new HashMap<String, Object>(  );
        //FIXME remove unnecessary field
        // GENERIC GRU
        lstEntries.add( TicketFacilFamillesNotifyGruConstants.MARK_FIRSTNAME );
        lstEntries.add( TicketFacilFamillesNotifyGruConstants.MARK_LASTNAME );
        lstEntries.add( TicketFacilFamillesNotifyGruConstants.MARK_FIXED_PHONE );
        lstEntries.add( TicketFacilFamillesNotifyGruConstants.MARK_MOBILE_PHONE );
        lstEntries.add( TicketFacilFamillesNotifyGruConstants.MARK_EMAIL );
        lstEntries.add( TicketFacilFamillesNotifyGruConstants.MARK_UNIT_NAME );
        lstEntries.add( TicketFacilFamillesNotifyGruConstants.MARK_REFERENCE );
        lstEntries.add( TicketFacilFamillesNotifyGruConstants.MARK_USER_TITLES );
        lstEntries.add( TicketFacilFamillesNotifyGruConstants.MARK_TICKET_TYPES );
        lstEntries.add( TicketFacilFamillesNotifyGruConstants.MARK_TICKET_DOMAINS );
        lstEntries.add( TicketFacilFamillesNotifyGruConstants.MARK_TICKET_CATEGORIES );
        lstEntries.add( TicketFacilFamillesNotifyGruConstants.MARK_CONTACT_MODES );
        lstEntries.add( TicketFacilFamillesNotifyGruConstants.MARK_COMMENT );
        lstEntries.add( TicketFacilFamillesNotifyGruConstants.MARK_CHANNEL );
        lstEntries.add( TicketFacilFamillesNotifyGruConstants.MARK_URL_COMPLETED );
        //SPECIFIC EMAIL AGENT;
        lstEntries.add( TicketFacilFamillesNotifyGruConstants.MARK_EMAILAGENT_MESSAGE );
        lstEntries.add( TicketFacilFamillesNotifyGruConstants.MARK_EMAILAGENT_LINK );

        model.put( LIST_ENTRIES, lstEntries );

        @SuppressWarnings( "deprecation" )
        HtmlTemplate t = AppTemplateService.getTemplateFromStringFtl( AppTemplateService.getTemplate( FORM_HELPER,
                    local, model ).getHtml(  ), local, model );
        String strResourceInfo = t.getHtml(  );

        return strResourceInfo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getInfos( int nIdResourceHistory )
    {
        Map<String, Object> model = new HashMap<String, Object>(  );

        if ( nIdResourceHistory > 0 )
        {
            model = buildModelNotifyGruTicketing( getTicket( nIdResourceHistory ),
                    getTicketFacilFamillesHistory( nIdResourceHistory ) );
        }
        else
        {
            model = buildModelNotifyGruTicketing( new Ticket(  ), new TicketFacilFamillesHistory(  ) );
        }

        return model;
    }

    /**
     * Builds the model notify gru ticketing.
     *
     * @param ticket the ticket
     * @param ticketFacilFamillesHistory the ticketFacilFamillesHistory
     * @return the map
     */
    private Map<String, Object> buildModelNotifyGruTicketing( Ticket ticket,
        TicketFacilFamillesHistory ticketFacilFamillesHistory )
    {
        //FIXME remove unnecessary field
        Map<String, Object> model = new HashMap<String, Object>(  );
        // GENERIC GRU
        model.put( TicketFacilFamillesNotifyGruConstants.MARK_GUID,
            ( ticket.getGuid(  ) != null ) ? ticket.getGuid(  ) : "" );
        model.put( TicketFacilFamillesNotifyGruConstants.MARK_FIRSTNAME,
            ( ticket.getFirstname(  ) != null ) ? ticket.getFirstname(  ) : "" );
        model.put( TicketFacilFamillesNotifyGruConstants.MARK_LASTNAME,
            ( ticket.getLastname(  ) != null ) ? ticket.getLastname(  ) : "" );
        model.put( TicketFacilFamillesNotifyGruConstants.MARK_FIXED_PHONE,
            ( ticket.getFixedPhoneNumber(  ) != null ) ? ticket.getFixedPhoneNumber(  ) : "" );
        model.put( TicketFacilFamillesNotifyGruConstants.MARK_MOBILE_PHONE,
            ( ticket.getMobilePhoneNumber(  ) != null ) ? ticket.getMobilePhoneNumber(  ) : "" );
        model.put( TicketFacilFamillesNotifyGruConstants.MARK_EMAIL,
            ( ticket.getEmail(  ) != null ) ? ticket.getEmail(  ) : "" );
        model.put( TicketFacilFamillesNotifyGruConstants.MARK_UNIT_NAME,
            ( ( ticket.getAssigneeUnit(  ) != null ) && ( ticket.getAssigneeUnit(  ).getName(  ) != null ) )
            ? ticket.getAssigneeUnit(  ).getName(  ) : "" );
        model.put( TicketFacilFamillesNotifyGruConstants.MARK_REFERENCE,
            ( ticket.getReference(  ) != null ) ? ticket.getReference(  ) : "" );
        model.put( TicketFacilFamillesNotifyGruConstants.MARK_TICKET, ticket );
        model.put( TicketFacilFamillesNotifyGruConstants.MARK_USER_TITLES,
            ( ticket.getUserTitle(  ) != null ) ? ticket.getUserTitle(  ) : "" );
        model.put( TicketFacilFamillesNotifyGruConstants.MARK_TICKET_TYPES,
            ( ticket.getTicketType(  ) != null ) ? ticket.getTicketType(  ) : "" );
        model.put( TicketFacilFamillesNotifyGruConstants.MARK_TICKET_DOMAINS,
            ( ticket.getTicketDomain(  ) != null ) ? ticket.getTicketDomain(  ) : "" );
        model.put( TicketFacilFamillesNotifyGruConstants.MARK_TICKET_CATEGORIES,
            ( ticket.getTicketCategory(  ) != null ) ? ticket.getTicketCategory(  ) : "" );
        model.put( TicketFacilFamillesNotifyGruConstants.MARK_CONTACT_MODES,
            ( ticket.getContactMode(  ) != null ) ? ticket.getContactMode(  ) : "" );
        model.put( TicketFacilFamillesNotifyGruConstants.MARK_COMMENT,
            ( ticket.getTicketComment(  ) != null ) ? ticket.getTicketComment(  ) : "" );

        model.put( TicketFacilFamillesNotifyGruConstants.MARK_CHANNEL,
            ( ( ticket.getChannel(  ) != null ) && ( ticket.getChannel(  ).getLabel(  ) != null ) )
            ? ticket.getChannel(  ).getLabel(  ) : "" );

        String strUrlCompleted = ( ticket.getUrl(  ) != null ) ? ticket.getUrl(  ) : "";

        model.put( TicketFacilFamillesNotifyGruConstants.MARK_URL_COMPLETED, strUrlCompleted.replaceAll( "&", "&amp;" ) );

        //SPECIFIC EMAIL AGENT
        model.put( TicketFacilFamillesNotifyGruConstants.MARK_EMAILAGENT_MESSAGE,
            ticketFacilFamillesHistory.getMessage(  ) );
        model.put( TicketFacilFamillesNotifyGruConstants.MARK_EMAILAGENT_LINK,
            buildTicketLink( ticket.getId(  ), ticketFacilFamillesHistory.getIdTask(  ) ) );

        return model;
    }

    /**
     * @param nIdTicket the ticket id
     * @param nIdTask the task id
     * @return build url
     */
    private String buildTicketLink( int nIdTicket, int nIdTask )
    {
        //TODO UPDATE
        StringBuilder strTicketLink = new StringBuilder( AppPropertiesService.getProperty( RESPONSE_URL ) );
        strTicketLink.append( "&id=" );
        strTicketLink.append( nIdTicket );

        return strTicketLink.toString(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateListProvider( ITask task )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateListProvider(  )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList buildReferenteListProvider(  )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList getReferenteListEntityProvider(  )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isKeyProvider( String strKey )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractServiceProvider getInstanceProvider( String strKey )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
}
