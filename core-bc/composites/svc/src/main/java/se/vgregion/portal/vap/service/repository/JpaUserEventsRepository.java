package se.vgregion.portal.vap.service.repository;

import se.vgregion.dao.domain.patterns.repository.db.jpa.JpaRepository;
import se.vgregion.portal.vap.domain.jpa.UserEvents;

/**
 * @author Patrik Bergström
 */
public interface JpaUserEventsRepository extends JpaRepository<UserEvents, Long, Long>, UserEventsRepository {
}
