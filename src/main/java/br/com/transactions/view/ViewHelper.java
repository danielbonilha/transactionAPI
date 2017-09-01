package br.com.transactions.view;

import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Component;

import br.com.transactions.entity.Transaction;

@Component
public class ViewHelper {
	
	public Object getErrorView(Transaction object) {
		MappingJacksonValue view = new MappingJacksonValue(object);
		view.setSerializationView(Views.ErrorView.class);
		return view;
	}
	
	public Object getTransactionView(Transaction object) {
		MappingJacksonValue view = new MappingJacksonValue(object);
		view.setSerializationView(Views.TransactionView.class);
		return view;
	}

}
