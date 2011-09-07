package server.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import server.IFileManager;
import server.ServerConfiguration;

public class ServerMainWindow {
	private JFrame frmAisMain;
	
	private ServerConsole serverConsole;
	private ServerConfigurationWindow configurationWindow;
	
	public ServerMainWindow(ServerConfiguration serverConfiguration, IFileManager fileManager) {
		initialize();
		
		serverConsole = new ServerConsole();
		configurationWindow = new  ServerConfigurationWindow(serverConfiguration, fileManager);
		
		frmAisMain.setVisible(false);
	}

	private void initialize() {
		frmAisMain = new JFrame();
		frmAisMain.setTitle("AIS Main");
		frmAisMain.setBounds(100, 100, 361, 177);
		frmAisMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 5, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		frmAisMain.getContentPane().setLayout(gridBagLayout);
		
		JButton btnConsole = new JButton("Console");
		btnConsole.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				serverConsole.setVisible(true);
			}
		});
		GridBagConstraints gbc_btnConsole = new GridBagConstraints();
		gbc_btnConsole.fill = GridBagConstraints.BOTH;
		gbc_btnConsole.insets = new Insets(0, 0, 5, 0);
		gbc_btnConsole.gridx = 0;
		gbc_btnConsole.gridy = 0;
		frmAisMain.getContentPane().add(btnConsole, gbc_btnConsole);
		
		JButton btnNewButton = new JButton("Configure");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				configurationWindow.setVisible(true);
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton.fill = GridBagConstraints.BOTH;
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 1;
		frmAisMain.getContentPane().add(btnNewButton, gbc_btnNewButton);
		
		JButton btnResetServer = new JButton("Reset Server");
		GridBagConstraints gbc_btnResetServer = new GridBagConstraints();
		gbc_btnResetServer.insets = new Insets(0, 0, 5, 0);
		gbc_btnResetServer.fill = GridBagConstraints.BOTH;
		gbc_btnResetServer.gridx = 0;
		gbc_btnResetServer.gridy = 3;
		frmAisMain.getContentPane().add(btnResetServer, gbc_btnResetServer);
		
		JButton btnShutDownServer = new JButton("Shut Down Server");
		GridBagConstraints gbc_btnShutDownServer = new GridBagConstraints();
		gbc_btnShutDownServer.fill = GridBagConstraints.BOTH;
		gbc_btnShutDownServer.gridx = 0;
		gbc_btnShutDownServer.gridy = 4;
		frmAisMain.getContentPane().add(btnShutDownServer, gbc_btnShutDownServer);
	}
	
	public IServerConsole getServerConsole() {
		return serverConsole;
	}
	
	public void setVisible(boolean b) {
		frmAisMain.setVisible(b);
	}
}
