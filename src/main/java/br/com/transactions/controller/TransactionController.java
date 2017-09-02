package br.com.transactions.controller;

import java.time.LocalDate;
import java.util.List;

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
import br.com.transactions.constants.OperationType;
import br.com.transactions.dao.TransactionDAO;
import br.com.transactions.entity.Transaction;
import br.com.transactions.service.PaymentService;
import br.com.transactions.service.RemoteServiceHandler;
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
	
	@Autowired
	RemoteServiceHandler remoteServiceHandler;
	
	
	@GetMapping(API.TRANSACTIONS)
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
		
		ValidatorUtil.validatePayment(newOne);
		if (newOne.getErrorMessage() != null) {
			return new ResponseEntity<>(viewHelper.getErrorView(newOne), HttpStatus.BAD_REQUEST);
		}
		
		if (newOne.getOperationType() == 4) {
			newOne.setErrorMessage("Operação não permitida nesta API");
			return new ResponseEntity<>(viewHelper.getErrorView(newOne), HttpStatus.BAD_REQUEST);
		}
		
		newOne.setAmount(-newOne.getAmount());
		newOne.setBalance(newOne.getAmount());
		if (newOne.getEventDate() == null)
			newOne.setEventDate(LocalDate.now());
		newOne.setDueDate(getDueDate(newOne.getEventDate()));
		newOne.setChargeOrder(getChargeOrder(newOne.getOperationType()));
		
		try {
			transactionDAO.save(newOne);
			remoteServiceHandler.updateAccountLimits(newOne.getAccountId(), newOne.getAmount());
			return new ResponseEntity<>(viewHelper.getTransactionView(newOne), HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("Erro ao criar transaction", e);
			newOne.setErrorMessage(e.getMessage());
			return new ResponseEntity<>(viewHelper.getErrorView(newOne), HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@PostMapping(API.PAYMENTS)
	public ResponseEntity<?> executePayment(@RequestBody List<Transaction> payments) {
		
		for (Transaction payment : payments) {
			payment.setId(null);
			
			ValidatorUtil.validatePayment(payment);
			if (payment.getErrorMessage() != null) {
				logger.error("Pagamento inválido. " + payment.toString());
				continue;
			}
			
			payment.setOperationType(4);
			payment.setBalance(payment.getAmount());
			if (payment.getEventDate() == null)
				payment.setEventDate(LocalDate.now());
			payment.setDueDate(getDueDate(payment.getEventDate()));
			
			try {
				transactionDAO.save(payment);
				paymentService.executePayment(payment);
			} catch (Exception e) {
				logger.error("Erro ao executar pagamento : " + payment.toString(), e);
				continue;
			}
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	private LocalDate getDueDate(LocalDate localDate) {
		return localDate.plusMonths(1);
	}
	
	private int getChargeOrder(int operationType) {
		OperationType[] ops = OperationType.values();
		for (OperationType op : ops) {
			if (op.getId() == operationType)
				return op.getChargeOrder();
		}
		return 0;
	}

}
