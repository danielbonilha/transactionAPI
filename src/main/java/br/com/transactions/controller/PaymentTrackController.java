package br.com.transactions.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import br.com.transactions.constants.API;
import br.com.transactions.dao.PaymentTrackDAO;
import br.com.transactions.entity.PaymentTrack;
import br.com.transactions.entity.Transaction;
import br.com.transactions.view.ViewHelper;

@RestController
@RequestMapping(API.V1)
public class PaymentTrackController {
	
	Logger logger = LoggerFactory.getLogger(PaymentTrackController.class);
	
	@Autowired
	PaymentTrackDAO trackDAO;
	
	@Autowired
	ViewHelper viewHelper;
	

	@GetMapping(API.TRACKING)
	public ResponseEntity<?> getAll() {
		try {
			return new ResponseEntity<Iterable<PaymentTrack>>(trackDAO.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Erro ao buscar payment tracking", e);
			Transaction result = new Transaction();
			result.setErrorMessage(e.getMessage());
			return new ResponseEntity<>(viewHelper.getErrorView(result), HttpStatus.BAD_REQUEST);
		}
	}
}
