package com.multiplication.social.gamification.service.impl;

import com.multiplication.social.gamification.domain.LeaderBoardRow;
import com.multiplication.social.gamification.repository.ScoreCardRepository;
import com.multiplication.social.gamification.service.LeaderBoardService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

class LeaderBoardServiceImpl implements LeaderBoardService {

    private ScoreCardRepository scoreCardRepository;

    @Autowired
    public LeaderBoardServiceImpl(final ScoreCardRepository scoreCardRepository) {
        this.scoreCardRepository = scoreCardRepository;
    }

    @Override
    public List<LeaderBoardRow> getCurrentLeaderBoard() {
        return scoreCardRepository.findFirst10();
    }
}
