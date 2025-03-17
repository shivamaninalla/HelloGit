package com.monocept.myapp;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity  // ✅ Mark it as an entity
@Table(name = "user")  // ✅ Specify the table name
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int user_id;
	
	
    private List<Orders> orders;


	public int getUser_id() {
		return user_id;
	}


	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}


	public List<Orders> getOrders() {
		return orders;
	}


	public void setOrders(List<Orders> orders) {
		this.orders = orders;
	}


}
