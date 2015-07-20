/**
 * ******************************************************************************************
 * Copyright (C) 2015 - Food and Agriculture Organization of the United Nations
 * (FAO). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,this
 * list of conditions and the following disclaimer. 2. Redistributions in binary
 * form must reproduce the above copyright notice,this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. 3. Neither the name of FAO nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT,STRICT LIABILITY,OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * *********************************************************************************************
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.flossola.common.messaging;

import java.lang.reflect.Field;
import java.util.Arrays;
import org.junit.Ignore;
import javax.swing.JOptionPane;
import java.util.Locale;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author soladev
 */
public class MessageUtilityTest {

    public MessageUtilityTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        Locale loc = new Locale("en", "US");
        Locale.setDefault(loc);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
    }

    /**
     * Checks that a default message is returned for a null code. This test will
     * fail if the default locale is not en_US
     */
    @Test
    public void testGetLocalizedMessage_NullCode() {
        System.out.println("getLocalizedMessage_NullCode");
        String msgCode = null;
        LocalizedMessage result = CommonMessageUtility.getLocalizedMessage(msgCode);
        assertNotNull(result);
        assertEquals(msgCode, result.getMessageCode());
        assertEquals("Unable to obtain localized message for null from locale en_US.",
                result.getMessage());
        assertEquals("Contact your system administrator for assistance.", result.getAction());
        assertEquals("Error", result.getTypeDescription());
        assertEquals(LocalizedMessage.Type.ERROR, result.getType());
        assertArrayEquals(new String[]{"OK"}, result.getDialogOptions());
        assertEquals("Unable to obtain localized message for null from locale en_US."
                + System.getProperty("line.separator") + "Contact your system administrator for assistance.",
                result.formatMessage(null));
        assertEquals("Error", result.formatTitle());
        assertEquals(JOptionPane.ERROR_MESSAGE, CommonMessageUtility.getJOptionPaneMessageType(result));
        assertEquals(JOptionPane.DEFAULT_OPTION, CommonMessageUtility.getJOptionPaneOptionType(result));
    }

    /**
     * Checks that a default message is returned for an invalid code. This test
     * will fail if the default locale is not en_US.
     */
    @Test
    public void testGetLocalizedMessage_InvalidCode() {
        System.out.println("getLocalizedMessage_InvalidCode");
        String msgCode = "invalid";
        LocalizedMessage result = CommonMessageUtility.getLocalizedMessage(msgCode);
        assertNotNull(result);
        assertEquals(msgCode, result.getMessageCode());
        assertEquals("Unable to obtain localized message for invalid from locale en_US.",
                result.getMessage());
        assertEquals("Contact your system administrator for assistance.", result.getAction());
        assertEquals("Error", result.getTypeDescription());
        assertEquals(LocalizedMessage.Type.ERROR, result.getType());
        assertArrayEquals(new String[]{"OK"}, result.getDialogOptions());
        assertEquals("Unable to obtain localized message for invalid from locale en_US."
                + System.getProperty("line.separator") + "Contact your system administrator for assistance.",
                result.formatMessage("err_num"));
        assertEquals("Error INVALID", result.formatTitle());
        assertEquals(JOptionPane.ERROR_MESSAGE, CommonMessageUtility.getJOptionPaneMessageType(result));
        assertEquals(JOptionPane.DEFAULT_OPTION, CommonMessageUtility.getJOptionPaneOptionType(result));
    }

    /**
     * Checks a valid message can be retrieved from the Service package
     */
    @Test
    public void testGetLocalizedMessage_ServiceTestMsg() {
        System.out.println("getLocalizedMessage_ServiceTestMsg");
        String msgCode = CommonMessage.TEST001;
        LocalizedMessage result = CommonMessageUtility.getLocalizedMessage(msgCode);
        assertNotNull(result);
        assertEquals(msgCode, result.getMessageCode());
        assertEquals("This is a service test message.", result.getMessage());
        assertEquals("No action required.", result.getAction());
        assertEquals("Message", result.getTypeDescription());
        assertEquals(LocalizedMessage.Type.PLAIN, result.getType());
        assertArrayEquals(new String[]{"OK"}, result.getDialogOptions());
        assertEquals("This is a service test message."
                + System.getProperty("line.separator") + "No action required.",
                result.formatMessage(null));
        assertEquals("Message " + CommonMessage.TEST001.toUpperCase(), result.formatTitle());
        assertEquals(JOptionPane.PLAIN_MESSAGE, CommonMessageUtility.getJOptionPaneMessageType(result));
        assertEquals(JOptionPane.DEFAULT_OPTION, CommonMessageUtility.getJOptionPaneOptionType(result));
    }

    /**
     * Checks a valid message can be retrieved from the Client package
     */
//    @Test
//    public void testGetLocalizedMessage_ClientTestMsg() {
//        System.out.println("getLocalizedMessage_ClientTestMsg");
//        String msgCode = ClientMessage.TEST001;
//        LocalizedMessage result = CommonMessageUtility.getLocalizedMessage(msgCode);
//        assertNotNull(result);
//        assertEquals(msgCode, result.getMessageCode());
//        assertEquals("This is a client test message.", result.getMessage());
//        assertNull(result.getAction());
//        assertEquals("Message", result.getTypeDescription());
//        assertEquals(LocalizedMessage.Type.PLAIN, result.getType());
//        assertArrayEquals(new String[]{"OK"}, result.getDialogOptions());
//        assertEquals("This is a client test message.", result.formatMessage("err num"));
//        assertEquals("Message " + ClientMessage.TEST001.toUpperCase(), result.formatTitle());
//        assertEquals(JOptionPane.PLAIN_MESSAGE, CommonMessageUtility.getJOptionPaneMessageType(result));
//        assertEquals(JOptionPane.DEFAULT_OPTION, CommonMessageUtility.getJOptionPaneOptionType(result));
//    }
    /**
     * Checks a valid message can be retrieved from the GIS package
     */
//    @Test
//    public void testGetLocalizedMessage_GISTestMsg() {
//        System.out.println("getLocalizedMessage_GISTestMsg");
//        String msgCode = GisMessage.TEST001;
//        LocalizedMessage result = CommonMessageUtility.getLocalizedMessage(msgCode);
//        assertNotNull(result);
//        assertEquals(msgCode, result.getMessageCode());
//        assertEquals("This is a gis test message.", result.getMessage());
//        assertNull(result.getAction());
//        assertEquals("Message", result.getTypeDescription());
//        assertEquals(LocalizedMessage.Type.PLAIN, result.getType());
//        assertArrayEquals(new String[]{"OK"}, result.getDialogOptions());
//        assertEquals("This is a gis test message.", result.formatMessage(null));
//        assertEquals("Message " + GisMessage.TEST001.toUpperCase(), result.formatTitle());
//        assertEquals(JOptionPane.PLAIN_MESSAGE, CommonMessageUtility.getJOptionPaneMessageType(result));
//        assertEquals(JOptionPane.DEFAULT_OPTION, CommonMessageUtility.getJOptionPaneOptionType(result));
//    }
    /**
     * Checks that the ERROR type is correctly mapped
     */
    @Test
    public void testGetLocalizedMessage_ServiceTestERROR() {
        System.out.println("getLocalizedMessage_ServiceTestERROR");
        String msgCode = CommonMessage.TEST003;
        LocalizedMessage result = CommonMessageUtility.getLocalizedMessage(msgCode);
        assertNotNull(result);
        assertEquals(msgCode, result.getMessageCode());
        assertEquals("Error test message. Error Number:", result.getMessage());
        assertNull(result.getAction());
        assertEquals("Error", result.getTypeDescription());
        assertEquals(LocalizedMessage.Type.ERROR, result.getType());
        assertArrayEquals(new String[]{"OK"}, result.getDialogOptions());
        assertEquals("Error test message. Error Number: errnum1", result.formatMessage("errnum1"));
        assertEquals("Error " + CommonMessage.TEST003.toUpperCase(), result.formatTitle());
        assertEquals(JOptionPane.ERROR_MESSAGE, CommonMessageUtility.getJOptionPaneMessageType(result));
        assertEquals(JOptionPane.DEFAULT_OPTION, CommonMessageUtility.getJOptionPaneOptionType(result));
    }

    /**
     * Checks that the WARNING type is correctly mapped
     */
    @Test
    public void testGetLocalizedMessage_ServiceTestWARNING() {
        System.out.println("getLocalizedMessage_ServiceTestWARNING");
        String msgCode = CommonMessage.TEST004;
        LocalizedMessage result = CommonMessageUtility.getLocalizedMessage(msgCode);
        assertNotNull(result);
        assertEquals(msgCode, result.getMessageCode());
        assertEquals("Warning test message.", result.getMessage());
        assertNull(result.getAction());
        assertEquals("Warning", result.getTypeDescription());
        assertEquals(LocalizedMessage.Type.WARNING, result.getType());
        assertArrayEquals(new String[]{"OK"}, result.getDialogOptions());
        assertEquals("Warning test message.", result.formatMessage(null));
        assertEquals("Warning " + CommonMessage.TEST004.toUpperCase(), result.formatTitle());
        assertEquals(JOptionPane.WARNING_MESSAGE, CommonMessageUtility.getJOptionPaneMessageType(result));
        assertEquals(JOptionPane.DEFAULT_OPTION, CommonMessageUtility.getJOptionPaneOptionType(result));
    }

    /**
     * Checks that the INFORMATION type is correctly mapped
     */
    @Test
    public void testGetLocalizedMessage_ServiceTestINFO() {
        System.out.println("getLocalizedMessage_ServiceTestINFO");
        String msgCode = CommonMessage.TEST005;
        LocalizedMessage result = CommonMessageUtility.getLocalizedMessage(msgCode);
        assertNotNull(result);
        assertEquals(msgCode, result.getMessageCode());
        assertEquals("Information test message.", result.getMessage());
        assertNull(result.getAction());
        assertEquals("Information", result.getTypeDescription());
        assertEquals(LocalizedMessage.Type.INFORMATION, result.getType());
        assertArrayEquals(new String[]{"OK"}, result.getDialogOptions());
        assertEquals("Information test message.", result.formatMessage(null));
        assertEquals("Information " + CommonMessage.TEST005.toUpperCase(), result.formatTitle());
        assertEquals(JOptionPane.INFORMATION_MESSAGE, CommonMessageUtility.getJOptionPaneMessageType(result));
        assertEquals(JOptionPane.DEFAULT_OPTION, CommonMessageUtility.getJOptionPaneOptionType(result));
    }

    /**
     * Checks that a message can have new parameters added
     */
    @Test
    public void testGetLocalizedMessage_ServiceParamTestMsg1() {
        System.out.println("getLocalizedMessage_ServiceParamTestMsg1");
        String msgCode = CommonMessage.TEST002;
        Object[] parms = {"one", "20000", "some other"};
        LocalizedMessage result = CommonMessageUtility.getLocalizedMessage(msgCode, parms);
        assertNotNull(result);
        assertEquals(msgCode, result.getMessageCode());
        assertEquals("one parameter test message 20000 and some other.", result.getMessage());
        assertNull(result.getAction());
        assertEquals("Question", result.getTypeDescription());
        assertEquals(LocalizedMessage.Type.QUESTION_YES_NO, result.getType());
        assertArrayEquals(new String[]{"Yes", "No"}, result.getDialogOptions());
        assertEquals("one parameter test message 20000 and some other.", result.formatMessage(null));
        assertEquals("Question " + CommonMessage.TEST002.toUpperCase(), result.formatTitle());
        assertEquals(JOptionPane.QUESTION_MESSAGE, CommonMessageUtility.getJOptionPaneMessageType(result));
        assertEquals(JOptionPane.YES_NO_OPTION, CommonMessageUtility.getJOptionPaneOptionType(result));
    }

    /**
     * Checks that not all parameters need to be substituted
     */
    @Test
    public void testGetLocalizedMessage_ServiceParamTestMsg2() {
        System.out.println("getLocalizedMessage_ServiceParamTestMsg2");
        String msgCode = CommonMessage.TEST002;
        Object[] parms = {"one", 20000};
        LocalizedMessage result = CommonMessageUtility.getLocalizedMessage(msgCode, parms);
        assertNotNull(result);
        assertEquals(msgCode, result.getMessageCode());
        assertEquals("one parameter test message 20,000 and {2}.", result.getMessage());
        assertNull(result.getAction());
        assertEquals("Question", result.getTypeDescription());
        assertEquals(LocalizedMessage.Type.QUESTION_YES_NO, result.getType());
        assertArrayEquals(new String[]{"Yes", "No"}, result.getDialogOptions());
        assertEquals("one parameter test message 20,000 and {2}.", result.formatMessage(null));
        assertEquals("Question " + CommonMessage.TEST002.toUpperCase(), result.formatTitle());
        assertEquals(JOptionPane.QUESTION_MESSAGE, CommonMessageUtility.getJOptionPaneMessageType(result));
        assertEquals(JOptionPane.YES_NO_OPTION, CommonMessageUtility.getJOptionPaneOptionType(result));
    }

    /**
     * Checks that the QUESTION_YES_NO_CANCEL type is correctly mapped
     */
    @Test
    public void testGetLocalizedMessage_ServiceTestQYNC() {
        System.out.println("getLocalizedMessage_ServiceTestQYNC");
        String msgCode = CommonMessage.TEST006;
        LocalizedMessage result = CommonMessageUtility.getLocalizedMessage(msgCode);
        assertNotNull(result);
        assertEquals(msgCode, result.getMessageCode());
        assertEquals("Question YNC message.", result.getMessage());
        assertNull(result.getAction());
        assertEquals("Question", result.getTypeDescription());
        assertEquals(LocalizedMessage.Type.QUESTION_YES_NO_CANCEL, result.getType());
        assertArrayEquals(new String[]{"Yes", "No", "Cancel"}, result.getDialogOptions());
        assertEquals("Question YNC message.", result.formatMessage(null));
        assertEquals("Question " + CommonMessage.TEST006.toUpperCase(), result.formatTitle());
        assertEquals(JOptionPane.QUESTION_MESSAGE, CommonMessageUtility.getJOptionPaneMessageType(result));
        assertEquals(JOptionPane.YES_NO_CANCEL_OPTION, CommonMessageUtility.getJOptionPaneOptionType(result));
    }

    /**
     * Checks that the QUESTION_OK_CANCEL type is correctly mapped
     */
    @Test
    public void testGetLocalizedMessage_ServiceTestQOC() {
        System.out.println("getLocalizedMessage_ServiceTestQOC");
        String msgCode = CommonMessage.TEST007;
        LocalizedMessage result = CommonMessageUtility.getLocalizedMessage(msgCode);
        assertNotNull(result);
        assertEquals(msgCode, result.getMessageCode());
        assertEquals("Question OC message.", result.getMessage());
        assertNull(result.getAction());
        assertEquals("Question", result.getTypeDescription());
        assertEquals(LocalizedMessage.Type.QUESTION_OK_CANCEL, result.getType());
        assertArrayEquals(new String[]{"Opt1", "Opt2", "Cancel"}, result.getDialogOptions());
        assertEquals("Question OC message.", result.formatMessage(null));
        assertEquals("Question " + CommonMessage.TEST007.toUpperCase(), result.formatTitle());
        assertEquals(JOptionPane.QUESTION_MESSAGE, CommonMessageUtility.getJOptionPaneMessageType(result));
        assertEquals(JOptionPane.OK_CANCEL_OPTION, CommonMessageUtility.getJOptionPaneOptionType(result));
    }

    /**
     * Checks that a default message is returned for an invalid code for the UK
     * locale No need for UK locale test, since English is default locale now
     * and all variations (e.g. UK) were removed
     */
    @Test
    @Ignore
    @Deprecated
    public void testGetLocalizedMessage_InvalidCodeUKLocale() {
        System.out.println("getLocalizedMessage_InvalidCodeUKLocale");
        String msgCode = "invalid";
        LocalizedMessage result = CommonMessageUtility.getLocalizedMessage(msgCode, Locale.UK);
        assertNotNull(result);
        assertEquals(msgCode, result.getMessageCode());
        assertEquals("Unable to obtain localized message for invalid from locale en_GB.",
                result.getMessage());
        assertEquals("Contact your system administrator for assistance.", result.getAction());
        assertEquals("Error", result.getTypeDescription());
        assertEquals(LocalizedMessage.Type.ERROR, result.getType());
        assertArrayEquals(new String[]{"OK"}, result.getDialogOptions());
        assertEquals("Unable to obtain localized message for invalid from locale en_GB."
                + System.getProperty("line.separator") + "Contact your system administrator for assistance.",
                result.formatMessage(null));
        assertEquals("Error INVALID", result.formatTitle());
        assertEquals(JOptionPane.ERROR_MESSAGE, CommonMessageUtility.getJOptionPaneMessageType(result));
        assertEquals(JOptionPane.DEFAULT_OPTION, CommonMessageUtility.getJOptionPaneOptionType(result));
    }

    /**
     * Checks a valid message can be retrieved from the Service package for the
     * UK locale No need for UK locale test, since English is default locale now
     * and all variations (e.g. UK) were removed
     */
    @Test
    @Ignore
    @Deprecated
    public void testGetLocalizedMessage_ServiceTestMsgUKLocale() {
        System.out.println("getLocalizedMessage_ServiceTestMsgUKLocale");
        String msgCode = CommonMessage.TEST001;
        LocalizedMessage result = CommonMessageUtility.getLocalizedMessage(msgCode, Locale.UK);
        assertNotNull(result);
        assertEquals(msgCode, result.getMessageCode());
        assertEquals("This is a UK service test message.", result.getMessage());
        assertEquals("No action required.", result.getAction());
        assertEquals("Message", result.getTypeDescription());
        assertEquals(LocalizedMessage.Type.PLAIN, result.getType());
        assertArrayEquals(new String[]{"OK"}, result.getDialogOptions());
        assertEquals("This is a UK service test message."
                + System.getProperty("line.separator") + "No action required.",
                result.formatMessage(null));
        assertEquals("Message " + CommonMessage.TEST001.toUpperCase(), result.formatTitle());
        assertEquals(JOptionPane.PLAIN_MESSAGE, CommonMessageUtility.getJOptionPaneMessageType(result));
        assertEquals(JOptionPane.DEFAULT_OPTION, CommonMessageUtility.getJOptionPaneOptionType(result));
    }

    /**
     * Checks that a message that is not in the named locale bundle is retrieved
     * from the default bundle No need for UK locale test, since English is
     * default locale now and all variations (e.g. UK) were removed
     */
    @Test
    @Ignore
    @Deprecated
    public void testGetLocalizedMessage_ServiceTestMsgUKLocale_Default() {
        System.out.println("getLocalizedMessage_ServiceTestMsgUKLocale_Default");
        String msgCode = CommonMessage.TEST004;
        LocalizedMessage result = CommonMessageUtility.getLocalizedMessage(msgCode, Locale.UK);
        assertNotNull(result);
        assertEquals(msgCode, result.getMessageCode());
        assertEquals("Warning test message.", result.getMessage());
        assertNull(result.getAction());
        assertEquals("Warning", result.getTypeDescription());
        assertEquals(LocalizedMessage.Type.WARNING, result.getType());
        assertArrayEquals(new String[]{"OK"}, result.getDialogOptions());
        assertEquals("Warning test message.", result.formatMessage(null));
        assertEquals("Warning " + CommonMessage.TEST004.toUpperCase(), result.formatTitle());
        assertEquals(JOptionPane.WARNING_MESSAGE, CommonMessageUtility.getJOptionPaneMessageType(result));
        assertEquals(JOptionPane.DEFAULT_OPTION, CommonMessageUtility.getJOptionPaneOptionType(result));
    }

    /**
     * Checks that the test messages can be correctly displayed. Ignored as this
     * method displays a dialog window. Should be used to verify changes to the
     * displayMessage only.
     */
    @Test
    @Ignore
    public void testDisplayMessage_ShowMessages() {
        System.out.println("testDisplayMessage_ShowMessages");
        CommonMessageUtility.displayMessage(CommonMessage.TEST001, "Test1");
        Object[] parms = {"one", 20000, "some other"};
        CommonMessageUtility.displayMessage(CommonMessage.TEST002, "Test2", parms);
        CommonMessageUtility.displayMessage(CommonMessage.TEST003, "Test3");
        CommonMessageUtility.displayMessage(CommonMessage.TEST004, (String) null);
        CommonMessageUtility.displayMessage(CommonMessage.TEST005, "Test5");
        CommonMessageUtility.displayMessage(CommonMessage.TEST006, "Test6");
        CommonMessageUtility.displayMessage(CommonMessage.TEST007, "Test7");
    }

    /**
     * Checks that all message constants on the CommonMessage class have a
     * message in the default Bundle.
     */
    @Test
    public void checkCommonMessagesExist() {
        System.out.println("checkCommonMessagesExist");
        String out = "";
        for (Field field : CommonMessage.class.getFields()) {
            String msgCode = null;
            if (!field.getName().equalsIgnoreCase("MSG_SER_PREFIX")
                    && !field.getName().equalsIgnoreCase("MSG_CLI_PREFIX")
                    && !field.getName().equalsIgnoreCase("MSG_GIS_PREFIX")) {
                try {
                    msgCode = field.get(null).toString();
                } catch (IllegalAccessException ex) {
                    out = out + System.getProperty("line.separator")
                            + "Field could not be accessed: " + field.getName();
                    break;
                }
                if (!CommonMessageUtility.hasLocalizedMessage(msgCode)) {
                    out = out + System.getProperty("line.separator")
                            + "No message for: " + msgCode;
                }
            }
        }
        assertEquals("", out);
    }

    /**
     * Checks that long parameters are dealt with correctly
     */
    @Test
    public void testGetLocalizedMessage_LongParamTestMsg1() {
        System.out.println("getLocalizedMessage_LongParamTestMsg1");
        String msgCode = CommonMessage.TEST002;
        Object[] parms = {"one", 20000, "A very very long paramter that will need to be broken at the "
            + "appropriate position otherwise it may cause some issues with viewing the message"};
        LocalizedMessage result = CommonMessageUtility.getLocalizedMessage(msgCode, parms);
        assertNotNull(result);
        assertEquals(msgCode, result.getMessageCode());
        assertEquals("one parameter test message 20,000 and A very very long paramter that will need to be broken at the "
                + "appropriate" + System.getProperty("line.separator")
                + "position otherwise it may cause some issues with viewing the message.", result.getMessage());
        assertNull(result.getAction());
        assertEquals("Question", result.getTypeDescription());
        assertEquals(LocalizedMessage.Type.QUESTION_YES_NO, result.getType());
        assertArrayEquals(new String[]{"Yes", "No"}, result.getDialogOptions());
        assertEquals("Question " + CommonMessage.TEST002.toUpperCase(), result.formatTitle());
        assertEquals(JOptionPane.QUESTION_MESSAGE, CommonMessageUtility.getJOptionPaneMessageType(result));
        assertEquals(JOptionPane.YES_NO_OPTION, CommonMessageUtility.getJOptionPaneOptionType(result));
    }

    /**
     * Checks that long parameters are dealt with correctly
     */
    @Test
    public void testGetLocalizedMessage_LongParamTestMsg2() {
        System.out.println("getLocalizedMessage_LongParamTestMsg2");
        String msgCode = CommonMessage.TEST002;
        Object[] parms = {"one", 20000, "A very very long paramter that will" + System.getProperty("line.separator")
            + "need to be broken at the appropriate position otherwise it may cause" + System.getProperty("line.separator")
            + "some issues with viewing the message"};
        LocalizedMessage result = CommonMessageUtility.getLocalizedMessage(msgCode, parms);
        assertNotNull(result);
        assertEquals(msgCode, result.getMessageCode());
        assertEquals("one parameter test message 20,000 and A very very long paramter that will need to be broken at the "
                + "appropriate" + System.getProperty("line.separator")
                + "position otherwise it may cause some issues with viewing the message.", result.getMessage());
        assertNull(result.getAction());
        assertEquals("Question", result.getTypeDescription());
        assertEquals(LocalizedMessage.Type.QUESTION_YES_NO, result.getType());
        assertArrayEquals(new String[]{"Yes", "No"}, result.getDialogOptions());
        assertEquals("Question " + CommonMessage.TEST002.toUpperCase(), result.formatTitle());
        assertEquals(JOptionPane.QUESTION_MESSAGE, CommonMessageUtility.getJOptionPaneMessageType(result));
        assertEquals(JOptionPane.YES_NO_OPTION, CommonMessageUtility.getJOptionPaneOptionType(result));
    }

    /**
     * Checks that long parameters are dealt with correctly
     */
    @Test
    public void testGetLocalizedMessage_LongParamTestMsg3() {
        System.out.println("getLocalizedMessage_LongParamTestMsg3");
        String msgCode = CommonMessage.TEST002;
        Object[] parms = {"one", 20000, "A very very long paramter that will" + System.getProperty("line.separator")
            + "need to be broken at the appropriate position otherwise it may cause" + System.getProperty("line.separator")
            + "some issues with viewing the message. This has some additional text to make sure it "
            + "works as expected otherwise for things longer than 160 chars"};
        LocalizedMessage result = CommonMessageUtility.getLocalizedMessage(msgCode, parms);
        assertNotNull(result);
        assertEquals(msgCode, result.getMessageCode());
        assertEquals("one parameter test message 20,000 and A very very long paramter that will need to be broken at the "
                + "appropriate" + System.getProperty("line.separator")
                + "position otherwise it may cause some issues with viewing the message. This has"
                + System.getProperty("line.separator")
                + "some additional text to make sure it works as expected otherwise for things"
                + System.getProperty("line.separator")
                + "longer than 160 chars.", result.getMessage());
        assertNull(result.getAction());
        assertEquals("Question", result.getTypeDescription());
        assertEquals(LocalizedMessage.Type.QUESTION_YES_NO, result.getType());
        assertArrayEquals(new String[]{"Yes", "No"}, result.getDialogOptions());
        assertEquals("Question " + CommonMessage.TEST002.toUpperCase(), result.formatTitle());
        assertEquals(JOptionPane.QUESTION_MESSAGE, CommonMessageUtility.getJOptionPaneMessageType(result));
        assertEquals(JOptionPane.YES_NO_OPTION, CommonMessageUtility.getJOptionPaneOptionType(result));
    }

    @Test
    public void testGetLocalizedMessage_LongParamTestMsg4() {
        System.out.println("getLocalizedMessage_LongParamTestMsg4");
        String msgCode = CommonMessage.TEST002;
        Object[] parms = {"one", 20000, "Averyverylongparamterthatwill"
            + "needtobebrokenattheappropriatepositionotherwiseitmaycause"
            + "someissueswithviewingthemessage"};
        LocalizedMessage result = CommonMessageUtility.getLocalizedMessage(msgCode, parms);
        assertNotNull(result);
        assertEquals(msgCode, result.getMessageCode());
        assertEquals("one parameter test message 20,000 and Averyverylongparamterthatwillneedtobebrokenatthe"
                + "appropriatepositionotherwiseitma" + System.getProperty("line.separator")
                + "ycausesomeissueswithviewingthemessage.", result.getMessage());
        assertNull(result.getAction());
        assertEquals("Question", result.getTypeDescription());
        assertEquals(LocalizedMessage.Type.QUESTION_YES_NO, result.getType());
        assertArrayEquals(new String[]{"Yes", "No"}, result.getDialogOptions());
        assertEquals("Question " + CommonMessage.TEST002.toUpperCase(), result.formatTitle());
        assertEquals(JOptionPane.QUESTION_MESSAGE, CommonMessageUtility.getJOptionPaneMessageType(result));
        assertEquals(JOptionPane.YES_NO_OPTION, CommonMessageUtility.getJOptionPaneOptionType(result));
    }

    /**
     * Tests the various displayMessage apis with a null messageResponder
     */
    @Test
    public void testDisplayMessage_SuppressedNoResponder() {
        System.out.println("displayMessage_SuppressedNoResponder");
        String msgCode = CommonMessage.TEST001;
        CommonMessageUtility.suppressDialog(null);

        int result = CommonMessageUtility.displayMessage(msgCode);
        assertEquals(0, result);

        result = CommonMessageUtility.displayMessage(msgCode, Arrays.asList("one", "2"));
        assertEquals(0, result);

        result = CommonMessageUtility.displayMessage(msgCode, new Object[]{"one", "2"});
        assertEquals(0, result);

        result = CommonMessageUtility.displayMessage(msgCode, "ERR_001");
        assertEquals(0, result);

        result = CommonMessageUtility.displayMessage(msgCode, "ERR_001", Arrays.asList("one", "2"));
        assertEquals(0, result);

        result = CommonMessageUtility.displayMessage(msgCode, "ERR_001", new Object[]{"one", "2"});
        assertEquals(0, result);

    }

    /**
     * Tests the various displayMessage apis with a messageResponder
     */
    @Test
    public void testDisplayMessage_SuppressedWithResponder() {
        System.out.println("displayMessage_SuppressedWithResponder");
        String msgCode = CommonMessage.TEST001;
        CommonMessageUtility.suppressDialog(new MessageResponder() {
            public int getResponse(LocalizedMessage msg, String errorNum, int defaultButton) {
                return 5;
            }
        });

        int result = CommonMessageUtility.displayMessage(msgCode);
        assertEquals(5, result);

        result = CommonMessageUtility.displayMessage(msgCode, Arrays.asList("one", "2"));
        assertEquals(5, result);

        result = CommonMessageUtility.displayMessage(msgCode, new Object[]{"one", "2"});
        assertEquals(5, result);

        result = CommonMessageUtility.displayMessage(msgCode, "ERR_001");
        assertEquals(5, result);

        result = CommonMessageUtility.displayMessage(msgCode, "ERR_001", Arrays.asList("one", "2"));
        assertEquals(5, result);

        result = CommonMessageUtility.displayMessage(msgCode, "ERR_001", new Object[]{"one", "2"});
        assertEquals(5, result);

    }

    @Test
    public void testGetLocalizedMessage_ServiceNullParamMsg1() {
        System.out.println("getLocalizedMessage_ServiceNullParamMsg1");
        String msgCode = CommonMessage.TEST002;
        Object[] parms = {"one", null, "some other"};
        LocalizedMessage result = CommonMessageUtility.getLocalizedMessage(msgCode, parms);
        assertNotNull(result);
        assertEquals(msgCode, result.getMessageCode());
        assertEquals("one parameter test message null and some other.", result.getMessage());
        assertNull(result.getAction());
        assertEquals("Question", result.getTypeDescription());
        assertEquals(LocalizedMessage.Type.QUESTION_YES_NO, result.getType());
        assertArrayEquals(new String[]{"Yes", "No"}, result.getDialogOptions());
        assertEquals("one parameter test message null and some other.", result.formatMessage(null));
        assertEquals("Question " + CommonMessage.TEST002.toUpperCase(), result.formatTitle());
        assertEquals(JOptionPane.QUESTION_MESSAGE, CommonMessageUtility.getJOptionPaneMessageType(result));
        assertEquals(JOptionPane.YES_NO_OPTION, CommonMessageUtility.getJOptionPaneOptionType(result));
    }
}
