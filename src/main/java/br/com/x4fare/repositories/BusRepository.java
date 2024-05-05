package br.com.x4fare.repositories;

import br.com.x4fare.models.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {

    List<Bus> findByName(String name);

}
