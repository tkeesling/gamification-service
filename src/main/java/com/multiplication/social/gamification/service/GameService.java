package com.multiplication.social.gamification.service;

import com.multiplication.social.gamification.domain.GameStats;

/**
 * This service includes the main logic for gamifying the system
 */
public interface GameService {

    /**
     * Process a new attempt from a given user.
     *
     * @param userId    id of the attempting user
     * @param attemptId id of the attempt which can be used to get extra data, if needed
     * @param correct   indicates whether the attempt was correct or not
     * @return a {@link GameStats} object containing the score and badge cards obtained
     */
    GameStats newAttemptforUser(Long userId, Long attemptId, boolean correct);

    /**
     * Gets the game statistics for a given user
     *
     * @param userId the user to get stats for
     * @return the total statistics for that user
     */
    GameStats retrieveStatsForUser(Long userId);
}
