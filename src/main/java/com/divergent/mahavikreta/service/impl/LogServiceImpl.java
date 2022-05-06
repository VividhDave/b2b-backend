package com.divergent.mahavikreta.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.divergent.mahavikreta.entity.Log;
import com.divergent.mahavikreta.repository.LogRepository;
import com.divergent.mahavikreta.service.LogService;

@Service
public class LogServiceImpl implements LogService {

	@Autowired
	private LogRepository logRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Log save(Log log) {
		return logRepository.save(log);
	}

	@Override
	public PageImpl<Log> getLogMessages(Pageable pageable) {
		List<Log> workEffortMaps = null;
		long count;
		Page<Log> page;
		page = logRepository.findAllLogs(pageable);
		workEffortMaps = (List<Log>) page.getContent();
		count = page.getTotalElements();

		return new PageImpl<>(workEffortMaps, pageable, count);
	}

	@Override
	public Log saveErrorLog(String message, String controllerName, String methodName, String exception) {
		try {
		Log log=new Log();
		log.setControllerName(controllerName);
		log.setException(exception);
		log.setMessage(message);
		log.setMethodName(methodName);
		return logRepository.save(log);
		}catch(Exception ex) {
			Log log=new Log();
			log.setControllerName("LogService");
			log.setException(ex.getMessage());
			log.setMessage("error in saving log service");
			log.setMethodName("saveErrorLog");
			logRepository.save(log);
		}
		return null;
	}

}
