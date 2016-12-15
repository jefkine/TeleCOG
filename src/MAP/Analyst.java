package MAP;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import java.io.BufferedWriter;
import org.encog.neural.som.SOM;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.util.csv.CSVFormat;
import org.encog.mathutil.Equilateral;
import org.encog.mathutil.rbf.RBFEnum;
import org.encog.util.simple.EncogUtility;
import org.encog.app.analyst.EncogAnalyst;
import org.encog.app.analyst.AnalystFileFormat;
import org.encog.app.analyst.wizard.AnalystWizard;
import org.encog.util.arrayutil.NormalizationAction;
import org.encog.neural.som.training.basic.BasicTrainSOM;
import org.encog.app.analyst.script.normalize.AnalystField;
import org.encog.app.analyst.csv.normalize.AnalystNormalizeCSV;
import org.encog.neural.som.training.basic.neighborhood.NeighborhoodRBF;
import org.encog.persist.EncogDirectoryPersistence;

import org.ini4j.*;

public class Analyst {

	public static Wini ini;

    public final static int INPUT_NEURON_COUNT = 11;
    public final static int WIDTH = 50;
	public final static int HEIGHT = 50;
	public static int MAX_EPOCH = 1000; // will be edited

    private static SOM network;
	private static BasicTrainSOM train;
	private static MLDataSet trainingSet;
	private static MLDataSet evaluationSet;

	private static double LEARNING_RATE = 0;
	private static double START_RATE = 0;
	private static double END_RATE = 0;
	private static int START_RADIUS = 0;
	private static int END_RADIUS = 0;

	private static org.encog.mathutil.matrices.Matrix weights;


	public static void main(String[] args) throws IOException {

		 BenchMarkTitle("START");

		 // STEP 1. Initialize Wini Object
		 ini = new Wini(new File(Config.Custom_Config));
		 MAX_EPOCH = ini.get("epoch", "iterations", int.class);
		 String COMPILE_CDR = ini.get("compile_cdr", "compile");

		 // STEP 2. Process the CDR Compilation
		 if(COMPILE_CDR.equals("yes")){
			 System.out.println("+++++++++++++ START CDR COMPILATION +++++++++++++");
			 Compile_CDR();
			 System.out.println("+++++++++++++ END CDR COMPILATION +++++++++++++");
		 }

		 // STEP 3. Generate The SOM Trained Map Data
		 System.out.println("+++++++++++++ START SOM TRAINER SETUP +++++++++++++");
		 NormalizeData();
		 CreateNetwork();
		 CreateTrainer();
		 System.out.println("+++++++++++++ END SOM TRAINER SETUP +++++++++++++");
		 System.out.println("+++++++++++++ START SOM TRAINING +++++++++++++");
		 Train();
		 System.out.println("+++++++++++++ END SOM TRAINING +++++++++++++");

		 // STEP 4. Log Trained Data To Files
		 System.out.println("+++++++++++++ START SOM DATA LOGGING +++++++++++++");
		 BmuWeights();
		 AllWeights();
		 System.out.println("+++++++++++++ END SOM DATA LOGGING +++++++++++++");

		 // STEP 5. Print Out End Of SOM Training
		 System.out.println("SYSTEM PROCs COMPLETED SUCCESSFULLY - END OF SOM TRAINING");

		 BenchMarkTitle("END");

	}

	public static void Compile_CDR() throws IOException{

		 // Start BenchMark

		 long start = System.currentTimeMillis();
		 String startTimeStamp = new SimpleDateFormat("yyyy - MM - dd HH:mm:ss").format(Calendar.getInstance().getTime());
		 System.out.println("TASK STARTED AT: "+startTimeStamp);

		 // Start BenchMark

		 CDR.CSVReadWrite.CompileCDR();

		 // End Benchmark

		 long elapsed = System.currentTimeMillis() - start;


		 System.out.println("ELAPSED TIME = " + elapsed + "MS");

		 long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsed);
		 long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsed);

		 String endTimeStamp = new SimpleDateFormat("yyyy - MM - dd HH:mm:ss").format(Calendar.getInstance().getTime());

		 System.out.println("TASK ENDED AT: "+endTimeStamp);
 	     System.out.println("TIME CONSUMED BY TASK : ~"+seconds+" SECONDS OR "+elapsed+" MILLISECONDS");

	     BenchMarker("Compiling CDR CSVs",
	    		     startTimeStamp,
	    		     endTimeStamp,
	    		     start,
	    		     elapsed,
	    		     seconds,
	    		     minutes);

	     // End Benchmark

	}

	public static void NormalizeData()
    {

		// Start BenchMark

		long start = System.currentTimeMillis();
		String startTimeStamp = new SimpleDateFormat("yyyy - MM - dd HH:mm:ss").format(Calendar.getInstance().getTime());
		System.out.println("TASK STARTED AT: "+startTimeStamp);

		// Start BenchMark

		EncogAnalyst analyst = new EncogAnalyst();

        // Wizard
		AnalystWizard wizard = new AnalystWizard(analyst);
        wizard.wizard(Config.TrainingFile, true, AnalystFileFormat.DECPNT_COMMA);

        // Set the fields to be ignored
        AnalystField field = analyst.getScript().getNormalize().getNormalizedFields().get(11);
        field.setAction(NormalizationAction.Ignore);

        // Norm for Training
        final AnalystNormalizeCSV norm = new AnalystNormalizeCSV();
		norm.analyze(Config.TrainingFile, true, CSVFormat.ENGLISH, analyst);
		norm.setProduceOutputHeaders(true);
		norm.normalize(Config.NormalizedTrainingFile);

		// End BenchMark

		long elapsed = System.currentTimeMillis() - start;

		 System.out.println("ELAPSED TIME = " + elapsed + "MS");

		 long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsed);
		 long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsed);

		 String endTimeStamp = new SimpleDateFormat("yyyy - MM - dd HH:mm:ss").format(Calendar.getInstance().getTime());

		 System.out.println("TASK ENDED AT: "+endTimeStamp);
 	     System.out.println("TIME CONSUMED BY TASK : ~"+seconds+" SECONDS OR "+elapsed+" MILLISECONDS");

	     try {
			BenchMarker("Nomalize The Data",
				     startTimeStamp,
				     endTimeStamp,
				     start,
				     elapsed,
				     seconds,
				     minutes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }

	private static void CreateNetwork() {

		// Start BenchMark

		long start = System.currentTimeMillis();
		String startTimeStamp = new SimpleDateFormat("yyyy - MM - dd HH:mm:ss").format(Calendar.getInstance().getTime());
		System.out.println("TASK STARTED AT: "+startTimeStamp);

		// Start BenchMark

		network = new SOM(INPUT_NEURON_COUNT, WIDTH * HEIGHT);
		network.reset();

		// End BenchMark

		long elapsed = System.currentTimeMillis() - start;

		 System.out.println("ELAPSED TIME = " + elapsed + "MS");

		 long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsed);
		 long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsed);

		 String endTimeStamp = new SimpleDateFormat("yyyy - MM - dd HH:mm:ss").format(Calendar.getInstance().getTime());

		 System.out.println("TASK ENDED AT: "+endTimeStamp);
 	     System.out.println("TIME CONSUMED BY TASK : ~"+seconds+" SECONDS OR "+elapsed+" MILLISECONDS");

	  try {
			BenchMarker("Create The Network",
							     startTimeStamp,
							     endTimeStamp,
							     start,
							     elapsed,
							     seconds,
							     minutes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    // End Benchmark

	}

	private static void CreateTrainer()
    {

		// Start BenchMark

		long start = System.currentTimeMillis();
		String startTimeStamp = new SimpleDateFormat("yyyy - MM - dd HH:mm:ss").format(Calendar.getInstance().getTime());
		System.out.println("TASK STARTED AT: "+startTimeStamp);

		// Start BenchMark

		// Collect Trainer Constraints
		LEARNING_RATE = ini.get("learning_rate", "rate", double.class);
		START_RATE = ini.get("autodecay", "start_rate", double.class);
		END_RATE = ini.get("autodecay", "end_rate", double.class);
		START_RADIUS = ini.get("autodecay", "start_radius", int.class);
		END_RADIUS = ini.get("autodecay", "end_radius", int.class);

        // Trainer
		NeighborhoodRBF neighborhood = new NeighborhoodRBF(RBFEnum.Gaussian, WIDTH, HEIGHT);
		train = new BasicTrainSOM(network, LEARNING_RATE, null, neighborhood);
        train.setForceWinner(false);
        train.setAutoDecay(MAX_EPOCH, START_RATE, END_RATE, START_RADIUS, END_RADIUS);

        // Training Data Set
        trainingSet = EncogUtility.loadCSV2Memory(Config.NormalizedTrainingFile.toString(),
            INPUT_NEURON_COUNT, 0, true, CSVFormat.ENGLISH, false);

		// End BenchMark

		long elapsed = System.currentTimeMillis() - start;

		 System.out.println("ELAPSED TIME = " + elapsed + "MS");

		 long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsed);
		 long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsed);

		 String endTimeStamp = new SimpleDateFormat("yyyy - MM - dd HH:mm:ss").format(Calendar.getInstance().getTime());

		 System.out.println("TASK ENDED AT: "+endTimeStamp);
 	     System.out.println("TIME CONSUMED BY TASK : ~"+seconds+" SECONDS OR "+elapsed+" MILLISECONDS");

	     try {
			BenchMarker("Create The Trainer",
				     startTimeStamp,
				     endTimeStamp,
				     start,
				     elapsed,
				     seconds,
				     minutes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	   // End Benchmark

    }

	private static void Train(){

		// Start BenchMark

		long start = System.currentTimeMillis();
		String startTimeStamp = new SimpleDateFormat("yyyy - MM - dd HH:mm:ss").format(Calendar.getInstance().getTime());
		System.out.println("TASK STARTED AT: "+startTimeStamp);

		// Start BenchMark

		int iteration = 0;

        for(iteration = 0; iteration < MAX_EPOCH; iteration++)
        {
        	int idx = (int) (Math.random() * trainingSet.size());
			MLData datarow = trainingSet.get(idx).getInput();

            train.trainPattern(datarow);
            train.autoDecay();

            EncogDirectoryPersistence.saveObject(Config.TrainedNetworkFile, network);
            System.out.println("ITERATION: " + iteration + ", ERROR:" + train.toString());

        }

		// End BenchMark

		long elapsed = System.currentTimeMillis() - start;

		 System.out.println("ELAPSED TIME = " + elapsed + "MS");

		 long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsed);
		 long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsed);

		 String endTimeStamp = new SimpleDateFormat("yyyy - MM - dd HH:mm:ss").format(Calendar.getInstance().getTime());

		 System.out.println("TASK ENDED AT: "+endTimeStamp);
 	     System.out.println("TIME CONSUMED BY TASK : ~"+seconds+" SECONDS OR "+elapsed+" MILLISECONDS");

	     try {
			BenchMarker("Train The Network",
				     startTimeStamp,
				     endTimeStamp,
				     start,
				     elapsed,
				     seconds,
				     minutes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    // End Benchmark

	}

    private static void BmuWeights() throws IOException{

    	// Start BenchMark

		long start = System.currentTimeMillis();
		String startTimeStamp = new SimpleDateFormat("yyyy - MM - dd HH:mm:ss").format(Calendar.getInstance().getTime());
		System.out.println("TASK STARTED AT: "+startTimeStamp);

		// Start BenchMark

		//network
        network = (SOM)EncogDirectoryPersistence.loadObject(Config.TrainedNetworkFile);

        EncogAnalyst analyst = new EncogAnalyst();

        // Wizard
		AnalystWizard wizard = new AnalystWizard(analyst);
        wizard.wizard(Config.TrainingFile, true, AnalystFileFormat.DECPNT_COMMA);

        //Norm for Training
        AnalystNormalizeCSV norm = new AnalystNormalizeCSV();
        norm.setProduceOutputHeaders(true);
        norm.analyze(Config.TrainingFile, true, CSVFormat.ENGLISH, analyst);
        norm.normalize(Config.NormalizedEvaluateFile);

        evaluationSet = EncogUtility.loadCSV2Memory(Config.NormalizedEvaluateFile.toString(),
                INPUT_NEURON_COUNT, 4, true, CSVFormat.ENGLISH, false);


        File file = new File(Config.BMUWeightsFile);

        if (!file.exists()) {
        	file.createNewFile(); // creates the file
		}

        FileWriter fw = new FileWriter(file);  // creates a FileWriter Object
        BufferedWriter writer = new BufferedWriter(fw);

        for(MLDataPair item : evaluationSet){


        	int bmuIndex = network.winner(item.getInput());
        	weights = network.getWeights();

        	// Get the ideal classes
        	int classCount = analyst.getScript().getNormalize().getNormalizedFields().get(11).getClasses().size();
        	double normalizationHigh = analyst.getScript().getNormalize().getNormalizedFields().get(11).getNormalizedHigh();
        	double normalizationLow = analyst.getScript().getNormalize().getNormalizedFields().get(11).getNormalizedLow();


        	Equilateral eq = new Equilateral(classCount, normalizationHigh, normalizationLow);
            int idealClassInt = eq.decode(item.getIdeal().getData());
            String idealClass = analyst.getScript().getNormalize().getNormalizedFields().get(11).getClasses().get(idealClassInt).getName();

            switch (idealClass)
            {
                case "PHONE-NUMBER-1":
                	System.out.println("LOGGING WEIGHTS FOR BMUIndex: "+bmuIndex);
                	System.out.println("SUBSCRIBER - 1: x " + weights.get(bmuIndex, 0) +" y "+  weights.get(bmuIndex, 1) +" z "+  weights.get(bmuIndex, 3)+ " .... "+  weights.get(bmuIndex, 10));
                	writer.write(weights.get(bmuIndex, 0)+","+ weights.get(bmuIndex, 1)+","+ weights.get(bmuIndex, 2)+","+ weights.get(bmuIndex, 3)+","+ weights.get(bmuIndex, 4)+","+ weights.get(bmuIndex, 5)+","+ weights.get(bmuIndex, 6)+","+ weights.get(bmuIndex, 7)+","+ weights.get(bmuIndex, 8)+","+ weights.get(bmuIndex, 9)+","+ weights.get(bmuIndex, 10)+",1");
                	writer.write("\n");
                	writer.flush();
                	break;

                case "PHONE-NUMBER-2":
                	System.out.println("LOGGING WEIGHTS FOR BMUIndex: "+bmuIndex);
                	System.out.println("SUBSCRIBER - 2: x " + weights.get(bmuIndex, 0) +" y "+  weights.get(bmuIndex, 1) +" z "+  weights.get(bmuIndex, 3)+ " .... "+  weights.get(bmuIndex, 10));
                	writer.write(weights.get(bmuIndex, 0)+","+ weights.get(bmuIndex, 1)+","+ weights.get(bmuIndex, 2)+","+ weights.get(bmuIndex, 3)+","+ weights.get(bmuIndex, 4)+","+ weights.get(bmuIndex, 5)+","+ weights.get(bmuIndex, 6)+","+ weights.get(bmuIndex, 7)+","+ weights.get(bmuIndex, 8)+","+ weights.get(bmuIndex, 9)+","+ weights.get(bmuIndex, 10)+",2");
                	writer.write("\n");
                	writer.flush();
                	break;

                case "PHONE-NUMBER-3":
                	System.out.println("LOGGING WEIGHTS FOR BMUIndex: "+bmuIndex);
                	System.out.println("SUBSCRIBER - 3: x " + weights.get(bmuIndex, 0) +" y "+  weights.get(bmuIndex, 1) +" z "+  weights.get(bmuIndex, 3)+ " .... "+  weights.get(bmuIndex, 10));
                	writer.write(weights.get(bmuIndex, 0)+","+ weights.get(bmuIndex, 1)+","+ weights.get(bmuIndex, 2)+","+ weights.get(bmuIndex, 3)+","+ weights.get(bmuIndex, 4)+","+ weights.get(bmuIndex, 5)+","+ weights.get(bmuIndex, 6)+","+ weights.get(bmuIndex, 7)+","+ weights.get(bmuIndex, 8)+","+ weights.get(bmuIndex, 9)+","+ weights.get(bmuIndex, 10)+",3");
                	writer.write("\n");
                	writer.flush();
                	break;

                case "PHONE-NUMBER-4":
                	System.out.println("LOGGING WEIGHTS FOR BMUIndex: "+bmuIndex);
                	System.out.println("SUBSCRIBER - 4: x " + weights.get(bmuIndex, 0) +" y "+  weights.get(bmuIndex, 1) +" z "+  weights.get(bmuIndex, 3)+ " .... "+  weights.get(bmuIndex, 10));
                	writer.write(weights.get(bmuIndex, 0)+","+ weights.get(bmuIndex, 1)+","+ weights.get(bmuIndex, 2)+","+ weights.get(bmuIndex, 3)+","+ weights.get(bmuIndex, 4)+","+ weights.get(bmuIndex, 5)+","+ weights.get(bmuIndex, 6)+","+ weights.get(bmuIndex, 7)+","+ weights.get(bmuIndex, 8)+","+ weights.get(bmuIndex, 9)+","+ weights.get(bmuIndex, 10)+",4");
                	writer.write("\n");
                	writer.flush();
                	break;

                case "PHONE-NUMBER-5":
                	System.out.println("LOGGING WEIGHTS FOR BMUIndex: "+bmuIndex);
                	System.out.println("SUBSCRIBER - 5: x " + weights.get(bmuIndex, 0) +" y "+  weights.get(bmuIndex, 1) +" z "+  weights.get(bmuIndex, 3)+ " .... "+  weights.get(bmuIndex, 10));
                	writer.write(weights.get(bmuIndex, 0)+","+ weights.get(bmuIndex, 1)+","+ weights.get(bmuIndex, 2)+","+ weights.get(bmuIndex, 3)+","+ weights.get(bmuIndex, 4)+","+ weights.get(bmuIndex, 5)+","+ weights.get(bmuIndex, 6)+","+ weights.get(bmuIndex, 7)+","+ weights.get(bmuIndex, 8)+","+ weights.get(bmuIndex, 9)+","+ weights.get(bmuIndex, 10)+",5");
                	writer.write("\n");
                	writer.flush();
                	break;

            }

        }

        writer.close();

        // End BenchMark

 		long elapsed = System.currentTimeMillis() - start;

 		 System.out.println("ELAPSED TIME = " + elapsed + "MS");

 		 long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsed);
 		 long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsed);

 		 String endTimeStamp = new SimpleDateFormat("yyyy - MM - dd HH:mm:ss").format(Calendar.getInstance().getTime());

 		 System.out.println("TASK ENDED AT: "+endTimeStamp);
	     System.out.println("TIME CONSUMED BY TASK : ~"+seconds+" SECONDS OR "+elapsed+" MILLISECONDS");

 	     try {
 			BenchMarker("Log BMUs (Best Matching Units)",
 				     startTimeStamp,
 				     endTimeStamp,
 				     start,
 				     elapsed,
 				     seconds,
 				     minutes);
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}

 	    // End Benchmark


	}

	private static void AllWeights() throws IOException{

		// Start BenchMark

		long start = System.currentTimeMillis();
		String startTimeStamp = new SimpleDateFormat("yyyy - MM - dd HH:mm:ss").format(Calendar.getInstance().getTime());
		System.out.println("TASK STARTED AT: "+startTimeStamp);

		// Start BenchMark

		//network
	    network = (SOM)EncogDirectoryPersistence.loadObject(Config.TrainedNetworkFile);

	    File file = new File(Config.AllWeights);

	    if (!file.exists()) {
	    	file.createNewFile(); // creates the file
		}

	    FileWriter fw = new FileWriter(file);  // creates a FileWriter Object
	    BufferedWriter writer = new BufferedWriter(fw);

	    org.encog.mathutil.matrices.Matrix weights = network.getWeights();

	    for (int i = 0; i < 2500; i++){
	       	System.out.println("LOGGING WEIGHTS FOR INDEX: "+i);
	        writer.write(weights.get(i, 0)+","+ weights.get(i, 1)+","+ weights.get(i, 2)+","+ weights.get(i, 3)+","+ weights.get(i, 4)+","+ weights.get(i, 5)+","+ weights.get(i, 6)+","+ weights.get(i, 7)+","+ weights.get(i, 8)+","+ weights.get(i, 9)+","+ weights.get(i, 10));
	        writer.write("\n");
	        writer.flush();
	    }

	    writer.close();

         // End BenchMark

 		 long elapsed = System.currentTimeMillis() - start;

 		 System.out.println("ELAPSED TIME = " + elapsed + "MS");

 		 long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsed);
 		 long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsed);

 		 String endTimeStamp = new SimpleDateFormat("yyyy - MM - dd HH:mm:ss").format(Calendar.getInstance().getTime());

 	     System.out.println("TASK ENDED AT: "+endTimeStamp);
 	     System.out.println("TIME CONSUMED BY TASK : ~"+seconds+" SECONDS OR "+elapsed+" MILLISECONDS");

 	     try {
 			BenchMarker("Log All Weights",
 				     startTimeStamp,
 				     endTimeStamp,
 				     start,
 				     elapsed,
 				     seconds,
 				     minutes);
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}

 	    // End Benchmark

	}

	private static void BenchMarkTitle(String type) throws IOException{

		File file = new File(Config.BenchMarks);

	    if (!file.exists()) {
	    	file.createNewFile(); // creates the file
		}

	    FileWriter fw = new FileWriter(file, true);  // creates a FileWriter Object
	    BufferedWriter writer = new BufferedWriter(fw);

	    String currTimeStamp = new SimpleDateFormat("yyyy - MM - dd HH:mm:ss").format(Calendar.getInstance().getTime());

	    writer.write("\n");
	    if(type == "START"){
	    	writer.write("---------- BEGIN BENCHMARK ---------");
		    writer.write("\n");
	    	writer.write("BENCHMARK STARTED AT: "+currTimeStamp);
	    }else{
	    	writer.write("BENCHMARK ENDED AT: "+currTimeStamp);
	    	writer.write("\n");
	    	writer.write("----------- END BENCHMARK ----------");
	    }
	    writer.write("\n");
	    writer.write("----------------------------------------");
        writer.flush();

        writer.close();

	}

	private static void BenchMarker(String TaskName, String currStartTimeStamp, String currEndTimeStamp, long start_Millis, long elapsed_Millis, long elapsed_Secs, long elapsed_Mins ) throws IOException{

		File file = new File(Config.BenchMarks);

	    if (!file.exists()) {
	    	file.createNewFile(); // creates the file
		}

	    FileWriter fw = new FileWriter(file, true);  // creates a FileWriter Object
	    BufferedWriter writer = new BufferedWriter(fw);

	    writer.write("\n");
	    writer.write("++++++ METRICS FOR TASK: "+TaskName.toUpperCase()+" ++++++");
	    writer.write("\n");
	    writer.write("----------------------------------------");
	    writer.write("\n");
	    writer.write("----------------------------------------");
	    writer.write("\n");
	    writer.write("------ TASK "+TaskName.toUpperCase()+" STARTED SUCCESFULLY ------");
        writer.write("\n");
        writer.write("\n");
        writer.write("START TIME: "+start_Millis+" MILLISECONDS");
        writer.write("\n");
        writer.write("START TIME: "+currStartTimeStamp);
        writer.write("\n");
        writer.write("TIME TAKEN: "+elapsed_Millis+" MILLISECONDS");
        writer.write("\n");
        writer.write("TIME TAKEN: ~"+elapsed_Secs+" SECONDS");
        writer.write("\n");
        writer.write("TIME TAKEN: ~"+elapsed_Mins+" MINUTES");
        writer.write("\n");
        writer.write("END TIME: "+currEndTimeStamp);
        writer.write("\n");
        writer.write("\n");
        writer.write("------ TASK "+TaskName.toUpperCase()+" ENDED SUCCESFULLY ------");
        writer.write("\n");
        writer.flush();

        writer.close();

	}

}
