package com.mariner.filemerger.parser;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mariner.filemerger.common.CommonUtility;
import com.mariner.filemerger.entity.Record;
import com.mariner.filemerger.exception.FileParserException;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

public class LocalCSVParser {
	

	public LocalCSVParser() {
	}
	
	/**
	 * Parses the csv file
	 * 
	 * @param pathToFile the path to file(ex: data/reports.csv) 
	 * @return the list of records and the order of the columns
	 * @throws FileParserException occurs when parsing is not successful
	 */
	public Map<String, Object> parse(String pathToFile) throws FileParserException {
		
		Map<String, Object> recordsAndHeaders = new HashMap<String, Object>();
		Reader reader;
		List<Record> listOfRecords = new ArrayList<Record>();
		CSVReader csvReader = null;

		Map<String, String> mapping = CommonUtility
				.getCsvToRecordFieldMapping();

		try {
			reader = new FileReader(pathToFile);
			csvReader = new CSVReader(reader);

			// read the header from the input file and use it in the output combined csv file
			String[] headers = csvReader.readNext();
			recordsAndHeaders.put("headers", headers);
            
			//to point the cursor to the beginning of the input file after reading the header
			reader = new FileReader(pathToFile);
			csvReader = new CSVReader(reader);
            
			//To set the csv column values to the mapped bean field
			HeaderColumnNameTranslateMappingStrategy<Record> strategy = new HeaderColumnNameTranslateMappingStrategy<Record>();
			strategy.setType(Record.class);
			strategy.setColumnMapping(mapping);

			CsvToBean csvToBean = new CsvToBean();
			csvToBean.setCsvReader(csvReader);
			csvToBean.setMappingStrategy(strategy);
			listOfRecords = csvToBean.parse();
			
			recordsAndHeaders.put("records", listOfRecords);
			
			csvReader.close();

		} catch (IOException e) {
			throw new FileParserException(
					"The input file is not found or not able to read from the input csv file "
							+ e.getMessage());
		} catch (Exception e) {
			throw new FileParserException(
					"Error occurred when parsing csv file. " + e.getMessage());
		}
		
		return recordsAndHeaders;

	}

	public static void main(final String[] args) throws Exception {
		
	//	new LocalCSVParser().parse("data/reports.csv");

	}

}
