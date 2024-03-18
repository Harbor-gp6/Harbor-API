package com.harbor.projectharborapi.barbearia.controller;

import com.harbor.projectharborapi.barbearia.Barbearia;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/barbearias")
public class BarbeariaController {

    private static List<Barbearia> barbearias = new ArrayList<>();

    private int id = 0;

    @PostMapping
    public ResponseEntity<Barbearia> cadastrarBarbearia(Barbearia b) {
        if (validaCnpj(b.getCnpj())) {
            return ResponseEntity.status(400).build();
        }
        b.setId(++id);
        if (b.getHorarioAbertura() == null) {
            b.setHorarioAbertura(LocalTime.of(9, 0));
        }
        if (b.getHorarioFechamento() == null) {
            b.setHorarioFechamento(LocalTime.of(19, 0));
        }
        barbearias.add(b);
        return ResponseEntity.status(201).body(b);
    }

    public static List<Barbearia> getBarbearias() {
        return barbearias;
    }

    @GetMapping
    public ResponseEntity<List<Barbearia>> listarBarbearias () {
        if (!barbearias.isEmpty()){
            return ResponseEntity.status(200).body(barbearias);
        }
        return ResponseEntity.status(204).build();
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Barbearia> atualizarBarbearia (@PathVariable int id, @RequestBody Barbearia novaBarbearia) {
        Barbearia barbeariaSelecionada = getBarbeariaPorId(id);
        if (barbeariaSelecionada != null){
            int indiceBarbeariaSelecionada = barbearias.indexOf(barbeariaSelecionada);
            barbearias.set(indiceBarbeariaSelecionada, novaBarbearia);
            return ResponseEntity.status(200).body(novaBarbearia);
        }
        return ResponseEntity.status(404).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarBarbearia (@PathVariable int id) {
        if (getBarbeariaPorId(id) != null){
            barbearias.remove(getBarbeariaPorId(id));
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(404).build();
    }

    private boolean validaCnpj(String cnpj) {
        return cnpj.matches("^[0-9]{2}[.]?[0-9]{3}[.]?[0-9]{3}/?[0-9]{4}-?[0-9]{2}$");
    }

    private Barbearia getBarbeariaPorId (int id) {
        for (Barbearia barbearia : barbearias) {
            if (barbearia.getId() == id){
                return barbearia;
            }
        }
        return null;
    }
}
