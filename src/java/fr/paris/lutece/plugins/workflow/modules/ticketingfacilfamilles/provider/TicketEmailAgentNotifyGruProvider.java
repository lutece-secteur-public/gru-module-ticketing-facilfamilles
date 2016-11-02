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
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.demand.ITicketingEmailAgentMessageDAO;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.demand.TicketingEmailAgentMessage;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.history.ITicketEmailAgentHistoryDAO;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.history.TicketEmailAgentHistory;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.service.RequestAuthenticationService;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.service.resource.IResourceHistoryService;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.LocalVariables;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;


/**
 *
 */
public class TicketEmailAgentNotifyGruProvider extends AbstractServiceProvider
{
    /** The Constant FORM_HELPER. */
    private static final String FORM_HELPER = "admin/plugins/workflow/modules/ticketingfacilfamilles/form_helper.html";
    private static final String LIST_ENTRIES = "list_entries";

    /** Parameter for response url */
    private static final String RESPONSE_URL = "module.workflow.ticketingfacilfamilles.task_ticket_emailagent.url_response";

    /** The _resource history service. */
    @Inject
    private IResourceHistoryService _resourceHistoryService;

    /** The TicketEmailAgentHistory DAO. */
    @Inject
    @Named( ITicketEmailAgentHistoryDAO.BEAN_SERVICE )
    private ITicketEmailAgentHistoryDAO _ticketEmailAgentHistoryDAO;

    /** The TicketingEmailAgentDemand DAO. */
    @Inject
    @Named( ITicketingEmailAgentMessageDAO.BEAN_SERVICE )
    private ITicketingEmailAgentMessageDAO _ticketingEmailAgentDemandDAO;
    private Ticket _ticket;
    private TicketEmailAgentHistory _ticketEmailAgentHistory;
    private TicketingEmailAgentMessage _emailAgentDemand;

    /**
     * Get ticket fot the given id resource
     *
     * @param nIdResourceHistory the n id resource history
     * @return the ticket
     */
    private Ticket getTicket( int nIdResourceHistory )
    {
        if ( _ticket == null )
        {
            ResourceHistory resourceHistory = _resourceHistoryService.findByPrimaryKey( nIdResourceHistory );
            _ticket = TicketHome.findByPrimaryKey( resourceHistory.getIdResource(  ) );

            if ( _ticket == null )
            {
                throw new AppException( "No ticket found for resource history Id : " + nIdResourceHistory );
            }
        }

        return _ticket;
    }

    /**
     * Get ticketEmailAgentHistory fot the given id resource
     *
     * @param nIdResourceHistory the n id resource history
     * @return the ticket
     */
    private TicketEmailAgentHistory getTicketEmailAgentHistory( int nIdResourceHistory )
    {
        if ( _ticketEmailAgentHistory == null )
        {
            _ticketEmailAgentHistory = _ticketEmailAgentHistoryDAO.loadByIdHistory( nIdResourceHistory );

            if ( _ticketEmailAgentHistory == null )
            {
                throw new AppException( "No ticketEmailAgentHistory found for resource history Id : " +
                    nIdResourceHistory );
            }
        }

        return _ticketEmailAgentHistory;
    }

    /**
     * Get TicketingEmailAgentMessage fot the given id demand
     *
     * @param nIdMessageAgent the id MessageAgent
     * @return the ticketingEmailAgentMessage
     */
    private TicketingEmailAgentMessage getTicketingEmailAgentMessage( int nIdMessageAgent )
    {
        if ( _emailAgentDemand == null )
        {
            _emailAgentDemand = _ticketingEmailAgentDemandDAO.loadByIdMessageAgent( nIdMessageAgent );

            if ( _emailAgentDemand == null )
            {
                throw new AppException( "No TicketingEmailAgentDemand found for demand id : " + nIdMessageAgent );
            }
        }

        return _emailAgentDemand;
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
        return getTicket( nIdResourceHistory ).getEmail(  );
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
        return TicketEmailAgentNotifyGruConstants.STR_NO_CUSTOMER_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getOptionalDemandIdType( int nIdResourceHistory )
    {
        //NOT USED FOR THE MOMENT
        return TicketEmailAgentNotifyGruConstants.EMPTY_OPTIONAL_DEMAND_ID_TYPE;
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
        // GENERIC GRU
        lstEntries.add( TicketEmailAgentNotifyGruConstants.MARK_FIRSTNAME );
        lstEntries.add( TicketEmailAgentNotifyGruConstants.MARK_LASTNAME );
        lstEntries.add( TicketEmailAgentNotifyGruConstants.MARK_FIXED_PHONE );
        lstEntries.add( TicketEmailAgentNotifyGruConstants.MARK_MOBILE_PHONE );
        lstEntries.add( TicketEmailAgentNotifyGruConstants.MARK_EMAIL );
        lstEntries.add( TicketEmailAgentNotifyGruConstants.MARK_UNIT_NAME );
        lstEntries.add( TicketEmailAgentNotifyGruConstants.MARK_REFERENCE );
        lstEntries.add( TicketEmailAgentNotifyGruConstants.MARK_USER_TITLES );
        lstEntries.add( TicketEmailAgentNotifyGruConstants.MARK_TICKET_TYPES );
        lstEntries.add( TicketEmailAgentNotifyGruConstants.MARK_TICKET_DOMAINS );
        lstEntries.add( TicketEmailAgentNotifyGruConstants.MARK_TICKET_CATEGORIES );
        lstEntries.add( TicketEmailAgentNotifyGruConstants.MARK_CONTACT_MODES );
        lstEntries.add( TicketEmailAgentNotifyGruConstants.MARK_COMMENT );
        lstEntries.add( TicketEmailAgentNotifyGruConstants.MARK_CHANNEL );
        lstEntries.add( TicketEmailAgentNotifyGruConstants.MARK_URL_COMPLETED );
        //SPECIFIC EMAIL AGENT;
        lstEntries.add( TicketEmailAgentNotifyGruConstants.MARK_FACILFAMILLE_EMAIL );
        lstEntries.add( TicketEmailAgentNotifyGruConstants.MARK_FACILFAMILLE_MESSAGE );
        lstEntries.add( TicketEmailAgentNotifyGruConstants.MARK_FACILFAMILLE_LINK );

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
            TicketEmailAgentHistory ticketEmailAgentHistory = getTicketEmailAgentHistory( nIdResourceHistory );
            model = buildModelNotifyGruTicketing( getTicket( nIdResourceHistory ),
                    getTicketingEmailAgentMessage( ticketEmailAgentHistory.getIdMessageAgent(  ) ) );
        }
        else
        {
            model = buildModelNotifyGruTicketing( new Ticket(  ), new TicketingEmailAgentMessage(  ) );
        }

        return model;
    }

    /**
     * Builds the model notify gru ticketing.
     *
     * @param ticket the ticket
     * @param emailAgentDemand the TicketingEmailAgentDemand
     * @return the map
     */
    private Map<String, Object> buildModelNotifyGruTicketing( Ticket ticket, TicketingEmailAgentMessage emailAgentDemand )
    {
        Map<String, Object> model = new HashMap<String, Object>(  );
        // GENERIC GRU
        model.put( TicketEmailAgentNotifyGruConstants.MARK_GUID,
            ( ticket.getGuid(  ) != null ) ? ticket.getGuid(  ) : "" );
        model.put( TicketEmailAgentNotifyGruConstants.MARK_FIRSTNAME,
            ( ticket.getFirstname(  ) != null ) ? ticket.getFirstname(  ) : "" );
        model.put( TicketEmailAgentNotifyGruConstants.MARK_LASTNAME,
            ( ticket.getLastname(  ) != null ) ? ticket.getLastname(  ) : "" );
        model.put( TicketEmailAgentNotifyGruConstants.MARK_FIXED_PHONE,
            ( ticket.getFixedPhoneNumber(  ) != null ) ? ticket.getFixedPhoneNumber(  ) : "" );
        model.put( TicketEmailAgentNotifyGruConstants.MARK_MOBILE_PHONE,
            ( ticket.getMobilePhoneNumber(  ) != null ) ? ticket.getMobilePhoneNumber(  ) : "" );
        model.put( TicketEmailAgentNotifyGruConstants.MARK_EMAIL,
            ( ticket.getEmail(  ) != null ) ? ticket.getEmail(  ) : "" );
        model.put( TicketEmailAgentNotifyGruConstants.MARK_UNIT_NAME,
            ( ( ticket.getAssigneeUnit(  ) != null ) && ( ticket.getAssigneeUnit(  ).getName(  ) != null ) )
            ? ticket.getAssigneeUnit(  ).getName(  ) : "" );
        model.put( TicketEmailAgentNotifyGruConstants.MARK_REFERENCE,
            ( ticket.getReference(  ) != null ) ? ticket.getReference(  ) : "" );
        model.put( TicketEmailAgentNotifyGruConstants.MARK_TICKET, ticket );
        model.put( TicketEmailAgentNotifyGruConstants.MARK_USER_TITLES,
            ( ticket.getUserTitle(  ) != null ) ? ticket.getUserTitle(  ) : "" );
        model.put( TicketEmailAgentNotifyGruConstants.MARK_TICKET_TYPES,
            ( ticket.getTicketType(  ) != null ) ? ticket.getTicketType(  ) : "" );
        model.put( TicketEmailAgentNotifyGruConstants.MARK_TICKET_DOMAINS,
            ( ticket.getTicketDomain(  ) != null ) ? ticket.getTicketDomain(  ) : "" );
        model.put( TicketEmailAgentNotifyGruConstants.MARK_TICKET_CATEGORIES,
            ( ticket.getTicketCategory(  ) != null ) ? ticket.getTicketCategory(  ) : "" );
        model.put( TicketEmailAgentNotifyGruConstants.MARK_CONTACT_MODES,
            ( ticket.getContactMode(  ) != null ) ? ticket.getContactMode(  ) : "" );
        model.put( TicketEmailAgentNotifyGruConstants.MARK_COMMENT,
            ( ticket.getTicketComment(  ) != null ) ? ticket.getTicketComment(  ) : "" );

        model.put( TicketEmailAgentNotifyGruConstants.MARK_CHANNEL,
            ( ( ticket.getChannel(  ) != null ) && ( ticket.getChannel(  ).getLabel(  ) != null ) )
            ? ticket.getChannel(  ).getLabel(  ) : "" );

        String strUrlCompleted = ( ticket.getUrl(  ) != null ) ? ticket.getUrl(  ) : "";

        model.put( TicketEmailAgentNotifyGruConstants.MARK_URL_COMPLETED,
            StringEscapeUtils.escapeHtml( strUrlCompleted ) );

        //SPECIFIC EMAIL AGENT
        model.put( TicketEmailAgentNotifyGruConstants.MARK_FACILFAMILLE_EMAIL,
            ( emailAgentDemand.getEmailAgent(  ) != null ) ? emailAgentDemand.getEmailAgent(  ) : StringUtils.EMPTY );
        model.put( TicketEmailAgentNotifyGruConstants.MARK_FACILFAMILLE_MESSAGE,
            ( emailAgentDemand.getMessageQuestion(  ) != null ) ? emailAgentDemand.getMessageQuestion(  )
                                                                : StringUtils.EMPTY );
        model.put( TicketEmailAgentNotifyGruConstants.MARK_FACILFAMILLE_LINK,
            buildTicketLink( emailAgentDemand.getIdMessageAgent(  ) ) );

        return model;
    }

    /**
     * @param nIdMessageAgent the MessageAgent id
     * @return build url
     */
    private String buildTicketLink( int nIdMessageAgent )
    {
        List<String> listElements = new ArrayList<String>(  );
        listElements.add( Integer.toString( nIdMessageAgent ) );

        String strTimestamp = Long.toString( new Date(  ).getTime(  ) );
        String strSignature = RequestAuthenticationService.getRequestAuthenticator(  )
                                                          .buildSignature( listElements, strTimestamp );

        UrlItem urlTicketLink = new UrlItem( AppPathService.getProdUrl( LocalVariables.getRequest(  ) ) +
                AppPropertiesService.getProperty( RESPONSE_URL ) );
        urlTicketLink.addParameter( TicketEmailAgentNotifyGruConstants.PARAMETER_ID_MESSAGE_AGENT, nIdMessageAgent );
        urlTicketLink.addParameter( TicketEmailAgentNotifyGruConstants.PARAMETER_SIGNATURE, strSignature );
        urlTicketLink.addParameter( TicketEmailAgentNotifyGruConstants.PARAMETER_ID_TIMETAMP, strTimestamp );

        return StringEscapeUtils.escapeHtml( urlTicketLink.getUrl(  ) );
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
