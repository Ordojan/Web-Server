package server;

import java.util.HashMap;
import java.util.Map;

public class HttpContext {
	private static Map<String, String> formCollection;
	
	public static String URL;
	public static String RequestMethod;
	
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
