package ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import ui.components.Button;
import ui.components.Label;
import ui.components.Layouts;
import ui.components.Panel;

public class UIGenerateKey {
	private String[] arrayUser;
	private String[] arrayCompanyID;
	private String[] arrayTimeUnit;

	private JFrame frameGenerate;

	private JPanel pnlAccessKey;

	private JLabel lblAccessKey;
	private JLabel lblCompanyID;
	private JLabel lblUserName;
	private JLabel lblExpiration;
	private JLabel lblUses;

	private JComboBox<String> listUser;
	private JComboBox<String> listCompanyID;
	private JComboBox<String> listTimeUnit;

	private JCheckBox cbUnlimited;

	private JTextField tfKey;

	private JSpinner spinExpiration;
	private JSpinner spinUses;

	private SpinnerModel smExpiration;
	private SpinnerModel smUses;

	private JButton btnGenerate;
	private JButton btnBack;

	public String generateKey() {
		// Input generate key function here
		String key = "";
		tfKey.setText(key);
		return key;
	}

	public void runGenerateAccessKey() {
		Panel p = new Panel();
		Label l = new Label();
		Button b = new Button();

		frameGenerate = new JFrame("Generate Access Key");
		pnlAccessKey = p.createPanel(Layouts.gridbag);
		GridBagConstraints gc = new GridBagConstraints();

		// Initialize your arrays here
		arrayUser = new String[] { "John Doe", "Mark Ignatius", "Clarence Castillo" };
		arrayCompanyID = new String[] { "1", "2", "3" };
		arrayTimeUnit = new String[] { "Hour", "Minutes", "Years", "Months", "Decades", "Century" };

		// initialize combobox here
		gc.gridx = 2;
		gc.gridy = 2;
		listUser = new JComboBox<>(arrayUser);
		listUser.setPreferredSize(new Dimension(150, 25));
		pnlAccessKey.add(listUser, gc);

		gc.gridx = 3;
		gc.gridy = 3;
		listTimeUnit = new JComboBox<>(arrayTimeUnit);
		pnlAccessKey.add(listTimeUnit, gc);

		// initialize spinner model
		smExpiration = new SpinnerNumberModel(1, 0, Integer.MAX_VALUE, 1);
		smUses = new SpinnerNumberModel(1, 0, Integer.MAX_VALUE, 1);

		// intialize spinner
		gc.gridx = 2;
		gc.gridy = 3;
		spinExpiration = new JSpinner(smExpiration);
		spinExpiration.setPreferredSize(new Dimension(150, 25));
		pnlAccessKey.add(spinExpiration, gc);

		gc.gridx = 2;
		gc.gridy = 4;
		spinUses = new JSpinner(smUses);
		spinUses.setPreferredSize(new Dimension(150, 25));
		pnlAccessKey.add(spinUses, gc);

		// init checkbox
		gc.gridx = 3;
		gc.gridy = 4;
		cbUnlimited = new JCheckBox("Unlimited");
		cbUnlimited.setBackground(customColor.CustomColor.NavyBlue.returnColor());
		cbUnlimited.setForeground(customColor.CustomColor.Grey.returnColor());
		cbUnlimited.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (cbUnlimited.isSelected()) {
					spinUses.setEnabled(false);
				} else {
					spinUses.setEnabled(true);
				}
			}
		});
		pnlAccessKey.add(cbUnlimited, gc);

		// initialize label
		gc.gridx = 1;
		gc.gridy = 1;

		gc.gridx = 1;
		gc.gridy = 2;
		lblUserName = l.createLabel("User Name:");
		lblUserName.setPreferredSize(new Dimension(150, 25));
		l.addPadding(lblUserName, 10);
		pnlAccessKey.add(lblUserName, gc);

		gc.gridx = 1;
		gc.gridy = 3;
		lblExpiration = l.createLabel("Expire:");
		lblExpiration.setPreferredSize(new Dimension(150, 25));
		l.addPadding(lblExpiration, 10);
		pnlAccessKey.add(lblExpiration, gc);

		gc.gridx = 1;
		gc.gridy = 4;
		lblUses = l.createLabel("Number of Uses:");
		lblUses.setPreferredSize(new Dimension(150, 25));
		l.addPadding(lblUses, 10);
		pnlAccessKey.add(lblUses, gc);

		gc.gridx = 1;
		gc.gridy = 5;
		lblAccessKey = l.createLabel("Access Key:");
		lblAccessKey.setPreferredSize(new Dimension(150, 25));
		l.addPadding(lblAccessKey, 10);
		pnlAccessKey.add(lblAccessKey, gc);

		// initialize TextField
		gc.gridx = 2;
		gc.gridy = 5;
		tfKey = new JTextField();
		tfKey.setEditable(false);
		tfKey.setPreferredSize(new Dimension(150, 25));
		pnlAccessKey.add(tfKey, gc);

		// initialize button
		gc.gridx = 3;
		gc.gridy = 8;
		gc.gridwidth = 150;
		btnGenerate = b.createButton("Generate");
		b.addPadding(btnGenerate, 10);
		btnGenerate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				generateKey();
			}
		});
		pnlAccessKey.add(btnGenerate, gc);

		gc.gridx = 5;
		gc.gridy = 8;
		gc.gridwidth = 150;
		btnBack = b.createButton("Back");
		b.addPadding(btnBack, 10);
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}
		});
		pnlAccessKey.add(btnBack, gc);

		frameGenerate.add(pnlAccessKey);
		frameGenerate.pack();
		frameGenerate.setVisible(true);
		frameGenerate.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
}
