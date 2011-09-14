package server;

import java.util.Map;

public interface IHttpContext {
	public String getRequestedFile();
	public String getRequestMethod();
	public String getVirtualPath();
	public Map<String, String> getFormCollection();
}
