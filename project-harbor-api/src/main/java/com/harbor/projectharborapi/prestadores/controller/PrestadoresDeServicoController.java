package com.harbor.projectharborapi.prestadores.controller;

import com.harbor.projectharborapi.barbearia.Barbearia;
import com.harbor.projectharborapi.barbearia.controller.BarbeariaController;
import com.harbor.projectharborapi.prestadores.abstrato.PrestadorDeServico;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/prestadores")
public class PrestadoresDeServicoController {

    private int id = 0;

    @PostMapping
    public ResponseEntity<PrestadorDeServico> cadastrarPrestador(PrestadorDeServico p) {
        if (!validaCpf(p.getCpf())) {
            return ResponseEntity.status(400).build();
        }

        for (int i = 9; i < 20; i++) {
            int minutos = 0;
            p.adicionarHorario(LocalTime.of(i, minutos));
            minutos += 30;
            p.adicionarHorario(LocalTime.of(i, minutos));
        }
        p.setId(++id);
        return ResponseEntity.status(201).body(p);
    }

    @GetMapping
    public ResponseEntity<List<PrestadorDeServico>> getPrestadores(Barbearia b) {
        if (b.getPrestadoresDeServico().isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(b.getPrestadoresDeServico());
    }

    @GetMapping("{barbeariaId}/buscar/{prestadorId}")
    public ResponseEntity<PrestadorDeServico> getPrestadorPorId(@PathVariable int barbeariaId, @PathVariable int prestadorId) {
        List<Barbearia> barbearia = BarbeariaController.getBarbearias().stream().filter(b -> b.getId() == id).toList();

        if (barbearia.isEmpty() || barbearia.get(0).getPrestadoresDeServico().stream().noneMatch(p -> p.getId() == prestadorId)) {
            return ResponseEntity.status(404).build();
        }

        PrestadorDeServico prestadorDeServico = null;

        for (PrestadorDeServico p : barbearia.get(0).getPrestadoresDeServico()) {
            if (p.getId() == prestadorId) {
                prestadorDeServico = p;
            }
        }

        return ResponseEntity.status(200).body(prestadorDeServico);
    }

    private boolean validaCpf(String cpf) {
        return cpf.matches("^([0-9]){3}.([0-9]){3}.([0-9]){3}-([0-9]){2}$");
    }
}
