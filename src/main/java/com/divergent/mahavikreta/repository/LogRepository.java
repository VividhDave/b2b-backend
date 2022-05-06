package com.divergent.mahavikreta.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.divergent.mahavikreta.entity.Log;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {

	@Query("select log from Log log order by log.createdDate DESC ")
	Page<Log> findAllLogs(Pageable pageable);

}
