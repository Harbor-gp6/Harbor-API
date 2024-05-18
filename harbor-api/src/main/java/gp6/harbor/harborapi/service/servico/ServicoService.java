package gp6.harbor.harborapi.service.servico;

import gp6.harbor.harborapi.domain.servico.repository.ServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServicoService {

    private final ServicoRepository servicoRepository;


}
