package com.careplus.client.patient.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

import com.careplus.client.patient.controller.PaymentController;

/**
 * Patient payment workspace: record a payment and review payment history.
 *
 * A JInternalFrame rather than a JFrame because every feature screen is a child
 * window of the MDI desktop owned by MainDashboard.
 *
 * Follows the project's view convention: the view owns the Swing widgets and
 * exposes them plus small helpers such as clearTable and addPayment, while all
 * decisions and server calls live in PaymentController. The two are joined by
 * registerActionListener after both exist.
 */
public class PaymentView extends JInternalFrame {

	private static final long serialVersionUID = 1L;

	// Labels
	private JLabel lblTitle;
	private JLabel lblAmount;
	private JLabel lblDescription;

	// Fields
	private JTextField txtAmount;
	private JTextArea txtDescription;

	// Buttons
	private JButton btnPay;
	private JButton btnRefresh;
	private JButton btnClear;

	// Table
	private JTable tblPayments;
	private DefaultTableModel tableModel;

	public PaymentView() {

		/*
		 * The four flags are resizable, closable, maximizable and iconifiable, all
		 * enabled so the frame behaves like a full window inside the MDI desktop.
		 */
		super("Payments", true, true, true, true);

		initializeComponents();
		buildGUI();
		configureKeyboardShortcuts();

		setSize(900, 600);
		/*
		 * Made visible here rather than by the caller, because a JInternalFrame is
		 * hidden by default and would otherwise be added to the desktop pane without
		 * ever appearing.
		 */
		setVisible(true);

	}

	private void initializeComponents() {

		lblTitle = new JLabel("Payment Management");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));

		lblAmount = new JLabel("Amount");
		lblDescription = new JLabel("Description");

		txtAmount = new JTextField(20);
		txtDescription = new JTextArea(3,20);
		txtDescription.setLineWrap(true);
		txtDescription.setWrapStyleWord(true);

		/*
		 * setLabelFor is what makes the mnemonic actually work: pressing Alt+A moves
		 * focus to the field rather than to the label, which cannot hold focus itself.
		 * It also lets screen readers announce the label when the field is focused.
		 */
		lblAmount.setDisplayedMnemonic(KeyEvent.VK_A);
		lblAmount.setLabelFor(txtAmount);

		lblDescription.setDisplayedMnemonic(KeyEvent.VK_D);
		lblDescription.setLabelFor(txtDescription);

		txtAmount.setToolTipText("Enter the amount to pay. Shortcut: Alt+A.");
		txtDescription.setToolTipText("Enter what the payment is for. Shortcut: Alt+D.");

		btnPay = new JButton("Make Payment");
		btnRefresh = new JButton("Refresh");
		btnClear = new JButton("Clear");

		btnPay.setMnemonic(KeyEvent.VK_P);
		btnRefresh.setMnemonic(KeyEvent.VK_R);
		btnClear.setMnemonic(KeyEvent.VK_C);

		btnPay.setToolTipText("Submit the payment. Shortcut: Alt+P or Ctrl+Enter.");
		btnRefresh.setToolTipText("Reload payment records. Shortcut: Alt+R or F5.");
		btnClear.setToolTipText("Clear the payment fields. Shortcut: Alt+C or Escape.");

		tableModel = new DefaultTableModel();

		/*
		 * Column order here is a contract with PaymentController: it builds each row as
		 * an untyped Object array in exactly this sequence. Reordering these headers
		 * without reordering the row construction there misaligns every value, and
		 * nothing will fail to compile.
		 */
		tableModel.setColumnIdentifiers(new Object[] { "Payment ID", "Amount Paid", "Outstanding Balance", "Description", "Date" });

		/*
		 * DefaultTableModel reports every cell as editable, so this history table can be
		 * typed into even though it is meant to be read only. Edits go nowhere: they
		 * change the model in memory, are never sent to the server, and vanish on the
		 * next refresh. Overriding isCellEditable to return false would make the
		 * intent explicit.
		 */
		tblPayments = new JTable(tableModel);
		tblPayments.setRowHeight(25);

	}

	private void buildGUI() {

		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		JPanel formPanel = new JPanel(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.insets = new Insets(6, 6, 6, 6);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;

		formPanel.add(lblTitle, gbc);

		gbc.gridwidth = 1;

		gbc.gridx = 0;
		gbc.gridy = 1;
		formPanel.add(lblAmount, gbc);

		gbc.gridx = 1;
		formPanel.add(txtAmount, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		
		formPanel.add(lblDescription, gbc);

		gbc.gridx = 1;
		gbc.gridheight = 3;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 1.0;		
		formPanel.add(txtDescription, gbc);
		
		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(btnPay);
		buttonPanel.add(btnRefresh);
		buttonPanel.add(btnClear);

		mainPanel.add(formPanel, BorderLayout.NORTH);
		mainPanel.add(new JScrollPane(tblPayments), BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		add(mainPanel);

	}

	private void configureKeyboardShortcuts() {

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK), "pay");

		getRootPane().getActionMap().put("pay", new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				btnPay.doClick();
			}
		});

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "refresh");

		getRootPane().getActionMap().put("refresh", new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				btnRefresh.doClick();
			}
		});

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "clear");

		getRootPane().getActionMap().put("clear", new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				btnClear.doClick();
			}
		});

	}

	/*
	 * setRowCount(0) discards every row in one call, which is cheaper than removing
	 * them individually and fires a single table change event rather than one per
	 * row. Paired with addPayment below it gives the clear and rebuild refresh the
	 * controllers rely on.
	 */
	public void clearTable() {
		tableModel.setRowCount(0);
	}

	public void addPayment(Object[] row) {
		tableModel.addRow(row);
	}

	/*
	 * Attaches this view's controls to the controller that handles them.
	 *
	 * Called by ClientApp after both objects exist, which is what breaks the
	 * circular dependency between them: the controller needs the view to read its
	 * inputs, and the view needs the controller to handle its buttons.
	 *
	 * Note Clear is wired to this view's own method rather than to the controller,
	 * because emptying the form is presentation only and needs no server round trip.
	 */
	public void registerActionListener(PaymentController controller) {

		btnPay.addActionListener(e -> controller.pay());
		btnRefresh.addActionListener(e -> controller.refresh());
		btnClear.addActionListener(e -> clearFields());

	}

	public void clearFields() {

		txtAmount.setText("");
		txtDescription.setText("");

	}

	public void showMessage(String message) {
		JOptionPane.showMessageDialog(this, message);
	}

	public JTextField getTxtAmount() {
		return txtAmount;
	}


	public JTextArea getTxtDescription() {
		return txtDescription;
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}


}
