package br.com.transactions.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import br.com.transactions.entity.Transaction;


@RepositoryRestResource(exported = false)
public interface TransactionDAO extends CrudRepository<Transaction, Long> {

	Transaction findById(Long id);

}
