package br.com.transactions.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class PaymentTrack implements Serializable {

	private static final long serialVersionUID = 703618648543729676L;

	private Long id;
	private Long creditTransactionId;
	private Long debitTransactionId;
	private double amount;

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCreditTransactionId() {
		return creditTransactionId;
	}

	public void setCreditTransactionId(Long creditTransactionId) {
		this.creditTransactionId = creditTransactionId;
	}

	public Long getDebitTransactionId() {
		return debitTransactionId;
	}

	public void setDebitTransactionId(Long debitTransactionId) {
		this.debitTransactionId = debitTransactionId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((creditTransactionId == null) ? 0 : creditTransactionId.hashCode());
		result = prime * result + ((debitTransactionId == null) ? 0 : debitTransactionId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PaymentTrack other = (PaymentTrack) obj;
		if (Double.doubleToLongBits(amount) != Double.doubleToLongBits(other.amount))
			return false;
		if (creditTransactionId == null) {
			if (other.creditTransactionId != null)
				return false;
		} else if (!creditTransactionId.equals(other.creditTransactionId))
			return false;
		if (debitTransactionId == null) {
			if (other.debitTransactionId != null)
				return false;
		} else if (!debitTransactionId.equals(other.debitTransactionId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PaymentTracking [id=" + id + ", creditTransactionId=" + creditTransactionId + ", debitTransactionId="
				+ debitTransactionId + ", amount=" + amount + "]";
	}

}
