package com.megatron.arenahub.repository;

import com.megatron.arenahub.model.Torneo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TorneoRepository extends JpaRepository<Torneo, Long> {
}
