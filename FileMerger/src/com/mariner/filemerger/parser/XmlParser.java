package com.mariner.filemerger.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.mariner.filemerger.entity.Record;
import com.mariner.filemerger.entity.Records;
import com.mariner.filemerger.exception.FileParserException;

public class XmlParser {
    
	/**
	 * Parses the xml file
	 * 
	 * @param pathToFile for example data/reports.xml
	 * @return the records
	 * @throws FileParserException occurs when parsing is not successful
	 */
	public List<Record> parseXml(String pathToFile) throws FileParserException {
		
		List<Record> listOfRecords = new ArrayList<Record>();
		try {

			JAXBContext jaxbContext = JAXBContext.newInstance(Records.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			// unmarshal the xml file and get the list of records
			Records records = (Records) jaxbUnmarshaller.unmarshal(new File(
					pathToFile));

			listOfRecords = records.getRecords();
			
		} catch (JAXBException e) {
			throw new FileParserException(
					"Error occurred when unmarshalling xml file. "
							+ e.getMessage());
		} catch (Exception e) {
			throw new FileParserException(
					"Error occurred when unmarshalling xml file. "
							+ e.getMessage());
		}
		return listOfRecords;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// new XmlParser().parseXml("data/reports.xml");

	}

}
