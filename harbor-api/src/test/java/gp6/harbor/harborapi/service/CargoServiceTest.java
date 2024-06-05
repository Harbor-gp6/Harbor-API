package gp6.harbor.harborapi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import gp6.harbor.harborapi.domain.cargo.Cargo;
import gp6.harbor.harborapi.domain.cargo.repository.CargoRepository;
import gp6.harbor.harborapi.exception.NaoEncontradoException;
import gp6.harbor.harborapi.service.cargo.CargoService;
import java.util.Optional;
import java.util.List;


public class CargoServiceTest {

    @Mock
    private CargoRepository repository;

    @InjectMocks
    private CargoService service;

    public CargoServiceTest(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Cenario de salvamento de cargo correto")
    void cadastroCorreto(){
        Cargo cargo = new Cargo();
        cargo.setId(1);
        cargo.setNomeCargo("gestor");

        when(repository.save(any(Cargo.class))).thenReturn(cargo);

        Cargo resultado = service.cadastrar(cargo);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(cargo.getId(), resultado.getId());
        Assertions.assertEquals(cargo.getNomeCargo(), resultado.getNomeCargo());
        
        verify(repository).save(cargo);
    }

    @Test
    @DisplayName("Cenario de busca por Id correto")
    void buscaCorreta(){
        Cargo cargo = new Cargo();
        cargo.setId(1);
        cargo.setNomeCargo("Gestor");
        repository.save(cargo);
        Integer idInformado = 1;

        when(repository.findById(idInformado)).thenReturn(Optional.of(cargo));

        Cargo resposta = service.buscarPeloId(idInformado);

        assertEquals(idInformado, resposta.getId());
        assertEquals(cargo.getNomeCargo(), resposta.getNomeCargo());

        verify(repository, times(0)).findAll();
        verify(repository, times(1)).findById(idInformado);
    }

    @Test
    @DisplayName("Cenario de busca por Id incorreto")
    void buscaIncorreta(){
        Integer idInformado = 1;

        when(repository.findById(idInformado)).thenReturn(Optional.empty());

        assertThrows(NaoEncontradoException.class,
        () -> service.buscarPeloId(idInformado));

        
        verify(repository, times(0)).findAll();
        verify(repository, times(1)).findById(idInformado);
    }

    @Test
    @DisplayName("Cenario de buscar todos")
    void buscarTodos(){
        List<Cargo> cargos = List.of(
                new Cargo(),
                new Cargo()
        );

        cargos.get(0).setId(1);
        cargos.get(0).setNomeCargo("Gestor");
        cargos.get(1).setId(2);
        cargos.get(1).setNomeCargo("Cabeleleiro");

        when(repository.findAll()).thenReturn(cargos);

        List<Cargo> resposta = service.buscarTodos();

        assertEquals(cargos.get(1), resposta.get(1));
        assertEquals(cargos.size(), resposta.size());

        verify(repository, times(1)).findAll();
    }
}
