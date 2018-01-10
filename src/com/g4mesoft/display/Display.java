package com.g4mesoft.display;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.g4mesoft.main.ChemicalEquationMain;

public class Display {

	private final JFrame frame;
	
	public final ReactionTextPane reactionPane;
	public final ReactionTable reactionTable;
	
	public Display(ChemicalEquationMain main) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		
		frame = new JFrame(ChemicalEquationMain.TITLE);
		
		reactionPane = new ReactionTextPane(main);
		reactionTable = new ReactionTable(main);
	}
	
	public void create() {
		initFrame();
	}
	
	private void initFrame() {
		uiLayout();
		uiEvents();
		
		frame.setResizable(false);
		frame.setSize(ChemicalEquationMain.WIDTH, ChemicalEquationMain.HEIGHT);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		frame.requestFocus();
	}
	
	private void uiLayout() {
		frame.setLayout(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(2, 2, 2, 2);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		
		frame.add(reactionPane, constraints);
		
		constraints.gridy++;
		constraints.weighty = 0.0;
		
		frame.add(reactionTable, constraints);
	}

	private void uiEvents() {
		
	}
}
