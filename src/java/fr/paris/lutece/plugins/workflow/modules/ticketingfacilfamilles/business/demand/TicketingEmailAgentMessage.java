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

/**
 *
 */
public class TicketingEmailAgentMessage
{
    /** The id message. */
    private int _nIdMessageAgent;
    
    /** The id demand. */
    private int _nIdTicket;

    /** the email of the user agent*/
    private String _strEmailAgent;

    /** the message to the agent*/
    private String _strMessageQuestion;

    /** the response to the agent*/
    private String _strMessageResponse;
    
    /** true if the field agent has answered */
    private boolean _bIsAnswered;

    /**
     * @return the idMessageAgent
     */
    public int getIdMessageAgent(  )
    {
        return _nIdMessageAgent;
    }

    /**
     * @param nIdMessageAgent the idMessageAgent to set
     */
    public void setIdMessageAgent( int nIdMessageAgent )
    {
        this._nIdMessageAgent = nIdMessageAgent;
    }    

    /**
	 * @return the IdTicket
	 */
	public int getIdTicket()
	{
		return _nIdTicket;
	}

	/**
	 * @param nIdTicket the IdTicket to set
	 */
	public void setIdTicket( int nIdTicket )
	{
		this._nIdTicket = nIdTicket;
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
	 * @return the strMessageQuestion
	 */
	public String getMessageQuestion()
	{
		return _strMessageQuestion;
	}

	/**
	 * @param strMessageQuestion the strMessageQuestion to set
	 */
	public void setMessageQuestion( String strMessageQuestion )
	{
		this._strMessageQuestion = strMessageQuestion;
	}

	/**
	 * @return the strMessageResponse
	 */
	public String getMessageResponse()
	{
		return _strMessageResponse;
	}

	/**
	 * @param strMessageResponse the strMessageResponse to set
	 */
	public void setMessageResponse( String strMessageResponse )
	{
		this._strMessageResponse = strMessageResponse;
	}

	/**
	 * @return the bIsAnswered
	 */
	public boolean getIsAnswered()
	{
		return _bIsAnswered;
	}

	/**
	 * @param bIsAnswered the bIsAnswered to set
	 */
	public void setIsAnswered( boolean bIsAnswered )
	{
		this._bIsAnswered = bIsAnswered;
	}
    
    
}
