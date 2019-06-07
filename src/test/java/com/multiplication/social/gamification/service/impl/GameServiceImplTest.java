package com.multiplication.social.gamification.service.impl;

import com.multiplication.social.gamification.domain.GameStats;
import com.multiplication.social.gamification.domain.ScoreCard;
import com.multiplication.social.gamification.repository.BadgeCardRepository;
import com.multiplication.social.gamification.repository.ScoreCardRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.multiplication.social.gamification.domain.Badge.FIRST_WIN;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class GameServiceImplTest {

    private GameServiceImpl gameService;

    @Mock
    private ScoreCardRepository scoreCardRepository;

    @Mock
    private BadgeCardRepository badgeCardRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        gameService = new GameServiceImpl(scoreCardRepository, badgeCardRepository);
    }

    @Test
    public void processFirstCorrectAttempt() {
        // given
        Long userId = 6L;
        Long attemptId = 1L;
        int totalScore = 10;
        ScoreCard scoreCard = new ScoreCard(userId, attemptId);
        given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(totalScore);
        given(scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId)).willReturn(singletonList(scoreCard));
        given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId)).willReturn(emptyList());

        // when
        GameStats expected = gameService.newAttemptforUser(userId, attemptId, true);

        // then
        assertThat(expected.getScore()).isEqualTo(scoreCard.getScore());
        assertThat(expected.getBadges()).containsOnly(FIRST_WIN);
    }
}