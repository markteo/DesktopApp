package ui.components.table.model;

import javax.swing.table.DefaultTableModel;

public class UneditableModel extends DefaultTableModel {
	public UneditableModel(Object[][] data, Object[] columnNames) {
		super(data, columnNames);
	}

	public UneditableModel() {
		super();
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

}
