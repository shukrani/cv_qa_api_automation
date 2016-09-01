package com.newton.responses;

import com.newton.utils.ProductData;

/**
 * 
 * @author chhagan
 * 
 *         to represent product api response request : GET
 *         http://52.74.175.195:9000/product?productId=4131780&with_variant=true&categoryRequired=true
 *         response url : http://codebeautify.org/jsonviewer/cb5999be
 * 
 */

public class ProductResponse {

	String success;
	String message;
	ProductData data;

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ProductData getData() {
		return data;
	}

	public void setData(ProductData data) {
		this.data = data;
	}

}
