package com.hello.opa.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
	USER, ADMIN, TEACHER;

	@Override
	public String getAuthority() {
		// TODO Auto-generated method stub
		return name();
	}
}
