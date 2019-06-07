package com.multiplication.social.gamification.repository;

import com.multiplication.social.gamification.domain.LeaderBoardRow;
import com.multiplication.social.gamification.domain.ScoreCard;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScoreCardRepository extends CrudRepository<ScoreCard, Long> {

    /**
     * Gets the total score for a given user, being the sum of the scores of all their ScoreCards
     *
     * @param userId the id of the user to get the total score
     * @return the total score for a given user
     */
    @Query("SELECT SUM(s.score) from com.multiplication.social.gamification.domain.ScoreCard s where s.userId = :userId GROUP BY s.userId")
    int getTotalScoreForUser(@Param("userId") final Long userId);

    /**
     * Retrieves a list of {@link LeaderBoardRow}s representing the Leader Board of users and their total score
     *
     * @return the leader board, sorted by highest score first
     */
    @Query("SELECT NEW com.multiplication.social.gamification.domain.LeaderBoardRow(s.userId, SUM(s.score)) "
            + "FROM com.multiplication.social.gamification.domain.ScoreCard s "
            + "GROUP BY s.userId ORDER BY SUM(s.score) DESC")
    List<LeaderBoardRow> findFirst10();

    /**
     * Retrieves all the ScoreCards for a given user by id
     *
     * @param userId id of the user
     * @return a list containing all of the ScoreCards for the given user, sorted by most recent
     */
    List<ScoreCard> findByUserIdOrderByScoreTimestampDesc(final Long userId);
}
