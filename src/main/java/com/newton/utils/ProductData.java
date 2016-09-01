package com.newton.utils;

import java.util.List;

public class ProductData {

	Product product;
	List<VariantProduct> variantProducts;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public List<VariantProduct> getVariantProducts() {
		return variantProducts;
	}

	public void setVariantProducts(List<VariantProduct> variantProducts) {
		this.variantProducts = variantProducts;
	}

}