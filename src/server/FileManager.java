package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class FileManager implements IFileManager {
	private File mimes;
	private File vDirs;
	private File defaultF;
	private File serverConfig;

	public FileManager() {
		//		vDirs = new File(ServerCore.ROOT + "Data\\VDirs.dat");
		try {
			File f = new File(ServerMain.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
			serverConfig = new File(f.getParent() + "\\ServerConfig.dat");
		} catch (URISyntaxException e) {
			e.printStackTrace(System.err);
		}

		//		URL url = ServerMain.class.getResource("server.ServerMain");
		//		Matcher matcher = Pattern.compile("jar:file:(.*)!.*").matcher(url.toString());
		//		if (matcher.matches()) {
		//			File file = new File(matcher.group(1));
		//			File dir = file.getParentFile();
		//
		//			serverConfig = new File(dir + "\\ServerConfig.dat");
		//		}
		//		else
		//			serverConfig = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "ServerConfig.dat");
	}

	@Override
	public String getMimeType(String fileName) {
		mimes = new File(ServerCore.ROOT + "Data\\Mimes.dat");

		FileReader fileReader;
		BufferedReader  reader;
		String line = "";
		String mimeType = "";
		String fileExtention = "";
		String mimeExt = "";

		fileName = fileName.toLowerCase();

		int iStartPos = fileName.indexOf('.');

		fileExtention = fileName.substring(iStartPos);

		try	{
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
		}
		catch (Exception e) {
			ServerCore.print("An Exception Occurred : " + e.toString());
		}

		if (mimeExt.equals(fileExtention))
			return mimeType; 
		else
			return "";
	}

	@Override
	public File getDefaultFile() {
		defaultF = new File(ServerCore.ROOT + "Data/Default.dat");

		FileReader fileReader;
		BufferedReader reader;
		String fileName;
		File file;

		try {
			fileReader = new FileReader(defaultF);
			reader = new BufferedReader(fileReader);

			while (reader.ready()) {
				fileName = reader.readLine().trim();

				file = new File(ServerCore.ROOT + fileName);

				if (!file.exists()) 
					return null;

				return file;
			}
		}
		catch (Exception e) {
			ServerCore.print("An Exception Occurred : " + e.toString());
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
	public void setDefaultFile(String fileName) {
		try {
			save(defaultF, fileName);
		} 
		catch (IOException e) {
			ServerCore.print("An Exception Occurred : " + e.toString());
		}
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
					//find the separator
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

							httpApplicationClass = (Class<HttpApplication>) jarloader.loadClass (value.trim());
						}
						else
							throw new IllegalArgumentException();
					}
				}
			}

			if (!root.isEmpty() && port != 0
					&& httpApplicationClass != null)
				config = new ServerConfiguration(root, port, httpApplicationClass);
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}

		return config;
	}

	@SuppressWarnings("static-access")
	@Override
	public void setServerConfiguration(ServerConfiguration configuration) throws Exception{
		String data = configuration.RootParamaterName + "; " + configuration.Root + "\r\n";
		data += configuration.PortParamaterName + "; " + configuration.Port + "\r\n";

		try {
			save(serverConfig, data);
		} 
		catch (IOException e) {
			ServerCore.print("An Exception Occurred : " + e.toString());
			throw e;
		}
	}
}
