package com.prospecta.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	  
	   @ExceptionHandler(IllegalArgumentException.class)
	    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
	        return ResponseEntity.badRequest().body(ex.getMessage());
	    }
	
	   @ExceptionHandler(InvalidFormulaException.class) 
		public ResponseEntity<ErrorDetails> invalidFormulaExceptionHandler(InvalidFormulaException ex, WebRequest req) {
			ErrorDetails ed = new ErrorDetails();
			
			ed.setTimestamp(LocalDateTime.now());
			ed.setMessage(ex.getMessage());
			ed.setUri(req.getDescription(false));
			
			return new ResponseEntity<>(ed, HttpStatus.BAD_REQUEST);
		}
	   @ExceptionHandler(FileEmptyException.class) 
		public ResponseEntity<ErrorDetails> emptyFileExceptionHandler(FileEmptyException ex, WebRequest req) {
			ErrorDetails ed = new ErrorDetails();
			
			ed.setTimestamp(LocalDateTime.now());
			ed.setMessage(ex.getMessage());
			ed.setUri(req.getDescription(false));
			
			return new ResponseEntity<>(ed, HttpStatus.BAD_REQUEST);
		}
	   
	
}
