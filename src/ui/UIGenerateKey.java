package ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ui.components.Button;
import ui.components.Label;
import ui.components.Layouts;
import ui.components.Panel;

public class UIGenerateKey {
	private JFrame frameAccessKey;
	private JPanel pnlAccessKey;
	private JLabel lblAccessKey;
	private JTextField tfKey;
	private JButton btnGenerate;

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

		frameAccessKey = new JFrame("Generate Access Key");
		pnlAccessKey = p.createPanel(Layouts.flow);
		pnlAccessKey.setAlignmentX(FlowLayout.CENTER);
		lblAccessKey = l.createLabel("Access Key:");
		l.addPadding(lblAccessKey, 10);
		tfKey = new JTextField();
		tfKey.setEditable(false);
		tfKey.setPreferredSize(new Dimension(300,50));
		btnGenerate = b.createButton("Generate");
		b.addPadding(btnGenerate, 10);
		btnGenerate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				generateKey();
			}
		});

		Component[] arrayAccessKeyComp = { lblAccessKey, tfKey, btnGenerate };
		p.addComponentsToPanel(pnlAccessKey, arrayAccessKeyComp);

		frameAccessKey.add(pnlAccessKey);
		frameAccessKey.pack();
		frameAccessKey.setVisible(true);
		frameAccessKey.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
}
