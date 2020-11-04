package com.example.demo.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateUserRequest {

	@JsonProperty
	private String username;
	
	//add
	@JsonProperty
    private String password;
	@JsonProperty
    private String confirmPassword;
	
	
	

	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public String getConfirmpassword() {
		return confirmPassword;
	}



	public void setConfirmpassword(String confirmpassword) {
		this.confirmPassword = confirmpassword;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
