package com.newton.utils;

/**
 * 
 * 
 * 
 * @author chhagan
 * 
 *         to represent no results response
 * 
 *         {"success":"0","message":"Invalid/Empty JSON","data":{}}
 */

public class NoResults {
	// String success;
	String message;
	Data data;

	// public String getSuccess() {
	// // return success;
	// }

	public void setSuccess(String success) {
		// this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	@Override
	public String toString() {

		return "****************\n" + "message :" + message + "\n" + "data: " + data;
	}

}
