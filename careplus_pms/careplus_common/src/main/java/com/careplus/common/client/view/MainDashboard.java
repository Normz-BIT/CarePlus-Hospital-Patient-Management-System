package com.careplus.common.client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.careplus.common.client.controller.LoginController;
import com.careplus.common.model.Person;

/**
 * MDI main window. Shows a menu bar of the features the logged-in user's role
 * may use each menu item opens that feature's internal frame re-focuses it if
 * it is already open.
 */
public class MainDashboard extends JFrame {

	private static final long serialVersionUID = 1L;

	/** Client property that tags an open internal frame with its feature label. */
	private static final String FEATURE_KEY = "careplus.feature";

	private static Person currentUser;
	private final List<DashboardFeature> features;
	private final JDesktopPane desktopPane = new JDesktopPane();

	private static final Logger logger = LogManager.getLogger(MainDashboard.class);

	public MainDashboard(Person currentUser) {
		this(currentUser, List.of());
	}

	public MainDashboard(Person currentUser, List<DashboardFeature> features) {

		MainDashboard.currentUser = currentUser;
		this.features = features;

		setTitle(currentUser == null ? "CarePlus Hospital Management System"
				: "CarePlus - " + currentUser.getFullName());

		desktopPane.setBackground(new Color(225, 238, 250));
		desktopPane.setToolTipText("CarePlus workspace. Select a feature from the menu bar.");

		setLayout(new BorderLayout());
		add(desktopPane, BorderLayout.CENTER);

		setJMenuBar(buildMenuBar());

		setSize(1300, 750);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		if (currentUser != null) {
			logger.info(
					"Main dashboard opened for user ID: {} with role: {}",
					currentUser.getPersonId(),
					currentUser.getRole());
		}
	}

	public static Person getCurrentUser() {

		return currentUser;

	}

	/** System menu plus one menu per feature group the current role may see. */
	private JMenuBar buildMenuBar() {

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(buildSystemMenu());

		Map<String, JMenu> menusByGroup = new LinkedHashMap<>();

		for (DashboardFeature feature : features) {
			if (currentUser != null && !feature.visibleFor(currentUser.getRole())) {
				continue;
			}

			JMenu menu = menusByGroup.computeIfAbsent(feature.getMenu(), menuName -> {

				JMenu featureMenu = new JMenu(menuName);
				setMnemonicFromText(featureMenu, menuName);
				featureMenu.setToolTipText("Open " + menuName + " features.");

				return featureMenu;
			});

			JMenuItem item = new JMenuItem(feature.getLabel());
			setMnemonicFromText(item, feature.getLabel());
			item.setToolTipText("Open " + feature.getLabel() + ".");
			item.addActionListener(e -> openFeature(feature));

			menu.add(item);
		}

		menusByGroup.values().forEach(menuBar::add);

		return menuBar;
	}

	private JMenu buildSystemMenu() {

		JMenuItem logout = new JMenuItem("Logout");
		logout.setMnemonic(KeyEvent.VK_L);
		logout.setAccelerator(
				KeyStroke.getKeyStroke(
						KeyEvent.VK_L,
						InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
		logout.setToolTipText("Log out of CarePlus. Shortcut: Ctrl+Shift+L.");

		logout.addActionListener(e -> {

			if (currentUser != null) {
				logger.info("User ID: {} logged out", currentUser.getPersonId());
			}

			currentUser = null;
			dispose();

			Login login = new Login();
			new LoginController(login, features);
			login.setVisible(true);
		});

		JMenuItem exit = new JMenuItem("Exit");
		exit.setMnemonic(KeyEvent.VK_X);
		exit.setAccelerator(
				KeyStroke.getKeyStroke(
						KeyEvent.VK_Q,
						InputEvent.CTRL_DOWN_MASK));
		exit.setToolTipText("Exit CarePlus. Shortcut: Ctrl+Q.");

		exit.addActionListener(e -> {

			logger.info("CarePlus application closed");
			System.exit(0);
		});

		JMenu menuSystem = new JMenu("System");
		menuSystem.setMnemonic(KeyEvent.VK_S);
		menuSystem.setToolTipText("CarePlus system options.");

		menuSystem.add(logout);
		menuSystem.addSeparator();
		menuSystem.add(exit);

		return menuSystem;
	}

	/*
	 * Assign the first available character as the component mnemonic.
	 */
	private void setMnemonicFromText(AbstractButton component, String text) {

		if (text == null || text.isEmpty()) {

			return;
		}

		for (char character : text.toCharArray()) {
			if (Character.isLetterOrDigit(character)) {

				component.setMnemonic(Character.toUpperCase(character));
				return;
			}
		}
	}

	/** Opens the feature's frame, or focuses it if it is already open. */
	private void openFeature(DashboardFeature feature) {

		JInternalFrame open = findOpenFrame(feature.getLabel());

		if (open != null) {

			logger.info("Existing feature selected: {}", feature.getLabel());
			focus(open);
			return;
		}

		try {

			JInternalFrame view = feature.createView();
			view.putClientProperty(FEATURE_KEY, feature.getLabel());
			desktopPane.add(view);
			focus(view);

			logger.info("Feature opened: {}", feature.getLabel());

		} catch (Exception ex) {

			logger.error(
					"Feature could not be opened: " + feature.getLabel(),
					ex);
		}
	}

	/** The open internal frame for this feature label, or null if none. */
	private JInternalFrame findOpenFrame(String featureLabel) {
		for (JInternalFrame frame : desktopPane.getAllFrames()) {
			if (featureLabel.equals(frame.getClientProperty(FEATURE_KEY))) {
				return frame;
			}
		}
		return null;
	}

	/** Makes a frame visible, restored, selected and on top. */
	private void focus(JInternalFrame frame) {
		try {

			frame.setMaximum(true);
			frame.setVisible(true);
			frame.setIcon(false);
			frame.setSelected(true);
			frame.toFront();

		} catch (Exception ex) {

			logger.error(
					"Internal frame could not be focused: " + frame.getTitle(),
					ex);
		}
	}
}
