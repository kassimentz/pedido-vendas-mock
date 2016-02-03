package com.algaworks.tdd.service;

import com.algaworks.tdd.email.NotificadorEmail;
import com.algaworks.tdd.model.Pedido;
import com.algaworks.tdd.repository.Pedidos;
import com.algaworks.tdd.sms.NotificadorSms;

public class PedidoService {

	private Pedidos pedidos;
	private NotificadorEmail email;
	private NotificadorSms sms;

	public PedidoService(Pedidos pedidos, NotificadorEmail email, NotificadorSms sms) {
		this.pedidos = pedidos;
		this.email = email;
		this.sms = sms;
	}

	public double lancar(Pedido pedido) {
		double imposto = pedido.getValor() * 0.1;
		pedidos.guardar(pedido);
		email.enviar(pedido);
		sms.notificar(pedido);
		return imposto;
	}

}
