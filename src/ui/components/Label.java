package ui.components;

import javax.swing.JLabel;

import customColor.CustomColor;

public class Label {
	public JLabel createLabel(String labelString){
		JLabel label = new JLabel(labelString);
		label.setForeground(CustomColor.Grey.returnColor());
		
		return label;
	}
}
