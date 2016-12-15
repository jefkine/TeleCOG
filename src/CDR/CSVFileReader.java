package CDR;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CSVFileReader {

	// Calling Record attributes
    private static final String CR_origIpAddr = "origIpAddr";
	private static final String CR_destIpAddr = "destIpAddr";
	private static final String CR_originalCalledPartyNumber = "originalCalledPartyNumber";
	private static final String CR_finalCalledPartyNumber = "finalCalledPartyNumber";
	private static final String CR_dateTimeConnect = "dateTimeConnect";
	private static final String CR_dateTimeDisconnect = "dateTimeDisconnect";
	private static final String CR_duration = "duration";
	private static final String CR_origDeviceName = "origDeviceName";
	private static final String CR_destDeviceName = "destDeviceName";
	private static final String CR_origIpv4v6Addr = "origIpv4v6Addr";
	private static final String CR_destIpv4v6Addr = "destIpv4v6Addr";
	private static final String CR_callingPartyNumber = "callingPartyNumber"; // used as the classifier

	public static List<CallingRecord> readCsvFile(String fileName) {

		FileReader fileReader = null;

		CSVParser csvFileParser = null;

		// Create the CSVFormat object with the header mapping
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader();

        try {

        	// Create a new list of student to be filled by CSV file data
        	List<CallingRecord> callingRecords = new ArrayList<CallingRecord>();

            // Initialize FileReader object
            fileReader = new FileReader(fileName);

            // Initialize CSVParser object
            csvFileParser = new CSVParser(fileReader, csvFileFormat);

            // Get a list of CSV file records
            List<CSVRecord> csvRecords = csvFileParser.getRecords();

            // Read the CSV file records starting from the second record to skip the header
            for (int i = 2; i < csvRecords.size(); i++) {

            	CSVRecord record = csvRecords.get(i);

            	// Create a new callingRecord object and fill his data
            	CallingRecord callingRecord = new CallingRecord(Math.abs(Integer.parseInt((record.isSet(CR_origIpAddr) == false) ? "0" : record.get(CR_origIpAddr).replace("+AC0-", ""))),
															    Math.abs(Integer.parseInt((record.isSet(CR_destIpAddr) == false) ? "0" : record.get(CR_destIpAddr).replace("+AC0-", ""))),
																(record.isSet(CR_originalCalledPartyNumber) == false) ? "0" : record.get(CR_originalCalledPartyNumber),
																(record.isSet(CR_finalCalledPartyNumber) == false) ? "0" : record.get(CR_finalCalledPartyNumber),
														        Math.abs(Integer.parseInt((record.isSet(CR_dateTimeConnect) == false) ? "0" : record.get(CR_dateTimeConnect).replace("+AC0-", ""))),
																Math.abs(Integer.parseInt((record.isSet(CR_dateTimeDisconnect) == false) ? "0" : record.get(CR_dateTimeDisconnect).replace("+AC0-", ""))),
																Math.abs(Integer.parseInt((record.isSet(CR_duration) == false) ? "0" : record.get(CR_duration).replace("+AC0-", ""))),
																(record.isSet(CR_origDeviceName) == false) ? "0" : record.get(CR_origDeviceName),
																(record.isSet(CR_destDeviceName) == false) ? "0" : record.get(CR_destDeviceName),
																(record.isSet(CR_origIpv4v6Addr) == false) ? "0" : record.get(CR_origIpv4v6Addr),
																(record.isSet(CR_destIpv4v6Addr) == false) ? "0" : record.get(CR_destIpv4v6Addr),
																(record.isSet(CR_callingPartyNumber) == false) ? "0" : record.get(CR_callingPartyNumber));
	            callingRecords.add(callingRecord);

			}

		    // Print to screen the new callingRecord list
		    for (CallingRecord callingRecord : callingRecords) {
				System.out.println(callingRecord.toString());
				
			}
		    
		    return callingRecords; // return a generic CallingRecord POJO list

        }
        catch (Exception e) {
        	System.out.println("Error in CsvFileReader !!!");
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
                csvFileParser.close();
            } catch (IOException e) {
            	System.out.println("Error while closing fileReader/csvFileParser !!!");
                e.printStackTrace();
            }
        }
		return null;


	}


}
