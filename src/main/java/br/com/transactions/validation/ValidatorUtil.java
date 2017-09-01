package br.com.transactions.validation;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import br.com.transactions.entity.Transaction;

public class ValidatorUtil {

	private static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private static Validator validator = factory.getValidator();

	public static void validatePayment(Transaction question) {
		Set<ConstraintViolation<Transaction>> constraintViolations = validator.validate(question);
		
		if (constraintViolations.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (ConstraintViolation<Transaction> cv : constraintViolations) {
				sb.append(cv.getMessage() + ", ");
			}
			
			question.setErrorMessage(sb.substring(0, sb.length() - 2));
		}
	}
	
}
