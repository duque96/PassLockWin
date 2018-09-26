package com.passlock.igu;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jvnet.substance.SubstanceLookAndFeel;

import com.passlock.async.AsyncGetAccounts;

public class AccountsList extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JScrollPane scrollPane;
	private JList<String> accountsList;

	// Local attributes
	private List<String> accounts;
	private Map<String, List<String>> map;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrame.setDefaultLookAndFeelDecorated(true);
					JDialog.setDefaultLookAndFeelDecorated(true);
					AccountsList frame = new AccountsList();
					frame.setVisible(true);
					SubstanceLookAndFeel.setSkin("org.jvnet.substance.skin.DustSkin");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AccountsList() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 862, 627);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.add(getScrollPane(), BorderLayout.CENTER);

		DefaultListCellRenderer renderer = (DefaultListCellRenderer) accountsList.getCellRenderer();
		renderer.setHorizontalAlignment(JLabel.CENTER);

		cargarAccounts();
	}

	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getAccountsList());
		}
		return scrollPane;
	}

	private JList<String> getAccountsList() {
		if (accountsList == null) {
			accountsList = new JList<String>();
			accountsList.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					if (e.getValueIsAdjusting()) {
						new AccountDetails(map.get(accounts.get(accountsList.getSelectedIndex())));
					}
				}
			});
			accountsList.setFixedCellWidth(50);
			accountsList.setFixedCellHeight(50);
			accountsList.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 28));
			accountsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		return accountsList;
	}

	private void cargarAccounts() {
		map = AsyncGetAccounts.getAccounts();
		accounts = new ArrayList<String>();
		Set<String> keySet = map.keySet();
		DefaultListModel<String> model = new DefaultListModel<>();

		for (String key : keySet) {
			model.addElement(key);
			accounts.add(key);
		}

		accountsList.setModel(model);
	}
}
