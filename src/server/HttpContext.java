package server;

import java.util.HashMap;
import java.util.Map;

class HttpContext implements IHttpContext {
	private Map<String, String> formCollection;
	private String RequestedFile;
	private String RequestMethod;
	private String VirtualPath;
	
	protected HttpContext() {
		formCollection = new HashMap<String, String>();
	}
	
	public String getRequestedFile() {
		return RequestedFile;
	}

	protected void setRequestedFile(String requestedFile) {
		RequestedFile = requestedFile;
	}

	public String getRequestMethod() {
		return RequestMethod;
	}

	protected void setRequestMethod(String requestMethod) {
		RequestMethod = requestMethod;
	}

	public String getVirtualPath() {
		return VirtualPath;
	}

	protected void setVirtualPath(String virtualPath) {
		VirtualPath = virtualPath;
	}

	public Map<String, String> getFormCollection() {
		return formCollection;
	}

	protected void setFormCollection(Map<String, String> formCollection) {
		this.formCollection = formCollection;
	}
}
