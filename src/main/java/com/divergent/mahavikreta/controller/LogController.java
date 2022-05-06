package com.divergent.mahavikreta.controller;

import java.text.ParseException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.divergent.mahavikreta.constants.MsgConstants;
import com.divergent.mahavikreta.constants.UriConstants;
import com.divergent.mahavikreta.entity.Log;
import com.divergent.mahavikreta.service.LogService;
import com.divergent.mahavikreta.utility.ConversionUtils;
import com.divergent.mahavikreta.utility.ResponseMessage;

/**
 * This class provide Log related Rest API
 * 
 * @see LogService
 * 
 * @author Aakash
 *
 */
@RestController
@RequestMapping(UriConstants.LOG)
public class LogController {

	@Autowired
	private LogService logService;

	/**
	 * This method provides an API for Save logs. This method accept Post Http
	 * request with log {@link Log} object and returns Log data.
	 * 
	 * 
	 * @param log {@link Log}
	 * @return {@link Log}
	 * 
	 * @see LogService
	 */
	@PostMapping(UriConstants.SAVE)
	public ResponseMessage<Log> saveLog(@Valid @RequestBody Log log) {
		return new ResponseMessage<>(HttpStatus.OK.value(), MsgConstants.LOG_MESSAGE_SAVED, logService.save(log));
	}

	/**
	 * This method provides an API for Get all Log list. This method accept
	 * Post Http request with pageSize, sortOrder. sortValue and
	 * return PageImpl object.
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @param sortOrder
	 * @param sortValue
	 * 
	 * @return {@link PageImpl<Log>}
	 * @throws ParseException
	 * 
	 * @see LogService
	 */
	@PostMapping(UriConstants.GET_ALL)
	public ResponseMessage<PageImpl<Log>> getAllOrderStatus(@RequestParam("pageIndex") int pageIndex,
			@RequestParam("pageSize") int pageSize,
			@RequestParam(required = false, name = "sortOrder") String sortOrder,
			@RequestParam(required = false, name = "sortValue") String sortValue) throws ParseException {
		return new ResponseMessage<>(HttpStatus.OK.value(),
				logService.getLogMessages(ConversionUtils.createPageObject(pageSize, pageIndex, sortOrder, sortValue)));
	}

}
