package server.gui;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import server.IFileManager;
import server.ServerConfiguration;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ServerConfigurationWindow {

	private JFrame frmServerConfiguration;
	private JTextField tfRoot;
	private JLabel lblPort;
	private JTextField tfPort;
	private JPanel PButton;
	private JButton btnSave;
	private JButton btnCancel;

	private String currentRoot;
	private int currentPort;
	private IFileManager fileManager;
	
	public ServerConfigurationWindow(ServerConfiguration serverConfiguration, IFileManager fileManager) {
		this.fileManager = fileManager;
		initialize();

		currentRoot = serverConfiguration.ServerRoot;
		currentPort = serverConfiguration.Port;
		tfRoot.setText(currentRoot);
		tfPort.setText(String.valueOf(currentPort));

		frmServerConfiguration.setVisible(false);
	}

	private void initialize() {
		frmServerConfiguration = new JFrame();
		frmServerConfiguration.setTitle("Server Configuration");
		frmServerConfiguration.setBounds(100, 100, 250, 135);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{45, 0, 0};
		gridBagLayout.rowHeights = new int[]{30, 30, 0, 30, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		frmServerConfiguration.getContentPane().setLayout(gridBagLayout);

		JLabel lblRoot = new JLabel("Root:");
		lblRoot.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_lblRoot = new GridBagConstraints();
		gbc_lblRoot.anchor = GridBagConstraints.EAST;
		gbc_lblRoot.insets = new Insets(0, 0, 5, 5);
		gbc_lblRoot.gridx = 0;
		gbc_lblRoot.gridy = 0;
		frmServerConfiguration.getContentPane().add(lblRoot, gbc_lblRoot);

		tfRoot = new JTextField();
		GridBagConstraints gbc_tfRoot = new GridBagConstraints();
		gbc_tfRoot.insets = new Insets(0, 0, 5, 0);
		gbc_tfRoot.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfRoot.gridx = 1;
		gbc_tfRoot.gridy = 0;
		frmServerConfiguration.getContentPane().add(tfRoot, gbc_tfRoot);
		tfRoot.setColumns(10);

		lblPort = new JLabel("Port:");
		lblPort.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_lblPort = new GridBagConstraints();
		gbc_lblPort.anchor = GridBagConstraints.EAST;
		gbc_lblPort.insets = new Insets(0, 0, 5, 5);
		gbc_lblPort.gridx = 0;
		gbc_lblPort.gridy = 1;
		frmServerConfiguration.getContentPane().add(lblPort, gbc_lblPort);

		tfPort = new JTextField();
		GridBagConstraints gbc_tfPort = new GridBagConstraints();
		gbc_tfPort.insets = new Insets(0, 0, 5, 0);
		gbc_tfPort.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfPort.gridx = 1;
		gbc_tfPort.gridy = 1;
		frmServerConfiguration.getContentPane().add(tfPort, gbc_tfPort);
		tfPort.setColumns(10);

		PButton = new JPanel();
		GridBagConstraints gbc_PButton = new GridBagConstraints();
		gbc_PButton.gridwidth = 2;
		gbc_PButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_PButton.gridx = 0;
		gbc_PButton.gridy = 3;
		frmServerConfiguration.getContentPane().add(PButton, gbc_PButton);
		GridBagLayout gbl_PButton = new GridBagLayout();
		gbl_PButton.columnWidths = new int[]{0, 0, 0, 0};
		gbl_PButton.rowHeights = new int[]{35, 0};
		gbl_PButton.columnWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_PButton.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		PButton.setLayout(gbl_PButton);

		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ServerConfiguration config;
				Component frame = null;
				
				String root = tfRoot.getText();
				int port = Integer.parseInt(tfPort.getText().trim());
				
				config = new ServerConfiguration(root, port);
				
				if(!root.equalsIgnoreCase(currentRoot) || port != currentPort) {
					JOptionPane.showMessageDialog(frame,
						    "You need to restart the web server for the changes to take affect.");
				}
					
				try {
					fileManager.setServerConfiguration(config);
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(frame,
						    "Unable to save the new server configuration.",
						    "Unable to save",
						    JOptionPane.ERROR_MESSAGE);
				}
				
				frmServerConfiguration.setVisible(false);
			}
		});
		GridBagConstraints gbc_btnSave = new GridBagConstraints();
		gbc_btnSave.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSave.insets = new Insets(0, 0, 0, 5);
		gbc_btnSave.gridx = 0;
		gbc_btnSave.gridy = 0;
		PButton.add(btnSave, gbc_btnSave);

		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmServerConfiguration.setVisible(false);
			}
		});
		GridBagConstraints gbc_btnCancel = new GridBagConstraints();
		gbc_btnCancel.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnCancel.gridx = 2;
		gbc_btnCancel.gridy = 0;
		PButton.add(btnCancel, gbc_btnCancel);
	}

	public void setVisible(boolean b) {
		frmServerConfiguration.setVisible(b);
	}
}
