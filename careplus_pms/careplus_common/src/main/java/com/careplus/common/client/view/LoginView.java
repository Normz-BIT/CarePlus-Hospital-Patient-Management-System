package com.careplus.common.client.view;

import com.careplus.common.client.controller.LoginController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

/*
 * Login View
 * Allows patients and employees to access the CarePlus system
 *
 * A JFrame rather than a JInternalFrame, unlike every feature screen, because it
 * exists before the MDI dashboard does. On success LoginController opens the
 * dashboard and disposes this window, so the two are never on screen together.
 *
 * One login form serves both patients and staff. Nothing here distinguishes them:
 * the server returns the concrete Person subclass and its role decides which
 * dashboard features appear.
 */
public class LoginView extends JFrame {

	private static final long serialVersionUID = 1L;

	// Labels
	private JLabel lblTitle;
	private JLabel lblId;
	private JLabel lblPassword;

	// Fields
	private JTextField txtId;
	private JPasswordField txtPassword;

	// Buttons
	private JButton btnLogin;
	private JButton btnClear;
	private JButton btnExit;
	
	//icons
	List<Image> icons;

	public LoginView() {

		super("CarePlus Hospital Login");

		initializeComponents();
		buildGUI();
		registerEvents();
		configureKeyboardShortcuts();

		setSize(500, 320);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	/*
	 * Initialize Login Components
	 */
	private void initializeComponents() {

		lblTitle = new JLabel("CarePlus Hospital Management System");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));

		lblId = new JLabel("ID");
		lblPassword = new JLabel("Password");

		txtId = new JTextField(20);
		txtPassword = new JPasswordField(20);

		lblId.setDisplayedMnemonic(KeyEvent.VK_I);
		lblId.setLabelFor(txtId);

		lblPassword.setDisplayedMnemonic(KeyEvent.VK_P);
		lblPassword.setLabelFor(txtPassword);

		txtId.setToolTipText("Enter your patient or employee ID. Shortcut: Alt+I.");
		txtPassword.setToolTipText("Enter your account password. Shortcut: Alt+P.");

		btnLogin = new JButton("Login");
		btnClear = new JButton("Clear");
		btnExit = new JButton("Exit");

		btnLogin.setMnemonic(KeyEvent.VK_L);
		btnClear.setMnemonic(KeyEvent.VK_C);
		btnExit.setMnemonic(KeyEvent.VK_X);

		btnLogin.setToolTipText("Sign in to CarePlus. Shortcut: Alt+L or Enter.");
		btnClear.setToolTipText("Clear the login fields. Shortcut: Alt+C or Escape.");
		btnExit.setToolTipText("Exit the application. Shortcut: Alt+X or Ctrl+Q.");

		btnLogin.setPreferredSize(new Dimension(100, 35));
		btnClear.setPreferredSize(new Dimension(100, 35));
		btnExit.setPreferredSize(new Dimension(100, 35));

	}

	/*
	 * Build Login Interface
	 */
	private void buildGUI() {

		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;

		formPanel.add(lblTitle, gbc);

		gbc.gridwidth = 1;

		gbc.gridx = 0;
		gbc.gridy = 1;
		formPanel.add(lblId, gbc);

		gbc.gridx = 1;
		formPanel.add(txtId, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		formPanel.add(lblPassword, gbc);

		gbc.gridx = 1;
		formPanel.add(txtPassword, gbc);

		JPanel buttonPanel = new JPanel();

		buttonPanel.add(btnLogin);
		buttonPanel.add(btnClear);
		buttonPanel.add(btnExit);

		add(formPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);

		getContentPane().setBackground(new Color(240, 248, 255));

		setWindowIcon();

	}

	/*
	 * Register Login Events
	 *
	 * Only the purely presentational controls are wired here. Anything that talks
	 * to the server goes through registerActionListener below, which keeps this
	 * view free of any dependency on the controller for local-only behaviour.
	 */
	private void registerEvents() {

		btnClear.addActionListener(e -> {

			txtId.setText("");
			txtPassword.setText("");
			/*
			 * Focus is returned to the ID field so the user can retype immediately after
			 * clearing, rather than having to click back into the form.
			 */
			txtId.requestFocusInWindow();

		});

		/*
		 * Exits the JVM outright rather than disposing the window, since abandoning
		 * login means there is nothing left to return to.
		 */
		btnExit.addActionListener(e -> System.exit(0));

	}

	/*
	 * Hooks this view's buttons up to the controller.
	 *
	 * ClientApp calls this after construction rather than us doing it in the
	 * constructor, because the two need each other: the controller needs the view
	 * to read the form, and the view needs the controller to handle the click.
	 */
	public void registerActionListener(LoginController controller) {

		btnLogin.addActionListener(e -> controller.login());

	}

	/*
	 * Configure Keyboard Shortcuts
	 */
	private void configureKeyboardShortcuts() {

		getRootPane().setDefaultButton(btnLogin);

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				"clear");

		getRootPane().getActionMap().put("clear", new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				btnClear.doClick();
			}
		});

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK), "exit");

		getRootPane().getActionMap().put("exit", new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				btnExit.doClick();
			}
		});

	}

	private void setWindowIcon() {

		icons = new ArrayList<>();

		// Load multiple resolutions of the logo
		icons.add(new ImageIcon(getClass().getResource("/CarePlusLogo64.png")).getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH));
		icons.add(new ImageIcon(getClass().getResource("/CarePlusLogo32.png")).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH));
		icons.add(new ImageIcon(getClass().getResource("/CarePlusLogo128.png")).getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH));

		// Apply the list to the window
		this.setIconImages(icons);

	}
	
	
	public List<Image> getWindowIconList() {
		
		return icons;
	}

	public JTextField getTxtId() {
		return txtId;
	}

	public JPasswordField getTxtPassword() {
		return txtPassword;
	}

	public void showMessage(String message) {
		JOptionPane.showMessageDialog(this, message);
	}
}
