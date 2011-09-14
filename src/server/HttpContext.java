package server;

import java.util.HashMap;
import java.util.Map;

public class HttpContext {
	private static Map<String, String> formCollection;
	
	public static String RequestedFile;
	public static String RequestMethod;
	public static String VirtualPath;
	
	static {
		formCollection = new HashMap<String, String>();
	}
	
	public static Map<String, String> getFormCollection() {
		return formCollection;
	}
	
	protected static void setFormCollection(Map<String, String> formCollection) {
		HttpContext.formCollection = formCollection;
	}
}
