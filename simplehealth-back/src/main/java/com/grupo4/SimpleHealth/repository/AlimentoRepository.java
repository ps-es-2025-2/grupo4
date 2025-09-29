package com.grupo4.SimpleHealth.repository;

import com.grupo4.SimpleHealth.entity.Alimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlimentoRepository extends JpaRepository<Alimento, Long> {

}
