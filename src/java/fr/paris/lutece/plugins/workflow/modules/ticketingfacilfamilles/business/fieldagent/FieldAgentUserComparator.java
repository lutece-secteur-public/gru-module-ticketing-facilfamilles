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

import org.apache.commons.lang.StringUtils;

import java.util.Comparator;


/**
 *
 */
public class FieldAgentUserComparator implements Comparator<FieldAgentUser>
{
    /**
     * {@inheritDoc}
     */
    @Override
    public int compare( FieldAgentUser o1, FieldAgentUser o2 )
    {
        int nCompare = 0;
        nCompare = compareAlphaNumeric( o1.getEntite(  ), o2.getEntite(  ), nCompare );
        nCompare = compareAlphaNumeric( o1.getLastname(  ), o2.getLastname(  ), nCompare );
        nCompare = compareAlphaNumeric( o1.getFirstname(  ), o2.getFirstname(  ), nCompare );
        nCompare = compareAlphaNumeric( o1.getEmail(  ), o2.getEmail(  ), nCompare );

        return nCompare;
    }

    /**
     * compare with natural order two string if nCurrentCompare is equal to 0
     * null/empty string have to be at 'the bottom' of the list
     * @param str1 , first string to compare
     * @param str2 , second string to compare
     * @param nCurrentCompare , current compare value
     * @return comparison value
     */
    private int compareAlphaNumeric( String str1, String str2, int nCurrentCompare )
    {
        if ( nCurrentCompare != 0 )
        {
            return nCurrentCompare;
        }

        int nCompare = 0;

        if ( StringUtils.isEmpty( str1 ) )
        {
            if ( StringUtils.isNotEmpty( str2 ) )
            {
                nCompare = 1;
            }
        }
        else
        {
            if ( StringUtils.isEmpty( str2 ) )
            {
                nCompare = -1;
            }
            else
            {
                int nIdxNum1 = StringUtils.indexOfAny( str1, "0123465789" );

                if ( nIdxNum1 < 0 )
                {
                    nIdxNum1 = str1.length(  );
                }

                int nIdxNum2 = StringUtils.indexOfAny( str2, "0123465789" );

                if ( nIdxNum2 < 0 )
                {
                    nIdxNum2 = str2.length(  );
                }

                int nIdxSub = Math.min( nIdxNum1, nIdxNum2 );

                String strPost1 = str1.substring( nIdxSub );
                String strPost2 = str2.substring( nIdxSub );
                String strPre1 = str1.substring( 0, nIdxSub );
                String strPre2 = str2.substring( 0, nIdxSub );
                nCompare = strPre1.compareToIgnoreCase( strPre2 );

                if ( nCompare == 0 )
                {
                    strPost1 = strPost1.replaceAll( "[^0-9]", "0" );
                    strPost2 = strPost2.replaceAll( "[^0-9]", "0" );
                    nCompare = strPost1.compareTo( strPost2 );
                }
            }
        }

        return nCompare;
    }
}
