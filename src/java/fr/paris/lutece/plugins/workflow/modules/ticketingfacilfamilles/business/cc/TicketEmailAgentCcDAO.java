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
package fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.cc;

import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.service.WorkflowTicketingFacilFamillesPlugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.util.sql.DAOUtil;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class TicketEmailAgentCcDAO implements ITicketEmailAgentCcDAO
{
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_cc ) FROM workflow_task_ticketing_facilfamilles_emailagent_cc";
    private static final String SQL_QUERY_FIND_BY_ID_HISTORY = " SELECT  id_cc, id_task, id_history, email FROM workflow_task_ticketing_facilfamilles_emailagent_cc "
            + " WHERE id_history = ? AND id_task = ? ORDER BY id_cc ASC";
    private static final String SQL_QUERY_INSERT = " INSERT INTO workflow_task_ticketing_facilfamilles_emailagent_cc ( id_cc, id_task, id_history, email ) "
            + " VALUES ( ?,?,?,? ) ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM workflow_task_ticketing_facilfamilles_emailagent_cc WHERE id_cc = ?";

    /**
     * Generates a new primary key
     *
     * @param plugin
     *            The Plugin
     * @return The new primary key
     */
    public int newPrimaryKey( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, PluginService.getPlugin( WorkflowTicketingFacilFamillesPlugin.PLUGIN_NAME ) );
        daoUtil.executeQuery( );

        int nKey = 1;

        if ( daoUtil.next( ) )
        {
            nKey = daoUtil.getInt( 1 ) + 1;
        }

        daoUtil.free( );

        return nKey;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( "workflow.transactionManager" )
    public synchronized void insert( TicketEmailAgentCc infosEmailAgent )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, PluginService.getPlugin( WorkflowTicketingFacilFamillesPlugin.PLUGIN_NAME ) );
        infosEmailAgent.setIdInfosHistory( newPrimaryKey( ) );
        
        int nIndex = 1;
        
        daoUtil.setInt( nIndex++, infosEmailAgent.getIdInfosHistory( ) );
        daoUtil.setInt( nIndex++, infosEmailAgent.getIdTask( ) );
        daoUtil.setInt( nIndex++, infosEmailAgent.getIdResourceHistory( ) );
        daoUtil.setString( nIndex++, infosEmailAgent.getEmail( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TicketEmailAgentCc> loadByIdHistory( int nIdHistory, int nIdTask )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_ID_HISTORY, PluginService.getPlugin( WorkflowTicketingFacilFamillesPlugin.PLUGIN_NAME ) );

        daoUtil.setInt( 1, nIdHistory );
        daoUtil.setInt( 2, nIdTask );

        daoUtil.executeQuery( );

        int nIndex = 1;
        TicketEmailAgentCc infosEmailAgent;
        List<TicketEmailAgentCc> listInfosEmailAgent = new ArrayList<TicketEmailAgentCc>();

        while ( daoUtil.next( ) )
        {
        	nIndex = 1;
        	infosEmailAgent = new TicketEmailAgentCc( );
        	infosEmailAgent.setIdInfosHistory( daoUtil.getInt( nIndex++ ) );
        	infosEmailAgent.setIdTask( daoUtil.getInt( nIndex++ ) );
        	infosEmailAgent.setIdResourceHistory( daoUtil.getInt( nIndex++ ) );
        	infosEmailAgent.setEmail( daoUtil.getString( nIndex++ ) );
            
            listInfosEmailAgent.add(infosEmailAgent);
        }

        daoUtil.free( );

        return listInfosEmailAgent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( "workflow.transactionManager" )
    public void deleteByIdCc( int nIdCc )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, PluginService.getPlugin( WorkflowTicketingFacilFamillesPlugin.PLUGIN_NAME ) );

        daoUtil.setInt( 1, nIdCc );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }
}
