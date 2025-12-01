package com.simplehealth.estoque.infrastructure.repositories;

import com.simplehealth.estoque.domain.entity.Pedido;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends CassandraRepository<Pedido, Long> {

}
