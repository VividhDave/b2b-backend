package com.divergent.mahavikreta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.divergent.mahavikreta.entity.Role;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByName(String name);
}
