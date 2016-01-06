package org.jhipster.health.repository.search;

import org.jhipster.health.domain.Point;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Point entity.
 */
public interface PointSearchRepository extends ElasticsearchRepository<Point, Long> {
}
