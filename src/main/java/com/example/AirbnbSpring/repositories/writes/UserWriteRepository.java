package com.example.AirbnbSpring.repositories.writes;
import java.util.Optional;

import com.example.AirbnbSpring.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserWriteRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

}