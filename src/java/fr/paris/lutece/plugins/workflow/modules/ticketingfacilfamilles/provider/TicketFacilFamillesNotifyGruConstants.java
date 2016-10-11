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
package fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.provider;

/**
 *
 */
public final class TicketFacilFamillesNotifyGruConstants
{
	// DEFAULT PROVIDER IMPL
	public static final String STR_NO_CUSTOMER_ID = "0";
	public static final int EMPTY_OPTIONAL_DEMAND_ID_TYPE = 0;
	// GENERIC GRU
	public static final String MARK_GUID = "identification_guid";
    public static final String MARK_FIRSTNAME = "firstname";
    public static final String MARK_LASTNAME = "lastname";
    public static final String MARK_FIXED_PHONE = "fixed_phone";
    public static final String MARK_MOBILE_PHONE = "mobile_phone";
    public static final String MARK_REFERENCE = "reference";
    public static final String MARK_EMAIL = "email";
    public static final String MARK_UNIT_NAME = "unit_name";
    public static final String MARK_USER_TITLES = "civility";
    public static final String MARK_TICKET_TYPES = "ticket_type";
    public static final String MARK_TICKET_DOMAINS = "ticket_domain";
    public static final String MARK_TICKET_CATEGORIES = "ticket_category";
    public static final String MARK_CONTACT_MODES = "contact_mode";
    public static final String MARK_COMMENT = "comment";
    public static final String MARK_URL_COMPLETED = "url_completed";
    public static final String MARK_USER_MESSAGE = "user_message";
    public static final String MARK_CHANNEL = "ticket_channel";
    public static final String MARK_LIST_FORM = "list_form";
    public static final String MARK_TICKET = "ticket";
    public static final String MARK_LIST_MARKER = "list_markers";
    //SPECIFIC EMAIL AGENT
    public static final String MARK_EMAILAGENT_MESSAGE = "message";
    public static final String MARK_EMAILAGENT_LINK = "ticketing_ticket_link";
    
    /**
     * Private constructor
     */
    private TicketFacilFamillesNotifyGruConstants(  )
    {
    }
}
