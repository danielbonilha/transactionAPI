package br.com.transactions.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import br.com.transactions.entity.PaymentTrack;


@RepositoryRestResource(exported = false)
public interface PaymentTrackDAO extends CrudRepository<PaymentTrack, Long> {

}
