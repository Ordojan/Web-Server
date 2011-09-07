package server.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import java.awt.Insets;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.Dimension;

public class ServerConsole implements IServerConsole {
	private JFrame frmWebServerConsole;
	private JTextArea textArea;
	private JPanel pFunctions;
	private JButton btnClear;

	public ServerConsole() {
		initialize();
		frmWebServerConsole.setVisible(false);
	}

	private void initialize() {
		frmWebServerConsole = new JFrame();
		frmWebServerConsole.setTitle("Web Server Console");
		frmWebServerConsole.setBounds(100, 100, 450, 300);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		frmWebServerConsole.getContentPane().setLayout(gridBagLayout);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		frmWebServerConsole.getContentPane().add(scrollPane, gbc_scrollPane);
		
		textArea = new JTextArea();
		textArea.setForeground(Color.WHITE);
		textArea.setBackground(Color.BLACK);
		scrollPane.setViewportView(textArea);
		scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e){
				textArea.select(textArea.getHeight() + 1000,0);
			}
		});
		
//		JTextArea display= new JTextArea();
//		JScrollPane scroll =new JScrollPane(display);
//		scroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener(){
//		public void adjustmentValueChanged(AdjustmentEvent e){
//		display.select(display.getHeight()+1000,0);
//		}});
		
		pFunctions = new JPanel();
		GridBagConstraints gbc_pFunctions = new GridBagConstraints();
		gbc_pFunctions.fill = GridBagConstraints.BOTH;
		gbc_pFunctions.gridx = 0;
		gbc_pFunctions.gridy = 1;
		frmWebServerConsole.getContentPane().add(pFunctions, gbc_pFunctions);
		GridBagLayout gbl_pFunctions = new GridBagLayout();
		gbl_pFunctions.columnWidths = new int[]{0, 0};
		gbl_pFunctions.rowHeights = new int[]{0, 0};
		gbl_pFunctions.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_pFunctions.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		pFunctions.setLayout(gbl_pFunctions);
		
		btnClear = new JButton("Clear");
		btnClear.setPreferredSize(new Dimension(59, 16));
		btnClear.setMinimumSize(new Dimension(59, 16));
		btnClear.setMaximumSize(new Dimension(59, 16));
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText("");
			}
		});
		GridBagConstraints gbc_btnClear = new GridBagConstraints();
		gbc_btnClear.fill = GridBagConstraints.BOTH;
		gbc_btnClear.gridx = 0;
		gbc_btnClear.gridy = 0;
		pFunctions.add(btnClear, gbc_btnClear);
	}

	@Override
	public void printToScreen(String text) {
		textArea.append(text);
	}
	
	public void setVisible(boolean b) {
		frmWebServerConsole.setVisible(b);
	}
}
