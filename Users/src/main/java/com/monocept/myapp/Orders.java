package com.monocept.myapp;

import java.util.List;

public class Orders {
	private int order_id;
	
	private List<String> order_items;

	public int getOrder_id() {
		return order_id;
	}

	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}

	public List<String> getOrder_items() {
		return order_items;
	}

	public void setOrder_items(List<String> order_items) {
		this.order_items = order_items;
	}

	public Orders(int order_id, List<String> order_items) {
		super();
		this.order_id = order_id;
		this.order_items = order_items;
	}

//	public Orders(int order_id, List<String> order_items) {
//		super();
//		this.order_id = order_id;
//		this.order_items = order_items;
//	}
	

	
	
}
