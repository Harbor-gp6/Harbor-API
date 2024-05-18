package gp6.harbor.harborapi.service.cargo;

import gp6.harbor.harborapi.domain.cargo.Cargo;
import gp6.harbor.harborapi.domain.cargo.repository.CargoRepository;
import gp6.harbor.harborapi.exception.NaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CargoService {

    private final CargoRepository cargoRepository;


    public Cargo cadastrar(Cargo cargo) {
        return cargoRepository.save(cargo);
    }

    public Cargo buscarPeloId(int id) {
        return cargoRepository.findById(id).orElseThrow(() -> new NaoEncontradoException("Cargo"));
    }

    public List<Cargo> buscarTodos() {
        return cargoRepository.findAll();
    }

}