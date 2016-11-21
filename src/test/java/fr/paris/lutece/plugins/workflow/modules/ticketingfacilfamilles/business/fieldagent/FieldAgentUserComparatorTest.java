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

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;


/**
 * test FieldAgentUserComparator
 */
public class FieldAgentUserComparatorTest extends TestCase
{
    /**
     * one to one test
     */
    @Test
    public void testOneToOneComparator(  )
    {
        FieldAgentUserComparator comp = new FieldAgentUserComparator(  );
        FieldAgentUser user1 = createFieldAgentUser( "3z", "z", "z", "z.z@z.com" );
        FieldAgentUser user2 = createFieldAgentUser( "ppp", "z", "z", "z.z@z.com" );
        FieldAgentUser user3 = createFieldAgentUser( "z0z", "z", "z", "z.z@z.com" );
        FieldAgentUser user4 = createFieldAgentUser( "z01", "z", "z", "z.z@z.com" );
        FieldAgentUser user5 = createFieldAgentUser( "z01", "a", "z", "z.z@z.com" );
        FieldAgentUser user6 = createFieldAgentUser( "z01", "z", "a", "z.z@z.com" );
        FieldAgentUser user7 = createFieldAgentUser( "z01", "z", "z", "a.z@z.com" );
        FieldAgentUser user8 = createFieldAgentUser( "a0z", "z", "z", "z.z@z.com" );
        Assert.assertTrue( "erreur comparaison " + fieldAgentUserToString( user1 ) + " / " +
            fieldAgentUserToString( user2 ) + " / " + comp.compare( user1, user2 ) + ">0",
            comp.compare( user1, user2 ) > 0 );
        Assert.assertTrue( "erreur comparaison " + fieldAgentUserToString( user1 ) + " / " +
            fieldAgentUserToString( user3 ) + " / " + comp.compare( user1, user3 ) + ">0",
            comp.compare( user1, user3 ) > 0 );
        Assert.assertTrue( "erreur comparaison " + fieldAgentUserToString( user4 ) + " / " +
            fieldAgentUserToString( user3 ) + " / " + comp.compare( user4, user3 ) + ">0",
            comp.compare( user4, user3 ) > 0 );
        Assert.assertTrue( "erreur comparaison " + fieldAgentUserToString( user4 ) + " / " +
            fieldAgentUserToString( user5 ) + " / " + comp.compare( user4, user5 ) + ">0",
            comp.compare( user4, user5 ) > 0 );
        Assert.assertTrue( "erreur comparaison " + fieldAgentUserToString( user4 ) + " / " +
            fieldAgentUserToString( user6 ) + " / " + comp.compare( user4, user6 ) + ">0",
            comp.compare( user4, user6 ) > 0 );
        Assert.assertTrue( "erreur comparaison " + fieldAgentUserToString( user4 ) + " / " +
            fieldAgentUserToString( user7 ) + " / " + comp.compare( user4, user7 ) + ">0",
            comp.compare( user4, user7 ) > 0 );
        Assert.assertTrue( "erreur comparaison " + fieldAgentUserToString( user3 ) + " / " +
            fieldAgentUserToString( user8 ) + " / " + comp.compare( user3, user8 ) + ">0",
            comp.compare( user3, user8 ) > 0 );
    }

    /**
     * create FieldAgentUser
     * @param strEntite , entite
     * @param strLastname , lastName
     * @param strFirstname , firstName
     * @param strEmail , email
     * @return created FieldAgentUser
     */
    private FieldAgentUser createFieldAgentUser( String strEntite, String strLastname, String strFirstname,
        String strEmail )
    {
        FieldAgentUser agentUser = new FieldAgentUser(  );
        agentUser.setEntite( strEntite );
        agentUser.setLastname( strLastname );
        agentUser.setFirstname( strFirstname );
        agentUser.setEmail( strEmail );

        return agentUser;
    }

    /**
     *
     * @param agentUser FieldAgentUser
     * @return agentUser to String
     */
    private String fieldAgentUserToString( FieldAgentUser agentUser )
    {
        StringBuilder strLog = new StringBuilder(  );
        strLog.append( agentUser.getEntite(  ) );
        strLog.append( ";" );
        strLog.append( agentUser.getLastname(  ) );
        strLog.append( ";" );
        strLog.append( agentUser.getFirstname(  ) );
        strLog.append( ";" );
        strLog.append( agentUser.getEmail(  ) );

        return strLog.toString(  );
    }
}
