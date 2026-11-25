package com.nexus.backend;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nexus.backend.dtos.PedidoEditarRequest;
import com.nexus.backend.entity.Pedido;
import com.nexus.backend.enums.ModalidadePagamento;
import com.nexus.backend.enums.StatusPedido;
import com.nexus.backend.exceptions.DataVencimentoObrigatoriaException;
import com.nexus.backend.exceptions.PedidoJaPagoNaoPodeSerModificadoException;
import com.nexus.backend.exceptions.PedidoNotFoundException;
import com.nexus.backend.repository.PedidoRepository;
import com.nexus.backend.service.PedidoService;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTests {
    
    @Mock 
    PedidoRepository pedidoRepository;

    @InjectMocks 
    PedidoService pedidoService;

    @Test
    void editarPedidoMustThrowWhenPedidoDoesntExist() {

        PedidoEditarRequest fixturePedidoEditar = new PedidoEditarRequest("Nova descrição",  
        null, 
        null, 
        null);

        when(pedidoRepository.findById(9L)).thenReturn(Optional.empty());

        assertThrows(PedidoNotFoundException.class, 
            () -> pedidoService.editarPedido(9L, fixturePedidoEditar));
    } 

    @Test 
    void editarPedidoShouldBlockWhenStatusIsntPendente() {

        PedidoEditarRequest fixturePedidoEditar = new PedidoEditarRequest("Nova descrição", 
        null, 
        null, 
        null);

        Pedido pedidoFixture = new Pedido(
        );

        for (StatusPedido status : StatusPedido.values()) {

            if (status == StatusPedido.PENDENTE)
                continue;

            pedidoFixture.setStatusPedido(status);
            when(pedidoRepository.findById(9L)).thenReturn(Optional.of(pedidoFixture));
            assertThrows(PedidoJaPagoNaoPodeSerModificadoException.class, 
                () -> pedidoService.editarPedido(9L, fixturePedidoEditar));
        
        }
        
    }

    @Test 
    void editarPedidoToPrazoMustThrowWhenNoDataVencimentoIsGiven() {
       
        PedidoEditarRequest fixturePedidoEditar = new PedidoEditarRequest("Nova descrição", 
        
        null, 
        ModalidadePagamento.A_PRAZO, 
        null);

        Pedido pedidoFixture = new Pedido(
        );

        pedidoFixture.setStatusPedido(StatusPedido.PENDENTE);
        pedidoFixture.setModalidadePagamento(ModalidadePagamento.A_VISTA);
        pedidoFixture.setDataVencimento(null);

        when(pedidoRepository.findById(9L)).thenReturn(Optional.of(pedidoFixture));

        assertThrows(DataVencimentoObrigatoriaException.class, 
            () -> pedidoService.editarPedido(9L, fixturePedidoEditar));
    }
    
}
