package com.mariner.filemerger.exception;
/**
 * The exception that occurs during file merge
 *
 */
public class FileMergerException extends Exception{
	
	public FileMergerException(String message) {
		super(message);
	}

	public FileMergerException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
