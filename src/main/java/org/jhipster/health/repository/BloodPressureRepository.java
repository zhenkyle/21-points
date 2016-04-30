package org.jhipster.health.repository;

import org.jhipster.health.domain.BloodPressure;

import org.springframework.data.jpa.repository.*;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the BloodPressure entity.
 */
public interface BloodPressureRepository extends JpaRepository<BloodPressure,Long> {

    @Query("select bloodPressure from BloodPressure bloodPressure where bloodPressure.user.login = ?#{principal.username}")
    List<BloodPressure> findByUserIsCurrentUser();
    List<BloodPressure> findAllByTimestampBetweenOrderByTimestampDesc(ZonedDateTime firstDate,
                                                                      ZonedDateTime secondDate);
}
