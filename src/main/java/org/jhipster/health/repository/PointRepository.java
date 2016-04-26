package org.jhipster.health.repository;

import org.jhipster.health.domain.Point;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data JPA repository for the Point entity.
 */
public interface PointRepository extends JpaRepository<Point,Long> {

    @Query("select point from Point point where point.user.login = ?#{principal.username}")
    List<Point> findByUserIsCurrentUser();

    @Query("select point from Point point where point.user.login = ?#{principal.username} order by point.date desc")
    Page<Point> findAllForCurrentUser(Pageable pageable);

    Page<Point> findAllByOrderByDateDesc(Pageable pageable);

    List<Point> findAllByDateBetween(LocalDate firstDate, LocalDate secondDate);
}
