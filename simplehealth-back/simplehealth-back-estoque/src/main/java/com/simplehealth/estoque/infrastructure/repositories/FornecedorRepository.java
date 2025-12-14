package com.simplehealth.estoque.infrastructure.repositories;

import com.simplehealth.estoque.domain.entity.Fornecedor;
import java.util.List;
import java.util.UUID;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FornecedorRepository extends CassandraRepository<Fornecedor, UUID> {
}
