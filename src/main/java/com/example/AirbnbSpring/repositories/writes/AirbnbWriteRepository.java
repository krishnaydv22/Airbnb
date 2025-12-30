package com.example.AirbnbSpring.repositories.writes;

import com.example.AirbnbSpring.models.Airbnb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirbnbWriteRepository extends JpaRepository<Airbnb, Long> {
}
