package com.alanfolha.helpdesk.api.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alanfolha.helpdesk.api.entity.User;
import com.alanfolha.helpdesk.api.response.Response;
import com.alanfolha.helpdesk.api.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value="API REST USER CONTROLLER")
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins="*")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@ApiOperation(value = "CREATE USER")
	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<User>> create(HttpServletRequest request, @RequestBody User user,
			BindingResult result){
		
		Response<User> response = new Response<User>();
		
		try {
			validateCreateUser(user, result);
			if(result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
				
				return ResponseEntity.badRequest().body(response);
			}
			
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			User userPersisted = (User)userService.createOrUpdate(user);
			
			response.setData(userPersisted);
			
		} catch (DuplicateKeyException dE) {
			response.getErros().add("E-mail already registered");
			return ResponseEntity.badRequest().body(response);
		} catch (Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		
		
		return ResponseEntity.ok(response);
	}
	
	private void validateCreateUser (User user, BindingResult result) {
		if(user.getEmail()== null) {
			result.addError(new ObjectError("User", "Email no information"));
		}
	}
	
	@ApiOperation(value = "UPDATE USER")
	@PutMapping
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<User>> update (HttpServletRequest request, @RequestBody User user,
			BindingResult result) {
		
		Response<User> response = new Response<User>();
		
		try {
			validateUpdateUser(user, result);
			
			if(result.hasErrors()) {
				result.getAllErrors().forEach(erro -> response.getErros().add(erro.getDefaultMessage()));
				
				return ResponseEntity.badRequest().body(response);
			}
			
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			User userPersisted = (User) userService.createOrUpdate(user);
			response.setData(userPersisted);
		} catch (Exception e) {
			response.getErros().add(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);
		}
		
		return ResponseEntity.ok(response);
	}
	
	private void validateUpdateUser (User user, BindingResult result) {
		if(user.getId()== null) {
			result.addError(new ObjectError("User", "Id no information"));
		}
		
		if(user.getEmail()== null) {
			result.addError(new ObjectError("User", "Email no information"));
		}
	}
	
	@ApiOperation(value = "FIND BY ID")
	@GetMapping(value ="{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<User>> findById(@PathVariable("id") String id ){
		Response<User> response = new Response<User>();
		Optional<User> user = userService.findById(id);
		if(user == null) {
			response.getErros().add("Register not found id: "+id);
			return ResponseEntity.badRequest().body(response);
		}
		
		response.setData(user.get());
		
		return ResponseEntity.ok(response);
	}
	
	@ApiOperation(value = "DELETE USER")
	@DeleteMapping(value ="{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<String>> delete(@PathVariable("id") String id ){
		
		Response<String> response = new Response<String>();
		Optional<User> user = userService.findById(id);
		
		if(user == null) {
			response.getErros().add("Register not found id: "+id);
			
			return ResponseEntity.badRequest().body(response);
		}
		
		userService.delete(id);
		
		return ResponseEntity.ok(new Response<String>());
	}
	
	@ApiOperation(value = "FIND ALL USER")
	@GetMapping(value= "{page}/{count}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<Page<User>>> findAll (@PathVariable int page, @PathVariable int count){
		
		Response<Page<User>> response = new Response<Page<User>>();
		Page<User> users = userService.findAll(page, count);
		response.setData(users);
		
		return ResponseEntity.ok(response);
	}

}
