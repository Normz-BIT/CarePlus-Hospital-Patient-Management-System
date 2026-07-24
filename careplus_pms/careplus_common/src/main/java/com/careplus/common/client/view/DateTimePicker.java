package com.careplus.common.client.view;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDateTime;
import java.time.YearMonth;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

/*
 * A little row of spinners for picking a date: Day, Month, Year, and optionally
 * Hour and Min, each with its own label above it.
 *
 * This replaced the typed date boxes we used to have. Those were three different
 * formats on three different screens (yyyy-MM-dd here, yyyy-MM-dd HH:mm:ss
 * there) and every one of them threw a parse error if you typed it even slightly
 * wrong. With spinners there's no format to remember and no way to type it
 * wrong, so all that parsing and the error messages that went with it are gone.
 *
 * It lives in careplus_common so the patient and employee screens share the one
 * copy instead of each building their own.
 */
public class DateTimePicker extends JPanel {

	private static final long serialVersionUID = 1L;

	/*
	 * Months as names rather than numbers, because "Jun" can't be misread the way
	 * 06 can. Index 0 is January, matching what LocalDateTime wants minus one.
	 */
	private static final String[] MONTHS = {
			"Jan", "Feb", "Mar", "Apr", "May", "Jun",
			"Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

	private final JSpinner spnDay;
	private final JSpinner spnMonth;
	private final JSpinner spnYear;
	private final JSpinner spnHour;
	private final JSpinner spnMinute;

	/* Whether this picker shows the Hour and Min spinners at all. */
	private final boolean withTime;

	/*
	 * Pass false for withTime on anything backed by a DATE column. The follow-up
	 * date on the diagnosis screen is the one that matters: its column can't store
	 * a time, so offering hour and min there would let a doctor pick 14:30 and
	 * then quietly throw it away.
	 */
	public DateTimePicker(boolean withTime) {

		this.withTime = withTime;

		LocalDateTime now = LocalDateTime.now();

		spnDay = new JSpinner(new SpinnerNumberModel(now.getDayOfMonth(), 1, 31, 1));
		spnMonth = new JSpinner(new javax.swing.SpinnerListModel(MONTHS));
		/*
		 * Year range is wide enough for a date of birth at one end and a follow-up a
		 * few years out at the other.
		 */
		spnYear = new JSpinner(new SpinnerNumberModel(now.getYear(), 1900, 2100, 1));
		spnHour = new JSpinner(new SpinnerNumberModel(now.getHour(), 0, 23, 1));
		spnMinute = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));

		spnMonth.setValue(MONTHS[now.getMonthValue() - 1]);

		/*
		 * Without this the year spinner renders as "2,026" with a thousands separator,
		 * which looks daft on a year.
		 */
		spnYear.setEditor(new JSpinner.NumberEditor(spnYear, "0000"));

		buildGUI();
	}

	private void buildGUI() {

		setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 0, 6);
		gbc.anchor = GridBagConstraints.WEST;

		addField(gbc, 0, "Day", spnDay, 3);
		addField(gbc, 1, "Month", spnMonth, 4);
		addField(gbc, 2, "Year", spnYear, 5);

		if (withTime) {
			addField(gbc, 3, "Hour", spnHour, 3);
			addField(gbc, 4, "Min", spnMinute, 3);
		}
	}

	/*
	 * Each part gets its own small label sitting above its spinner, so the user
	 * reads "Day Month Year Hour Min" straight across without needing a format
	 * hint anywhere.
	 */
	private void addField(GridBagConstraints gbc, int column, String caption, JSpinner spinner, int columns) {

		JLabel label = new JLabel(caption, SwingConstants.CENTER);
		label.setFont(label.getFont().deriveFont(Font.PLAIN, 10f));
		label.setLabelFor(spinner);

		((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setColumns(columns);

		spinner.setToolTipText("Select the " + caption.toLowerCase() + ".");

		gbc.gridx = column;

		gbc.gridy = 0;
		add(label, gbc);

		gbc.gridy = 1;
		add(spinner, gbc);
	}

	/*
	 * The value currently showing.
	 *
	 * The day is clamped to what the chosen month actually has, so picking 31 and
	 * then switching to February gives you the 28th (or 29th in a leap year)
	 * instead of throwing. Letting the spinner offer 31 for every month and fixing
	 * it up here is simpler than rewiring the day spinner's range every time the
	 * month or year changes.
	 */
	public LocalDateTime getDateTime() {

		int year = (Integer) spnYear.getValue();
		int month = indexOfMonth((String) spnMonth.getValue()) + 1;

		int day = Math.min((Integer) spnDay.getValue(), YearMonth.of(year, month).lengthOfMonth());

		int hour = withTime ? (Integer) spnHour.getValue() : 0;
		int minute = withTime ? (Integer) spnMinute.getValue() : 0;

		return LocalDateTime.of(year, month, day, hour, minute);
	}

	/* Puts an existing value into the spinners, for editing something already saved. */
	public void setDateTime(LocalDateTime value) {

		if (value == null) {
			return;
		}

		spnDay.setValue(value.getDayOfMonth());
		spnMonth.setValue(MONTHS[value.getMonthValue() - 1]);
		spnYear.setValue(value.getYear());
		spnHour.setValue(value.getHour());
		spnMinute.setValue(value.getMinute());
	}

	/* Back to today, used by the Clear buttons. */
	public void reset() {

		setDateTime(LocalDateTime.now().withMinute(0));
	}

	private int indexOfMonth(String name) {

		for (int i = 0; i < MONTHS.length; i++) {

			if (MONTHS[i].equals(name)) {
				return i;
			}
		}

		// Can't happen, the spinner only ever holds one of our own values.
		return 0;
	}
}
