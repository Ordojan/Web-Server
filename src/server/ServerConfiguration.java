package server;

public class ServerConfiguration {
	public static final String RootParamaterName = "root";
	public static final String PortParamaterName = "port";
	public static final String HttpApplication_ClassParamaterName = "HttpApplication_Class";

	public final String Root;
	public final int Port;
	public final Class<HttpApplication> HttpApplicationClass;

	public ServerConfiguration(String root, int port, Class<HttpApplication> httpAppClass) {
		Root = root;
		Port = port;
		HttpApplicationClass = httpAppClass;
	}

}
