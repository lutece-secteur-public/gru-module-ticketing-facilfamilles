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
package fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.service.search;

import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.fieldagent.FieldAgentUser;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.business.fieldagent.IFieldAgentUserDAO;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AdminAuthenticationService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.utils.MVCMessage;
import fr.paris.lutece.util.ErrorMessage;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 */
public class FieldAgentSearchServlet extends HttpServlet
{
    // Other constants
    public static final String URL_SERVLET = "servlet/plugins/workflow/ticketingfacilfamilles/fieldagent";

    /**
     * Generated serial Id
     */
    private static final long serialVersionUID = -1109810381598265699L;

    // Template
    private static final String TEMPLATE_SEARCH_RESULT = "admin/plugins/workflow/modules/ticketingfacilfamilles/search_field_agent_result.html";

    // Properties
    private static final String PROPERTY_ENCODING = "lutece.encoding";

    // Model
    private static final String MARK_ERRORS = "errors";
    private static final String MARK_INFOS = "infos";
    private static final String MARK_LIST_USERS = "result_user";
    private static final String MARK_INPUT_EMAIL = "input_email";
    private static final String MARK_ID_TASK = "id_task";

    // Request parameter
    private static final String PARAM_INPUT_EMAIL = "input_email";
    private static final String PARAM_ID_TASK = "id_task";
    private static final String PARAM_LASTNAME = "lastname";
    private static final String PARAM_ENTITY = "entity";

    // message
    private static final String LOG_UNAUTHENTICATED_USER = "Calling FieldAgentSearchServlet with unauthenticated user";
    private static final String KEY_ERROR_NO_RESULT = "module.workflow.ticketingfacilfamilles.task_ticket_emailagent.error.search.no_result";
    private static final String KEY_INFOS_LIMIT_RESULT = "module.workflow.ticketingfacilfamilles.task_ticket_emailagent.info.search.limit_result";

    // BEAN
    private IFieldAgentUserDAO _fieldAgentUserDAO = SpringContextService.getBean( IFieldAgentUserDAO.BEAN_SERVICE );

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     * @param request servlet request
     * @param httpResponse servlet response
     * @throws ServletException the servlet Exception
     * @throws IOException the io exception
     */
    protected void processRequest( HttpServletRequest request, HttpServletResponse httpResponse )
        throws ServletException, IOException
    {
        AdminUser user = AdminAuthenticationService.getInstance(  ).getRegisteredUser( request );

        if ( user == null )
        {
            AppLogService.error( LOG_UNAUTHENTICATED_USER );
            throw new ServletException( LOG_UNAUTHENTICATED_USER );
        }

        //request param
        String strInputEmail = request.getParameter( PARAM_INPUT_EMAIL );
        String strIdTask = request.getParameter( PARAM_ID_TASK );
        String strLastname = request.getParameter( PARAM_LASTNAME );
        String strEntity = request.getParameter( PARAM_ENTITY );

        Locale locale = user.getLocale(  );
        int searchLimit = _fieldAgentUserDAO.getSearchLimit(  );
        Map<String, Object> model = new HashMap<String, Object>(  );
        List<ErrorMessage> listErrors = new ArrayList<ErrorMessage>(  );
        List<ErrorMessage> listInfos = new ArrayList<ErrorMessage>(  );

        //TMP        
        List<FieldAgentUser> listUsers = _fieldAgentUserDAO.findFieldAgentUser( strLastname, null, strEntity );

        //error no result
        if ( listUsers.isEmpty(  ) )
        {
            listErrors.add( new MVCMessage( I18nService.getLocalizedString( KEY_ERROR_NO_RESULT, locale ) ) );
        }

        if ( ( searchLimit > 0 ) && ( listUsers.size(  ) > searchLimit ) )
        {
            listInfos.add( new MVCMessage( I18nService.getLocalizedString( KEY_INFOS_LIMIT_RESULT,
                        new Object[] { searchLimit, listUsers.size(  ) }, locale ) ) );
            listUsers = listUsers.subList( 0, searchLimit );
        }

        model.put( MARK_ERRORS, listErrors );
        model.put( MARK_INFOS, listInfos );
        model.put( MARK_LIST_USERS, listUsers );
        model.put( MARK_INPUT_EMAIL, strInputEmail );
        model.put( MARK_ID_TASK, strIdTask );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_SEARCH_RESULT, locale, model );
        ServletOutputStream outStream = httpResponse.getOutputStream(  );
        outStream.write( template.getHtml(  ).getBytes( AppPropertiesService.getProperty( PROPERTY_ENCODING ) ) );
        outStream.flush(  );
        outStream.close(  );
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException the servlet Exception
     * @throws IOException the io exception
     */
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response )
        throws ServletException, IOException
    {
        processRequest( request, response );
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException the servlet Exception
     * @throws IOException the io exception
     */
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response )
        throws ServletException, IOException
    {
        processRequest( request, response );
    }

    /**
     * Returns a short description of the servlet.
     * @return message
     */
    @Override
    public String getServletInfo(  )
    {
        return "Servlet serving field agent search result";
    }
}
