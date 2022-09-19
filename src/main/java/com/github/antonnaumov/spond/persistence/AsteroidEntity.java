package com.github.antonnaumov.spond.persistence;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "asteroids")
@Cacheable
@NamedQueries({
        @NamedQuery(name = "Asteroids.findByStartAndDate",
                query = "SELECT a FROM AsteroidEntity a WHERE a.approachDate >= :startDate AND a.approachDate < :endDate ORDER BY a.missDistance DESC",
                hints = @QueryHint(name = "org.hibernate.cacheable", value = "true")),
        @NamedQuery(name = "Asteroids.findTheBiggestByMaxDiameterInYear",
                query = "SELECT a FROM AsteroidEntity a WHERE a.approachDate >= :startDate AND a.approachDate < :endDate AND a.estimatedDiameterMax = (SELECT MAX(estimatedDiameterMax) FROM AsteroidEntity)",
                hints = @QueryHint(name = "org.hibernate.cacheable", value = "true")),
})
public class AsteroidEntity {
    private UUID id;
    private LocalDate approachDate;
    private String neoID;
    private String name;
    private Double estimatedDiameterMin;
    private Double estimatedDiameterMax;
    private Double missDistance;

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    UUID getId() {
        return id;
    }

    void setId(final UUID id) {
        this.id = id;
    }

    @Column(name = "approach_date")
    public LocalDate getApproachDate() {
        return approachDate;
    }

    public void setApproachDate(final LocalDate approachDate) {
        this.approachDate = approachDate;
    }

    @Column(name = "neo_id")
    public String getNeoID() {
        return neoID;
    }

    public void setNeoID(final String neoID) {
        this.neoID = neoID;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Column(name = "estimated_diameter_min")
    public Double getEstimatedDiameterMin() {
        return estimatedDiameterMin;
    }

    public void setEstimatedDiameterMin(final Double estimatedDiameterMin) {
        this.estimatedDiameterMin = estimatedDiameterMin;
    }

    @Column(name = "estimated_diameter_max")
    public Double getEstimatedDiameterMax() {
        return estimatedDiameterMax;
    }

    public void setEstimatedDiameterMax(final Double estimatedDiameterMax) {
        this.estimatedDiameterMax = estimatedDiameterMax;
    }

    @Column(name = "miss_distance")
    public Double getMissDistance() {
        return missDistance;
    }

    public void setMissDistance(final Double missDistance) {
        this.missDistance = missDistance;
    }
}
