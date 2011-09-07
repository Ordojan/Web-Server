package server;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

class JarFileLoader extends URLClassLoader {
	public JarFileLoader (URL[] urls) {
        super(urls);
    }
	
    public void addFile(String path) throws MalformedURLException {
    	// maybe need to change to 'jar:file:/' or 'jar:file:\\'
        String urlPath = "jar:file:/" + path + "!/";
        addURL(new URL(urlPath));
    }
}
