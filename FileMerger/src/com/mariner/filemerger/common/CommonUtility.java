package com.mariner.filemerger.common;

import java.util.HashMap;
import java.util.Map;

public final class CommonUtility {

	/** gets the mapping csv to bean mapping
	 * 
	 * @return the mapping between csv columns and bean field names
	 */
	public static Map<String, String> getCsvToRecordFieldMapping() {
		Map<String, String> mapping = new HashMap<String, String>();

		mapping.put("packets-serviced", "packetsServiced");
		mapping.put("client-guid", "clientGuid");

		mapping.put("packets-requested", "packetsRequested");

		mapping.put("service-guid", "serviceGuid");

		mapping.put("retries-request", "retriesRequest");

		mapping.put("request-time", "requestTimeAsString");

		mapping.put("client-address", "clientAddress");

		mapping.put("max-hole-size", "maxHoleSize");

		return mapping;

	}

}
