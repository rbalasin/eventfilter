package com.mariner.filemerger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.mariner.filemerger.common.CommonUtility;
import com.mariner.filemerger.entity.Record;
import com.mariner.filemerger.exception.FileMergerException;
import com.mariner.filemerger.exception.FileParserException;
import com.mariner.filemerger.parser.Parser;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

public class FileMerger {

	public static final String FILENAME_COMBINED = "combined.csv";
	public static final String PATH_SEPARATOR = "/";

	//the records in the combined record file are to be sorted by the request time
	class RecordComparator implements Comparator<Record> {

		@Override
		public int compare(Record r1, Record r2) {

			if (r1.getRequestTime().after(r2.getRequestTime())) {
				return 1;
			} else if (r1.getRequestTime().before(r2.getRequestTime())) {
				return -1;
			} else {
				return 0;
			}
		}
	}
	
	/**
	 * Merge the files and print the combined records to the output file.
	 * 
	 * @param fileNames
	 *            the list of file names
	 * @param directoryPath
	 *            the directoryPath of the input and output files           
	 * @throws FileMergerException
	 *             the merge was not successful.
	 */
	public void mergeFiles(String[] fileNames, String directoryPath)
			throws FileMergerException {
		List<Record> listOfRecords = new ArrayList<Record>();
		String[] outputHeaders = {};

		Set<Record> listOfCombinedRecords = new TreeSet<Record>(
				new RecordComparator());
		
		String pathToFile = "";
		
		try {
			for (String fileName : fileNames) {

				pathToFile = directoryPath + PATH_SEPARATOR + fileName;
               
				Map<String, Object> recordsAndHeaders = new Parser()
						.parse(pathToFile);
				listOfRecords = (List<Record>) recordsAndHeaders.get("records");
				
				//input csv file header order is used in the output csv file
				if (((String[]) recordsAndHeaders.get("headers")).length > 0) {
					outputHeaders = (String[]) recordsAndHeaders.get("headers");
				}

				for (Record record : listOfRecords) {
					
					//Set the requestTime in the record so that the list can be sorted by the request time.
					//Since the json and xml parsers do not invoke getter/setter methods of Record during parsing, 
					//the requestTime cannot be in record.setRequestTimeAsString() 
					record.setRequestTime(record.getRequestTimeAsString());
                    
					// select the records that do not have 0 packet serviced
					if (record.getPacketsServiced() != 0) {
						listOfCombinedRecords.add(record);
					}
				}
			}
		} catch (FileParserException e) {
			throw new FileMergerException(
					"Unable to parse the files successfully." + e.getMessage());
		}

		if (outputHeaders.length != 0) {
			writeToFile(directoryPath, listOfCombinedRecords, outputHeaders);
			printNumOfRecordsByServiceGuid(listOfCombinedRecords);
		} else {
			throw new FileMergerException(
					"There are no headers for the output csv file. This occurs when there is no csv file as one of the input files");
		}

	}
  
	/**
	 * Writes the file to 
	 * 
	 * @param directoryPath the directory of the output file
	 * @param listOfCombinedRecords all sorted records to be printed
	 * @param headers the headers of the output csv file
	 * @throws FileMergerException occurs when the merge is not successful
	 */
	public void writeToFile(String directoryPath,
			Set<Record> listOfCombinedRecords, String[] headers)
			throws FileMergerException {
		
		CSVWriter csvWriter = null;

		Map<String, String> mapping = CommonUtility
				.getCsvToRecordFieldMapping();

		try {

			Writer writer = new FileWriter(directoryPath + "/"
					+ FILENAME_COMBINED);
			csvWriter = new CSVWriter(writer);
            
			//get the bean field names mapped to the csv header names
			String[] headerOrderInBean = new String[headers.length];
			int count = 0;
			for (String header : headers) {
				headerOrderInBean[count] = mapping.get(header);
				count++;
			}
			
            //The order of the columns(the bean field names) in the output file
			ColumnPositionMappingStrategy<Record> mappingStrategy = new ColumnPositionMappingStrategy<Record>();
			mappingStrategy.setType(Record.class);
			mappingStrategy.setColumnMapping(headerOrderInBean);
            
			//write the header to the output file
			csvWriter.writeNext(headers);

			// get the bean to csv builder an writer
			StatefulBeanToCsvBuilder<Record> builder = new StatefulBeanToCsvBuilder<Record>(
					csvWriter);
			StatefulBeanToCsv<Record> beanWriter = builder.withMappingStrategy(mappingStrategy).build();

			//write the list of records to the output file
			Map<String,Integer> serviceId = new HashMap<String,Integer>();
			List<Record> list = new ArrayList<Record>();
			for (Record record : listOfCombinedRecords) {
				list.add(record);
			}
			beanWriter.write(list);

			// closing the writer objects
			csvWriter.close();
			writer.close();

		} catch (IOException e) {
			throw new FileMergerException(
					"Unable to write the records to the output file. "
							+ e.getMessage());

		} catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
			throw new FileMergerException(
					"The required fields are empty or there is a datatype mismatch in csv data. "
							+ e.getMessage());
		} catch (Exception e) {
			throw new FileMergerException(
					"Error occurred when writing records to file. "
							+ e.getMessage());
		}
	}
	
	/**
	 * Prints the number of records associated with each service guid
	 * 
	 * @param listOfCombinedRecords list of records
	 */
	public void printNumOfRecordsByServiceGuid(Set<Record> listOfCombinedRecords){
		
		Map<String,Integer> numOfRecordsByServiceId = new HashMap<String,Integer>();
		Integer count = 0;
		for(Record record:listOfCombinedRecords){
			if(numOfRecordsByServiceId.get(record.getServiceGuid()) == null){
				numOfRecordsByServiceId.put(record.getServiceGuid(),1);
			} else {
				count = numOfRecordsByServiceId.get(record.getServiceGuid());
				numOfRecordsByServiceId.put(record.getServiceGuid(), count+1);
			}
		}
		System.out.println("             Service Guid           :  Number Of Records");
		System.out.println("-------------------------------------------------------");
		for(Map.Entry<String, Integer> entry:numOfRecordsByServiceId.entrySet()){
			System.out.println(entry.getKey() + ":  " + entry.getValue());
		}
	}

	/**
	 * Entry point
	 * 
	 * @param args
	 *            command line arguments: reports.csv,reports.xml,reports.json.
	 * @throws Exception
	 *             bad things had happened.
	 */
	public static void main(final String[] args) throws Exception {

		if (args.length == 0) {
			System.err.println("Usage: java FileMerger file1 [ file2 [...] ]");
			System.exit(1);
		}

		// Here the "data" is the input/output folder name
		new FileMerger().mergeFiles(args, "data");

		System.out.println("Successfully processed all the files!!");
	}
}
