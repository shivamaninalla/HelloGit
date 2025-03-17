package com.monocept.myapp;

import java.util.List;

import org.springframework.stereotype.Service;


public interface UserService {

	Users createUser(Users user);

	List<Users> getAllUsers();

	Users getUserById(int id);

	Users updateUser(int id, Users updatedUser);

	boolean deleteUser(int id);

	Users getUserOrders(int id);
	
	

}
