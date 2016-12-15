package MAP;

import java.io.File;
import java.lang.String;
import org.apache.commons.io.FileUtils;


public class Config {
	
	// static ClassLoader classLoader = Config.class.getClassLoader();
	// static File file = new File(classLoader.getResource("").getFile());	
	// private static final String BASE_PATH = file.getAbsolutePath()+"/";
	private static final String BASE_PATH = System.getProperty("user.dir")+"/Data/";
		
	public static String Custom_Config = BASE_PATH + "SOM.ini";
	public static String AllWeights = BASE_PATH + "AllWeights.csv";		
	public static String BenchMarks = BASE_PATH + "BenchMarks.txt";
	public static String BMUWeightsFile = BASE_PATH + "BmuWeights.csv";			
	public static File BMUFile = FileUtils.getFile(BASE_PATH + "BMU.csv");
	public static File TrainingFile = FileUtils.getFile(BASE_PATH + "pr_cdr.csv");		
	public static File AnalystFile = FileUtils.getFile(BASE_PATH + "SOMData_Analyst.ega");
	public static File TrainedNetworkFile = FileUtils.getFile(BASE_PATH + "SOMData_Train.eg");	
	public static File NormalizedTrainingFile = FileUtils.getFile(BASE_PATH + "SOMData_Train_Norm.csv");	
	public static File NormalizedEvaluateFile = FileUtils.getFile(BASE_PATH + "SSOMData_Eval_Norm.csv");	
	
	
}