package ui.components;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

import customColor.CustomColor;

public class Label {
	public JLabel createLabel(String labelString) {
		JLabel label = new JLabel(labelString);
		label.setForeground(CustomColor.Grey.returnColor());

		return label;
	}

	public JLabel createResultLabel(String labelString) {
		JLabel label = new JLabel(labelString);
		label.setForeground(Color.ORANGE);

		return label;
	}

	public JLabel createLabel(String labelString, int alignment) {
		JLabel label = new JLabel(labelString, alignment);
		label.setPreferredSize(new Dimension(150, 25));
		label.setForeground(CustomColor.Grey.returnColor());

		return label;
	}

	public void addPadding(JLabel l, int padding) {
		l.setBorder(new EmptyBorder(padding, padding, padding, padding));
	}
}
