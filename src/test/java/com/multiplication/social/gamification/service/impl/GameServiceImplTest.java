package com.multiplication.social.gamification.service.impl;

import com.multiplication.social.gamification.client.MultiplicationResultAttemptClient;
import com.multiplication.social.gamification.client.dto.MultiplicationResultAttempt;
import com.multiplication.social.gamification.domain.Badge;
import com.multiplication.social.gamification.domain.BadgeCard;
import com.multiplication.social.gamification.domain.GameStats;
import com.multiplication.social.gamification.domain.ScoreCard;
import com.multiplication.social.gamification.repository.BadgeCardRepository;
import com.multiplication.social.gamification.repository.ScoreCardRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class GameServiceImplTest {

    private GameServiceImpl gameService;

    @Mock
    private ScoreCardRepository scoreCardRepository;

    @Mock
    private BadgeCardRepository badgeCardRepository;

    @Mock
    private MultiplicationResultAttemptClient attemptClient;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        gameService = new GameServiceImpl(scoreCardRepository, badgeCardRepository, attemptClient);
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
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt("tyler", 1, 10, 10, true);
        given(attemptClient.retrieveMultiplicationResultAttemptbyId(attemptId)).willReturn(attempt);

        // when
        GameStats actual = gameService.newAttemptforUser(userId, attemptId, true);

        // then
        assertThat(actual.getScore()).isEqualTo(scoreCard.getScore());
        assertThat(actual.getBadges()).containsOnly(Badge.FIRST_WIN);
    }

    @Test
    public void shouldProcessCorrectAttemptForScoreBadge() {
        // given
        Long userId = 1L;
        Long attemptId = 29L;
        int totalScore = 100;
        BadgeCard firstWinBadge = new BadgeCard(userId, Badge.FIRST_WIN);
        given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(totalScore);
        given(scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId)).willReturn(createNScoreCards(10, userId));
        given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId)).willReturn(singletonList(firstWinBadge));
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt("tyler", 1, 10, 10, true);
        given(attemptClient.retrieveMultiplicationResultAttemptbyId(attemptId)).willReturn(attempt);

        // when
        GameStats actual = gameService.newAttemptforUser(userId, attemptId, true);

        // then
        assertThat(actual.getScore()).isEqualTo(ScoreCard.DEFAULT_SCORE);
        assertThat(actual.getBadges()).containsOnly(Badge.BRONZE_MULTIPLICATOR);
    }

    @Test
    public void shouldReturnNoBadgeForWrongAttempt() {
        // given
        Long userId = 1L;
        Long attemptId = 20L;
        int totalScore = 0;
        ScoreCard scoreCard = new ScoreCard(userId, attemptId);
        given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(totalScore);
        given(scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId)).willReturn(singletonList(scoreCard));
        given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId)).willReturn(emptyList());

        // when
        GameStats actual = gameService.newAttemptforUser(userId, attemptId, false);

        // then
        assertThat(actual.getScore()).isEqualTo(0);
        assertThat(actual.getBadges()).isEmpty();
    }

    @Test
    public void shouldRetrieveStatsForUserById() {
        // given
        Long userId = 1L;
        int totalScore = 1000;
        BadgeCard badgeCard = new BadgeCard(userId, Badge.GOLD_MULTIPLICATOR);
        given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(totalScore);
        given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId)).willReturn(singletonList(badgeCard));

        // when
        GameStats actual = gameService.retrieveStatsForUser(userId);

        // then
        assertThat(actual.getScore()).isEqualTo(totalScore);
        assertThat(actual.getBadges()).containsOnly(Badge.GOLD_MULTIPLICATOR);
    }

    @Test
    public void shouldGiveLuckyNumberBadgeWhenAnswerIs42() {
        // given
        Long userId = 1L;
        Long attemptId = 2L;
        int totalScore = 10;
        BadgeCard firstWinBadge = new BadgeCard(userId, Badge.FIRST_WIN);
        given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(totalScore);
        given(scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId)).willReturn(createNScoreCards(1, userId));
        given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId)).willReturn(singletonList(firstWinBadge));
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt("tyler", 42, 10, 420, true);
        given(attemptClient.retrieveMultiplicationResultAttemptbyId(attemptId)).willReturn(attempt);

        // when
        GameStats actual = gameService.newAttemptforUser(userId, attemptId, true);

        // then
        assertThat(actual.getScore()).isEqualTo(ScoreCard.DEFAULT_SCORE);
        assertThat(actual.getBadges()).containsOnly(Badge.LUCKY_NUMBER);
    }

    private List<ScoreCard> createNScoreCards(int n, Long userId) {
        return IntStream.range(0, n)
                .mapToObj(i -> new ScoreCard(userId, (long) i))
                .collect(toList());
    }
}
