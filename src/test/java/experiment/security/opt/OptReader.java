package experiment.security.opt;

import java.io.File;

public class OptReader {

	public void read() {
		File optFile=new File("/tmp");
//		System.out.println(this.getClass().getResource("/"));
		System.out.println("OptReader.read() can read : "+optFile.canRead());
	}
	
}
