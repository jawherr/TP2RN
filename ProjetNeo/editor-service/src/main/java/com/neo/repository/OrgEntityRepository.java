package com.neo.repository;

import com.neo.entity.OrgEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;

@Repository
@Transactional
public interface OrgEntityRepository extends JpaRepository<OrgEntity, Integer> {
}
