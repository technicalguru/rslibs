/*
 * This file is part of RS Library (Base Library).
 *
 *  RS Library is free software: you can redistribute it 
 *  and/or modify it under the terms of version 3 of the GNU 
 *  Lesser General Public  License as published by the Free Software 
 *  Foundation.
 *  
 *  RS Library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public 
 *  License along with RS Library.  If not, see 
 *  <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package rs.baselib.security;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;

import rs.baselib.configuration.IConfigurable;

/**
 * Implements authorization information retrieval from pop-up dialog.
 * This implementation prompts the user for name and password.
 * <p>
 * Configuration:
 * </p>
 * <pre>
 * &lt;AuthorizationCallback class="b4j.core.session.GuiAuthorizationCallback"&gt;
 * 
 *    &lt;!-- The default user name (optional). --&gt;
 *    &lt;login&gt;default-user-name&lt;/login&gt;
 *    
 *    &lt;!-- The label to be used for user name text field (optional). --&gt;
 *    &lt;login-label&gt;a-login-label&lt;/login-label&gt;
 *    
 *    &lt;!-- The label to be used for password field (optional). --&gt;
 *    &lt;password-label&gt;a-password-label&lt;/password-label&gt;
 *    
 *    &lt;!-- The text to be shown explaining what to do (optional). --&gt;
 *    &lt;prompt&gt;a-short-sentence&lt;/prompt&gt;
 *    
 *    &lt;!-- The title of the dialog (optional). --&gt;
 *    &lt;title&gt;a-title&lt;/title&gt;
 *    
 * &lt;/AuthorizationCallback&gt;
 * </pre>
 * @author Ralph Schuster
 *
 */
public class GuiAuthorizationCallback extends AbstractAuthorizationCallback implements IConfigurable {

	/** The default label for the login name ("Login:"). */
	public static final String DEFAULT_LOGIN_LABEL = "Login:"; 
	/** The default label for the password ("Password:"). */
	public static final String DEFAULT_PASSWORD_LABEL = "Password:"; 
	/** The default prompt text ("Please enter your Bugzilla login data!"). */
	public static final String DEFAULT_PROMPT = "Please enter your Bugzilla login data!"; 
	/** The default popup title ("Bugzilla Login"). */
	public static final String DEFAULT_TITLE = "Bugzilla Login"; 

	private String loginLabel;
	private String passwordLabel;
	private String prompt;
	private String title;
	
	/**
	 * Default Constructor.
	 */
	public GuiAuthorizationCallback() {
	}

	/**
	 * Configures the name callback.
	 * Configuration can contain three elements:
	 * <ul>
	 * <li>&lt;login&gt; - the default user name</li>
	 * <li>&lt;login-label&gt; - the label text for login name</li>
	 * <li>&lt;password-label&gt; - the label text for password</li>
	 * <li>&lt;prompt&gt; - the starting text for the popup</li>
	 * <li>&lt;title&gt; - the titel for the popup</li>
	 * </ul>
	 * @param config - configuration object
	 * @throws ConfigurationException - when configuration fails
	 */
	@Override
	public void configure(Configuration config) throws ConfigurationException {
		setName(config.getString("login"));
		setLoginLabel(config.getString("login-label"));
		setPasswordLabel(config.getString("password-label"));
		setPrompt(config.getString("prompt"));
		setTitle(config.getString("title"));
	}

	/**
	 * Returns the login label text.
	 * @return the login label text.
	 */
	public String getLoginLabel() {
		return loginLabel;
	}

	/**
	 * Sets a text for the login label. 
	 * @param loginLabel - the login label text to set
	 */
	public void setLoginLabel(String loginLabel) {
		if (loginLabel == null) loginLabel = DEFAULT_LOGIN_LABEL;
		this.loginLabel = loginLabel;
	}

	/**
	 * Returns the password label text.
	 * @return the password label text
	 */
	public String getPasswordLabel() {
		return passwordLabel;
	}

	/**
	 * Sets a text for the password label.
	 * @param passwordLabel - the password label text to set
	 */
	public void setPasswordLabel(String passwordLabel) {
		if (passwordLabel == null) passwordLabel = DEFAULT_PASSWORD_LABEL;
		this.passwordLabel = passwordLabel;
	}

	/**
	 * Returns the prompt text.
	 * @return the prompt text
	 */
	public String getPrompt() {
		return prompt;
	}

	/**
	 * Sets a text for the prompt.
	 * @param prompt - the prompt text to set
	 */
	public void setPrompt(String prompt) {
		if (prompt == null) prompt = DEFAULT_PROMPT;
		this.prompt = prompt;
	}

	/**
	 * Returns the popup title.
	 * @return the popup title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets a text for the popup title.
	 * @param title - the popup title to set
	 */
	public void setTitle(String title) {
		if (title == null) title = DEFAULT_TITLE;
		this.title = title;
	}

	/**
	 * Shows the popup if not done before and asks the user for credentials.
	 * @return input from user.
	 */
	@Override
	public String getName() {
		if (super.getPassword() == null) showPopup();
		
		return super.getName();
	}

	/**
	 * Shows the popup if not done before and asks the user for credentials.
	 * @return password from user
	 */
	@Override
	public String getPassword() {
		if (super.getPassword() == null) showPopup();

		return super.getPassword();
	}

	/**
	 * Shows the popup and asks for credentials.
	 * The result of the input will be saved.
	 */
	protected void showPopup() {
		// Create the dialog
		loginField = new JTextField(super.getName());
		JPasswordField passwordField = new JPasswordField();
		JLabel generalPrompt = new JLabel(getPrompt());
		JLabel loginLabel = new JLabel(getLoginLabel());
		JLabel passwordLabel = new JLabel(getPasswordLabel());
		okButton = new JButton("OK");
		cancelButton = new JButton("Cancel");
		
		// Creating the panel
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		// Adding all components
		GridBagConstraints gb = new GridBagConstraints();
		gb.gridwidth = GridBagConstraints.REMAINDER;
		gb.weightx = 1;
		gb.insets = new Insets(10,10,10,10);
		panel.add(generalPrompt, gb);
		
		gb = new GridBagConstraints();
		gb.insets = new Insets(10,10,10,10);
		gb.anchor = GridBagConstraints.WEST;
		panel.add(loginLabel, gb);
		
		gb = new GridBagConstraints();
		gb.gridwidth = GridBagConstraints.REMAINDER;
		gb.weightx = 1;
		gb.fill = GridBagConstraints.HORIZONTAL;
		gb.insets = new Insets(10,10,10,10);
		panel.add(loginField, gb);
		
		gb = new GridBagConstraints();
		gb.anchor = GridBagConstraints.WEST;
		gb.insets = new Insets(10,10,10,10);
		panel.add(passwordLabel, gb);
		
		gb = new GridBagConstraints();
		gb.gridwidth = GridBagConstraints.REMAINDER;
		gb.weightx = 1;
		gb.fill = GridBagConstraints.HORIZONTAL;
		gb.insets = new Insets(10,10,10,10);
		panel.add(passwordField, gb);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());
		gb = new GridBagConstraints();
		gb.anchor = GridBagConstraints.EAST;
		gb.insets = new Insets(10,10,10,10);
		buttonPanel.add(okButton, gb);
		gb = new GridBagConstraints();
		gb.anchor = GridBagConstraints.WEST;
		gb.insets = new Insets(10,10,10,10);
		buttonPanel.add(cancelButton, gb);
		gb = new GridBagConstraints();
		gb.gridwidth = GridBagConstraints.REMAINDER;
		gb.gridheight = GridBagConstraints.REMAINDER;
		gb.weightx = 1;
		gb.weighty = 1;
		gb.fill = GridBagConstraints.BOTH;
		gb.insets = new Insets(10,10,10,10);
		panel.add(buttonPanel, gb);
		
		// Creating the dialog
		dlg = new JDialog((JFrame)null, getTitle(), true);
		dlg.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		dlg.getContentPane().add(panel, BorderLayout.CENTER);
		dlg.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				dlgClosedOption = false;
				dlg.dispose();
			}
			public void windowOpened(WindowEvent evt) {
				loginField.requestFocus();
			}
		});
		
		// Catching the buttons
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				dlgClosedOption = true;
				dlg.dispose();
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				dlgClosedOption = false;
				dlg.dispose();
			}
		});
		
		// Showing it
		dlg.getRootPane().setDefaultButton(okButton);
		dlgClosedOption = false;
		dlg.pack();
		dlg.setVisible(true);

		// Evaluating result
		if (!dlgClosedOption) {
			throw new IllegalStateException("User aborted action");
		}
		setName(loginField.getText());
		setPassword(new String(passwordField.getPassword()));
	}
	
	private boolean dlgClosedOption;
	private JDialog dlg;
	private JTextField loginField;
	private JButton okButton;
	private JButton cancelButton;
}
