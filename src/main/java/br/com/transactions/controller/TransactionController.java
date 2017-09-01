package br.com.transactions.controller;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import br.com.transactions.constants.API;
import br.com.transactions.dao.TransactionDAO;
import br.com.transactions.entity.Transaction;
import br.com.transactions.service.PaymentService;
import br.com.transactions.validation.ValidatorUtil;
import br.com.transactions.view.ViewHelper;
import br.com.transactions.view.Views;

@RestController
@RequestMapping(API.V1)
public class TransactionController {
	
	Logger logger = LoggerFactory.getLogger(TransactionController.class);

	
	@Autowired
	TransactionDAO transactionDAO;
	
	@Autowired
	ViewHelper viewHelper;
	
	@Autowired
	PaymentService paymentService;
	
	
	@GetMapping
	@JsonView(Views.TransactionView.class)
	public ResponseEntity<?> getAll() {
		try {
			return new ResponseEntity<Iterable<Transaction>>(transactionDAO.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Erro ao buscar accounts", e);
			Transaction result = new Transaction();
			result.setErrorMessage(e.getMessage());
			return new ResponseEntity<>(viewHelper.getErrorView(result), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(API.TRANSACTIONS)
	public ResponseEntity<?> postOne(@RequestBody Transaction newOne) {
		newOne.setId(null);
		
		ValidatorUtil.validateAccount(newOne);
		if (newOne.getErrorMessage() != null) {
			return new ResponseEntity<>(viewHelper.getErrorView(newOne), HttpStatus.BAD_REQUEST);
		}
		
		newOne.setBalance(newOne.getAmount());
		newOne.setEventDate(LocalDate.now());
		newOne.setDueDate(LocalDate.now().plusMonths(1));
		
		try {
			transactionDAO.save(newOne);
			
			if (newOne.getOperationType() == 4) {
				paymentService.executePayment(newOne);
			}
			
			return new ResponseEntity<>(viewHelper.getTransactionView(newOne), HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("Erro ao criar transaction", e);
			newOne.setErrorMessage(e.getMessage());
			return new ResponseEntity<>(viewHelper.getErrorView(newOne), HttpStatus.BAD_REQUEST);
		}
	}
	
	
}
