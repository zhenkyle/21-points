package org.jhipster.health.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.jhipster.health.domain.Point;
import org.jhipster.health.repository.PointRepository;
import org.jhipster.health.repository.UserRepository;
import org.jhipster.health.repository.search.PointSearchRepository;
import org.jhipster.health.security.AuthoritiesConstants;
import org.jhipster.health.security.SecurityUtils;
import org.jhipster.health.web.rest.dto.PointsPerWeekDTO;
import org.jhipster.health.web.rest.util.HeaderUtil;
import org.jhipster.health.web.rest.util.PaginationUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.util.Calendar.MONDAY;
import static java.util.Calendar.SUNDAY;
import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Point.
 */
@RestController
@RequestMapping("/api")
public class PointResource {

    private final Logger log = LoggerFactory.getLogger(PointResource.class);

    @Inject
    private PointRepository pointRepository;

    @Inject
    private PointSearchRepository pointSearchRepository;

    @Inject
    private UserRepository userRepository;
    /**
     * POST  /points -> Create a new point.
     */
    @RequestMapping(value = "/points",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Point> createPoint(@RequestBody Point point) throws URISyntaxException {
        log.debug("REST request to save Point : {}", point);
        if (point.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("point", "idexists", "A new point cannot already have an ID")).body(null);
        }
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            log.debug("No user passed in, using current user: {}",
                SecurityUtils.getCurrentUserLogin());
                point.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get());
        }
        Point result = pointRepository.save(point);
        pointSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/points/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("point", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /points -> Updates an existing point.
     */
    @RequestMapping(value = "/points",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Point> updatePoint(@RequestBody Point point) throws URISyntaxException {
        log.debug("REST request to update Point : {}", point);
        if (point.getId() == null) {
            return createPoint(point);
        }
        Point result = pointRepository.save(point);
        pointSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("point", point.getId().toString()))
            .body(result);
    }

    /**
     * GET  /points -> get all the points.
     */
    @RequestMapping(value = "/points",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Point>> getAllPoints(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Points");
        Page<Point> page;
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            page = pointRepository.findAllByOrderByDateDesc(pageable);
        } else {
            page = pointRepository.findAllForCurrentUser(pageable);
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/points");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET /points-this-week -> get all the points for the current week.
     */
    @RequestMapping(value = "/points-this-week")
    @Timed
    public ResponseEntity<PointsPerWeekDTO> getPointsThisWeek() {
        // Get current date
        LocalDate now = LocalDate.now();
        // Get first day of week
        LocalDate startOfWeek = now.with(DAY_OF_WEEK, 1);
        // Get last day of week
        LocalDate endOfWeek = now.with(DAY_OF_WEEK,7);
        log.debug("Looking for points between: {} and {}", startOfWeek, endOfWeek);

        List<Point> points = pointRepository.findAllByDateBetween(startOfWeek, endOfWeek);
        // filter by current user and sum the points
        Integer numPoints = points.stream()
            .filter(p -> p.getUser().getLogin().equals(SecurityUtils.getCurrentUserLogin()))
            .mapToInt(p -> p.getExercise() + p.getMeals() + p.getAlcohol())
            .sum();
        log.debug("numPoints: {}", numPoints);
        PointsPerWeekDTO count = new PointsPerWeekDTO(startOfWeek, numPoints);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    /**
     * GET  /points/:id -> get the "id" point.
     */
    @RequestMapping(value = "/points/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Point> getPoint(@PathVariable Long id) {
        log.debug("REST request to get Point : {}", id);
        Point point = pointRepository.findOne(id);
        return Optional.ofNullable(point)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /points/:id -> delete the "id" point.
     */
    @RequestMapping(value = "/points/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePoint(@PathVariable Long id) {
        log.debug("REST request to delete Point : {}", id);
        pointRepository.delete(id);
        pointSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("point", id.toString())).build();
    }

    /**
     * SEARCH  /_search/points/:query -> search for the point corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/points/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Point> searchPoints(@PathVariable String query) {
        log.debug("REST request to search Points for query {}", query);
        return StreamSupport
            .stream(pointSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
