package gp6.harbor.harborapi.repository;

import gp6.harbor.harborapi.entity.Prestador;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PrestadorRepository extends JpaRepository<Prestador, Integer> {
    boolean existsByCpf(String cpf);
}
