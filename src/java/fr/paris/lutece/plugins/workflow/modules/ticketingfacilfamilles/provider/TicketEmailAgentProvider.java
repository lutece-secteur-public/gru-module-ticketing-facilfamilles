/*
 * Copyright (c) 2002-2017, Mairie de Paris
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringEscapeUtils;

import fr.paris.lutece.plugins.ticketing.business.ticket.Ticket;
import fr.paris.lutece.plugins.ticketing.business.ticket.TicketHome;
import fr.paris.lutece.plugins.ticketing.business.tickettype.TicketTypeHome;
import fr.paris.lutece.plugins.workflow.modules.notifygru.service.provider.IProvider;
import fr.paris.lutece.plugins.workflow.modules.notifygru.service.provider.NotifyGruMarker;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.demand.ITicketingEmailAgentMessageDAO;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.demand.TicketingEmailAgentMessage;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.history.ITicketEmailAgentHistoryDAO;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.history.TicketEmailAgentHistory;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.service.RequestAuthenticationService;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.LocalVariables;
import fr.paris.lutece.util.url.UrlItem;

/**
 *
 */
public class TicketEmailAgentProvider implements IProvider
{
    // MESSAGE KEY
    private static final String MESSAGE_MARKER_TICKET_REFERENCE = "module.workflow.ticketingfacilfamilles.task_ticket_emailagent_config.label_entry.reference";
    private static final String MESSAGE_MARKER_TICKET_DOMAIN = "module.workflow.ticketingfacilfamilles.task_ticket_emailagent_config.label_entry.ticket_domain";
    private static final String MESSAGE_MARKER_TICKET_TYPE = "module.workflow.ticketingfacilfamilles.task_ticket_emailagent_config.label_entry.ticket_type";
    private static final String MESSAGE_MARKER_TICKET_CATEGORY = "module.workflow.ticketingfacilfamilles.task_ticket_emailagent_config.label_entry.ticket_category";
    private static final String MESSAGE_MARKER_TICKET_CATEGORY_PRECISION = "module.workflow.ticketingfacilfamilles.task_ticket_emailagent_config.label_entry.ticket_category_precision";
    private static final String MESSAGE_MARKER_TICKET_CHANNEL = "module.workflow.ticketingfacilfamilles.task_ticket_emailagent_config.label_entry.ticket_channel";
    private static final String MESSAGE_MARKER_TICKET_COMMENT = "module.workflow.ticketingfacilfamilles.task_ticket_emailagent_config.label_entry.comment";
    private static final String MESSAGE_MARKER_USER_TITLE = "module.workflow.ticketingfacilfamilles.task_ticket_emailagent_config.label_entry.civility";
    private static final String MESSAGE_MARKER_USER_FIRSTNAME = "module.workflow.ticketingfacilfamilles.task_ticket_emailagent_config.label_entry.firstname";
    private static final String MESSAGE_MARKER_USER_LASTNAME = "module.workflow.ticketingfacilfamilles.task_ticket_emailagent_config.label_entry.lastname";
    private static final String MESSAGE_MARKER_USER_UNIT = "module.workflow.ticketingfacilfamilles.task_ticket_emailagent_config.label_entry.unit_name";
    private static final String MESSAGE_MARKER_USER_CONTACT_MODE = "module.workflow.ticketingfacilfamilles.task_ticket_emailagent_config.label_entry.contact_mode";
    private static final String MESSAGE_MARKER_USER_FIXED_PHONE_NUMBER = "module.workflow.ticketingfacilfamilles.task_ticket_emailagent_config.label_entry.fixed_phone";
    private static final String MESSAGE_MARKER_USER_MOBILE_PHONE_NUMBER = "module.workflow.ticketingfacilfamilles.task_ticket_emailagent_config.label_entry.mobile_phone";
    private static final String MESSAGE_MARKER_USER_EMAIL = "module.workflow.ticketingfacilfamilles.task_ticket_emailagent_config.label_entry.email";
    private static final String MESSAGE_MARKER_TECHNICAL_URL_COMPLETE = "module.workflow.ticketingfacilfamilles.task_ticket_emailagent_config.label_entry.url_completed";
    private static final String MESSAGE_MARKER_FACILFAMILLE_EMAIL_RECIPIENTS = "module.workflow.ticketingfacilfamilles.task_ticket_emailagent_config.label_entry.email_recipients";
    private static final String MESSAGE_MARKER_FACILFAMILLE_EMAIL_RECIPIENTS_CC = "module.workflow.ticketingfacilfamilles.task_ticket_emailagent_config.label_entry.email_recipients_cc";
    private static final String MESSAGE_MARKER_FACILFAMILLE_MESSAGE = "module.workflow.ticketingfacilfamilles.task_ticket_emailagent_config.label_entry.message";
    private static final String MESSAGE_MARKER_FACILFAMILLE_LINK = "module.workflow.ticketingfacilfamilles.task_ticket_emailagent_config.label_entry.ticketing_ticket_link";

    /** Parameter for response url */
    private static final String RESPONSE_URL = "module.workflow.ticketingfacilfamilles.task_ticket_emailagent.url_response";

    /** The TicketEmailAgentHistory DAO. */
    private ITicketEmailAgentHistoryDAO _ticketEmailAgentHistoryDAO = SpringContextService.getBean( ITicketEmailAgentHistoryDAO.BEAN_SERVICE );

    /** The TicketingEmailAgentDemand DAO. */
    private ITicketingEmailAgentMessageDAO _ticketingEmailAgentDemandDAO = SpringContextService.getBean( ITicketingEmailAgentMessageDAO.BEAN_SERVICE );

    private Ticket _ticket;
    private TicketingEmailAgentMessage _emailAgentDemand;

    /**
     * Constructor for a given resource
     * 
     * @param resourceHistory
     *            the resource wich require the provider
     */
    public TicketEmailAgentProvider( ResourceHistory resourceHistory )
    {
        _ticket = TicketHome.findByPrimaryKey( resourceHistory.getIdResource( ) );
        if ( _ticket == null )
        {
            throw new AppException( "No ticket for resource history Id : " + resourceHistory.getIdResource( ) );
        }
        TicketEmailAgentHistory ticketEmailAgentHistory = _ticketEmailAgentHistoryDAO.loadByIdHistory( resourceHistory.getId( ) );
        if ( ticketEmailAgentHistory == null )
        {
            throw new AppException( "No ticketEmailAgentHistory found for resource history Id : " + resourceHistory.getId( ) );
        }
        _emailAgentDemand = _ticketingEmailAgentDemandDAO.loadByIdMessageAgent( ticketEmailAgentHistory.getIdMessageAgent( ) );
        if ( _emailAgentDemand == null )
        {
            throw new AppException( "No TicketingEmailAgentDemand found for demand id : " + ticketEmailAgentHistory.getIdMessageAgent( ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String provideDemandId( )
    {
        return String.valueOf( _ticket.getId( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String provideDemandTypeId( )
    {
        return String.valueOf( TicketTypeHome.findByPrimaryKey( _ticket.getIdTicketType( ) ).getDemandTypeId( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String provideDemandReference( )
    {
        return _ticket.getReference( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String provideCustomerConnectionId( )
    {
        return _ticket.getGuid( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String provideCustomerId( )
    {
        return _ticket.getCustomerId( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String provideCustomerEmail( )
    {
        return _ticket.getEmail( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String provideCustomerMobilePhone( )
    {
        return _ticket.getMobilePhoneNumber( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<NotifyGruMarker> provideMarkerValues( )
    {
        Collection<NotifyGruMarker> collectionNotifyGruMarkers = new ArrayList<>( );

        collectionNotifyGruMarkers.add( createMarkerValues( TicketEmailAgentNotifyGruConstants.MARK_USER_TITLE, _ticket.getUserTitle( ) ) );
        collectionNotifyGruMarkers.add( createMarkerValues( TicketEmailAgentNotifyGruConstants.MARK_USER_FIRSTNAME, _ticket.getFirstname( ) ) );
        collectionNotifyGruMarkers.add( createMarkerValues( TicketEmailAgentNotifyGruConstants.MARK_USER_LASTNAME, _ticket.getLastname( ) ) );

        if ( _ticket.getAssigneeUnit( ) != null )
        {
            collectionNotifyGruMarkers
                    .add( createMarkerValues( TicketEmailAgentNotifyGruConstants.MARK_USER_UNIT_NAME, _ticket.getAssigneeUnit( ).getName( ) ) );
        }

        collectionNotifyGruMarkers.add( createMarkerValues( TicketEmailAgentNotifyGruConstants.MARK_USER_CONTACT_MODE, _ticket.getContactMode( ) ) );
        collectionNotifyGruMarkers.add( createMarkerValues( TicketEmailAgentNotifyGruConstants.MARK_USER_FIXED_PHONE, _ticket.getFixedPhoneNumber( ) ) );
        collectionNotifyGruMarkers.add( createMarkerValues( TicketEmailAgentNotifyGruConstants.MARK_USER_MOBILE_PHONE, _ticket.getMobilePhoneNumber( ) ) );
        collectionNotifyGruMarkers.add( createMarkerValues( TicketEmailAgentNotifyGruConstants.MARK_USER_EMAIL, _ticket.getEmail( ) ) );
        collectionNotifyGruMarkers.add( createMarkerValues( TicketEmailAgentNotifyGruConstants.MARK_USER_MESSAGE, _ticket.getUserMessage( ) ) );
        collectionNotifyGruMarkers.add( createMarkerValues( TicketEmailAgentNotifyGruConstants.MARK_TICKET_REFERENCE, _ticket.getReference( ) ) );
        collectionNotifyGruMarkers.add( createMarkerValues( TicketEmailAgentNotifyGruConstants.MARK_TICKET_TYPE, _ticket.getTicketType( ) ) );
        collectionNotifyGruMarkers.add( createMarkerValues( TicketEmailAgentNotifyGruConstants.MARK_TICKET_DOMAIN, _ticket.getTicketDomain( ) ) );

        if ( _ticket.getTicketCategory( ) != null )
        {
            collectionNotifyGruMarkers.add( createMarkerValues( TicketEmailAgentNotifyGruConstants.MARK_TICKET_CATEGORY, _ticket.getTicketCategory( )
                    .getLabel( ) ) );
            collectionNotifyGruMarkers.add( createMarkerValues( TicketEmailAgentNotifyGruConstants.MARK_TICKET_CATEGORY_PRECISION, _ticket.getTicketCategory( )
                    .getPrecision( ) ) );
        }

        if ( _ticket.getChannel( ) != null )
        {
            collectionNotifyGruMarkers.add( createMarkerValues( TicketEmailAgentNotifyGruConstants.MARK_TICKET_CHANNEL, _ticket.getChannel( ).getLabel( ) ) );
        }

        collectionNotifyGruMarkers.add( createMarkerValues( TicketEmailAgentNotifyGruConstants.MARK_TICKET_COMMENT, _ticket.getTicketComment( ) ) );

        if ( _ticket.getUrl( ) != null )
        {
            collectionNotifyGruMarkers.add( createMarkerValues( TicketEmailAgentNotifyGruConstants.MARK_TECHNICAL_URL_COMPLETED,
                    StringEscapeUtils.escapeHtml( _ticket.getUrl( ) ) ) );
        }

        // SPECIFIC EMAIL AGENT
        collectionNotifyGruMarkers.add( createMarkerValues( TicketEmailAgentNotifyGruConstants.MARK_FACILFAMILLE_EMAIL_RECIPIENTS,
                _emailAgentDemand.getEmailRecipients( ) ) );
        collectionNotifyGruMarkers.add( createMarkerValues( TicketEmailAgentNotifyGruConstants.MARK_FACILFAMILLE_EMAIL_RECIPIENTS_CC,
                _emailAgentDemand.getEmailRecipientsCc( ) ) );
        collectionNotifyGruMarkers.add( createMarkerValues( TicketEmailAgentNotifyGruConstants.MARK_FACILFAMILLE_MESSAGE,
                _emailAgentDemand.getMessageQuestion( ) ) );
        collectionNotifyGruMarkers.add( createMarkerValues( TicketEmailAgentNotifyGruConstants.MARK_FACILFAMILLE_LINK,
                buildTicketLink( _emailAgentDemand.getIdMessageAgent( ) ) ) );

        return collectionNotifyGruMarkers;
    }

    /**
     * static method for retrieving descriptions of available marks
     * 
     * @return Collection of NotifyGruMarker
     */
    public static Collection<NotifyGruMarker> getProviderMarkerDescriptions( )
    {
        Collection<NotifyGruMarker> collectionNotifyGruMarkers = new ArrayList<>( );

        // GENERIC GRU
        collectionNotifyGruMarkers.add( createMarkerDescriptions( TicketEmailAgentNotifyGruConstants.MARK_USER_TITLE, MESSAGE_MARKER_USER_TITLE ) );
        collectionNotifyGruMarkers.add( createMarkerDescriptions( TicketEmailAgentNotifyGruConstants.MARK_USER_FIRSTNAME, MESSAGE_MARKER_USER_FIRSTNAME ) );
        collectionNotifyGruMarkers.add( createMarkerDescriptions( TicketEmailAgentNotifyGruConstants.MARK_USER_LASTNAME, MESSAGE_MARKER_USER_LASTNAME ) );
        collectionNotifyGruMarkers.add( createMarkerDescriptions( TicketEmailAgentNotifyGruConstants.MARK_USER_UNIT_NAME, MESSAGE_MARKER_USER_UNIT ) );
        collectionNotifyGruMarkers
                .add( createMarkerDescriptions( TicketEmailAgentNotifyGruConstants.MARK_USER_CONTACT_MODE, MESSAGE_MARKER_USER_CONTACT_MODE ) );
        collectionNotifyGruMarkers.add( createMarkerDescriptions( TicketEmailAgentNotifyGruConstants.MARK_USER_FIXED_PHONE,
                MESSAGE_MARKER_USER_FIXED_PHONE_NUMBER ) );
        collectionNotifyGruMarkers.add( createMarkerDescriptions( TicketEmailAgentNotifyGruConstants.MARK_USER_MOBILE_PHONE,
                MESSAGE_MARKER_USER_MOBILE_PHONE_NUMBER ) );
        collectionNotifyGruMarkers.add( createMarkerDescriptions( TicketEmailAgentNotifyGruConstants.MARK_USER_EMAIL, MESSAGE_MARKER_USER_EMAIL ) );
        collectionNotifyGruMarkers.add( createMarkerDescriptions( TicketEmailAgentNotifyGruConstants.MARK_TICKET_REFERENCE, MESSAGE_MARKER_TICKET_REFERENCE ) );
        collectionNotifyGruMarkers.add( createMarkerDescriptions( TicketEmailAgentNotifyGruConstants.MARK_TICKET_TYPE, MESSAGE_MARKER_TICKET_TYPE ) );
        collectionNotifyGruMarkers.add( createMarkerDescriptions( TicketEmailAgentNotifyGruConstants.MARK_TICKET_DOMAIN, MESSAGE_MARKER_TICKET_DOMAIN ) );
        collectionNotifyGruMarkers.add( createMarkerDescriptions( TicketEmailAgentNotifyGruConstants.MARK_TICKET_CATEGORY, MESSAGE_MARKER_TICKET_CATEGORY ) );
        collectionNotifyGruMarkers.add( createMarkerDescriptions( TicketEmailAgentNotifyGruConstants.MARK_TICKET_CATEGORY_PRECISION,
                MESSAGE_MARKER_TICKET_CATEGORY_PRECISION ) );
        collectionNotifyGruMarkers.add( createMarkerDescriptions( TicketEmailAgentNotifyGruConstants.MARK_TICKET_CHANNEL, MESSAGE_MARKER_TICKET_CHANNEL ) );
        collectionNotifyGruMarkers.add( createMarkerDescriptions( TicketEmailAgentNotifyGruConstants.MARK_TICKET_COMMENT, MESSAGE_MARKER_TICKET_COMMENT ) );
        collectionNotifyGruMarkers.add( createMarkerDescriptions( TicketEmailAgentNotifyGruConstants.MARK_TECHNICAL_URL_COMPLETED,
                MESSAGE_MARKER_TECHNICAL_URL_COMPLETE ) );
        // SPECIFIC EMAIL AGENT
        collectionNotifyGruMarkers.add( createMarkerDescriptions( TicketEmailAgentNotifyGruConstants.MARK_FACILFAMILLE_EMAIL_RECIPIENTS,
                MESSAGE_MARKER_FACILFAMILLE_EMAIL_RECIPIENTS ) );
        collectionNotifyGruMarkers.add( createMarkerDescriptions( TicketEmailAgentNotifyGruConstants.MARK_FACILFAMILLE_EMAIL_RECIPIENTS_CC,
                MESSAGE_MARKER_FACILFAMILLE_EMAIL_RECIPIENTS_CC ) );
        collectionNotifyGruMarkers.add( createMarkerDescriptions( TicketEmailAgentNotifyGruConstants.MARK_FACILFAMILLE_MESSAGE,
                MESSAGE_MARKER_FACILFAMILLE_MESSAGE ) );
        collectionNotifyGruMarkers
                .add( createMarkerDescriptions( TicketEmailAgentNotifyGruConstants.MARK_FACILFAMILLE_LINK, MESSAGE_MARKER_FACILFAMILLE_LINK ) );

        return collectionNotifyGruMarkers;
    }

    /**
     * Construct a NotifyGruMarker with value for given parameters
     * 
     * @param strMarker
     * @param strValue
     * @return a NotifyGruMarker
     */
    private static NotifyGruMarker createMarkerValues( String strMarker, String strValue )
    {
        NotifyGruMarker notifyGruMarker = new NotifyGruMarker( strMarker );
        notifyGruMarker.setValue( strValue );

        return notifyGruMarker;
    }

    /**
     * Construct a NotifyGruMarker with descrition for given parameters
     * 
     * @param strMarker
     * @param strDescription
     * @return a NotifyGruMarker
     */
    private static NotifyGruMarker createMarkerDescriptions( String strMarker, String strDescription )
    {
        NotifyGruMarker notifyGruMarker = new NotifyGruMarker( strMarker );
        notifyGruMarker.setDescription( I18nService.getLocalizedString( strDescription, I18nService.getDefaultLocale( ) ) );

        return notifyGruMarker;
    }

    /**
     * @param nIdMessageAgent
     *            the MessageAgent id
     * @return build url
     */
    private String buildTicketLink( int nIdMessageAgent )
    {
        List<String> listElements = new ArrayList<String>( );
        listElements.add( Integer.toString( nIdMessageAgent ) );

        String strTimestamp = Long.toString( new Date( ).getTime( ) );
        String strSignature = RequestAuthenticationService.getRequestAuthenticator( ).buildSignature( listElements, strTimestamp );

        UrlItem urlTicketLink = new UrlItem( AppPathService.getBaseUrl( LocalVariables.getRequest( ) ) + AppPropertiesService.getProperty( RESPONSE_URL ) );
        urlTicketLink.addParameter( TicketEmailAgentNotifyGruConstants.PARAMETER_ID_MESSAGE_AGENT, nIdMessageAgent );
        urlTicketLink.addParameter( TicketEmailAgentNotifyGruConstants.PARAMETER_SIGNATURE, strSignature );
        urlTicketLink.addParameter( TicketEmailAgentNotifyGruConstants.PARAMETER_ID_TIMETAMP, strTimestamp );

        return StringEscapeUtils.escapeHtml( urlTicketLink.getUrl( ) );
    }

}
