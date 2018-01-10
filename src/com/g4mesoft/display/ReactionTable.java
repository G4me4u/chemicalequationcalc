package com.g4mesoft.display;

import java.awt.Color;
import java.awt.Component;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.g4mesoft.data.Reaction;
import com.g4mesoft.data.Reaction.MoleculeEntry;
import com.g4mesoft.main.ChemicalEquationMain;

import sun.swing.DefaultLookup;

@SuppressWarnings("serial")
public class ReactionTable extends JTable {

	private static final int NUM_ROWS = 4;
	private static final int FONT_SIZE = 18;
	private static final DecimalFormat FORMAT = new DecimalFormat("#.0000");

	private final DefaultTableModel model;

	private final ReactionCellRenderer reactionRenderer;
	private final NumberCellRenderer numberRenderer;
	
	public ReactionTable(ChemicalEquationMain main) {
		super(new DefaultTableModel(NUM_ROWS, 1));
		
		model = (DefaultTableModel)getModel();
		reactionRenderer = new ReactionCellRenderer(main);
		numberRenderer = new NumberCellRenderer();
		
		setRowHeight(36);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
	
	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {
		return reactionRenderer;
	}
	
	public void updateTable(Reaction reaction) {
		List<MoleculeEntry> reactants = reaction.getReactants();
		List<MoleculeEntry> products = reaction.getProducts();
		
		int numReactants = reactants.size();
		int numProducts = products.size();

		Vector<?> dataVector = model.getDataVector();
		for (int r = 0; r < dataVector.size(); r++) {
			Vector<?> rowVector = (Vector<?>)dataVector.get(r);
			for (int c = 0; c < rowVector.size(); c++)
				rowVector.set(c, null);
		}
		
		int numColumns = numReactants + numProducts + 1;
		if (numProducts > 0)
			numColumns++;
		
		model.setColumnCount(numColumns);

		int c = 0;
		leadingColumn(c++);
		
		for (int i = 0; i < numReactants; i++, c++) {
			MoleculeEntry entry = reactants.get(i);
			moleculeColumn(entry, c);
		}
		
		if (numProducts > 0) {
			model.setValueAt(SymbolManager.fromAlias(reaction.getSeperator()), 0, c++);
			
			for (int i = 0; i < numProducts; i++, c++) {
				MoleculeEntry entry = products.get(i);
				moleculeColumn(entry, c);
			}
		}
	}
	
	private void leadingColumn(int column) {
		model.setValueAt("M_w", 1, column);
		model.setValueAt("m", 2, column);
		model.setValueAt("n", 3, column);
	}
	
	private void moleculeColumn(MoleculeEntry entry, int column) {
		model.setValueAt(entry.toString(), 0, column);
		
		double mass = entry.molecule.calcMolarMass();
		model.setValueAt(FORMAT.format(mass), 1, column);
	}
	
	private class ReactionCellRenderer extends ReactionTextPane implements TableCellRenderer {

		private final Border EMPTY_BORDER = new EmptyBorder(1, 1, 1, 1);

		private ReactionCellRenderer(ChemicalEquationMain main) {
			super(main, FONT_SIZE);

			getStyledDocument().removeDocumentListener(this);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			if (isSelected) {
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
			} else {
				setForeground(table.getForeground());
				setBackground(table.getBackground());
			}

			if (hasFocus) {
				Border border = null;
				if (isSelected)
					border = DefaultLookup.getBorder(this, ui, "Table.focusSelectedCellHighlightBorder");
				if (border == null)
					border = DefaultLookup.getBorder(this, ui, "Table.focusCellHighlightBorder");
				setBorder(border);

				if (!isSelected && table.isCellEditable(row, column)) {
					Color col;
					
					col = DefaultLookup.getColor(this, ui, "Table.focusCellForeground");
					if (col != null)
						super.setForeground(col);
					
					col = DefaultLookup.getColor(this, ui, "Table.focusCellBackground");
					if (col != null)
						super.setBackground(col);
				}
			} else {
				setBorder(EMPTY_BORDER);
			}
			
			setText(value != null ? value.toString() : "");
			styleReaction();
			return this;
		}
	}
	
	private class NumberCellRenderer extends DefaultTableCellRenderer {
		
		private NumberCellRenderer() {
			setHorizontalAlignment(JLabel.CENTER);
		}
	}
}
