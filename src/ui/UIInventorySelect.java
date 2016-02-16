package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import customColor.CustomColor;
import ui.components.Button;
import ui.components.Label;
import ui.components.Layouts;
import ui.components.Panel;

public class UIInventorySelect {
	public void runInventorySelect() {
		Panel p = new Panel();
		Button b = new Button();
		Label l = new Label();

		DefaultListModel<String> model = new DefaultListModel<String>();
		// fetch list from server
		for (int i = 0; i < 10; i++){
			model.addElement("Element " + i);
		}

		// start of ui
		JFrame inventoryFrame = new JFrame("Inventory");
		inventoryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		inventoryFrame.setLayout(new BorderLayout());
		inventoryFrame.setVisible(true);

		JPanel pnlInstruction = p.createPanel(Layouts.flow);
		JLabel lblInstruction = l.createLabel("The Inventory List show the currently registered Nodes");
		pnlInstruction.setBackground(CustomColor.LightBlue.returnColor());
		lblInstruction.setForeground(Color.white);
		lblInstruction.setFont(new Font("San Serif", Font.PLAIN, 18));
		pnlInstruction.add(lblInstruction);

		JPanel pnlInventoryList = p.createPanel(Layouts.flow);
		JLabel lblInventoryList = l.createLabel("Inventory List:");
		JList listInventory = new JList(model);
		listInventory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollInventory = new JScrollPane(listInventory);
		scrollInventory.setPreferredSize(new Dimension(300, 150));
		Component[] inventoryListComponents = { lblInventoryList, scrollInventory };
		p.addComponentsToPanel(pnlInventoryList, inventoryListComponents);

		JPanel pnlButtons = p.createPanel(Layouts.flow);
		JButton btnAddElements = b.createButton("Add Item");

		// Button events
		btnAddElements.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// add inventory code here
				model.addElement("Element Test");
			}
		});

		JButton btnSelectElements = b.createButton("Next");
		btnSelectElements.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// do something with selected inventory
				System.out.println(listInventory.getModel().getElementAt(listInventory.getSelectedIndex()));
			}
		});

		pnlButtons.add(btnAddElements);
		pnlButtons.add(btnSelectElements);

		inventoryFrame.add(pnlInstruction, BorderLayout.NORTH);
		inventoryFrame.add(pnlInventoryList, BorderLayout.CENTER);
		inventoryFrame.add(pnlButtons, BorderLayout.SOUTH);
		inventoryFrame.pack();
	}

}
