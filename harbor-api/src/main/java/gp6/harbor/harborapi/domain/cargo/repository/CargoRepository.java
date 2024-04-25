package gp6.harbor.harborapi.domain.cargo.repository;

import gp6.harbor.harborapi.domain.cargo.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CargoRepository extends JpaRepository<Cargo, Integer> {

}
