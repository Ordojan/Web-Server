package server;

import java.io.File;

public interface IHttpModule {
	public void beginRequest();
	public File endRequest();
}
