package gp6.harbor.harborapi.api.controller.endereco;


import gp6.harbor.harborapi.domain.endereco.Endereco;
import gp6.harbor.harborapi.domain.listaobj.ListaObj;
import gp6.harbor.harborapi.dto.endereco.dto.*;
import gp6.harbor.harborapi.service.endereco.EnderecoService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import org.slf4j.Logger;

import java.util.List;


@RestController
@RequestMapping("/enderecos")
@RequiredArgsConstructor
public class EnderecoController {

    private final EnderecoService enderecoService;



    private static final Logger log = LoggerFactory.getLogger(EnderecoController.class);

    @Hidden
    @PostMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<EnderecoListagemDto> cadastrar (@RequestBody @Valid EnderecoCriacaoDto novoEndereco){
        Endereco enderecoSalvo = enderecoService.cadastrar(EnderecoMapper.toEntity(novoEndereco));
        EnderecoListagemDto listagemDto = EnderecoMapper.toDto(enderecoSalvo);
        return ResponseEntity.status(201).body(listagemDto);
    }

    @Hidden
    @GetMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<EnderecoListagemDto> buscarPeloId(@PathVariable int id){
        Endereco endereco = enderecoService.buscarPorId(id);

        EnderecoListagemDto dto = EnderecoMapper.toDto(endereco);
        return ResponseEntity.status(200).body(dto);
    }

    @Hidden
    @GetMapping("/cep")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<EnderecoListagemDto>> buscarPeloCep(@RequestParam String cep){
        List<Endereco> enderecos = enderecoService.buscarPorCep(cep);

        if (enderecos.isEmpty()){
            return ResponseEntity.status(204).build();
        }

        List<EnderecoListagemDto> listaAuxiliar = EnderecoMapper.toDto(enderecos);

        return ResponseEntity.status(200).body(listaAuxiliar);
    }

    @Hidden
    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<EnderecoListagemDto>> buscar(){
        List<Endereco> enderecos = enderecoService.listar();

        if (enderecos.isEmpty()){
            return ResponseEntity.status(204).build();
        }

        List<EnderecoListagemDto> listaAuxiliar = EnderecoMapper.toDto(enderecos);



        return ResponseEntity.status(200).body(listaAuxiliar);
    }

    @Hidden
    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<EnderecoListagemDto> atualizarEndereco(
            @PathVariable int id,
            @RequestBody @Valid EnderecoCriacaoDto enderecoAtualizado){

        enderecoService.buscarPorId(id);
        Endereco endereco = EnderecoMapper.toEntity(enderecoAtualizado);
        endereco.setId(id);
        Endereco enderecoSalvo = enderecoService.cadastrar(endereco);
        EnderecoListagemDto listagemDto = EnderecoMapper.toDto(enderecoSalvo);

        return ResponseEntity.status(200).body(listagemDto);
    }

    @Hidden
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

    @Hidden
    @GetMapping("/cep-cadastrado")
    public ResponseEntity<EnderecoListagemDto> buscarCepCadastrado(@RequestParam String cep) {
        List<Endereco> enderecos = enderecoService.buscarPorCep(cep);

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

    @Hidden
    public void ordenaPorCep(ListaObj<Endereco> listaEnderecos) {
        if (listaEnderecos == null || listaEnderecos.getTamanho() == 0) {
            return;
        }
        sort(listaEnderecos, 0, listaEnderecos.getTamanho() - 1);
    }

    @Hidden
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

    @Hidden
    private void trocar(ListaObj<Endereco> listaEnderecos, int i, int j) {
        Endereco temp = listaEnderecos.getElemento(i);
        listaEnderecos.substitui(listaEnderecos.getElemento(i), listaEnderecos.getElemento(j));
        listaEnderecos.substitui(listaEnderecos.getElemento(j), temp);
    }

    @Hidden
    public int buscaBinariaPorCep(ListaObj<Endereco> listaEnderecos, String cepAlvo) {
        if (listaEnderecos == null || listaEnderecos.getTamanho() == 0) {
            return -1;
        }
        return buscar(listaEnderecos, cepAlvo, 0, listaEnderecos.getTamanho() - 1);
    }

    @Hidden
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
