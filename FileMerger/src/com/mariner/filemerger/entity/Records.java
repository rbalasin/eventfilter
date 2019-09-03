package com.mariner.filemerger.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "records")
@XmlAccessorType(XmlAccessType.FIELD)
public class Records {

	@XmlElement(name = "report")
	private List<Record> records = null;

	public List<Record> getRecords() {
		return records;
	}

	public void setEmployees(List<Record> records) {
		this.records = records;
	}

}
