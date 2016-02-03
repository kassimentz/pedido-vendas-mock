package com.algaworks.tdd.service;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.algaworks.tdd.email.NotificadorEmail;
import com.algaworks.tdd.model.Pedido;
import com.algaworks.tdd.model.StatusPedido;
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

		List<AcaoLancamentoPedido> acoes = Arrays.asList(pedidos, email, sms);
		pedidoService = new PedidoService(pedidos, acoes);
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
		verify(pedidos).executar(pedido);
	}

	@Test
	public void deveNotificarPorEmail() throws Exception {
		pedidoService.lancar(pedido);
		verify(email).executar(pedido);
	}

	@Test
	public void deveNotificarPorSMS() throws Exception {
		pedidoService.lancar(pedido);
		verify(sms).executar(pedido);
	}

	@Test
	public void devePagarPedidoPendente() throws Exception {
		Long codigoPedido = 135L;

		Pedido pedidoPendente = new Pedido();
		pedidoPendente.setStatus(StatusPedido.PENDENTE);
		when(pedidos.buscarPeloCodigo(codigoPedido)).thenReturn(pedidoPendente);
		Pedido pedidoPago = pedidoService.pagar(codigoPedido);
		assertEquals(StatusPedido.PAGO, pedidoPago.getStatus());
	}
	
	@Test(expected = StatusPedidoInvalidoException.class)
	public void deveNegarPagamento() throws Exception {
		Long codigoPedido = 135L;

		Pedido pedidoPendente = new Pedido();
		pedidoPendente.setStatus(StatusPedido.PAGO);
		when(pedidos.buscarPeloCodigo(codigoPedido)).thenReturn(pedidoPendente);
		pedidoService.pagar(codigoPedido);
		
	}
}
