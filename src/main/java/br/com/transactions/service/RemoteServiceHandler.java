package br.com.transactions.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.transactions.constants.API;
import br.com.transactions.dto.Account;

@Service
public class RemoteServiceHandler {
	
	Logger logger = LoggerFactory.getLogger(RemoteServiceHandler.class);
	
	@Value("${account.service.host}")
	private String accountServiceHost;
	
	@Autowired
	RestTemplate restTemplate;
	

	public void updateAccountLimits(int accountId, double amountToSettle) {
		Account acc = new Account();
		acc.setId(Long.valueOf(accountId));
		acc.setAvailableCreditLimit(amountToSettle);
		acc.setAvailableWithdrawalLimit(amountToSettle);
		
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		
		try {
			String url = accountServiceHost + API.ACCOUNT_LIMIT_SVC + "/" + accountId;
			restTemplate.patchForObject(url, new HttpEntity<Account>(acc), Account.class);
		} catch (Exception e) {
			logger.error("Erro ao conectar no serviço accountService", e);
		}
	}
}
