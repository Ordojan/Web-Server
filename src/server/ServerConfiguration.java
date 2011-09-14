package server;

public class ServerConfiguration {
	public static final String PortParamaterName = "port";
	public static final String RootParamaterName = "root";
	public static final String HttpApplication_ClassParamaterName = "HttpApplication_Class";

	public final String ServerRoot;
	public final int Port;
	public WebApplication WebApplication;
	
	
	public ServerConfiguration(String serverRoot, int port) {
		ServerRoot = serverRoot;
		Port = port;
	}

	public class WebApplication {
		final String Root;
		final Class<HttpApplication> HttpApplicationClass;
		
		public WebApplication(String root, Class<HttpApplication> httpApplicationClass) {
			Root = root;
			HttpApplicationClass = httpApplicationClass;
		}
	}
}
