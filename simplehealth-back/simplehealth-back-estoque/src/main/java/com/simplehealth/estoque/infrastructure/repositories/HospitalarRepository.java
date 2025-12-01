package com.simplehealth.estoque.infrastructure.repositories;

import com.simplehealth.estoque.domain.entity.Hospitalar;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalarRepository extends CassandraRepository<Hospitalar, Long> {

}
