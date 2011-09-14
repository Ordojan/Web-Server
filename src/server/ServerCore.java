package server;

import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpServerConnection;
import org.apache.http.HttpStatus;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.protocol.HttpRequestHandlerRegistry;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;
import org.apache.http.util.EntityUtils;

import server.gui.IServerConsole;

class ServerCore {
	private static IServerConsole console;

	public static ServerConfiguration ServerCongiguration;

	private static IHttpModule httpModule;
	public static IFileManager fileManager;

	public ServerCore(IFileManager fileManager, ServerConfiguration config, 
			IServerConsole serverConsole) throws IOException {
		ServerCongiguration = config;

		console = serverConsole;
		ServerCore.fileManager = fileManager;

		console.printToScreen("The simple Http Server\r\nCoded by Andrius Ordojan\r\n" +
				"https://github.com/CombatCow\r\n");

		print("Server running in directory: " + ServerCongiguration.ServerRoot);
		print("Application running in directory: " + ServerCongiguration.WebApplication.Root);

		Thread thread = new RequestListenerThread(ServerCongiguration.Port);
		thread.setDaemon(false);
		thread.start();
	}

	public static void print(String text) {
		console.printToScreen("\r\n" + text.concat("\r\n"));
	}

	public void registerHttpModule(IHttpModule module) {
		if (module == null) throw new IllegalArgumentException();

		ServerCore.httpModule = module;
	}

	public static File getResponse() throws IOException {
		httpModule.beginRequest();

		return httpModule.endRequest();
	}
}

class HttpFileHandler implements HttpRequestHandler {
	public HttpFileHandler() {
		super();
	}

	private Map<String, String> parsePostContent(byte[] entityContent) throws UnsupportedEncodingException {
		String content = URLDecoder.decode(new String(entityContent), "UTF-8");
		Map<String, String> formCollection = new HashMap<String, String>();
		char[] contentSplit = content.toCharArray();

		String key = null;
		String value = null;
		int valueEnd = 0;
		for (int i = 0; i < contentSplit.length; i++) {
			if(contentSplit[i] == '=') {
				key = new String();
				for (int j = valueEnd; j < i; j++) {
					key += contentSplit[j];
				}

				value = new String();
				for (int j = i + 1; j < contentSplit.length; j++) {
					if(contentSplit[j] == '&') {
						valueEnd = j + 1;
						break;
					}

					value += contentSplit[j];
				}

				formCollection.put(key, value);
			}
		}

		return formCollection;
	}

	public void handle(final HttpRequest request, final HttpResponse response,
			final HttpContext context) throws HttpException, IOException {
		String localDirectory;
		String virtualDirectory;
		String requestedFile;
		int iStartPos = 0;

		String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
		server.HttpContext.RequestMethod = method;

		if (!method.equals(HttpRequestMethods.HttpMethod_GET) 
				&& !method.equals(HttpRequestMethods.HttpMethod_HEAD) 
				&& !method.equals(HttpRequestMethods.HttpMethod_POST)) {

			ServerCore.print(method + " method not supported");
			throw new MethodNotSupportedException(method + " method not supported"); 
		}

		String uri = request.getRequestLine().getUri();

		if ((uri.indexOf(".") < 1) && (!uri.endsWith("/"))) {
			uri = uri + "/"; 
		}

		virtualDirectory = uri;

		server.HttpContext.VirtualPath = virtualDirectory;

		localDirectory = ServerCore.fileManager.getLocalPath(virtualDirectory);
		
		iStartPos = localDirectory.lastIndexOf("\\") + 1;
		requestedFile = localDirectory.substring(iStartPos);
		
		server.HttpContext.RequestedFile = requestedFile;
		
		ServerCore.print("Directory Requested : " +  localDirectory);

		if (request instanceof HttpEntityEnclosingRequest) {
			HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
			byte[] entityContent = EntityUtils.toByteArray(entity);

			Map<String, String> formCollection = parsePostContent(entityContent);
			server.HttpContext.setFormCollection(formCollection);

			ServerCore.print("Incoming entity content (bytes): " + entityContent.length);
		}

		File file = ServerCore.getResponse();
		String mimeType = ServerCore.fileManager.getMimeType(file.getName());
		FileEntity body = new FileEntity(file, mimeType);

		response.setStatusCode(HttpStatus.SC_OK);
		response.setEntity(body);

		/*
		final File file = new File(this.Root, URLDecoder.decode(target, "UTF-8"));

		if (!file.exists()) {
			response.setStatusCode(HttpStatus.SC_NOT_FOUND);
			EntityTemplate body = new EntityTemplate(new ContentProducer() {

				public void writeTo(final OutputStream outstream) throws IOException {
					OutputStreamWriter writer = new OutputStreamWriter(outstream, "UTF-8"); 
					writer.write("<html><body><h1>");
					writer.write("File ");
					writer.write(file.getPath());
					writer.write(" not found");
					writer.write("</h1></body></html>");
					writer.flush();
				}

			});

			body.setContentType("text/html; charset=UTF-8");
			response.setEntity(body);

			WebServer.print("File " + file.getPath() + " not found");
		}
		else if (!file.canRead() || file.isDirectory()) {

			response.setStatusCode(HttpStatus.SC_FORBIDDEN);
			EntityTemplate body = new EntityTemplate(new ContentProducer() {

				public void writeTo(final OutputStream outstream) throws IOException {
					OutputStreamWriter writer = new OutputStreamWriter(outstream, "UTF-8"); 
					writer.write("<html><body><h1>");
					writer.write("Access denied");
					writer.write("</h1></body></html>");
					writer.flush();
				}

			});

			body.setContentType("text/html; charset=UTF-8");
			response.setEntity(body);

			WebServer.print("Cannot read file " + file.getPath());
		}
		else {
			response.setStatusCode(HttpStatus.SC_OK);
			FileEntity body = new FileEntity(file, "text/html");
			response.setEntity(body);

			WebServer.print("Serving file " + file.getPath());
		}*/
	}
}

class RequestListenerThread extends Thread {
	private final ServerSocket serverSocket;
	private final HttpParams params; 
	private final HttpService httpService;

	public RequestListenerThread(int port) throws IOException {
		serverSocket = new ServerSocket(port);

		this.params = new SyncBasicHttpParams();
		this.params
		.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 5000)
		.setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, 8 * 1024)
		.setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false)
		.setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true)
		.setParameter(CoreProtocolPNames.ORIGIN_SERVER, "HttpComponents/1.1");

		// Set up the HTTP protocol processor
		HttpProcessor httpProc = new ImmutableHttpProcessor(new HttpResponseInterceptor[] {
				new ResponseDate(),
				new ResponseServer(),
				new ResponseContent(),
				new ResponseConnControl()
		});

		// Set up request handlers
		HttpRequestHandlerRegistry requestHandlerRegistry = new HttpRequestHandlerRegistry();
		requestHandlerRegistry.register("*", new HttpFileHandler());

		// Set up the HTTP service
		this.httpService = new HttpService(
				httpProc, 
				new DefaultConnectionReuseStrategy(), 
				new DefaultHttpResponseFactory(),
				requestHandlerRegistry,
				this.params);
	}

	@Override
	public void run() {
		ServerCore.print("Listening on port " + this.serverSocket.getLocalPort());

		while (!Thread.interrupted()) {
			try {
				// Set up HTTP connection
				Socket socket = this.serverSocket.accept();
				DefaultHttpServerConnection conn = new DefaultHttpServerConnection();

				ServerCore.print("Incoming connection from " + socket.getInetAddress());

				conn.bind(socket, this.params);

				Thread t = new RequestThread(this.httpService, conn);
				t.setDaemon(true);
				t.start();
			}
			catch (InterruptedIOException ex) {
				break;
			} 
			catch (IOException e) {
				ServerCore.print("I/O error initialising connection thread: " 
						+ e.getMessage());
				break;
			}
		}
	}

	class RequestThread extends Thread {
		private final HttpService httpservice;
		private final HttpServerConnection conn;

		public RequestThread(final HttpService httpservice, 
				final HttpServerConnection conn) {
			super();

			this.httpservice = httpservice;
			this.conn = conn;
		}

		public void run() {
			ServerCore.print("New connection thread");

			HttpContext context = new BasicHttpContext(null);
			try {
				while (!Thread.interrupted() && this.conn.isOpen()) {
					this.httpservice.handleRequest(this.conn, context);
				}
			} catch (ConnectionClosedException ex) {
				ServerCore.print("Client closed connection\r\n" + ex.getMessage());
			}catch (IOException ex) {
				ServerCore.print("I/O error: " + ex.getMessage());
			} catch (HttpException ex) {
				ServerCore.print("Unrecoverable HTTP protocol violation: " + ex.getMessage());
			} 
			finally {
				try {
					this.conn.shutdown();
				} catch (IOException ignore) {}
			}
		}
	}
}
