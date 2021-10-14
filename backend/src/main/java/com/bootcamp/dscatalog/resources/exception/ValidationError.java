package com.bootcamp.dscatalog.resources.exception;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {
	private static final long serialVersionUID = 1L;

	private List<FieldMessage> fieldMessage = new ArrayList<>();

	public List<FieldMessage> getFieldMessage() {
		return fieldMessage;
	}
	
	public void addError(String field, String message) {
		fieldMessage.add(new FieldMessage(field, message));
	}
}
