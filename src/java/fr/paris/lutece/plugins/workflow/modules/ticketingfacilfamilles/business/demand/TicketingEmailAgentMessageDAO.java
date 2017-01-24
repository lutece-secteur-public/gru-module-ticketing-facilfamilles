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
package fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.demand;

import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.service.WorkflowTicketingFacilFamillesPlugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
public class TicketingEmailAgentMessageDAO implements ITicketingEmailAgentMessageDAO
{
    private static final String SQL_QUERY_NEW_PK = " SELECT max( id_message_agent ) FROM ticketing_facilfamilles_emailagent ";
    private static final String SQL_QUERY_LAST_QUESTION = " SELECT max( id_message_agent ) FROM ticketing_facilfamilles_emailagent WHERE id_ticket = ? AND is_answered = 0";
    private static final String SQL_QUERY_FIND_BY_ID_DEMAND = " SELECT id_message_agent, id_ticket, email_agent, message_question, message_response, is_answered FROM ticketing_facilfamilles_emailagent "
            + " WHERE id_message_agent = ? ";
    private static final String SQL_QUERY_INSERT_QUESTION = " INSERT INTO ticketing_facilfamilles_emailagent ( id_message_agent, id_ticket, email_agent, message_question ) "
            + " VALUES ( ?,?,?,? ) ";
    private static final String SQL_QUERY_ADD_ANSWER = " UPDATE ticketing_facilfamilles_emailagent SET message_response = ?, is_answered = 1 WHERE id_message_agent = ? ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM ticketing_facilfamilles_emailagent WHERE id_message_agent = ? ";
    private static final String SQL_QUERY_FIND_BY_ID_TICKET_NOT_CLOSED = " SELECT id_message_agent, id_ticket, email_agent, message_question, message_response, is_answered FROM ticketing_facilfamilles_emailagent "
            + " WHERE id_ticket = ? AND is_answered = 0 ORDER BY id_message_agent DESC ";
    private static final String SQL_QUERY_CLOSE_BY_ID_TICKET = " UPDATE ticketing_facilfamilles_emailagent SET is_answered = 1 WHERE id_ticket = ? ";
    private static final String SQL_QUERY_FIRST_MESSAGE = " SELECT min(id_message_agent), id_ticket, email_agent, message_question, message_response, is_answered FROM ticketing_facilfamilles_emailagent "
            + " WHERE id_ticket = ? AND is_answered = 0 ";

    /**
     * Generates a new primary key
     * 
     * @return The new primary key
     */
    private int nextPrimaryKey( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, PluginService.getPlugin( WorkflowTicketingFacilFamillesPlugin.PLUGIN_NAME ) );
        daoUtil.executeQuery( );

        int nKey;

        if ( !daoUtil.next( ) )
        {
            // if the table is empty
            nKey = 1;
        }

        nKey = daoUtil.getInt( 1 ) + 1;
        daoUtil.free( );

        return nKey;
    }

    /**
     * Retrieve the last demande with no response for a given ticket id
     * 
     * @param nIdTicket
     *            the ticket id
     * @return the id of TicketingEmailAgentMessage
     */
    private int getLastQuestion( int nIdTicket )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_LAST_QUESTION, PluginService.getPlugin( WorkflowTicketingFacilFamillesPlugin.PLUGIN_NAME ) );

        daoUtil.setInt( 1, nIdTicket );
        daoUtil.executeQuery( );

        int nKey = 0;

        if ( daoUtil.next( ) )
        {
            // if the table is not empty
            nKey = daoUtil.getInt( 1 );
        }

        daoUtil.free( );

        return nKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( "workflow.transactionManager" )
    public synchronized void createQuestion( TicketingEmailAgentMessage emailAgentDemand )
    {
        emailAgentDemand.setIdMessageAgent( nextPrimaryKey( ) );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_QUESTION, PluginService.getPlugin( WorkflowTicketingFacilFamillesPlugin.PLUGIN_NAME ) );

        int nIndex = 1;

        daoUtil.setInt( nIndex++, emailAgentDemand.getIdMessageAgent( ) );
        daoUtil.setInt( nIndex++, emailAgentDemand.getIdTicket( ) );
        daoUtil.setString( nIndex++, emailAgentDemand.getEmailAgent( ) );
        daoUtil.setString( nIndex++, emailAgentDemand.getMessageQuestion( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLastQuestion( int nIdTicket, int nIdMessageAgent )
    {
        int nLastIdMessageAgent = getLastQuestion( nIdTicket );

        return ( ( nLastIdMessageAgent > 0 ) && ( nIdMessageAgent == nLastIdMessageAgent ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( "workflow.transactionManager" )
    public synchronized int addAnswer( int nIdTicket, String strReponse )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_ADD_ANSWER, PluginService.getPlugin( WorkflowTicketingFacilFamillesPlugin.PLUGIN_NAME ) );

        int nIndex = 1;
        int nIdMessageAgent = getLastQuestion( nIdTicket );

        daoUtil.setString( nIndex++, strReponse );
        daoUtil.setInt( nIndex++, nIdMessageAgent );

        daoUtil.executeUpdate( );
        daoUtil.free( );

        return nIdMessageAgent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( "workflow.transactionManager" )
    public void closeMessagesByIdTicket( int nIdTicket )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_CLOSE_BY_ID_TICKET, PluginService.getPlugin( WorkflowTicketingFacilFamillesPlugin.PLUGIN_NAME ) );

        daoUtil.setInt( 1, nIdTicket );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TicketingEmailAgentMessage loadByIdMessageAgent( int nIdMessageAgent )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_ID_DEMAND, PluginService.getPlugin( WorkflowTicketingFacilFamillesPlugin.PLUGIN_NAME ) );

        daoUtil.setInt( 1, nIdMessageAgent );

        daoUtil.executeQuery( );

        int nIndex = 1;
        TicketingEmailAgentMessage emailAgentDemand = null;

        if ( daoUtil.next( ) )
        {
            emailAgentDemand = new TicketingEmailAgentMessage( );
            emailAgentDemand.setIdMessageAgent( daoUtil.getInt( nIndex++ ) );
            emailAgentDemand.setIdTicket( daoUtil.getInt( nIndex++ ) );
            emailAgentDemand.setEmailAgent( daoUtil.getString( nIndex++ ) );
            emailAgentDemand.setMessageQuestion( daoUtil.getString( nIndex++ ) );
            emailAgentDemand.setMessageResponse( daoUtil.getString( nIndex++ ) );
            emailAgentDemand.setIsAnswered( daoUtil.getBoolean( nIndex++ ) );
        }

        daoUtil.free( );

        return emailAgentDemand;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TicketingEmailAgentMessage loadFirstByIdTicket( int nIdTicket )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIRST_MESSAGE, PluginService.getPlugin( WorkflowTicketingFacilFamillesPlugin.PLUGIN_NAME ) );

        daoUtil.setInt( 1, nIdTicket );

        daoUtil.executeQuery( );

        int nIndex = 1;
        TicketingEmailAgentMessage emailAgentDemand = null;

        if ( daoUtil.next( ) )
        {
            emailAgentDemand = new TicketingEmailAgentMessage( );
            emailAgentDemand.setIdMessageAgent( daoUtil.getInt( nIndex++ ) );
            emailAgentDemand.setIdTicket( daoUtil.getInt( nIndex++ ) );
            emailAgentDemand.setEmailAgent( daoUtil.getString( nIndex++ ) );
            emailAgentDemand.setMessageQuestion( daoUtil.getString( nIndex++ ) );
            emailAgentDemand.setMessageResponse( daoUtil.getString( nIndex++ ) );
            emailAgentDemand.setIsAnswered( daoUtil.getBoolean( nIndex++ ) );
        }

        daoUtil.free( );

        return emailAgentDemand;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TicketingEmailAgentMessage> loadByIdTicketNotClosed( int nIdTicket )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_ID_TICKET_NOT_CLOSED, PluginService.getPlugin( WorkflowTicketingFacilFamillesPlugin.PLUGIN_NAME ) );

        daoUtil.setInt( 1, nIdTicket );

        daoUtil.executeQuery( );

        int nIndex = 1;
        List<TicketingEmailAgentMessage> listEmailAgentDemand = new ArrayList<TicketingEmailAgentMessage>( );
        TicketingEmailAgentMessage emailAgentDemand = null;

        while ( daoUtil.next( ) )
        {
            nIndex = 1;
            emailAgentDemand = new TicketingEmailAgentMessage( );
            emailAgentDemand.setIdMessageAgent( daoUtil.getInt( nIndex++ ) );
            emailAgentDemand.setIdTicket( daoUtil.getInt( nIndex++ ) );
            emailAgentDemand.setEmailAgent( daoUtil.getString( nIndex++ ) );
            emailAgentDemand.setMessageQuestion( daoUtil.getString( nIndex++ ) );
            emailAgentDemand.setMessageResponse( daoUtil.getString( nIndex++ ) );
            emailAgentDemand.setIsAnswered( daoUtil.getBoolean( nIndex++ ) );
            listEmailAgentDemand.add( emailAgentDemand );
        }

        daoUtil.free( );

        return listEmailAgentDemand;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( "workflow.transactionManager" )
    public void deleteByIdMessageAgent( int nIdMessageAgent )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, PluginService.getPlugin( WorkflowTicketingFacilFamillesPlugin.PLUGIN_NAME ) );

        daoUtil.setInt( 1, nIdMessageAgent );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }
}
