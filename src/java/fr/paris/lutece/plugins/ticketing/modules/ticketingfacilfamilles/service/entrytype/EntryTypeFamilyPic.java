/*
 * Copyright (c) 2002-2015, Mairie de Paris
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
package fr.paris.lutece.plugins.ticketing.modules.ticketingfacilfamilles.service.entrytype;

import fr.paris.lutece.plugins.genericattributes.business.Entry;
import fr.paris.lutece.plugins.genericattributes.business.Field;
import fr.paris.lutece.plugins.genericattributes.business.FieldHome;
import fr.paris.lutece.plugins.genericattributes.business.GenericAttributeError;
import fr.paris.lutece.plugins.genericattributes.business.MandatoryError;
import fr.paris.lutece.plugins.genericattributes.business.Response;
import fr.paris.lutece.plugins.genericattributes.service.entrytype.EntryTypeService;
import fr.paris.lutece.plugins.genericattributes.util.GenericAttributesUtils;
import fr.paris.lutece.plugins.ticketing.service.entrytype.EntryTypeUtils;
import fr.paris.lutece.plugins.ticketing.web.TicketingConstants;
import fr.paris.lutece.plugins.workflow.modules.ticketingfacilfamilles.service.FamilyPicResourceIdService;
import fr.paris.lutece.portal.business.regularexpression.RegularExpression;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.rbac.RBACResource;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.regularexpression.RegularExpressionService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.string.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * class EntryTypeText
 */
public class EntryTypeFamilyPic extends EntryTypeService implements RBACResource
{
    // PARAMETERS
    protected static final String PARAMETER_BASE_URL = "base_url";

    // CONSTANTS
    protected static final String CONSTANT_BASE_URL = "base_url";
    private static final String CONSTANT_LINK_FAMILYPIC = "link_familypic";
    public static final String RESOURCE_TYPE = "FAMILY_PIC";
    private static final String RESOURCE_ID = "1";

    // Templates
    private static final String TEMPLATE_CREATE = "admin/plugins/ticketing/modules/facilfamilles/entries/create_entry_type_familypic.html";
    private static final String TEMPLATE_MODIFY = "admin/plugins/ticketing/modules/facilfamilles/entries/modify_entry_type_familypic.html";
    private static final String TEMPLATE_HTML_CODE = "skin/plugins/ticketing/modules/facilfamilles/entries/html_code_entry_type_familypic.html";
    private static final String TEMPLATE_HTML_CODE_ADMIN = "admin/plugins/ticketing/modules/facilfamilles/entries/html_code_entry_type_familypic.html";
    private static final String TEMPLATE_READ_ONLY_HTML_ADMIN = "admin/plugins/ticketing/modules/facilfamilles/entries/read_only_entry_type_familypic.html";
    private static final String TEMPLATE_LINK_FAMILYPIC = "admin/plugins/ticketing/modules/facilfamilles/entries/link_familypic.html";
    private static final String TEMPLATE_READ_ONLY_HTML = "skin/plugins/ticketing/modules/facilfamilles/entries/read_only_entry_type_familypic.html";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTemplateHtmlForm( Entry entry, boolean bDisplayFront )
    {
        return bDisplayFront ? TEMPLATE_HTML_CODE : TEMPLATE_HTML_CODE_ADMIN;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTemplateCreate( Entry entry, boolean bDisplayFront )
    {
        return TEMPLATE_CREATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTemplateModify( Entry entry, boolean bDisplayFront )
    {
        return TEMPLATE_MODIFY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResponseValueForRecap( Entry entry, HttpServletRequest request, Response response, Locale locale )
    {
        Map<String, Object> model = EntryTypeUtils.initModel( entry, response );

        for ( Field field : FieldHome.getFieldListByIdEntry( entry.getIdEntry( ) ) )
        {
            if ( CONSTANT_BASE_URL.equals( field.getTitle( ) ) )
            {
                model.put( CONSTANT_BASE_URL, field.getValue( ) );
            }
        }
        
        HtmlTemplate template = new HtmlTemplate( );
        Object bDisplayFront = request.getAttribute( TicketingConstants.ATTRIBUTE_IS_DISPLAY_FRONT );
        boolean bIsFront = ( bDisplayFront != null && (Boolean) bDisplayFront );
        
        AdminUser user = AdminUserService.getAdminUser( request );
        if ( !bIsFront && RBACService.isAuthorized( this, FamilyPicResourceIdService.PERMISSION_ACCESS, user ) )
        {
            template = AppTemplateService.getTemplate( TEMPLATE_LINK_FAMILYPIC, locale, model );
            model.put( CONSTANT_LINK_FAMILYPIC, template.getHtml( ) );
        }

        template = new HtmlTemplate( );

        if ( bIsFront )
        {
            template = AppTemplateService.getTemplate( TEMPLATE_READ_ONLY_HTML, locale, model );
        }
        else
        {
            template = AppTemplateService.getTemplate( TEMPLATE_READ_ONLY_HTML_ADMIN, locale, model );
        }

        return template.getHtml( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRequestData( Entry entry, HttpServletRequest request, Locale locale )
    {
        String strCode = request.getParameter( PARAMETER_ENTRY_CODE );
        String strTitle = request.getParameter( PARAMETER_TITLE );
        String strHelpMessage = ( request.getParameter( PARAMETER_HELP_MESSAGE ) != null ) ? request.getParameter( PARAMETER_HELP_MESSAGE ).trim( ) : null;
        String strComment = request.getParameter( PARAMETER_COMMENT );
        String strValue = request.getParameter( PARAMETER_VALUE );
        String strMandatory = request.getParameter( PARAMETER_MANDATORY );
        String strWidth = request.getParameter( PARAMETER_WIDTH );
        String strMaxSizeEnter = request.getParameter( PARAMETER_MAX_SIZE_ENTER );
        String strConfirmField = request.getParameter( PARAMETER_CONFIRM_FIELD );
        String strConfirmFieldTitle = request.getParameter( PARAMETER_CONFIRM_FIELD_TITLE );
        String strUnique = request.getParameter( PARAMETER_UNIQUE );
        String strCSSClass = request.getParameter( PARAMETER_CSS_CLASS );
        String strOnlyDisplayInBack = request.getParameter( PARAMETER_ONLY_DISPLAY_IN_BACK );
        String strErrorMessage = request.getParameter( PARAMETER_ERROR_MESSAGE );

        int nWidth = -1;
        int nMaxSizeEnter = -1;

        String strFieldError = StringUtils.EMPTY;

        if ( StringUtils.isBlank( strTitle ) )
        {
            strFieldError = FIELD_TITLE;
        }

        else
            if ( StringUtils.isBlank( strWidth ) )
            {
                strFieldError = FIELD_WIDTH;
            }

        if ( ( strConfirmField != null ) && StringUtils.isBlank( strConfirmFieldTitle ) )
        {
            strFieldError = FIELD_CONFIRM_FIELD_TITLE;
        }

        if ( StringUtils.isNotBlank( strFieldError ) )
        {
            Object [ ] tabRequiredFields = {
                I18nService.getLocalizedString( strFieldError, locale )
            };

            return AdminMessageService.getMessageUrl( request, MESSAGE_MANDATORY_FIELD, tabRequiredFields, AdminMessage.TYPE_STOP );
        }

        try
        {
            nWidth = Integer.parseInt( strWidth );
        }
        catch( NumberFormatException ne )
        {
            strFieldError = FIELD_WIDTH;
        }

        try
        {
            if ( StringUtils.isNotBlank( strMaxSizeEnter ) )
            {
                nMaxSizeEnter = Integer.parseInt( strMaxSizeEnter );
            }
        }
        catch( NumberFormatException ne )
        {
            strFieldError = FIELD_MAX_SIZE_ENTER;
        }

        if ( StringUtils.isNotBlank( strFieldError ) )
        {
            Object [ ] tabRequiredFields = {
                I18nService.getLocalizedString( strFieldError, locale )
            };

            return AdminMessageService.getMessageUrl( request, MESSAGE_NUMERIC_FIELD, tabRequiredFields, AdminMessage.TYPE_STOP );
        }

        entry.setTitle( strTitle );
        entry.setHelpMessage( strHelpMessage );
        entry.setComment( strComment );
        entry.setCSSClass( strCSSClass );
        entry.setErrorMessage( strErrorMessage );

        if ( entry.getFields( ) == null )
        {
            ArrayList<Field> listFields = new ArrayList<Field>( );
            Field field = new Field( );
            listFields.add( field );
            entry.setFields( listFields );
        }

        entry.setCode( strCode );
        entry.getFields( ).get( 0 ).setCode( strCode );
        entry.getFields( ).get( 0 ).setValue( strValue );
        entry.getFields( ).get( 0 ).setWidth( nWidth );
        entry.getFields( ).get( 0 ).setMaxSizeEnter( nMaxSizeEnter );

        entry.setMandatory( strMandatory != null );
        entry.setOnlyDisplayInBack( strOnlyDisplayInBack != null );

        if ( strConfirmField != null )
        {
            entry.setConfirmField( true );
            entry.setConfirmFieldTitle( strConfirmFieldTitle );
        }
        else
        {
            entry.setConfirmField( false );
            entry.setConfirmFieldTitle( null );
        }

        if ( strUnique != null )
        {
            entry.setUnique( true );
        }
        else
        {
            entry.setUnique( false );
        }

        setFields( entry, request );

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList getReferenceListRegularExpression( Entry entry, Plugin plugin )
    {
        ReferenceList refListRegularExpression = null;

        if ( RegularExpressionService.getInstance( ).isAvailable( ) )
        {
            refListRegularExpression = new ReferenceList( );

            List<RegularExpression> listRegularExpression = RegularExpressionService.getInstance( ).getAllRegularExpression( );

            for ( RegularExpression regularExpression : listRegularExpression )
            {
                if ( !entry.getFields( ).get( 0 ).getRegularExpressionList( ).contains( regularExpression ) )
                {
                    refListRegularExpression.addItem( regularExpression.getIdExpression( ), regularExpression.getTitle( ) );
                }
            }
        }

        return refListRegularExpression;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GenericAttributeError getResponseData( Entry entry, HttpServletRequest request, List<Response> listResponse, Locale locale )
    {
        String strValueEntry = request.getParameter( PREFIX_ATTRIBUTE + entry.getIdEntry( ) ).trim( );
        boolean bConfirmField = entry.isConfirmField( );
        String strValueEntryConfirmField = null;

        if ( bConfirmField )
        {
            strValueEntryConfirmField = request.getParameter( PREFIX_ATTRIBUTE + entry.getIdEntry( ) + SUFFIX_CONFIRM_FIELD ).trim( );
        }

        List<RegularExpression> listRegularExpression = entry.getFields( ).get( 0 ).getRegularExpressionList( );
        Response response = new Response( );
        response.setEntry( entry );

        if ( strValueEntry != null )
        {
            response.setResponseValue( strValueEntry );

            if ( StringUtils.isNotBlank( response.getResponseValue( ) ) )
            {
                response.setToStringValueResponse( getResponseValueForRecap( entry, request, response, locale ) );
            }
            else
            {
                response.setToStringValueResponse( StringUtils.EMPTY );
            }

            listResponse.add( response );

            // Checks if the entry value contains XSS characters
            if ( StringUtil.containsXssCharacters( strValueEntry ) )
            {
                GenericAttributeError error = new GenericAttributeError( );
                error.setMandatoryError( false );
                error.setTitleQuestion( entry.getTitle( ) );
                error.setErrorMessage( I18nService.getLocalizedString( MESSAGE_XSS_FIELD, request.getLocale( ) ) );

                return error;
            }

            if ( entry.isMandatory( ) )
            {
                if ( StringUtils.isBlank( strValueEntry ) )
                {
                    if ( StringUtils.isNotEmpty( entry.getErrorMessage( ) ) )
                    {
                        GenericAttributeError error = new GenericAttributeError( );
                        error.setMandatoryError( true );
                        error.setErrorMessage( entry.getErrorMessage( ) );

                        return error;
                    }

                    return new MandatoryError( entry, locale );
                }
            }

            if ( ( !strValueEntry.equals( StringUtils.EMPTY ) ) && ( listRegularExpression != null ) && ( listRegularExpression.size( ) != 0 )
                    && RegularExpressionService.getInstance( ).isAvailable( ) )
            {
                for ( RegularExpression regularExpression : listRegularExpression )
                {
                    if ( !RegularExpressionService.getInstance( ).isMatches( strValueEntry, regularExpression ) )
                    {
                        GenericAttributeError error = new GenericAttributeError( );
                        error.setMandatoryError( false );
                        error.setTitleQuestion( entry.getTitle( ) );
                        error.setErrorMessage( regularExpression.getErrorMessage( ) );

                        return error;
                    }
                }
            }

            if ( bConfirmField && ( ( strValueEntryConfirmField == null ) || !strValueEntry.equals( strValueEntryConfirmField ) ) )
            {
                GenericAttributeError error = new GenericAttributeError( );
                error.setMandatoryError( false );
                error.setTitleQuestion( entry.getConfirmFieldTitle( ) );
                error.setErrorMessage( I18nService.getLocalizedString( MESSAGE_CONFIRM_FIELD, new String [ ] {
                    entry.getTitle( )
                }, request.getLocale( ) ) );

                return error;
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResponseValueForExport( Entry entry, HttpServletRequest request, Response response, Locale locale )
    {
        return response.getResponseValue( );
    }

    /**
     * Set the list of fields
     * 
     * @param entry
     *            The entry
     * @param request
     *            the HTTP request
     */
    protected void setFields( Entry entry, HttpServletRequest request )
    {
        entry.getFields( ).add( buildBaseUrlField( entry, request ) );
    }

    /**
     * Build the field for base url
     * 
     * @param entry
     *            The entry
     * @param request
     *            the HTTP request
     * @return the field
     */
    private Field buildBaseUrlField( Entry entry, HttpServletRequest request )
    {
        String strBaseUrl = request.getParameter( PARAMETER_BASE_URL );
        Field fieldBaseUrl = GenericAttributesUtils.findFieldByTitleInTheList( CONSTANT_BASE_URL, entry.getFields( ) );

        if ( fieldBaseUrl == null )
        {
            fieldBaseUrl = new Field( );
        }

        fieldBaseUrl.setParentEntry( entry );
        fieldBaseUrl.setTitle( CONSTANT_BASE_URL );
        fieldBaseUrl.setValue( strBaseUrl );

        return fieldBaseUrl;
    }
    
    @Override
    public String getResourceId( )
    {
        return RESOURCE_ID;
    }

    @Override
    public String getResourceTypeCode( )
    {
        return RESOURCE_TYPE;
    }
}
