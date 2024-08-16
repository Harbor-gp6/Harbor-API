package gp6.harbor.harborapi.domain.servico.repository;

import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.servico.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServicoRepository extends JpaRepository<Servico, Integer>{

    List<Servico> findByIdIn(List<Integer> ids);
    List<Servico> findByEmpresa(Empresa empresa);}
