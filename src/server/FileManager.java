package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

class FileManager implements IFileManager {
	private File serverConfig;

	public FileManager() throws URISyntaxException {
		File file = new File(ServerMain.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		serverConfig = new File(file.getParent() + "\\ServerConfig.dat");
	}

	/**
	 * Returns the Physical Path
	 * @param sMyWebServerRoot Web Server Root Directory
	 * @param virtualDir Virtual Directory
	 * @return Physical local Path
	 * @throws IOException 
	 */
	@Override
	public String getLocalPath(String virtualDir) throws IOException {
		File vDirs = new File(ServerCore.ServerCongiguration.WebApplication.Root + "Data\\VDirs.dat");
		
		FileReader fileReader;
		BufferedReader  reader;
		String line = "";
		String fileVirtualDir = ""; 
		String fileRealDir = "";
		int iStartPos = 0;

		virtualDir = virtualDir.trim();

		//		sMyWebServerRoot = sMyWebServerRoot.ToLower();

		//		virtualDir = virtualDir.ToLower();

		//sDirName = sDirName.Substring(1, sDirName.Length - 2);

		fileReader = new FileReader(vDirs);
		reader = new BufferedReader(fileReader);

		while(reader.ready()) {
			line = reader.readLine();
			line.trim();

			if (line.length() > 0) {
				iStartPos = line.indexOf(";");

				line = line.toLowerCase();

				fileVirtualDir = line.substring(0,iStartPos);
				fileRealDir = line.substring(iStartPos + 1);

				if (fileVirtualDir.equals(virtualDir)) {
					break;
				}
			}
		}

		ServerCore.print("Virtual Dir : " + fileVirtualDir);
		ServerCore.print("Directory   : " + virtualDir);
		ServerCore.print("Physical Dir: " + fileRealDir);
		
		if (fileVirtualDir.equals(virtualDir))
			return fileRealDir;
		else
			return virtualDir;
	}

	@Override
	public String getMimeType(String fileName) throws IOException {
		File mimes = new File(ServerCore.ServerCongiguration.WebApplication.Root + "Data\\Mimes.dat");

		FileReader fileReader;
		BufferedReader  reader;
		String line = "";
		String mimeType = "";
		String fileExtention = "";
		String mimeExt = "";

		fileName = fileName.toLowerCase();

		int iStartPos = fileName.indexOf('.');

		fileExtention = fileName.substring(iStartPos);

		fileReader = new FileReader(mimes);
		reader = new BufferedReader(fileReader);

		while (reader.ready()) {
			line = reader.readLine().trim();

			if (line.length() > 0) {
				//find the separator
				iStartPos = line.indexOf(';');

				// Convert to lower case
				line = line.toLowerCase();

				mimeExt = line.substring(0,iStartPos);
				mimeType = line.substring(iStartPos + 1);

				if (mimeExt.equals(fileExtention))
					break;
			}
		}

		if (mimeExt.equals(fileExtention))
			return mimeType; 
		else
			return "";
	}

	@Override
	public File getDefaultFile() throws IOException {
		File defaultF = new File(ServerCore.ServerCongiguration.WebApplication.Root + "Data/Default.dat");

		FileReader fileReader;
		BufferedReader reader;
		String fileName;
		File file;

		fileReader = new FileReader(defaultF);
		reader = new BufferedReader(fileReader);

		while (reader.ready()) {
			fileName = reader.readLine().trim();

			file = new File(ServerCore.ServerCongiguration.WebApplication.Root + fileName);

			return file.exists() ? file : null;
		}

		return null;
	}

	@Override
	public boolean save(File file, String data) throws IOException {
		if(file == null) throw new IllegalArgumentException();

		FileWriter writer = new FileWriter(file);
		writer.write(data);
		writer.close();

		return file.exists();
	}

	@Override
	public String load(File file) throws IOException {
		if(file == null) throw new IllegalArgumentException();

		FileReader reader = new FileReader(file);
		char[] c = new char[4096];

		if(reader.ready())
			reader.read(c);

		reader.close();

		return new String(c);
	}

	@Override
	public void setDefaultFile(String fileName) throws IOException {
		File defaultF = new File(ServerCore.ServerCongiguration.WebApplication.Root + "Data/Default.dat");
		
		save(defaultF, fileName);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ServerConfiguration getServerConfiguration() throws Exception {
		FileReader fileReader;
		BufferedReader reader;
		String line;
		int iStartPos;
		ServerConfiguration config = null;
		String root = "";
		int port = 0;
		Class<HttpApplication> httpApplicationClass = null;
		String paramater;
		String value;

		try {
			fileReader = new FileReader(serverConfig);
			reader = new BufferedReader(fileReader);

			while (reader.ready()) {
				line = reader.readLine().trim();

				if (line.length() > 0) {
					iStartPos = line.indexOf(';');

					paramater = line.substring(0,iStartPos);
					value = line.substring(iStartPos + 1);

					if (ServerConfiguration.RootParamaterName.equals(paramater.trim()))
						root = value.trim();
					else if (ServerConfiguration.PortParamaterName.equals(paramater)) 
						port = Integer.parseInt(value.trim());
					else if (ServerConfiguration.HttpApplication_ClassParamaterName.equals(paramater)) {
						if(!root.isEmpty()) {
							URL urls [] = {};
							String path = root + "app.jar";
							JarFileLoader jarloader = new JarFileLoader(urls);
							jarloader.addFile(path);

							httpApplicationClass = (Class<HttpApplication>) jarloader.loadClass(value.trim());
						}
						else
							throw new IllegalArgumentException();
					}
				}
			}

			if (!root.isEmpty() && port != 0
					&& httpApplicationClass != null) {
				String serverRoot = ServerMain.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
				config = new ServerConfiguration(serverRoot, port);

				ServerConfiguration.WebApplication app = 
						config.new WebApplication(root, httpApplicationClass);

				config.WebApplication = app;
			}

		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}

		return config;
	}

	@Override
	public void setServerConfiguration(ServerConfiguration configuration) throws IOException {
		String data = ServerConfiguration.PortParamaterName + "; " + configuration.Port + "\r\n";
		data += "\r\n";
		data += ServerConfiguration.RootParamaterName + "; " + configuration.WebApplication.Root + "\r\n";
		data += ServerConfiguration.RootParamaterName + "; " + configuration.WebApplication.HttpApplicationClass + "\r\n";

		save(serverConfig, data);
	}
}
