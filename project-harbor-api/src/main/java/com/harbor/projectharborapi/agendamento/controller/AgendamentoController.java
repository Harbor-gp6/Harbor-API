package com.harbor.projectharborapi.agendamento.controller;

import com.harbor.projectharborapi.barbearia.Barbearia;
import com.harbor.projectharborapi.barbearia.controller.BarbeariaController;
import com.harbor.projectharborapi.cliente.Cliente;
import com.harbor.projectharborapi.pedido.Pedido;
import com.harbor.projectharborapi.prestadores.abstrato.PrestadorDeServico;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.status(200).body(prestadorDeServico.getServicosAgendados());
    }

    @PostMapping
    public ResponseEntity<Pedido> agendarPedido(@RequestParam String nome, @RequestBody Cliente c, @RequestBody Pedido pedido) {

    }
}
