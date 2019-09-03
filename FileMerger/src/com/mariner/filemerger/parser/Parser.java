package com.mariner.filemerger.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mariner.filemerger.entity.FileFormat;
import com.mariner.filemerger.entity.Record;
import com.mariner.filemerger.exception.FileParserException;

public class Parser {
    /**
     * Parses any file
     * @param pathToFile
     * @return the records and headers
     * @throws FileParserException occurs when parsing is not successful
     */
	public Map<String, Object> parse(String pathToFile)
			throws FileParserException {
		Map<String, Object> recordsAndHeaders = new HashMap<String, Object>();
		List<Record> listOfRecords = new ArrayList<Record>();
		String[] headers = {};

		FileFormat fileFormat = FileFormat.valueOf(pathToFile.substring(
				pathToFile.lastIndexOf('.') + 1, pathToFile.length())
				.toUpperCase());

		switch (fileFormat) {
		case CSV:

			Map<String, Object> headersNRecords = new LocalCSVParser()
					.parse(pathToFile);
			listOfRecords = (List<Record>) headersNRecords.get("records");
			headers = (String[]) headersNRecords.get("headers");
			break;

		case JSON:

			listOfRecords = new JsonParser().parse(pathToFile);
			break;

		case XML:
			listOfRecords = new XmlParser().parseXml(pathToFile);
			break;

		default:
             
		}
		recordsAndHeaders.put("records", listOfRecords);
		recordsAndHeaders.put("headers", headers);
		return recordsAndHeaders;
	}

}
