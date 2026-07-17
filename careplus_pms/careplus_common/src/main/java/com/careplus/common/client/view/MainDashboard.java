package com.careplus.common.client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

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

	public MainDashboard(Person currentUser) {
		this(currentUser, List.of());
	}

	public MainDashboard(Person currentUser, List<DashboardFeature> features) {

		MainDashboard.currentUser = currentUser;
		this.features = features;

		setTitle(currentUser == null ? "CarePlus Hospital Management System"
				: "CarePlus - " + currentUser.getFullName());

		desktopPane.setBackground(new Color(225, 238, 250));
		setLayout(new BorderLayout());
		add(desktopPane, BorderLayout.CENTER);

		setJMenuBar(buildMenuBar());

		setSize(1300, 750);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
			JMenu menu = menusByGroup.computeIfAbsent(feature.getMenu(), JMenu::new);
			JMenuItem item = new JMenuItem(feature.getLabel());
			item.addActionListener(e -> openFeature(feature));
			menu.add(item);
		}

		menusByGroup.values().forEach(menuBar::add);

		return menuBar;
	}

	private JMenu buildSystemMenu() {

		JMenuItem logout = new JMenuItem("Logout");
		logout.addActionListener(e -> {
			dispose();
			Login login = new Login();
			new LoginController(login, features);
			login.setVisible(true);
		});

		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(e -> System.exit(0));

		JMenu menuSystem = new JMenu("System");
		menuSystem.add(logout);
		menuSystem.addSeparator();
		menuSystem.add(exit);
		return menuSystem;
	}

	/** Opens the feature's frame, or focuses it if it is already open. */
	private void openFeature(DashboardFeature feature) {

		JInternalFrame open = findOpenFrame(feature.getLabel());
		if (open != null) {
			focus(open);
			return;
		}

		JInternalFrame view = feature.createView();
		view.putClientProperty(FEATURE_KEY, feature.getLabel());
		desktopPane.add(view);
		focus(view);
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

		}
	}
}
