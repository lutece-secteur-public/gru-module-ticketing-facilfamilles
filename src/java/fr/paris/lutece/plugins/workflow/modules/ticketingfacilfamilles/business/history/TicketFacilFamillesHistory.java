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


/**
 *
 */
public class TicketFacilFamillesHistory
{
    /** The _n id task. */
    private int _nIdTask;

    /** The _n id resource history. */
    private int _nIdResourceHistory;

    /** the email of the user agent*/
    private String _strEmailAgent;

    /** the message to the agent*/
    private String _strMessage;

    /**
     * @return the _nIdTask
     */
    public int getIdTask(  )
    {
        return _nIdTask;
    }

    /**
     * @param nIdTask the nIdTask to set
     */
    public void setIdTask( int nIdTask )
    {
        this._nIdTask = nIdTask;
    }

    /**
     * @return the _nIdResourceHistory
     */
    public int getIdResourceHistory(  )
    {
        return _nIdResourceHistory;
    }

    /**
     * @param nIdResourceHistory the nIdResourceHistory to set
     */
    public void setIdResourceHistory( int nIdResourceHistory )
    {
        this._nIdResourceHistory = nIdResourceHistory;
    }

    /**
     * @return the _strEmailAgent
     */
    public String getEmailAgent(  )
    {
        return _strEmailAgent;
    }

    /**
     * @param strEmailAgent the strEmailAgent to set
     */
    public void setEmailAgent( String strEmailAgent )
    {
        this._strEmailAgent = strEmailAgent;
    }

    /**
     * @return the _strMessage
     */
    public String getMessage(  )
    {
        return _strMessage;
    }

    /**
     * @param strMessage the strMessage to set
     */
    public void setMessage( String strMessage )
    {
        this._strMessage = strMessage;
    }
}
