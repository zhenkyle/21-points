package org.jhipster.health.web.rest.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jhipster.health.domain.util.CustomLocalDateSerializer;
import org.jhipster.health.domain.util.JSR310LocalDateDeserializer;

import java.time.LocalDate;


/**
 * Created by zhenke_cd on 2016/4/26.
 */
public class PointsPerWeekDTO {
    private LocalDate week;
    private Integer points;

    public PointsPerWeekDTO(LocalDate week, Integer points) {
        this.week = week;
        this.points = points;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = JSR310LocalDateDeserializer.class)
    public LocalDate getWeek() {
        return week;
    }

    public void setWeek(LocalDate week) {
        this.week = week;
    }

    @Override
    public String toString() {
        return "PointsThisWeek{" +
            "points=" + points +
            ", week=" + week +
            '}';
    }
}
