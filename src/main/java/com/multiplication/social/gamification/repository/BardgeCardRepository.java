package com.multiplication.social.gamification.repository;

import com.multiplication.social.gamification.domain.BadgeCard;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Handles data operations for the {@link BadgeCard}
 */
public interface BardgeCardRepository extends CrudRepository<BadgeCard, Long> {

    /**
     * Retrieves all BadgeCards for a given user.
     * @param userId the id of the user to look for BadgeCards
     * @return the list of BadgeCards, sorted by most recent
     */
    List<BadgeCard> findByUserIdOrderByBadgeTimestampDesc(final Long userId);
}
