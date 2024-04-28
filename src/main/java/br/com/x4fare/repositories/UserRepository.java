package br.com.x4fare.repositories;

import br.com.x4fare.models.Bus;
import br.com.x4fare.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByName(String name);

}
