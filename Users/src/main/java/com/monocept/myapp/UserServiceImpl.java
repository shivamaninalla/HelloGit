package com.monocept.myapp;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	

	@Override
	public Users createUser(Users user) {

		Users save = userRepository.save(user);

		return save;
	}

	@Override
	public List<Users> getAllUsers() {

		List<Users> users = userRepository.findAll();

		return users;
	}

	@Override
	public Users getUserById(int id) {
		Users user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found with this id: "+id));
		return user;
	}

	public Users updateUser(int id, Users updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setUser_name(updatedUser.getUser_name());
            user.setMobile_number(updatedUser.getMobile_number());
            return userRepository.save(user);
        }).orElse(null);
    }
	
	
	
	public boolean deleteUser(int id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

	@Override
	public Users getUserOrders(int id) {
		Users user = userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found"));
		ResponseEntity<Orders[]> responseEntity = restTemplate.getForEntity("http://localhost:8083/orders/users/"+id, Orders[].class);
		Orders[] body = responseEntity.getBody();
		List<Orders> asList = Arrays.asList(body);
		user.setOrders(asList);
		return user;
	}
}
