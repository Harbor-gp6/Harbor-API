package com.harbor.projectharborapi.agendamento.controller;

import com.harbor.projectharborapi.barbearia.Barbearia;
import com.harbor.projectharborapi.barbearia.controller.BarbeariaController;
import com.harbor.projectharborapi.cliente.Cliente;
import com.harbor.projectharborapi.pedido.Pedido;
import com.harbor.projectharborapi.prestadores.abstrato.PrestadorDeServico;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private int id = 0;

    @GetMapping("/{prestadorId}")
    public ResponseEntity<List<Pedido>> getServicosAgendadosPorPrestador(@PathVariable int prestadorId) {
        Barbearia barbearia = null;
        PrestadorDeServico prestadorDeServico;
        for (Barbearia b : BarbeariaController.getBarbearias()) {
            if (b.getPrestadoresDeServico().stream().anyMatch(p -> p.getId() == prestadorId)) {
                barbearia = b;
            }
        }
        if (barbearia != null) {
            prestadorDeServico = (PrestadorDeServico) barbearia.getPrestadoresDeServico().stream().filter(p -> p.getId() == prestadorId);
        } else {
            return ResponseEntity.status(404).build();
        }

        for (int i = 0; i < prestadorDeServico.getServicosAgendados().size(); i++) {
            int indMenor = i;
            for (int j = i + 1; j < prestadorDeServico.getServicosAgendados().size() - 1; j++) {
                if (prestadorDeServico.getServicosAgendados().get(j).getDataHora().isBefore(prestadorDeServico.getServicosAgendados().get(i).getDataHora())) {
                    indMenor = j;
                }
            }
            Pedido aux = prestadorDeServico.getServicosAgendados().get(i);
            prestadorDeServico.getServicosAgendados().set(i, prestadorDeServico.getServicosAgendados().get(indMenor));
            prestadorDeServico.getServicosAgendados().set(indMenor, aux);
        }

        return ResponseEntity.status(200).body(prestadorDeServico.getServicosAgendados());
    }

    @PostMapping
    public ResponseEntity<Pedido> agendarPedido(@RequestBody PrestadorDeServico prestador, @RequestBody Cliente cliente, @RequestBody Pedido novoPedido) {
        if (prestador != null && cliente != null && novoPedido != null){
            novoPedido.setPrestadorDeServico(prestador);
            novoPedido.setCliente(cliente);
            prestador.adicionarPedido(novoPedido);
            return ResponseEntity.status(200).body(novoPedido);
        }
        return ResponseEntity.status(400).build();
    }
}
