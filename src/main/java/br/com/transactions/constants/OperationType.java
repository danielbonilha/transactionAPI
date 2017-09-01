package br.com.transactions.constants;

public enum OperationType {
	COMPRA_VISTA(1,2),
	COMPRA_PARCELADA(2,1),
	SAQUE(3,0),
	PAGAMENTO(4,0);
	
	private int id;
	private int chargeOrder;
	
	private OperationType(int id, int chargeOrder) {
		this.id = id;
		this.chargeOrder = chargeOrder;
	}
	
	public int getId() {
		return id;
	}
	
	public int getChargeOrder() {
		return chargeOrder;
	}
	
}
