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
package org.flossola.common.utilities;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.util.prefs.Preferences;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

/**
 * Provides methods to work with Windows.
 */
public class WindowUtility {

    private static Class<?> mainAppClass;

    /**
     * Retrieves the preference package based on the specified preferenceClass
     */
    public static Preferences getUserPreferences() {
        return Preferences.userNodeForPackage(mainAppClass);
    }

    public static boolean hasUserPreferences() {
        return mainAppClass != null;
    }

    public static Class<?> getMainAppClass() {
        return mainAppClass;
    }

    public static void setMainAppClass(Class<?> mainAppClass) {
        WindowUtility.mainAppClass = mainAppClass;
    }

    /**
     * Returns the top visible frame.
     */
    public static Frame getTopFrame() {
        Frame[] frames = Frame.getFrames();
        for (int i = 0; i < frames.length; i++) {
            if (frames[i].getFocusOwner() != null) {
                return frames[i];
            }
        }
        if (frames.length > 0) {
            return frames[0];
        }
        return null;
    }

    /**
     * Finds opened frame by the class type.
     *
     * @param formClass The class of the frame to find.
     */
    public static Frame findOpenedFrameByClassName(Class formClass) {
        Frame[] frames = Frame.getFrames();
        for (int i = 0; i < frames.length; i++) {
            if (frames[i].getClass().getName().equals(formClass.getName())) {
                return frames[i];
            }
        }
        return null;
    }

    /**
     * Positions form at the center of screen.
     *
     * @param form Window object to position
     */
    public static void centerForm(Window form) {
        if (form != null) {
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            int x = ((dim.width) / 2);
            int y = ((dim.height) / 2);
            form.setLocation(x - (form.getWidth() / 2), y - (form.getHeight() / 2));
        }
    }

    /**
     * Commits changes on such fields as editable combobox and formatted text
     * fields
     *
     * @param c Topmost container to start searching for components.
     */
    public static void commitChanges(Container c) {
        for (Component co : c.getComponents()) {
            if (Container.class.isAssignableFrom(co.getClass())) {
                commitChanges((Container) co);
            }

            // Editable combobox
            if (JComboBox.class.isAssignableFrom(co.getClass())) {
                JComboBox cbx = (JComboBox) co;
                if (cbx.isEditable() && cbx.isEnabled() && cbx.getEditor().getEditorComponent().hasFocus()) {
                    cbx.setSelectedItem(cbx.getEditor().getItem());
                }
            }

            // Formatted text field
            if (JFormattedTextField.class.isAssignableFrom(co.getClass())) {
                JFormattedTextField fmtFiled = (JFormattedTextField) co;
                if (fmtFiled.isEditable() && fmtFiled.isEnabled() && fmtFiled.hasFocus()) {
                    try {
                        fmtFiled.commitEdit();
                    } catch (ParseException ex) {
                    }
                }
            }
        }
    }

    /**
     * Adds a listener to the dialog to close the dialog if the user presses the
     * escape key.
     *
     * @param dialog
     * @param hide If true, the dialog will be hidden rather than disposed of.
     */
    public static void addEscapeListener(final JDialog dialog, final boolean hide) {
        ActionListener escListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (hide) {
                    // Hide the dialog
                    dialog.setVisible(false);
                } else {
                    // Dispose of the dialog. 
                    dialog.dispose();
                }
            }
        };
        dialog.getRootPane().registerKeyboardAction(escListener,
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
    }
	
	    /**
     * Obtains the title bar icon configured for this SOLA application based on
     * the LAF theme.
     *
     * @return
     */
    public static Icon getTitleBarIcon() {
        Icon result = UIManager.getIcon("solaTitleBarIcon");
        if (result == null) {
            result = new ImageIcon();
        }
        return result;
    }

    /**
     * Obtains the title bar image configured for this SOLA application based on
     * the LAF theme.
     *
     * @return
     */
    public static Image getTitleBarImage() {
        return ((ImageIcon) getTitleBarIcon()).getImage();
    }

    /**
     * Formats the frame title by moving it 8 spaces to the left to account for
     * the sola image icon.
     *
     * @param title Title of the dialog
     * @return
     */
    public static String formatFrameTitle(String title) {
        String pre = "";
        if (UIManager.getBoolean("solaLAF")) {
            pre = String.format("%" + 8 + "s", pre);
        }
        return pre + title;
    }
}
