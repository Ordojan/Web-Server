package server;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import server.gui.ServerMainWindow;

public class ServerMain {
	private static ServerCore webServer;
	private static IHttpModule httpModule;

	public static File getDeafaultFile() throws IOException {
		return ServerCore.fileManager.getDefaultFile();
	}

	public static void setDeafaultFile(String fileName) throws IOException {
		ServerCore.fileManager.setDefaultFile(fileName);
	}

	public static void main(String[] args) {
		try {
			IFileManager fileManager = new FileManager();
			ServerConfiguration serverConfig = fileManager.getServerConfiguration();

			ServerMainWindow mainWindow = new ServerMainWindow(serverConfig, fileManager);

			Method appStartMeth = serverConfig.WebApplication.HttpApplicationClass.getMethod("application_Start");
			appStartMeth.invoke(serverConfig.WebApplication.HttpApplicationClass.newInstance());

			webServer = new ServerCore(fileManager, serverConfig, mainWindow.getServerConsole());
			webServer.registerHttpModule(httpModule);
			
			mainWindow.setVisible(true);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void registerHttpModule(IHttpModule module) {
		httpModule = module;
	}
}
