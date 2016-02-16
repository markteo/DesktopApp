package ui.components;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import customColor.CustomColor;

public class Button {

	public static JButton createButton(String btnName) {
		JButton button = new JButton(btnName);
		button.setForeground(CustomColor.Grey.returnColor());
		button.setBackground(CustomColor.GreyishBlue.returnColor());
		Border line = new LineBorder(CustomColor.GreyishBlue.returnColor());
		Border margin = new EmptyBorder(5, 15, 5, 15);
		Border compound = new CompoundBorder(line, margin);
		button.setBorder(compound);
		
		return button;
	}
}
