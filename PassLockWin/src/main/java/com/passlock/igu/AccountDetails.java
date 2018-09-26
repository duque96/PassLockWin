package com.passlock.igu;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class AccountDetails extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JDialog dialog;

	/**
	 * Create the dialog.
	 */
	public AccountDetails(List<String> list) {
		setResizable(false);
		setModal(true);
		setBounds(100, 100, 862, 627);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel emailLabel = new JLabel("New label");
		emailLabel.setHorizontalAlignment(SwingConstants.CENTER);
		emailLabel.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 26));
		emailLabel.setBounds(263, 214, 357, 31);
		emailLabel.setText(list.get(0));
		contentPanel.add(emailLabel);

		JLabel passwordLabel = new JLabel("New label");
		passwordLabel.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 26));
		passwordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		passwordLabel.setBounds(263, 302, 357, 31);
		passwordLabel.setText(list.get(1));
		contentPanel.add(passwordLabel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton cerrarButton = new JButton("Cerrar");
				cerrarButton.setActionCommand("Cancel");
				dialog = this;
				cerrarButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						dialog.dispose();
					}
				});
				buttonPane.add(cerrarButton);
			}
		}

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}