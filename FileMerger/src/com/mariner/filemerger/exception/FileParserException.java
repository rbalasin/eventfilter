package com.mariner.filemerger.exception;

/**
 * The exception that occurs during parsing the files
 *
 */
public class FileParserException extends Exception{
	
	public FileParserException(String message) {
		super(message);
	}
	
	public FileParserException(String message, Throwable cause) {
		super(message, cause);
	}

}
