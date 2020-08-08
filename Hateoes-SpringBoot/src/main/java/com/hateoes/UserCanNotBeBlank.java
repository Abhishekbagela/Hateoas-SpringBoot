package com.hateoes;

public class UserCanNotBeBlank extends RuntimeException {

	private static final long serialVersionUID = 1L;

	UserCanNotBeBlank(String s) {
		super(s);
	}

}
