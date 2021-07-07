package experiment.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ClassLoaderA extends ClassLoader{
	String path;
	public ClassLoaderA(String path) {
		this.path=path;
	}
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		try(FileInputStream fr=new FileInputStream(new File(path))){
			byte[] clazzBytes=new byte[fr.available()];
			fr.read(clazzBytes);
			return this.defineClass(name, clazzBytes, 0, clazzBytes.length);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
