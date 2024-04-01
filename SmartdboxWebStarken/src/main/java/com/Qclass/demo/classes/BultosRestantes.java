package com.Qclass.demo.classes;

public class BultosRestantes {

	int bultos_QTY;
	int BultosRecepcionados_QTY;
	
	public BultosRestantes(int bultos_QTY, int bultosRecepcionados_QTY) {
		super();
		this.bultos_QTY = bultos_QTY;
		BultosRecepcionados_QTY = bultosRecepcionados_QTY;
	}

	public int getBultos_QTY() {
		return bultos_QTY;
	}

	public void setBultos_QTY(int bultos_QTY) {
		this.bultos_QTY = bultos_QTY;
	}

	public int getBultosRecepcionados_QTY() {
		return BultosRecepcionados_QTY;
	}

	public void setBultosRecepcionados_QTY(int bultosRecepcionados_QTY) {
		BultosRecepcionados_QTY = bultosRecepcionados_QTY;
	}
	
	
	
}
