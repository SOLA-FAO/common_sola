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
package org.flossola.common.utilities.constants;

/**
 * Holds the list of constants that map to the configuration settings in the
 * system.setting table
 */
public class ConfigConstants {

    /**
     * Number of days before a users password expires. Calculated from the last
     * date the user changed their password. Recommended values are 60, 90 or
     * 180. If NULL (or settings is disabled), then no password expiry applies.
     * Default is 90 days. SOLA will prompt the user to change their password if
     * it is within 14 days of expiry so it is recommended to set it to a value
     * > 14. Changes to this value will be recognized immediately if the
     * SolaRealm in Glassfish is setup to use the system.active_users view for
     * username details. This setting value is used directly by the
     * system.user_pword_expiry view (and indirectly by the system.active_users
     * view).tetest2
     */
    public static final String PWORD_EXPIRY_DAYS = "pword-expiry-days";
    /**
     * tax-rate - The tax rate to use for financial calculations. Changes to
     * this setting will have immediate effect. Default 0.075.
     */
    public static final String TAX_RATE = "tax-rate";
    /**
     * network-scan-folder - The network folder location used to store scanned
     * images. Used by the Digital Archive Service to display new scanned
     * documents for attachment to applications. Also used by the
     * SharedFolderCleaner service to remove images from the scan folder. The
     * SOLA Services must be restarted before changes to this value take effect.
     * Default is <user home>/sola/scan
     */
    public static final String NETWORK_SCAN_FOLDER = "network-scan-folder";
    /**
     * clean-network-scan-folder - Flag (Y or N) to indicate if the network scan
     * folder should be periodically cleaned of old files (Y) or not (N). The
     * lifetime of the files in the scan folder can also be set. Changes to this
     * value will be detected at the next scheduled run of the
     * SharedFolderCleaner service (i.e. within 60 minutes). Default N. Used by
     * the SharedFolderCleaner service.
     */
    public static final String CLEAN_NETWORK_SCAN_FOLDER = "clean-network-scan-folder";
    /**
     * network-scan-folder-domain - The domain of the user account that should
     * be used to connect to the network scan folder. Required if the network
     * scan folder is located at a different computer to the one hosting the
     * Glassfish server. Used by the Digital Archive Service to display new
     * scanned documents for attachment to applications. Also used by the
     * SharedFolderCleaner service to remove images from the scan folder. The
     * SOLA Services must be restarted before changes to this value take effect.
     * Default is null.
     */
    public static final String NETWORK_SCAN_FOLDER_DOMAIN = "network-scan-folder-domain";
    /**
     * network-scan-folder-user - The user account that should be used to
     * connect to the network scan folder. Required if the network scan folder
     * is located at a different computer to the one hosting the Glassfish
     * server. Used by the Digital Archive Service to display new scanned
     * documents for attachment to applications. Also used by the
     * SharedFolderCleaner service to remove images from the scan folder. The
     * SOLA Services must be restarted before changes to this value take effect.
     * Default is null. As the users password must be stored un-encrypted, the
     * User account used for the share must be a least privilege account. DO NOT
     * USE A LOCAL ADMINISTRATOR OR A DOMAIN ADMINISTRATOR ACCOUNT!
     */
    public static final String NETWORK_SCAN_FOLDER_USER = "network-scan-folder-user";
    /**
     * network-scan-folder-password - The password for the user account that
     * should be used to connect to the network scan folder. Required if the
     * network scan folder is located at a different computer to the one hosting
     * the Glassfish server. Used by the Digital Archive Service to display new
     * scanned documents for attachment to applications. Also used by the
     * SharedFolderCleaner service to remove images from the scan folder. The
     * SOLA Services must be restarted before changes to this value take effect.
     * Default is null. As the users password must be stored un-encrypted, the
     * User account used for the share must be a least privilege account. DO NOT
     * USE A LOCAL ADMINISTRATOR OR A DOMAIN ADMINISTRATOR ACCOUNT!
     */
    public static final String NETWORK_SCAN_FOLDER_PASSWORD = "network-scan-folder-password";
    /**
     * scanned-file-lifetime - The length of time in hours a file will be left
     * in the network scan folder before it is deleted by the
     * SharedFolderCleaner service. Note that the CLEAN_NETWORK_SCAN_FOLDER
     * setting must be Y for this setting to have any effect. Changes to this
     * value will be detected at the next scheduled run of the
     * SharedFolderCleaner service (i.e. within 60 minutes). Default is 720
     * hours (i.e. 30 days). Used by the SharedFolderCleaner service.
     */
    public static final String SCANNED_FILE_LIFETIME = "scanned-file-lifetime";
    /**
     * clean-network-scan-folder-poll-period - The length of time in minutes
     * between each scheduled run of the SharedFolderCleaner service. The SOLA
     * Services must be restarted before changes to this value take effect.
     * Default is 60 minutes. Used by the SharedFolderCleaner service.
     */
    public static final String CLEAN_NETWORK_SCAN_FOLDER_POLL_PERIOD = "clean-network-scan-folder-poll-period";
    /**
     * server-document-cache-folder - The folder the server document cache is
     * located. It is recommended that this is a different location to the
     * NETWORK_SCAN_FOLDER. The SOLA Services must be restarted before changes
     * to this value take effect. Default is <user home>/sola/cache/documents.
     * Used by the Digital Archive Service.
     */
    public static final String SERVER_DOCUMENT_CACHE_FOLDER = "server-document-cache-folder";
    /**
     * server-document-cache-max-size - The maximum size in MB of the server
     * document cache. The SOLA Services must be restarted before changes to
     * this value take effect. Default value is 500MB. Used by the Digital
     * Archive Service.
     */
    public static final String SERVER_DOCUMENT_CACHE_MAX_SIZE = "server-document-cache-max-size";
    /**
     * server-document-cache-resized - The maximum size in MB of the server
     * document cache after it is resized/maintained. The SOLA Services must be
     * restarted before changes to this value take effect. Default value is
     * 200MB. Used by the Digital Archive Service.
     */
    public static final String SERVER_DOCUMENT_CACHE_RESIZED = "server-document-cache-resized";
    /**
     * map-tolerance - The tolerance used while snapping geometries to each
     * other. If two points are within this distance they are considered being
     * in the same location. Default 0.01 of the map units.
     */
    public static final String MAP_TOLERANCE = "map-tolerance";
    /**
     * map-shift-tolernace-rural - The shift tolerance of boundary points used
     * in cadastre change in rural areas. Users will need to restart their
     * client applications if they want changes to this setting to take effect.
     * Default 20
     */
    public static final String MAP_SHIFT_TOLERANCE_RURAL = "map-shift-tolernace-rural";
    /**
     * map-shift-tolernace-urban - The shift tolerance of boundary points used
     * in cadastre change in urban areas. Users will need to restart their
     * client applications if they want changes to this setting to take effect.
     * Default 5
     */
    public static final String MAP_SHIFT_TOLERANCE_URBAN = "map-shift-tolernace-urban";
    /**
     * map-srid - The srid of the geographic data administered by the system.
     * Users will need to restart their client applications if they want changes
     * to this setting to take effect. Default is 32702 (i.e. New Zealand)
     */
    public static final String MAP_SRID = "map-srid";
    /**
     * map-west - The western most coordinate when displaying the initial map
     * extent. Users will need to restart their client applications if they want
     * changes to this setting to take effect.
     */
    public static final String MAP_WEST = "map-west";
    /**
     * map-east - The eastern most coordinate when displaying the initial map
     * extent. Users will need to restart their client applications if they want
     * changes to this setting to take effect.
     */
    public static final String MAP_EAST = "map-east";
    /**
     * map-south - The southern most coordinate when displaying the initial map
     * extent. Users will need to restart their client applications if they want
     * changes to this setting to take effect.
     */
    public static final String MAP_SOUTH = "map-south";
    /**
     * map-north - The northern most coordinate when displaying the initial map
     * extent. Users will need to restart their client applications if they want
     * changes to this setting to take effect.
     */
    public static final String MAP_NORTH = "map-north";
    /**
     * Maximum file size in KB for uploading.
     */
    public static final String MAX_FILE_SIZE = "max-file-size";
    /**
     * Maximum size of all files in KB, uploaded by user during the day.
     */
    public static final String MAX_UPLOADING_DAILY_LIMIT = "max-uploading-daily-limit";
    /**
     * Duration of moderation time in days for submitted claim.
     */
    public static final String MODERATION_DAYS = "moderation-days";
    /**
     * Account activation timeout in hours. After this time elapsed, activation
     * should expire.
     */
    public static final String ACCOUNT_ACTIVATION_TIMEOUT = "account-activation-timeout";
    /**
     * Email address of server administrator.
     */
    public static final String EMAIL_ADMIN_ADDRESS = "email-admin-address";
    /**
     * Name of server administrator.
     */
    public static final String EMAIL_ADMIN_NAME = "email-admin-name";
    /**
     * JNDI name of the mailer service, configured on GlassFish
     */
    public static final String EMAIL_MAILER_JNDI_NAME = "email-mailer-jndi-name";
    /**
     * Enables or disables email service. 1 - enable, 0 - disable.
     */
    public static final String EMAIL_ENABLE_SERVICE = "email-enable-email-service";
    /**
     * Message body format. text - for simple text format, html - for html format
     */
    public static final String EMAIL_BODY_FORMAT = "email-body-format";
    
    
    
    /**
     * New claim challenge body text.
     */
    public static final String EMAIL_MSG_REC_NOTIFIABLE_BODY = "email-msg-notifiable-submit-body";
     /**
     * Subject text for new user registration on OpenTenure Web-site. Sent to
     * user.
     */
    public static final String EMAIL_MSG_NOTIFIABLE_SUBJECT = "email-msg-notifiable-subject";
  
    /**
     * New claim challenge body text.
     */
    public static final String EMAIL_MSG_CLAIM_CHALLENGE_SUBMITTED_BODY = "email-msg-claim-challenge-submitted-body";
    /**
     * New claim challenge subject text.
     */
    public static final String EMAIL_MSG_CLAIM_CHALLENGE_SUBMITTED_SUBJECT = "email-msg-claim-challenge-submitted-subject";
    /**
     * Claim challenge update body text.
     */
    public static final String EMAIL_MSG_CLAIM_CHALLENGE_UPDATED_BODY = "email-msg-claim-challenge-updated-body";
    /**
     * Claim challenge update subject text.
     */
    public static final String EMAIL_MSG_CLAIM_CHALLENGE_UPDATED_SUBJECT = "email-msg-claim-challenge-updated-subject";
    /**
     * New claim body text.
     */
    public static final String EMAIL_MSG_CLAIM_SUBMITTED_BODY = "email-msg-claim-submit-body";
    /**
     * New claim subject text.
     */
    public static final String EMAIL_MSG_CLAIM_SUBMITTED_SUBJECT = "email-msg-claim-submit-subject";
    /**
     * Body text for claim withdrawal action
     */
    public static final String EMAIL_MSG_CLAIM_WITHDRAW_BODY = "email-msg-claim-withdraw-body";
    /**
     * Subject text for claim withdrawal action
     */
    public static final String EMAIL_MSG_CLAIM_WITHDRAW_SUBJECT = "email-msg-claim-withdraw-subject";
    /** Body text for claim reject action */
    public static final String EMAIL_MSG_CLAIM_REJECT_BODY = "email-msg-claim-reject-body";
    /** Subject text for claim reject action */
    public static final String EMAIL_MSG_CLAIM_REJECT_SUBJECT = "email-msg-claim-reject-subject";
    /** Body text for claim review approve action */
    public static final String EMAIL_MSG_CLAIM_REVIEW_APPROVE_BODY = "email-msg-claim-approve-review-body";
    /** Subject text for claim review approve action */
    public static final String EMAIL_MSG_CLAIM_REVIEW_APPROVE_SUBJECT = "email-msg-claim-approve-review-subject";
    /** Body text for claim moderation approve action */
    public static final String EMAIL_MSG_CLAIM_MODERATION_APPROVE_BODY = "email-msg-claim-approve-moderation-body";
    /** Subject text for claim moderation approve action */
    public static final String EMAIL_MSG_CLAIM_MODERATION_APPROVE_SUBJECT = "email-msg-claim-approve-moderation-subject";
    /**
     * Claim update body text.
     */
    public static final String EMAIL_MSG_CLAIM_UPDATED_BODY = "email-msg-claim-updated-body";
    /**
     * Claim update subject text.
     */
    public static final String EMAIL_MSG_CLAIM_UPDATED_SUBJECT = "email-msg-claim-updated-subject";
    /** Body text for claim challenge review action. */
    public static final String EMAIL_MSG_CLAIM_CHALLENGE_REVIEW_BODY = "email-msg-claim-challenge-approve-review-body";
    /** Subject text for claim challenge review action. */
    public static final String EMAIL_MSG_CLAIM_CHALLENGE_REVIEW_SUBJECT = "email-msg-claim-challenge-approve-review-subject";
    /** Body text for claim challenge moderation action. */
    public static final String EMAIL_MSG_CLAIM_CHALLENGE_MODERATION_BODY = "email-msg-claim-challenge-approve-moderation-body";
    /** Subject text for claim challenge moderation action. */
    public static final String EMAIL_MSG_CLAIM_CHALLENGE_MODERATION_SUBJECT = "email-msg-claim-challenge-approve-moderation-subj";
    /** Body text for claim challenge rejection action. */
    public static final String EMAIL_MSG_CLAIM_CHALLENGE_REJECTION_BODY = "email-msg-claim-challenge-reject-body";
    /** Subject text for claim challenge rejection action. */
    public static final String EMAIL_MSG_CLAIM_CHALLENGE_REJECTION_SUBJECT = "email-msg-claim-challenge-reject-subject";
    /** Body text for claim challenge withdrawal action. */
    public static final String EMAIL_MSG_CLAIM_CHALLENGE_WITHDRAWAL_BODY = "email-msg-claim-challenge-withdraw-body";
    /** Subject text for claim challenge withdrawal action. */
    public static final String EMAIL_MSG_CLAIM_CHALLENGE_WITHDRAWAL_SUBJECT = "email-msg-claim-challenge-withdraw-subject";
    /**
     * Message text for delivery failure.
     */
    public static final String EMAIL_MSG_FAILED_SEND_BODY = "email-msg-failed-send-body";
    /**
     * Subject text for delivery failure of message.
     */
    public static final String EMAIL_MSG_FAILED_SEND_SUBJECT = "email-msg-failed-send-subject";
    /**
     * Message text for password restore.
     */
    public static final String EMAIL_MSG_PASSWD_RESTORE_BODY = "email-msg-pswd-restore-body";
    /**
     * Password restore subject.
     */
    public static final String EMAIL_MSG_PASSWD_RESTORE_SUBJECT = "email-msg-pswd-restore-subject";
    /**
     * Message text for new user registration on OpenTenure Web-site. Sent to
     * user.
     */
    public static final String EMAIL_MSG_REG_BODY = "email-msg-reg-body";
    /**
     * Subject text for new user registration on OpenTenure Web-site. Sent to
     * user.
     */
    public static final String EMAIL_MSG_REG_SUBJECT = "email-msg-reg-subject";
    /**
     * Message text for new user registration on OpenTenure Web-site. Sent to
     * system administrator.
     */
    public static final String EMAIL_MSG_USER_REG_BODY = "email-msg-user-registration-body";
    /**
     * Subject text for new user registration on OpenTenure Web-site. Sent to
     * system administrator.
     */
    public static final String EMAIL_MSG_USER_REG_SUBJECT = "email-msg-user-registration-subject";
    /**
     * Message text for new user registration on OpenTenure Web-site. Sent to
     * user.
     */
    public static final String EMAIL_MSG_ACTIVATION_BODY = "email-msg-user-activation-body";
    /**
     * Subject text for new user registration on OpenTenure Web-site. Sent to
     * user.
     */
    public static final String EMAIL_MSG_ACTIVATION_SUBJECT = "email-msg-user-activation-subject";
    /**
     * Number of attempts to send email with first interval timeout
     */
    public static final String EMAIL_SEND_ATTEMPTS1 = "email-send-attempts1";
    /**
     * Number of attempts to send email with second interval timeout
     */
    public static final String EMAIL_SEND_ATTEMPTS2 = "email-send-attempts2";
    /**
     * Number of attempts to send email with third interval timeout
     */
    public static final String EMAIL_SEND_ATTEMPTS3 = "email-send-attempts3";
    /**
     * Time interval in minutes for the first attempt to send email message.
     */
    public static final String EMAIL_SEND_INTERVAL1 = "email-send-interval1";
    /**
     * Time interval in minutes for the second attempt to send email message.
     */
    public static final String EMAIL_SEND_INTERVAL2 = "email-send-interval2";
    /**
     * Time interval in minutes for the third attempt to send email message.
     */
    public static final String EMAIL_SEND_INTERVAL3 = "email-send-interval3";
    /**
     * Time interval in seconds for email service to check and process scheduled messages.
     */
    public static final String EMAIL_SERVICE_INTERVAL = "email-service-interval";
    
    /** Open Tenure community area where parcels can be claimed */
    public static final String OT_COMMUNITY_AREA = "ot-community-area";
    
    /** Full path to PostgreSQL utilities (bin) folder (e.g. C:\Program Files\PostgreSQL\9.1\bin). Used for backup/restore implementation of SOLA Web admin application */
    public static final String DB_UTILITIES_FOLDER = "db-utilities-folder";
    
    /** SOLA product name */
    public static final String PRODUCT_NAME = "product-name";
    
    /** SOLA product code */
    public static final String PRODUCT_CODE = "product-code";
    
    /** SOLA Registry */
    public static final String SOLA_REGISTRY = "sr";
    /** SOLA Systematic Registration */
    public static final String SOLA_SYSTEMATIC_REGISTRATION = "ssr";
    /** SOLA State Land */
    public static final String SOLA_STATE_LAND = "ssl";
    /** SOLA Community Server */
    public static final String SOLA_COMMUNITY_SERVER = "scs";
}
