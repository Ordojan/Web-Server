package server;

import java.io.File;
import java.io.IOException;

public interface IHttpModule {
	public void beginRequest();
	public File endRequest() throws IOException;
}
