package org.jhipster.health.repository;

import org.jhipster.health.domain.Weight;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the Weight entity.
 */
public interface WeightRepository extends JpaRepository<Weight,Long> {

    @Query("select weight from Weight weight where weight.user.login = ?#{principal.username}")
    List<Weight> findByUserIsCurrentUser();

    Page<Weight> findAllByOrderByTimestampDesc(Pageable pageable);

    List<Weight> findAllByTimestampBetweenOrderByTimestampDesc(ZonedDateTime firstDate, ZonedDateTime secondDate);
}
