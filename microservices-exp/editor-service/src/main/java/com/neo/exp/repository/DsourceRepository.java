package com.neo.exp.repository;


import com.neo.exp.domain.Dsource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DsourceRepository extends JpaRepository<Dsource, Integer> {
}
