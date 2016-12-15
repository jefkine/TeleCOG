package CDR;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class CSVFileWriter {
	
	 //Delimiter used in CSV file
	 private static final String NEW_LINE_SEPARATOR = "\n";
	
	 //CSV file header
	 private static final Object [] FILE_HEADER = {"origIpAddr", "destIpAddr", "originalCalledPartyNumber", "finalCalledPartyNumber", "dateTimeConnect", "dateTimeDisconnect", "duration", "origDeviceName", "destDeviceName", "origIpv4v6Addr", "destIpv4v6Addr", "callingPartyNumber"};
	
	 private static final String[] SOM_CLASS = new String[]{"21670","23355","admin","b011212563001","100"};
	 
	 
	 public static void writeCsvFile(List<CallingRecord> callingRecords, String fileName) {
		 
		FileWriter fileWriter = null;
			
		CSVPrinter csvFilePrinter = null;
		
		//Create the CSVFormat object with "\n" as a record delimiter
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);
        
        try {
			
			// Initialize FileWriter object
			fileWriter = new FileWriter(fileName);
			
			// Initialize CSVPrinter object 
	        csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
	        
	        // Create CSV file header
	        csvFilePrinter.printRecord(FILE_HEADER);
	        
	        
			
			// Write a new student object list to the CSV file
			for (CallingRecord callingRecord : callingRecords) {			
				
				
				if (findElement(SOM_CLASS, callingRecord.getCallingPartyNumber())) // check the callingPartyNumber belongs to this class
				{
					
					// Declare cdrDataRecords List
					List<String> cdrDataRecords = new ArrayList<String>();
					
					// Add Elements into the cdrDataRecords List
					cdrDataRecords.add(String.valueOf(callingRecord.getOrigIpAddr()));
					cdrDataRecords.add(String.valueOf(callingRecord.getDestIpAddr()));
					cdrDataRecords.add(callingRecord.getOriginalCalledPartyNumber());
					cdrDataRecords.add(callingRecord.getFinalCalledPartyNumber());
					cdrDataRecords.add(String.valueOf(callingRecord.getDateTimeConnect()));
					cdrDataRecords.add(String.valueOf(callingRecord.getDateTimeDisconnect()));
					cdrDataRecords.add(String.valueOf(callingRecord.getDuration()));
					cdrDataRecords.add(callingRecord.getOrigDeviceName());
					cdrDataRecords.add(callingRecord.getDestDeviceName());
					cdrDataRecords.add(callingRecord.getOrigIpv4v6Addr());
					cdrDataRecords.add(callingRecord.getDestIpv4v6Addr());
					cdrDataRecords.add(callingRecord.getCallingPartyNumber()); // used as the classifier
					
					// Write cdrDataRecords List to a CSV File
		            csvFilePrinter.printRecord(cdrDataRecords);
					
				}			
				
	       
			}

			System.out.println("CSV file was created successfully !!!");
			
		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
				csvFilePrinter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter/csvPrinter !!!");
                e.printStackTrace();
			}
		}
	 
	 }
	 
	 private static boolean findElement(String[] arr, String targetValue) {
	    for(String s: arr){
	        if(s.equals(targetValue))
	            return true;
	    }
	    return false;
	 }

}
