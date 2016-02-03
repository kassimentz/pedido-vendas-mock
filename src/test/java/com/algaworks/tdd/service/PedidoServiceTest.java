package com.algaworks.tdd.service;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.algaworks.tdd.email.NotificadorEmail;
import com.algaworks.tdd.model.Pedido;
import com.algaworks.tdd.model.builder.PedidoBuilder;
import com.algaworks.tdd.repository.Pedidos;
import com.algaworks.tdd.sms.NotificadorSms;

public class PedidoServiceTest {

	private PedidoService pedidoService;
	private Pedido pedido;

	@Mock
	private Pedidos pedidos;
	
	@Mock
	private NotificadorEmail email;
	
	@Mock
	private NotificadorSms sms; 

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		pedidoService = new PedidoService(pedidos, email, sms);
		pedido = new PedidoBuilder().comValor(100.0).para("Joao", "joao@joao.com", "32323232").construir();
	}

	@Test
	public void deveCalcularOImposto() throws Exception {
		double imposto = pedidoService.lancar(pedido);
		assertEquals(10.0, imposto, 0.0001);

	}

	@Test
	public void deveSalvarPedidoNoBancoDeDados() throws Exception {
		pedidoService.lancar(pedido);
		Mockito.verify(pedidos).guardar(pedido);
	}
	
	@Test
	public void deveNotificarPorEmail() throws Exception {
		pedidoService.lancar(pedido);
		Mockito.verify(email).enviar(pedido);
	}

	@Test
	public void deveNotificarPorSMS() throws Exception {
		pedidoService.lancar(pedido);
		Mockito.verify(sms).notificar(pedido);
	}
}
