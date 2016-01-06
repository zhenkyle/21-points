package org.jhipster.health.repository;

import org.jhipster.health.domain.Point;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Point entity.
 */
public interface PointRepository extends JpaRepository<Point,Long> {

    @Query("select point from Point point where point.user.login = ?#{principal.username}")
    List<Point> findByUserIsCurrentUser();

}
