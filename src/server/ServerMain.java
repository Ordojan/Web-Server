package server;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import server.gui.ServerMainWindow;

public class ServerMain {
	private static ServerCore webServer;
	private static IHttpModule httpModule;

	public static File getDeafaultFile() {
		return ServerCore.fileManager.getDefaultFile();
	}

	public static void setDeafaultFile(String fileName) {
		ServerCore.fileManager.setDefaultFile(fileName);
	}

	public static void main(String[] args) {
		try {
			IFileManager fileManager = new FileManager();
			ServerConfiguration config = fileManager.getServerConfiguration();

			ServerMainWindow mainWindow = new ServerMainWindow(config, fileManager);

			Method appStartMeth = config.HttpApplicationClass.getMethod("application_Start");
			appStartMeth.invoke(config.HttpApplicationClass.newInstance());

			webServer = new ServerCore(fileManager, config, mainWindow.getServerConsole());
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
