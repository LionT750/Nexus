package com.nexus.backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexus.backend.dtos.PedidoCriarRequest;
import com.nexus.backend.dtos.PedidoCriarResponse;
import com.nexus.backend.dtos.PedidoDetailResponse;
import com.nexus.backend.dtos.PedidoListarResponse;
import com.nexus.backend.dtos.PedidoPorClienteRequest;
import com.nexus.backend.entity.Pedido;
import com.nexus.backend.service.PedidoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;




@RestController 
@RequestMapping("/pedidos")
@RequiredArgsConstructor 
public class PedidoRestController {
    private final PedidoService pedidoService;

    @PostMapping
    public PedidoCriarResponse criarPedido(@Valid @RequestBody PedidoCriarRequest pedido) {
        return pedidoService.criar(pedido);
    }


    @GetMapping("/{id}")
    public PedidoDetailResponse buscarPedidoPorId(@RequestParam Long id) {
        return pedidoService.buscarPedidoPorId(id);
    }
    

    @GetMapping
    public List<PedidoListarResponse> listarPedidos() {
        return pedidoService.listar();
    }
    
    @GetMapping("/vencidosEmAberto")
    public List<PedidoListarResponse> listarPedidosVencidosEmAberto() {
        return pedidoService.listarPedidosVencidosEmAberto();
    }

    @GetMapping("/cliente")
    public List<PedidoListarResponse> listarPedidosDoCliente(@Valid @RequestParam(required = false) Long idCliente,@Valid @RequestParam(required = false)  String emailCliente,@Valid @RequestParam(required = false)  String cnpjCliente) {
        PedidoPorClienteRequest dto = new PedidoPorClienteRequest(idCliente, cnpjCliente, emailCliente);
        return pedidoService.listarPedidosDoCliente(dto);
    }
    
    
    


}
