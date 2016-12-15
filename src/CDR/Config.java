package CDR;

import java.lang.String;

public class Config {
	
	// static ClassLoader classLoader = Config.class.getClassLoader();
	// static File file = new File(classLoader.getResource("").getFile());
	// private static final String BASE_PATH = file.getAbsolutePath()+"/";	
	private static final String BASE_PATH = System.getProperty("user.dir")+"/Data/";
	
	public static final String rawCDR = BASE_PATH + "cdr.csv";
	public static final String processedCDR = BASE_PATH + "pr_cdr.csv";
	public static final String COLLECTIONS_BASE_PATH = BASE_PATH+"Collection/";
	
}
