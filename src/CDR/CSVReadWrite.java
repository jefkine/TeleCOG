package CDR;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CSVReadWrite {
	
public static void CompileCDR() {
				
		// Run the simulation on a collection of raw CDR dumps
		File folder = new File(Config.COLLECTIONS_BASE_PATH);
		File[] listOfFiles = folder.listFiles();
		
		// declare a generic list for all the filtered records
		List<CallingRecord> rCDRs = new ArrayList<CallingRecord>();
		
	    for (int i = 0; i < listOfFiles.length; i++) {
	      if (listOfFiles[i].isFile()) {
	        
	        // populate the list with all the filtered records
	        rCDRs.addAll(CSVFileReader.readCsvFile(Config.COLLECTIONS_BASE_PATH+listOfFiles[i].getName()));
	        	        
	      } else if (listOfFiles[i].isDirectory()) {
	    	// Not traversing directories yet  
	        // System.out.println("Directory " + listOfFiles[i].getName());
	      }
	      
	    }
	    
	    // Write the callingRecords List to a Processed CSV
        CSVFileWriter.writeCsvFile(rCDRs, Config.processedCDR);
		
	}

}
