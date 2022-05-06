package com.divergent.mahavikreta.service;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.divergent.mahavikreta.entity.Log;

public interface LogService {

	public Log save(Log log);
	
	public Log saveErrorLog(String message,String controllerName, String methodName,String exception);

	PageImpl<Log> getLogMessages(Pageable pageable);
}
