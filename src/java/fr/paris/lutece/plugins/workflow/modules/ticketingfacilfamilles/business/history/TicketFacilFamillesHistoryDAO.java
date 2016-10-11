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
package fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.history;

import org.springframework.transaction.annotation.Transactional;

import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.service.WorkflowTicketingFacilFamillesPlugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 *
 */
public class TicketFacilFamillesHistoryDAO implements ITicketFacilFamillesHistoryDAO
{
	private static final String SQL_QUERY_FIND_BY_ID_HISTORY = " SELECT id_task, id_history, email_agent, message FROM workflow_task_ticketing_facilfamilles_history " +
	        " WHERE id_history = ? ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO workflow_task_ticketing_facilfamilles_history ( id_task, id_history, email_agent, message ) " +
        " VALUES ( ?,?,?,? ) ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM workflow_task_ticketing_facilfamilles_history WHERE id_task = ? and id_history = ? ";

	/**
	 * {@inheritDoc}
	 */
    @Override
    @Transactional( "workflow.transactionManager" )
    public void insert( TicketFacilFamillesHistory emailAgent )
    {
    	DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, PluginService.getPlugin( WorkflowTicketingFacilFamillesPlugin.PLUGIN_NAME ) );

    	int nIndex = 1;

        daoUtil.setInt( nIndex++, emailAgent.getIdTask(  ) );
        daoUtil.setInt( nIndex++, emailAgent.getIdResourceHistory(  ) );
        daoUtil.setString( nIndex++, emailAgent.getEmailAgent(  ) );
        daoUtil.setString( nIndex++, emailAgent.getMessage(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    public TicketFacilFamillesHistory loadByIdHistory( int nIdHistory )
    {
    	DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_ID_HISTORY, PluginService.getPlugin( WorkflowTicketingFacilFamillesPlugin.PLUGIN_NAME ) );

    	daoUtil.setInt( 1, nIdHistory );

        daoUtil.executeQuery(  );

        int nIndex = 1;
        TicketFacilFamillesHistory emailAgent = null;

        if ( daoUtil.next(  ) )
        {
        	emailAgent = new TicketFacilFamillesHistory(  );
        	emailAgent.setIdTask( daoUtil.getInt( nIndex++ ) );
        	emailAgent.setIdResourceHistory( daoUtil.getInt( nIndex++ ) );
        	emailAgent.setEmailAgent( daoUtil.getString( nIndex++ ) );
        	emailAgent.setMessage( daoUtil.getString( nIndex++ ) );
        }

        daoUtil.free(  );
        
        return emailAgent;
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    @Transactional( "workflow.transactionManager" )
    public void deleteByHistory( int nIdHistory, int nIdTask )
    {
    	DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, PluginService.getPlugin( WorkflowTicketingFacilFamillesPlugin.PLUGIN_NAME ) );
    	
    	daoUtil.setInt( 1, nIdTask );
    	daoUtil.setInt( 2, nIdHistory );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }
	
}
