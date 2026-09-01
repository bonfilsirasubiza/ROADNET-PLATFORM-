package com.roadnet.repository;

import com.roadnet.model.Connection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConnectionRepository extends JpaRepository<Connection, Long> {

    Optional<Connection> findByConnectionCode(String connectionCode);

    boolean existsByUserAIdAndUserBId(Long userAId, Long userBId);
}
