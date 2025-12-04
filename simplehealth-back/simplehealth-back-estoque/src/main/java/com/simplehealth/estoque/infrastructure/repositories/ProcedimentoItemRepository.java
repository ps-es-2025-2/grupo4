package com.simplehealth.estoque.infrastructure.repositories;

import com.simplehealth.estoque.domain.entity.ProcedimentoItem;
import java.util.UUID;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcedimentoItemRepository extends CassandraRepository<ProcedimentoItem, UUID> {

}
