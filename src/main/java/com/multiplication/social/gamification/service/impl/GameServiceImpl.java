package com.multiplication.social.gamification.service.impl;

import com.multiplication.social.gamification.domain.GameStats;
import com.multiplication.social.gamification.service.GameService;

public class GameServiceImpl implements GameService {

    @Override
    public GameStats newAttemptforUser(Long userId, Long attemptId, boolean correct) {
        return null;
    }

    @Override
    public GameStats retrieveStatsForUser(Long userId) {
        return null;
    }
}
