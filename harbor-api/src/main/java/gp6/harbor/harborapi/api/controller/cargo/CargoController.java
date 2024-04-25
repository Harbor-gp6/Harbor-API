package gp6.harbor.harborapi.api.controller.cargo;

import gp6.harbor.harborapi.domain.cargo.Cargo;
import gp6.harbor.harborapi.domain.cargo.repository.CargoRepository;
import gp6.harbor.harborapi.service.cargo.dto.CargoCriacaoDto;
import gp6.harbor.harborapi.service.cargo.dto.CargoListagemDto;
import gp6.harbor.harborapi.service.cargo.dto.CargoMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cargos")
public class CargoController {
    
    @Autowired
    private CargoRepository cargoRepository;

    @PostMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<CargoListagemDto> cadastro (@RequestBody @Valid CargoCriacaoDto novoCargo){
        Cargo cargo = CargoMapper.toEntity(novoCargo);
        Cargo cargoSalvo = cargoRepository.save(cargo);
        CargoListagemDto listagemDto = CargoMapper.toDto(cargoSalvo);

        return ResponseEntity.status(201).body(listagemDto);
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<CargoListagemDto> buscarPeloId(@PathVariable int id){
        Optional<Cargo> cargoOptional = cargoRepository.findById(id);

        if (cargoOptional.isEmpty()){
            return ResponseEntity.status(404).build();
        }

        CargoListagemDto dto = CargoMapper.toDto(cargoOptional.get());
        return ResponseEntity.status(200).body(dto);
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<CargoListagemDto>> buscar(){
        List<Cargo> cargos = cargoRepository.findAll();

        if (cargos.isEmpty()){
            return ResponseEntity.status(204).build();
        }

        List<CargoListagemDto> listaAuxiliar = CargoMapper.toDto(cargos);

        return ResponseEntity.status(200).body(listaAuxiliar);
    }
    
}
