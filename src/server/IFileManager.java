package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface IFileManager {
	String getMimeType(String fileName) throws IOException;
	File getDefaultFile() throws IOException;
	void setDefaultFile(String fileName);

	boolean save(File file, String data) throws IOException;
	String load(File file) throws FileNotFoundException, IOException;
	
	ServerConfiguration getServerConfiguration() throws Exception;
	void setServerConfiguration(ServerConfiguration configuration) throws Exception;
}
