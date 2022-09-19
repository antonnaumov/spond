package com.github.antonnaumov.spond.persistence;

import com.github.antonnaumov.spond.domain.Asteroid;
import com.github.antonnaumov.spond.Utils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Collection;

@ApplicationScoped
public final class AsteroidDAO {
    @Inject
    EntityManager em;

    public Collection<Asteroid> findByStartDateAndEndDate(final LocalDate startDate, final LocalDate endDate) {
        final var q = em.createNamedQuery("Asteroids.findByStartAndDate", AsteroidEntity.class);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        q.setMaxResults(10);
        return q.getResultStream().map(Utils::entityToAsteroid).toList();
    }

    public Collection<LocalDate> findMissingDatesBetweenStartDateAndEndDate(final LocalDate startDate, final LocalDate endDate) {
        final var nativeQuery = "SELECT * FROM generate_series(:startDate, :endDate, interval '1 day') AS dates WHERE dates NOT IN (SELECT approach_date FROM asteroids)";
        final var q = em.createNativeQuery(nativeQuery);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        final var rows = q.getResultList();
        return rows.stream().map(row -> ((Timestamp) row).toLocalDateTime().toLocalDate()).toList();
    }

    public Asteroid findMaxByDiameterByStartAndEndDate(final LocalDate startDate, final LocalDate endDate) {
        final var q = em.createNamedQuery("Asteroids.findTheBiggestByMaxDiameterInYear", AsteroidEntity.class);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        return Utils.entityToAsteroid(q.getSingleResult());
    }

    @Transactional
    public void batchInsert(final Collection<Asteroid> asteroids) {
        for (final var a : asteroids) {
            em.persist(Utils.asteroidToEntity(a));
        }
        em.flush();
    }
}
