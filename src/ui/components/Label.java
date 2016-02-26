package ui.components;

import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

import customColor.CustomColor;

public class Label {
	public JLabel createLabel(String labelString){
		JLabel label = new JLabel(labelString);
		label.setForeground(CustomColor.Grey.returnColor());
		
		return label;
	}
	
	public void addPadding(JLabel l, int padding){
		l.setBorder(new EmptyBorder(padding,padding,padding,padding));
	}
}
