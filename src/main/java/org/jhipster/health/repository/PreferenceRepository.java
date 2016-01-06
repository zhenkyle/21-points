package org.jhipster.health.repository;

import org.jhipster.health.domain.Preference;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Preference entity.
 */
public interface PreferenceRepository extends JpaRepository<Preference,Long> {

}
