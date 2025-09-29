package com.grupo4.SimpleHealth.repository;

import com.grupo4.SimpleHealth.entity.Hospitalar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalarRepository extends JpaRepository<Hospitalar, Long> {

}
