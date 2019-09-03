package com.mariner.filemerger.parser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.mariner.filemerger.entity.Record;
import com.mariner.filemerger.exception.FileParserException;

public class JsonParser {

	/**
	 * Parses the json file and returns the records
	 *  
	 * @param pathToFile the path to the json file(example: data/reports.json)
	 * @return the list of records
	 * @throws FileParserException occurs when parsing is not successful
	 */
	public List<Record> parse(String pathToFile) throws FileParserException {
		List<Record> listOfRecords = new ArrayList<Record>();

		try {
			//converts the json content to list of records using @SerializedName in the Record bean
			Type REVIEW_TYPE = new TypeToken<List<Record>>() {
			}.getType();
			Gson gson = new GsonBuilder().create();
			JsonReader reader = new JsonReader(new FileReader(pathToFile));
			listOfRecords = gson.fromJson(reader, REVIEW_TYPE);

		} catch (FileNotFoundException e) {
			throw new FileParserException("The input file is not found. "
					+ e.getMessage());
		} catch (Exception e) {
			throw new FileParserException(
					"Error occurred when parsing Json file. " + e.getMessage());
		}
		return listOfRecords;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// new JsonParser().parse("data/reports.json");
	}

}
