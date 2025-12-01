package com.simplehealth.estoque.infrastructure.repositories;

import com.simplehealth.estoque.domain.entity.Fornecedor;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FornecedorRepository extends CassandraRepository<Fornecedor, Long> {

}
