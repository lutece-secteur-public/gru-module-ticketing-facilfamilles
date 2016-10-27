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
 * TicketingEmailAgentDemand DAO interface
 */
public interface ITicketingEmailAgentMessageDAO
{
    /** The Constant BEAN_SERVICE. */
    String BEAN_SERVICE = "workflow-ticketingfacilfamilles.ticketingEmailAgentMessageDAO";

    /**
     * Create Question.
     *
     * @param emailAgentMessage the TicketingEmailAgentMessage
     */
    void createQuestion( TicketingEmailAgentMessage emailAgentMessage );

    /**
     * Test if the id given is the last question of a ticket
     * @param nIdTicket ticket ID
     * @param nIdMessageAgent message ID
     * @return true if the nIdMessageAgent is the last question of nIdTicket
     */
    boolean isLastQuestion( int nIdTicket, int nIdMessageAgent );
    /**
     * add Answer.
     *
     * @param nIdTicket the id of the ticket
     * @param strReponse the response to the question
     * @return the id of the TicketingEmailAgentMessage
     */
    int addAnswer( int nIdTicket, String strReponse );

    /**
     * Load.
     *
     * @param nIdMessageAgent the id of message
     * @return the TicketingEmailAgentMessage element
     */
    TicketingEmailAgentMessage loadByIdMessageAgent( int nIdMessageAgent );

    /**
     * Delete by idMessageAgent.
     *
     * @param nIdMessageAgent the n id of message
     */
    void deleteByIdMessageAgent( int nIdMessageAgent );
}
