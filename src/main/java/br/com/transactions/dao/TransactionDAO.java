package br.com.transactions.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import br.com.transactions.entity.Transaction;


@RepositoryRestResource(exported = false)
public interface TransactionDAO extends CrudRepository<Transaction, Long> {

	Transaction findById(Long id);
	
	@Query("select t from Transaction t where t.accountId = ?1 and t.balance < 0 order by t.chargeOrder,t.eventDate")
	List<Transaction> findByAccountIdOrderByChargeOrderAndDueDate(int accountId);

}
