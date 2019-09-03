package com.mariner.filemerger.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.SerializedName;
import com.mariner.filemerger.exception.FileParserException;

@XmlRootElement(name = "report")
@XmlAccessorType(XmlAccessType.FIELD)
public class Record {

	@SerializedName("packets-serviced")
	@XmlElement(name = "packets-serviced")
	private int packetsServiced;

	@SerializedName("client-guid")
	@XmlElement(name = "client-guid")
	private String clientGuid;

	@SerializedName("packets-requested")
	@XmlElement(name = "packets-requested")
	private int packetsRequested;

	@SerializedName("service-guid")
	@XmlElement(name = "service-guid")
	private String serviceGuid;

	@SerializedName("retries-request")
	@XmlElement(name = "retries-request")
	private int retriesRequest;

	@SerializedName("request-time")
	@XmlElement(name = "request-time")
	private String requestTimeAsString;

	private Date requestTime;

	@SerializedName("client-address")
	@XmlElement(name = "client-address")
	private String clientAddress;

	@SerializedName("max-hole-size")
	@XmlElement(name = "max-hole-size")
	private String maxHoleSize;

	public int getPacketsServiced() {
		return this.packetsServiced;
	}

	public void setPacketsServiced(int packetsServiced) {
		this.packetsServiced = packetsServiced;
	}

	public String getClientGuid() {
		return this.clientGuid;
	}

	public void setClientGuid(String clientGuid) {
		this.clientGuid = clientGuid;
	}

	public int getPacketsRequested() {
		return this.packetsRequested;
	}

	public void setPacketsRequested(int packetsRequested) {
		this.packetsRequested = packetsRequested;
	}

	public String getServiceGuid() {
		return this.serviceGuid;
	}

	public void setServiceGuid(String serviceGuid) {
		this.serviceGuid = serviceGuid;
	}

	public int getRetriesRequest() {
		return this.retriesRequest;
	}

	public void setRetriesRequest(int retriesRequest) {
		this.retriesRequest = retriesRequest;
	}

	public Date getRequestTime() {
		return this.requestTime;
	}

	public void setRequestTime(String time) throws FileParserException {
		String regExp = "^\\d+$";
		boolean matches = Pattern.matches(regExp, time);
		Date date = new Date();
		try {
			if (matches) { //if it is a long number
				Long longTime = Long.valueOf(time);
				TimeZone adtTZ = TimeZone.getTimeZone("Canada/Atlantic");
				Calendar adtCal = Calendar.getInstance(adtTZ);
				adtCal.setTimeInMillis(longTime);
				date = adtCal.getTime();
			} else {

				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss zzz");
				sdf.setTimeZone(TimeZone.getTimeZone("Canada/Atlantic"));
				date = sdf.parse((String) time);
			}
			this.requestTime = date;
		} catch (ParseException e) {
			throw new FileParserException("Request time is in invalid format. "
					+ e.getMessage());
		}
	}

	public String getRequestTimeAsString() {
		// this is used to output the request time to the file
		if (this.requestTime != null) { 
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz");
			sdf.setTimeZone(TimeZone.getTimeZone("Canada/Atlantic"));
			return sdf.format(this.requestTime);
			
		// this is used to set the requestTime in the bean
		} else { 
			return this.requestTimeAsString;
		}
	}

	public void setRequestTimeAsString(String time) throws ParseException {
		this.requestTimeAsString = time;
	}

	public String getClientAddress() {
		return this.clientAddress;
	}

	public void setClientAddress(String address) {
		this.clientAddress = address;
	}

	public String getMaxHoleSize() {
		return this.maxHoleSize;
	}

	public void setMaxHoleSize(String maxHoleSize) {
		this.maxHoleSize = maxHoleSize;
	}

	@Override
	public String toString() {
		return "Record [PacketsServiced=" + packetsServiced + ", ClientGuid="
				+ clientGuid + ", PacketsRequested=" + packetsRequested
				+ ", ServiceGuid = " + serviceGuid + ", RetriesRequest = "
				+ retriesRequest + ", RequestTime=" + requestTime
				+ ", ClientAddress= " + clientAddress + ", MaxHoleSize = "
				+ maxHoleSize + "]";
	}

}
