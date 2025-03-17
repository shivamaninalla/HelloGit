package com.monocept.myapp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/create-user")
	public ResponseEntity<Users> createEmployee(@RequestBody Users user) {
		return new ResponseEntity<Users>(userService.createUser(user),
				HttpStatus.CREATED);
	}
	
	@GetMapping("/all")
    public ResponseEntity<List<Users>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }
	
	 @GetMapping("/{id}")
	    public ResponseEntity<Users> getUserById(@PathVariable int id) {
	     
	       return new ResponseEntity<Users>(userService.getUserById(id),
					HttpStatus.CREATED);
	    }
	 
	 
	 @PutMapping("/update/{id}")
	    public ResponseEntity<Users> updateUser(@PathVariable int id, @RequestBody Users updatedUser) {
	        return new ResponseEntity<Users>(userService.updateUser(id,updatedUser),
					HttpStatus.CREATED);
	    }
	 
	 
	 @DeleteMapping("/delete/{id}")
	    public ResponseEntity<String> deleteUser(@PathVariable int id) {
	        boolean deleted = userService.deleteUser(id);
	        return deleted ? new ResponseEntity<>("User deleted successfully!", HttpStatus.OK) 
	                       : new ResponseEntity<>("User not found!", HttpStatus.NOT_FOUND);
	    }
	 
	 @GetMapping("{id}/orders")
	 public Users getUserOrders(@PathVariable int id) {
		 return userService.getUserOrders(id);
	 }

}
