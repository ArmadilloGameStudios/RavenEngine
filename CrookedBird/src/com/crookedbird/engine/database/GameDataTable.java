package com.crookedbird.engine.database;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class GameDataTable extends AbstractTableModel {
	private List<String> cols = new ArrayList<String>();
	private List<GameDataRow> rows = new ArrayList<GameDataRow>();
	private Object[][] rowData = null;
	private String name;
	private boolean modified = false;

	public GameDataTable(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public GameDataRow get(String colName, Object rowVal) {
		int c = cols.indexOf(colName);
		for (GameDataRow row : rows) {
			if (row.get(c).equals(rowVal)) {
				return row;
			}
		}
		return null;
	}

	public GameDataRow get(String colName[], Object rowVal[]) {
		for (GameDataRow row : rows) {
			boolean valid = true;
			for (int i = 0; valid && i < colName.length && i < rowVal.length; i++) {
				int c = cols.indexOf(colName[i]);
				if (!row.get(c).equals(rowVal[i])) {
					valid = false;
				}
			}
			if (valid) {
				return row;
			}
		}
		return null;
	}

	protected void setColumns(List<String> cols) {
		this.cols = cols;
	}

	public List<String> getColumns() {
		return cols;
	}

	@Override
	public int getColumnCount() {
		return cols.size();
	}

	public void addColumn(String name) {
		rowData = null;

		cols.add(name);

		for (GameDataRow row : rows) {
			row.getValues().add(null);
		}

		fireTableStructureChanged();
	}

	public void deleteColumn(int col) {
		rowData = null;

		cols.remove(col);

		for (GameDataRow row : rows) {
			row.getValues().remove(col);
		}

		fireTableStructureChanged();
	}

	public List<GameDataRow> getRows() {
		return rows;
	}

	@Override
	public int getRowCount() {
		return rows.size();
	}

	public void addRow(List<Object> vals) {
		rowData = null;
		rows.add(new GameDataRow(vals, this));

		fireTableCellUpdated(0, rows.size());
	}

	public void deleteRow(int row) {
		rowData = null;
		rows.remove(row);

		fireTableCellUpdated(0, row);
	}

	public Object[][] getRowsData() {
		if (rowData == null) {
			rowData = new Object[rows.size()][];

			for (int i = 0; i < rows.size(); i++) {
				rowData[i] = rows.get(i).getValues().toArray();
			}
		}

		return rowData;
	}

	@Override
	public Object getValueAt(int r, int c) {
		return getRowsData()[r][c];
	}

	@Override
	public String getColumnName(int r) {
		return cols.get(r);
	}

	public int getColumnIndex(String name) {
		return this.cols.indexOf(name);
	}

	@Override
	public boolean isCellEditable(int r, int c) {
		return true;
	}

	@Override
	public void setValueAt(Object o, int r, int c) {
		rowData = null;

		try {
			GameDataRow row = rows.get(r);
			row.set(o, c);
		} catch (Exception e) {

		}
		modified = true;
		fireTableCellUpdated(r, c);
	}

	// public ActionListener getButtonClickEvent() {
	// final GameDataTable tb = this;
	//
	// return new ActionListener() {
	// @Override
	// public void actionPerformed(ActionEvent e) {
	// tb.addRow(Arrays.asList(new Object[]{"","",""}));
	// }
	// };
	// }
}
