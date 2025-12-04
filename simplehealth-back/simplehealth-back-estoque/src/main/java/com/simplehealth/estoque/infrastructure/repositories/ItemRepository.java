package com.simplehealth.estoque.infrastructure.repositories;

import com.simplehealth.estoque.domain.entity.Item;
import java.util.UUID;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends CassandraRepository<Item, UUID> {

}
