package uiComponents;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import customColor.CustomColor;

public class Panel {
	public JPanel createPanel(Layouts layout) {
		JPanel panel = new JPanel();

		if (layout == Layouts.border) {
			borderLayout(panel);
		} else if (layout == Layouts.card) {
			cardLayout(panel);
		} else if (layout == Layouts.flow) {
			flowLayout(panel);
		}

		panel.setBackground(CustomColor.NavyBlue.returnColor());
		return panel;
	}

	public JPanel createPanel(Layouts layout, int row, int cols) {
		JPanel panel = new JPanel();

		if (layout == Layouts.grid) {
			gridLayout(panel, row, cols);
		}

		panel.setBackground(CustomColor.NavyBlue.returnColor());
		return panel;
	}

	public JPanel createPanel(Layouts layout, int axis) {
		JPanel panel = new JPanel();

		if (layout == Layouts.box) {
			boxLayout(panel, axis);
		}

		panel.setBackground(CustomColor.NavyBlue.returnColor());
		return panel;
	}
	
	public void addComponentsToPanel(JPanel p,Component[] componentList){
		for(Component c:componentList){
			p.add(c);
		}
	}
	
	public void addComponentsToPanel(JPanel p,Component[] componentList,String direction){
		for(Component c:componentList){
			p.add(c,direction);
		}
	}
	
	//setting layout for panel
	private void borderLayout(JPanel p) {
		p.setLayout(new BorderLayout());
	}

	private void boxLayout(JPanel p, int axis) {
		p.setLayout(new BoxLayout(p, axis));
	}

	private void cardLayout(JPanel p) {
		p.setLayout(new CardLayout());
	}

	private void flowLayout(JPanel p) {
		p.setLayout(new FlowLayout());
	}

	private void gridLayout(JPanel p, int row, int cols) {
		p.setLayout(new GridLayout(row, cols));
	}
}
