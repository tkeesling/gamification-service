package com.multiplication.social.gamification.service;

import com.multiplication.social.gamification.domain.LeaderBoardRow;

import java.util.List;

/**
 * Provides methods to access the LeaderBoard with users and scores
 */
public interface LeaderBoardService {

    /**
     * Retrieves the current leader board with the top score users
     *
     * @return the users with the highest score
     */
    List<LeaderBoardRow> getCurrentLeaderBoard();
}
