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
package fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.fieldagent;

import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.service.WorkflowTicketingFacilFamillesPlugin;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.sql.DAOUtil;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


/**
 *
 */
public class FieldAgentUserDAO implements IFieldAgentUserDAO
{
    //PROPERTIES
    private static final String DSKEY_ENTITEATTRIBUT_ID = "module.workflow.ticketingfacilfamilles.site_property.fieldagent.entiteattribut";
    private static final String PROP_SEARCH_LIMIT = "module.workflow.ticketingfacilfamilles.fieldagent.search.limit";

    // constants
    private static final String CONSTANT_PERCENT = "%";

    //SQL
    private static final String SQL_SELECT_USER_ADMIN = "SELECT u.last_name, u.first_name, u.email, f.user_field_value FROM core_admin_user u INNER JOIN core_user_right r ON u.id_user = r.id_user LEFT JOIN core_admin_user_field f ON u.id_user = f.id_user AND f.id_attribute = ? WHERE r.id_right = 'TICKETING_ACTEUR_TERRAIN' ";
    private static final String SQL_VALID_EMAIL_USER_ADMIN = "SELECT u.first_name, u.email FROM core_admin_user u INNER JOIN core_user_right r ON u.id_user = r.id_user LEFT JOIN core_admin_user_field f ON u.id_user = f.id_user AND f.id_attribute = ? WHERE r.id_right = 'TICKETING_ACTEUR_TERRAIN' AND u.email = ?";
    private static final String SQL_WHERE_LASTNAME_CLAUSE = " u.last_name LIKE ? ";
    private static final String SQL_WHERE_EMAIL_CLAUSE = " u.email LIKE ? ";
    private static final String SQL_WHERE_ENTITY_CLAUSE = " f.user_field_value LIKE ? ";
    private static final String SQL_SEPARATOR_AND = " AND ";

    /**
     * @return the value store in properties or empty string
     */
    private String getEntiteAttributID(  )
    {
        return DatastoreService.getDataValue( DSKEY_ENTITEATTRIBUT_ID, StringUtils.EMPTY );
    }

    /**
         * {@inheritDoc}
         */
    @Override
    public int getSearchLimit(  )
    {
        String strLimit = AppPropertiesService.getProperty( PROP_SEARCH_LIMIT );
        int nLimit = 0;

        if ( !StringUtils.isEmpty( strLimit ) && StringUtils.isNumeric( strLimit ) )
        {
            nLimit = Integer.parseInt( strLimit );
        }

        return Math.max( 0, nLimit );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValidEmail( String strEmail )
    {
    	StringBuilder strQuery = new StringBuilder( SQL_VALID_EMAIL_USER_ADMIN );

        DAOUtil daoUtil = new DAOUtil( strQuery.toString(  ),
                PluginService.getPlugin( WorkflowTicketingFacilFamillesPlugin.PLUGIN_NAME ) );

        int nIndex = 1;
        daoUtil.setString( nIndex++, getEntiteAttributID(  ) );
        daoUtil.setString( nIndex++, strEmail );
        daoUtil.executeQuery(  );
        
        boolean bEmailOk = daoUtil.next(  );
                
        daoUtil.free(  );

        return bEmailOk;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FieldAgentUser> findFieldAgentUser( String strLastname, String strEmail, String strEntity )
    {
        StringBuilder strQuery = new StringBuilder( SQL_SELECT_USER_ADMIN );

        if ( StringUtils.isNotEmpty( strLastname ) )
        {
            strQuery.append( SQL_SEPARATOR_AND );
            strQuery.append( SQL_WHERE_LASTNAME_CLAUSE );
        }

        if ( StringUtils.isNotEmpty( strEmail ) )
        {
            strQuery.append( SQL_SEPARATOR_AND );
            strQuery.append( SQL_WHERE_EMAIL_CLAUSE );
        }

        if ( StringUtils.isNotEmpty( strEntity ) )
        {
            strQuery.append( SQL_SEPARATOR_AND );
            strQuery.append( SQL_WHERE_ENTITY_CLAUSE );
        }

        DAOUtil daoUtil = new DAOUtil( strQuery.toString(  ),
                PluginService.getPlugin( WorkflowTicketingFacilFamillesPlugin.PLUGIN_NAME ) );

        int nIndex = 1;
        daoUtil.setString( nIndex++, getEntiteAttributID(  ) );

        if ( StringUtils.isNotEmpty( strLastname ) )
        {
            daoUtil.setString( nIndex++, CONSTANT_PERCENT + strLastname + CONSTANT_PERCENT );
        }

        if ( StringUtils.isNotEmpty( strEmail ) )
        {
            daoUtil.setString( nIndex++, CONSTANT_PERCENT + strEmail + CONSTANT_PERCENT );
        }

        if ( StringUtils.isNotEmpty( strEntity ) )
        {
            daoUtil.setString( nIndex++, CONSTANT_PERCENT + strEntity + CONSTANT_PERCENT );
        }

        daoUtil.executeQuery(  );

        Set<FieldAgentUser> lstFieldAgent = new TreeSet<FieldAgentUser>( new FieldAgentUserComparator(  ) );

        while ( daoUtil.next(  ) )
        {
            FieldAgentUser fieldAgent = new FieldAgentUser(  );
            fieldAgent.setLastname( daoUtil.getString( 1 ) );
            fieldAgent.setFirstname( daoUtil.getString( 2 ) );
            fieldAgent.setEmail( daoUtil.getString( 3 ) );
            fieldAgent.setEntite( daoUtil.getString( 4 ) );
            lstFieldAgent.add( fieldAgent );
        }
                
        daoUtil.free(  );

        return ( new ArrayList<FieldAgentUser>( lstFieldAgent ) );
    }
}
