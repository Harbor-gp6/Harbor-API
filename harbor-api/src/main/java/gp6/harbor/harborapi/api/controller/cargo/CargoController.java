package gp6.harbor.harborapi.api.controller.cargo;

import gp6.harbor.harborapi.domain.cargo.Cargo;
import gp6.harbor.harborapi.domain.cargo.repository.CargoRepository;
import gp6.harbor.harborapi.dto.cargo.dto.CargoCriacaoDto;
import gp6.harbor.harborapi.dto.cargo.dto.CargoListagemDto;
import gp6.harbor.harborapi.dto.cargo.dto.CargoMapper;
import gp6.harbor.harborapi.service.cargo.CargoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cargos")
@RequiredArgsConstructor
public class CargoController {

    private CargoService cargoService;

    @PostMapping
    public ResponseEntity<CargoListagemDto> cadastro (@RequestBody @Valid CargoCriacaoDto novoCargo){
        Cargo cargo = CargoMapper.toEntity(novoCargo);
        Cargo cargoSalvo = cargoService.cadastrar(cargo);
        CargoListagemDto listagemDto = CargoMapper.toDto(cargoSalvo);

        return ResponseEntity.status(201).body(listagemDto);
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<CargoListagemDto> buscarPeloId(@PathVariable int id){

        CargoListagemDto dto = CargoMapper.toDto(cargoService.buscarPeloId(id));
        return ResponseEntity.status(200).body(dto);
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<CargoListagemDto>> buscar(){
        List<Cargo> cargos = cargoService.buscarTodos();

        if (cargos.isEmpty()){
            return ResponseEntity.status(204).build();
        }

        List<CargoListagemDto> listaAuxiliar = CargoMapper.toDto(cargos);

        return ResponseEntity.status(200).body(listaAuxiliar);
    }
    
}
