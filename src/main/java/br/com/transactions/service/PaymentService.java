package br.com.transactions.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.transactions.dao.PaymentTrackDAO;
import br.com.transactions.dao.TransactionDAO;
import br.com.transactions.entity.PaymentTrack;
import br.com.transactions.entity.Transaction;

@Service
public class PaymentService {
	
	Logger logger = LoggerFactory.getLogger(PaymentService.class);
	
	
	@Autowired
	TransactionDAO transactionDAO;
	
	@Autowired
	PaymentTrackDAO trackingDAO;
	
	@Autowired
	RemoteServiceHandler remoteServiceHandler;
	
	
	public void executePayment(Transaction payment) {
		List<Transaction> transactions = null;
		
		try {
			transactions = transactionDAO.findByAccountIdOrderByChargeOrderAndDueDate(payment.getAccountId());
			if (transactions == null || transactions.isEmpty())
				return;
			
			executePayment(transactions, payment);
			
		} catch (Exception e) {
			logger.error("Erro ao buscar transações", e);
			return;
		}
		
	}

	private void executePayment(List<Transaction> transactions, Transaction payment) {
		for (Transaction t : transactions) {

			if (payment.getBalance() == 0)
				break;
			
			executePayment(t, payment);
		}
	}
	
	private void executePayment(Transaction transactions, Transaction payment) {
		double paymentBalance = payment.getBalance();
		double transactionBalance = transactions.getBalance();
		double amountToSettle = paymentBalance > -transactionBalance ? -transactionBalance : paymentBalance;
		transactions.setBalance(transactions.getBalance() + amountToSettle);
		payment.setBalance(payment.getBalance() - amountToSettle);
		remoteServiceHandler.updateAccountLimits(payment.getAccountId(), amountToSettle);
		trackPayment(payment, transactions, amountToSettle);
	}
	
	private void trackPayment(Transaction payment, Transaction transaction, double amountToSettle) {
		PaymentTrack tracking = new PaymentTrack();
		tracking.setDebitTransactionId(payment.getId());
		tracking.setCreditTransactionId(transaction.getId());
		tracking.setAmount(amountToSettle);
		try {
			trackingDAO.save(tracking);
		} catch (Exception e) {
			logger.error("Erro ao salvar payment track");
		}
	}
	
}
