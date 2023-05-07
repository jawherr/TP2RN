package com.neo.repository;


import com.neo.entity.Dsource;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface DsourceRepository extends JpaRepository<Dsource, Integer> {
}
