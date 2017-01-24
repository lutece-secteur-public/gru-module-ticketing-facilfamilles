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
package fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.config;

import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.service.WorkflowTicketingFacilFamillesPlugin;
import fr.paris.lutece.plugins.workflowcore.business.config.ITaskConfigDAO;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.util.sql.DAOUtil;

import org.apache.commons.lang.StringUtils;

/**
 *
 */
public class TaskTicketEmailAgentConfigDAO implements ITaskConfigDAO<TaskTicketEmailAgentConfig>
{
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = " SELECT id_task, message_direction, id_following_action FROM workflow_task_ticketing_facilfamilles_emailagent_config WHERE id_task = ? ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO workflow_task_ticketing_facilfamilles_emailagent_config ( id_task, message_direction, id_following_action ) VALUES ( ?,?,? ) ";
    private static final String SQL_QUERY_UPDATE = "UPDATE workflow_task_ticketing_facilfamilles_emailagent_config SET message_direction = ?, id_following_action = ? WHERE id_task = ? ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM workflow_task_ticketing_facilfamilles_emailagent_config WHERE id_task = ? ";

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void insert( TaskTicketEmailAgentConfig config )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, PluginService.getPlugin( WorkflowTicketingFacilFamillesPlugin.PLUGIN_NAME ) );

        int nIndex = 1;

        daoUtil.setInt( nIndex++, config.getIdTask( ) );
        daoUtil.setInt( nIndex++, config.getMessageDirection( ).ordinal( ) );

        if ( config.getIdFollowingAction( ) == null )
        {
            daoUtil.setIntNull( nIndex++ );
        }
        else
        {
            daoUtil.setInt( nIndex++, config.getIdFollowingAction( ) );
        }

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( TaskTicketEmailAgentConfig config )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, PluginService.getPlugin( WorkflowTicketingFacilFamillesPlugin.PLUGIN_NAME ) );

        int nIndex = 1;

        daoUtil.setInt( nIndex++, config.getMessageDirection( ).ordinal( ) );

        if ( config.getIdFollowingAction( ) == null )
        {
            daoUtil.setIntNull( nIndex++ );
        }
        else
        {
            daoUtil.setInt( nIndex++, config.getIdFollowingAction( ) );
        }

        daoUtil.setInt( nIndex++, config.getIdTask( ) );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskTicketEmailAgentConfig load( int nIdTask )
    {
        TaskTicketEmailAgentConfig config = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, PluginService.getPlugin( WorkflowTicketingFacilFamillesPlugin.PLUGIN_NAME ) );

        daoUtil.setInt( 1, nIdTask );

        daoUtil.executeQuery( );

        int nIndex = 1;

        if ( daoUtil.next( ) )
        {
            config = new TaskTicketEmailAgentConfig( );
            config.setIdTask( daoUtil.getInt( nIndex++ ) );
            config.setMessageDirection( MessageDirection.valueOf( daoUtil.getInt( nIndex++ ) ) );

            String strIdFollowingAction = daoUtil.getString( nIndex++ );

            if ( StringUtils.isNotEmpty( strIdFollowingAction ) )
            {
                config.setIdFollowingAction( Integer.parseInt( strIdFollowingAction ) );
            }
        }

        daoUtil.free( );

        return config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( int nIdTask )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, PluginService.getPlugin( WorkflowTicketingFacilFamillesPlugin.PLUGIN_NAME ) );

        daoUtil.setInt( 1, nIdTask );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }
}
