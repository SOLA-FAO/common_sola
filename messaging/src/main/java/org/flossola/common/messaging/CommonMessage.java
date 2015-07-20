/**
 * ******************************************************************************************
 * Copyright (C) 2015 - Food and Agriculture Organization of the United Nations (FAO).
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice,this list
 *       of conditions and the following disclaimer.
 *    2. Redistributions in binary form must reproduce the above copyright notice,this list
 *       of conditions and the following disclaimer in the documentation and/or other
 *       materials provided with the distribution.
 *    3. Neither the name of FAO nor the names of its contributors may be used to endorse or
 *       promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,STRICT LIABILITY,OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * *********************************************************************************************
 */
package org.flossola.common.messaging;

/**
 * Contains message codes for messages raised from the services.
 * @author soladev
 */
public class CommonMessage {

    // Message prefixes
    public static final String MSG_SER_PREFIX = "ser";
    public static final String MSG_CLI_PREFIX = "cli";
    public static final String MSG_GIS_PREFIX = "gis";
    
    // Service Message groups
    private static final String TEST = MSG_SER_PREFIX + "test";
    private static final String GENERAL = MSG_SER_PREFIX + "gnrl";
    private static final String EXCEPTION = MSG_SER_PREFIX + "excp";
    private static final String RULE = MSG_SER_PREFIX + "rule";
    private static final String REDACT = MSG_SER_PREFIX + "redact";
    
    // Client Message Groups
    private static final String CLIENT_GENERAL = MSG_CLI_PREFIX + "gnrl";
    private static final String SECURITY = MSG_CLI_PREFIX + "sec";
    private static final String GENERAL_ERRORS = MSG_CLI_PREFIX + "errs";  
    private static final String PROGRESSMSG = MSG_CLI_PREFIX + "prgs";
    
    // GIS Message Groups
    private static final String GEOTOOLS = MSG_GIS_PREFIX + "geotools5";
    
    // <editor-fold defaultstate="collapsed" desc="Test Messages">  
    /** sertest001 - Unit Test Message */
    public static final String TEST001 = TEST + "001";
    /** sertest002 - Unit Test Message */
    public static final String TEST002 = TEST + "002";
    /** sertest003 - Unit Test Message */
    public static final String TEST003 = TEST + "003";
    /** sertest004 - Unit Test Message */
    public static final String TEST004 = TEST + "004";
    /** sertest005 - Unit Test Message */
    public static final String TEST005 = TEST + "005";
    /** sertest006 - Unit Test Message */
    public static final String TEST006 = TEST + "006";
    /** sertest007 - Unit Test Message */
    public static final String TEST007 = TEST + "007";
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Service Messages">
    // General Messages
    /** sergnrl001 - An unexpected error has occurred. */
    public static final String GENERAL_UNEXPECTED = GENERAL + "001";
    /** sergnrl002 - Your changes cannot be saved as the record you are editing has been changed 
                     by someone else. */
    public static final String GENERAL_OPTIMISTIC_LOCK = GENERAL + "002";
    /** sergnrl003 - An unexpected error has occurred while performing {0}. Error details: {1} */
    public static final String GENERAL_UNEXPECTED_ERROR_DETAILS = GENERAL + "003";
    /** sergnrl004 - An unexpected error has occurred while performing {0}. Error details: {1} */
    public static final String GENERAL_UNEXPECTED_ERROR_DETAILS_ERR_NUM = GENERAL + "004";
    /** sergnrl005 - Your account is not active. If you just registered, you need to activate it first. */
    public static final String GENERAL_ACCOUNT_LOCKED = GENERAL + "005";
    /** sergnrl006 - Provided object is null. */
    public static final String GENERAL_OBJECT_IS_NULL = GENERAL + "006";
    /** sergnrl007 - Object already exists. */
    public static final String GENERAL_OBJECT_EXIST = GENERAL + "007";
    /** sergnrl008 - File size is wrong. */
    public static final String GENERAL_WRONG_FILE_SIZE = GENERAL + "008";
    /** sergnrl009 - MD5 is not matching. */
    public static final String GENERAL_WRONG_MD5 = GENERAL + "009";
    
    // Exception Messages
    /** serexcp001 - An error occurred while logging an exception. Error details: {0} */
    public static final String EXCEPTION_FAILED_LOGGING = EXCEPTION + "001";
    /** serexcp002 - An error occurred while formatting an exception. Error details: {0}. */
    public static final String EXCEPTION_FAILED_FORMATTING = EXCEPTION + "002";
    /** serexcp003 - The service url is malformed. Error details: {1} */
    public static final String EXCEPTION_MALFORMED_URL = EXCEPTION + "003";
    /** serexcp004 - The username and password are incorrect. */
    public static final String EXCEPTION_AUTHENTICATION_FAILED = EXCEPTION + "004";
    /** serexcp005 - Unable to connect to the service at {0}. Error details: {1} */
    public static final String EXCEPTION_SERVICE_CONNECTION = EXCEPTION + "005";
    /** serexcp006 - You do not have enough rights to access this function. */
    public static final String EXCEPTION_INSUFFICIENT_RIGHTS = EXCEPTION + "006";
    /** serexcp007 - Entity does not belong to the called EJB. */
    public static final String EXCEPTION_ENTITY_PACKAGE_VIOLATION = EXCEPTION + "007";
    /** serexcp008 - Unable to initialize web service client for service {0}. Error details: {1} */
    public static final String EXCEPTION_INITALIZE_WSCLIENT = EXCEPTION + "008";
    /* serexcp009 - The username and password are incorrect. */
    public static final String EXCEPTION_INVALID_SECURITY_HEADER = EXCEPTION + "009";
    /* serexcp010 - The file cannot be attached due to its size ({0}MB) */
    public static final String EXCEPTION_FILE_TOO_BIG = EXCEPTION + "010";
    /* serexcp020 - The file cannot be attached due to its size ({0}MB) */
    public static final String EXCEPTION_NETWORK_SCAN_FOLDER = EXCEPTION + "020";
    /** serexcp021 - You can't do changes to the object belonging to another user. */
    public static final String EXCEPTION_OBJECT_ACCESS_RIGHTS = EXCEPTION + "021";
    
    // Business Rule Messages
    /** serrule001 - Business rule {0} does not exist.*/
    public static final String RULE_NOT_FOUND = RULE + "001";
    /** serrule002 - Business rule {0} did not execute successfully.*/
    public static final String RULE_FAILED_EXECUTION = RULE + "002";
    /** serrule003 - Validation failed.*/
    public static final String RULE_VALIDATION_FAILED = RULE + "003";

    // Redaction Messages
    /** serredact001 - Restricted. */
    public static final String REDACT_RESTRICTED = REDACT + "001";
    /** serredact002 - Not Applicable. */
    public static final String REDACT_GENDER = REDACT + "002";
    /** serredact003 - JAN 1, 1800 00:00. */
    public static final String REDACT_DATE_OF_BIRTH = REDACT + "003";
    /** serredact004 - MMM d, yyyy HH:mm */
    public static final String REDACT_DATE_FORMAT = REDACT + "004";
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Client Messages"> 
    /** clignrl001 - An unexpected error has occurred. Error details: {0} */
    public static final String CLIENTS_GENERAL_UNEXPECTED = CLIENT_GENERAL + "001";
    /**  clierrs001 - Can't cerate a new file. {0}  */
    public static final String ERR_FAILED_CREATE_NEW_FILE = GENERAL_ERRORS + "001";
    /** clignrl003 - There are {0} running tasks currently. Please wait until finishing. */
    public static final String GENERAL_ACTIVE_TASKS_EXIST = CLIENT_GENERAL + "003";
    /** clierrs002 - The file {0} cannot be opened automatically. */
    public static final String ERR_FAILED_OPEN_FILE = GENERAL_ERRORS + "002";
    /**  cliprgs030 - Searching {0}... */
    public static final String PROGRESS_MSG_MAP_SEARCHING = PROGRESSMSG + "030";
    /** clisec004 - Admin Desktop.*/
    public static final String SECURITY_LOGIN_TITLE_ADMIN = SECURITY + "004";
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Geotools Messages">
    // GEOTOOLS
    public static final String ADDING_FEATURE_ERROR = GEOTOOLS + "01";
    public static final String MAPCONTROL_MAPCONTEXT_WITHOUT_SRID_ERROR = GEOTOOLS + "02";
    public static final String DRAWINGTOOL_GEOMETRY_NOT_VALID_ERROR = GEOTOOLS + "03";
    public static final String LAYERGRAPHICS_STARTUP_ERROR = GEOTOOLS + "04";
    public static final String SHAPEFILELAYER_FILE_NOT_FOUND_ERROR = GEOTOOLS + "05";
    public static final String REMOVE_ALL_FEATURES_ERROR = GEOTOOLS + "06";
    public static final String LAYER_NOT_ADDED_ERROR = GEOTOOLS + "07";
    public static final String WMSLAYER_NOT_INITIALIZED_ERROR = GEOTOOLS + "08";
    public static final String WMSLAYER_LAYER_NOT_FOUND_ERROR = GEOTOOLS + "09";
    public static final String UTILITIES_SLD_DOESNOT_EXIST_ERROR = GEOTOOLS + "10";
    public static final String UTILITIES_SLD_LOADING_ERROR = GEOTOOLS + "11";
    public static final String UTILITIES_COORDSYS_COULDNOT_BE_CREATED_ERROR = GEOTOOLS + "12";
    public static final String DRAWINGTOOL_NOT_ENOUGH_POINTS_INFORMATIVE = GEOTOOLS + "13";
    public static final String PARCEL_TARGET_NOT_FOUND = GEOTOOLS + "14";
    public static final String PARCEL_ERROR_ADDING_PARCEL  = GEOTOOLS + "15";
    public static final String GEOTOOL_ADDING_FEATURE_ERROR = GEOTOOLS + "17";
    public static final String GEOTOOL_MAPCONTEXT_WITHOUT_SRID_ERROR = GEOTOOLS + "18";
    public static final String GEOTOOL_GEOMETRY_NOT_VALID_ERROR = GEOTOOLS + "19";
    public static final String GEOTOOL_LAYERGRAPHICS_STARTUP_ERROR    = GEOTOOLS + "20";
    public static final String GEOTOOL_FILE_NOT_FOUND_ERROR    = GEOTOOLS + "21";
    public static final String GEOTOOL_REMOVE_ALL_FEATURES_ERROR   = GEOTOOLS + "22";
    public static final String GEOTOOL_LAYER_NOT_ADDED  = GEOTOOLS + "23";
    public static final String GEOTOOL_WMSLAYER_NOT_INITIALIZED_ERROR  = GEOTOOLS + "24";
    public static final String GEOTOOL_WMSLAYER_LAYER_NOT_FOUND_ERROR = GEOTOOLS + "25";
    public static final String GEOTOOL_SLD_DOESNOT_EXIST_ERROR  = GEOTOOLS + "26";
    public static final String GEOTOOL_SLD_LOADING_ERROR  = GEOTOOLS + "27";
    public static final String GEOTOOL_COORDSYS_COULDNOT_BE_CREATED_ERROR  = GEOTOOLS + "28";
    public static final String GEOTOOL_NOT_ENOUGH_POINTS_INFORMATIVE  = GEOTOOLS + "29";
    public static final String GEOTOOL_TOOLTIP_FULL_EXTENT  = GEOTOOLS + "30";
    public static final String GEOTOOL_TOOLTIP_ZOOM_OUT  = GEOTOOLS + "31";
    public static final String GEOTOOL_TOOLTIP_ZOOM_IN  = GEOTOOLS + "32";
    public static final String GEOTOOL_TOOLTIP_PAN  = GEOTOOLS + "33";
    public static final String PRINT  = GEOTOOLS + "34";
    public static final String PRINT_LAYOUT_NOT_SELECTED = GEOTOOLS + "35";
    public static final String PRINT_SCALE_NOT_CORRECT = GEOTOOLS + "36";
    public static final String ADD_DIRECT_IMAGE_TOOLTIP = GEOTOOLS + "37";
    public static final String ADD_DIRECT_IMAGE_ADD_FIRST_POINT = GEOTOOLS + "38";
    public static final String ADD_DIRECT_IMAGE_ADD_SECOND_POINT = GEOTOOLS + "39";
    public static final String ADD_DIRECT_IMAGE_LOAD_IMAGE_ERROR = GEOTOOLS + "40";
    public static final String ADD_DIRECT_IMAGE_DEFINE_POINT_ERROR = GEOTOOLS + "41";
    public static final String REMOVE_DIRECT_IMAGE_TOOLTIP = GEOTOOLS + "42";
    public static final String ADD_DIRECT_IMAGE_DEFINE_POINT_IN_IMAGE_ERROR = GEOTOOLS + "43";
    public static final String ADD_DIRECT_IMAGE_DEFINE_ORIENTATION_POINT_1_IN_IMAGE= GEOTOOLS + "44";
    public static final String ADD_DIRECT_IMAGE_DEFINE_ORIENTATION_POINT_2_IN_IMAGE= GEOTOOLS + "45";
    public static final String ADD_DIRECT_IMAGE_LOAD_IMAGE= GEOTOOLS + "46";
    public static final String PRINT_LAYOUT_GENERATION_ERROR = GEOTOOLS + "47";
    /** gisgeotools548 - Invalid scale */
    public static final String MAP_SCALE_ERROR = GEOTOOLS + "48";
       /** gisgeotools549 - < 0.01 */
    public static final String MIN_DISPLAY_SCALE = GEOTOOLS + "49";
    /** gisgeotools550 - Scale: */
    public static final String SCALE_LABEL = GEOTOOLS + "50";
    /** gisgeotools551 - The file {0} cannot be opened automatically */
    public static final String FAILED_OPEN_FILE = GEOTOOLS + "51";
     /** gisgeotools552 - Export selected feature(s) to KML. */
    public static final String KML_EXPORT_TOOLTIP = GEOTOOLS + "52";
     /** gisgeotools553 - An error occurred while attempting to export the selected feature(s)  */
    public static final String KML_EXPORT_ERROR = GEOTOOLS + "53";
     /** gisgeotools554 - Map feature(s) have been successfully exported to %s" */
    public static final String KML_EXPORT_FILE_LOCATION = GEOTOOLS + "54";
     /** gisgeotools555 - No features are selected for export  */
    public static final String KML_EXPORT_NO_FEATURE_SELECTED = GEOTOOLS + "55";   
     /** gisgeotools556 - WMS Layer is not rendered. Most probably the wms server is not available. Switch the layer off in order not to get this message. */
    public static final String WMSLAYER_LAYER_RENDER_ERROR = GEOTOOLS + "56";
    // </editor-fold>
}
