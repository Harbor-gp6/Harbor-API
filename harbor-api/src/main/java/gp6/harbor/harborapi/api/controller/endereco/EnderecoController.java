package gp6.harbor.harborapi.api.controller.endereco;


import gp6.harbor.harborapi.domain.endereco.Endereco;
import gp6.harbor.harborapi.domain.endereco.repository.EnderecoRepository;
import gp6.harbor.harborapi.domain.listaobj.ListaObj;
import gp6.harbor.harborapi.service.endereco.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/enderecos")
public class
EnderecoController {

    @Autowired
    private EnderecoRepository enderecoRepository;



    private static final Logger log = LoggerFactory.getLogger(EnderecoController.class);


    @PostMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<EnderecoListagemDto> cadastrar (@RequestBody @Valid EnderecoCriacaoDto novoEndereco){
        Endereco endereco = EnderecoMapper.toEntity(novoEndereco);
        Endereco enderecoSalvo = enderecoRepository.save(endereco);
        EnderecoListagemDto listagemDto = EnderecoMapper.toDto(enderecoSalvo);
        return ResponseEntity.status(201).body(listagemDto);
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<EnderecoListagemDto> buscarPeloId(@PathVariable int id){
        Optional<Endereco> enderecoOptional = enderecoRepository.findById(id);

        if (enderecoOptional.isEmpty()){
            return ResponseEntity.status(404).build();
        }

        EnderecoListagemDto dto = EnderecoMapper.toDto(enderecoOptional.get());
        return ResponseEntity.status(200).body(dto);
    }

    @GetMapping("/cep")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<EnderecoListagemDto>> buscarPeloCep(@RequestParam String cep){
        List<Endereco> enderecos = enderecoRepository.findByCep(cep);

        if (enderecos.isEmpty()){
            return ResponseEntity.status(204).build();
        }

        List<EnderecoListagemDto> listaAuxiliar = EnderecoMapper.toDto(enderecos);

        return ResponseEntity.status(200).body(listaAuxiliar);
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<EnderecoListagemDto>> buscar(){
        List<Endereco> enderecos = enderecoRepository.findAll();

        if (enderecos.isEmpty()){
            return ResponseEntity.status(204).build();
        }

        List<EnderecoListagemDto> listaAuxiliar = EnderecoMapper.toDto(enderecos);



        return ResponseEntity.status(200).body(listaAuxiliar);
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<EnderecoListagemDto> atualizarEndereco(
            @PathVariable int id,
            @RequestBody @Valid EnderecoCriacaoDto enderecoAtualizado){

        if (!enderecoRepository.existsById(id)){
            return ResponseEntity.status(404).build();
        }

        Endereco endereco = EnderecoMapper.toEntity(enderecoAtualizado);
        endereco.setId(id);
        Endereco enderecoSalvo = enderecoRepository.save(endereco);
        EnderecoListagemDto listagemDto = EnderecoMapper.toDto(enderecoSalvo);

        return ResponseEntity.status(200).body(listagemDto);
    }

    @GetMapping("/busca")
    @Operation(summary = "Buscar dados do endereço")
    @ApiResponse(responseCode = "200", description = "Dados de endereço")
    public ResponseEntity<EnderecoDto> buscarEndereco(@RequestParam String cep) {

        RestClient client = RestClient.builder()
                .baseUrl("https://viacep.com.br/ws/")
                .messageConverters(httpMessageConverters -> httpMessageConverters.add(new MappingJackson2HttpMessageConverter()))
                .build();

        String raw = client.get()
                .uri(cep + "/json")
                .retrieve()
                .body(String.class);

        log.info("Resposta da API: " + raw);

        EnderecoApiExternaDto endereco = client.get()
                .uri(cep + "/json")
                .retrieve()
                .body(EnderecoApiExternaDto.class);

        if (endereco == null) {
            return ResponseEntity.noContent().build();
        }

        EnderecoDto resposta = new EnderecoDto();
        resposta.setBairro(endereco.getBairro());
        resposta.setCep(endereco.getCep());
        resposta.setCidade(endereco.getCidade());
        resposta.setEstado(endereco.getEstado());
        resposta.setRua(endereco.getRua());

        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/cep-cadastrado")
    public ResponseEntity<EnderecoListagemDto> buscarCepCadastrado(@RequestParam String cep) {
        List<Endereco> enderecos = enderecoRepository.findAll();

        if (enderecos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        ListaObj<Endereco> enderecosListaObj = new ListaObj<>(enderecos.size());

        for (Endereco e : enderecos) {
            enderecosListaObj.adiciona(e);
        }

        ordenaPorCep(enderecosListaObj);

        int indiceEndereco = buscaBinariaPorCep(enderecosListaObj, cep);

        if (indiceEndereco == -1) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(EnderecoMapper.toDto(enderecosListaObj.getElemento(indiceEndereco)));


    }

    public void ordenaPorCep(ListaObj<Endereco> listaEnderecos) {
        if (listaEnderecos == null || listaEnderecos.getTamanho() == 0) {
            return;
        }
        sort(listaEnderecos, 0, listaEnderecos.getTamanho() - 1);
    }

    private void sort(ListaObj<Endereco> listaEnderecos, int indInicio, int indFim) {
        int i = indInicio, j = indFim;
        Endereco pivot = listaEnderecos.getElemento((indFim + indInicio) / 2);

        while (i <= j) {
            while (listaEnderecos.getElemento(i).getCep().compareTo(pivot.getCep()) < 0 && i < listaEnderecos.getTamanho() - 1) {
                i++;
            }
            while (listaEnderecos.getElemento(j).getCep().compareTo(pivot.getCep()) > 0 && j > 0) {
                j--;
            }
            if (i <= j) {
                trocar(listaEnderecos, i, j);
                i++;
                j--;
            }
        }
        if (indInicio < j) {
            sort(listaEnderecos, indInicio, j);
        }
        if (i < indFim) {
            sort(listaEnderecos, i, indFim);
        }
    }

    private void trocar(ListaObj<Endereco> listaEnderecos, int i, int j) {
        Endereco temp = listaEnderecos.getElemento(i);
        listaEnderecos.substitui(listaEnderecos.getElemento(i), listaEnderecos.getElemento(j));
        listaEnderecos.substitui(listaEnderecos.getElemento(j), temp);
    }

    public int buscaBinariaPorCep(ListaObj<Endereco> listaEnderecos, String cepAlvo) {
        if (listaEnderecos == null || listaEnderecos.getTamanho() == 0) {
            return -1;
        }
        return buscar(listaEnderecos, cepAlvo, 0, listaEnderecos.getTamanho() - 1);
    }

    private int buscar(ListaObj<Endereco> listaEnderecos, String cepAlvo, int indInicio, int indFim) {
        while (indInicio <= indFim) {
            int meio = (indFim + indInicio) / 2;
            String cepMeio = listaEnderecos.getElemento(meio).getCep();
            int comparacao = cepAlvo.compareTo(cepMeio);
            if (comparacao == 0) {
                return meio;
            } else if (comparacao < 0) {
                indFim = meio - 1;
            } else {
                indInicio = meio + 1;
            }
        }
        return -1;
    }




    //To-Do
    //buscar por nome
    //No cadastro de Endereco, checar de o novo endereço é exatamente igual
}
