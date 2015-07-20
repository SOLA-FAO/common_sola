package org.flossola.common.utilities.constants;

/**
 * Holds the list of email variables, which can be used in the email text or subject.
 * These variables can be found in the text and replaced with appropriate values.
 */
public class EmailVariables {
    /** Full user name (e.g. John Smith) */
    public static final String FULL_USER_NAME = "#{userFullName}";
    /** First user name (e.g. John) */
    public static final String USER_FIRST_NAME = "#{userFirstName}";
    /** User name, used for login into the system */
    public static final String USER_NAME = "#{userName}";
    /** Name of the person to be notified to(e.g. Mary Smith) */
    public static final String NOTIFIABLE_PARTY_NAME = "#{notifiablePartyName}";
    /** Name of the person to be notified about (e.g. John Smith) */
    public static final String TARGET_PARTY_NAME = "#{targetPartyName}";
    /** Name of the property to be notified about (e.g. NA67B/16) */
    public static final String BA_UNIT_NAME = "#{baUnitName}";
    /** The office that is sending the message */
    public static final String SENDING_OFFICE = "#{sendingOffice}";
    /** The action on the interest */
    public static final String ACTION_TO_NOTIFY = "#{actionToNotify}";
    
    /** Error message text */
    public static final String ERROR_MESSAGE = "#{error}";
    /** Activation URL, used to activate created user account */
    public static final String ACTIVATION_LINK = "#{activationLink}";
    /** Activation page URL, used to activate created user account */
    public static final String ACTIVATION_PAGE = "#{activationPage}";
    /** Activation code for activating created user account */
    public static final String ACTIVATION_CODE = "#{activationCode}";
    /** Password restore URL, used to restore lost or forgotten password */
    public static final String PASSWORD_RESTORE_LINK = "#{passwordRestoreLink}";
    /** Open Tenure claim URL */
    public static final String CLAIM_LINK = "#{claimLink}";
    /** Open Tenure claim number */
    public static final String CLAIM_NUMBER = "#{claimNumber}";
    /** Open Tenure claim challenge URL */
    public static final String CLAIM_CHALLENGE_LINK = "#{challengeLink}";
    /** Open Tenure claim challenge number */
    public static final String CLAIM_CHALLENGE_NUMBER = "#{challengeNumber}";
    /** Open Tenure claim comments */
    public static final String CLAIM_COMMENTS = "#{claimComments}";
    /** Open Tenure claim challenge comments */
    public static final String CLAIM_CHALLENGE_COMMENTS = "#{challengeComments}";
    /** Open Tenure claim party role (e.g. claimant) */
    public static final String CLAIM_PARTY_ROLE = "#{partyRole}";
    /** Open Tenure claim rejection reason */
    public static final String CLAIM_REJECTION_REASON = "#{claimRejectionReason}";
    /** Open Tenure claim challenge rejection reason */
    public static final String CLAIM_CHALLENGE_REJECTION_REASON = "#{challengeRejectionReason}";
}
