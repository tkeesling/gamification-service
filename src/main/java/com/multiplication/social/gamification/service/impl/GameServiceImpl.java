package com.multiplication.social.gamification.service.impl;

import com.multiplication.social.gamification.domain.Badge;
import com.multiplication.social.gamification.domain.BadgeCard;
import com.multiplication.social.gamification.domain.GameStats;
import com.multiplication.social.gamification.domain.ScoreCard;
import com.multiplication.social.gamification.repository.BadgeCardRepository;
import com.multiplication.social.gamification.repository.ScoreCardRepository;
import com.multiplication.social.gamification.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.multiplication.social.gamification.domain.Badge.BRONZE_MULTIPLICATOR;
import static com.multiplication.social.gamification.domain.Badge.FIRST_WIN;
import static com.multiplication.social.gamification.domain.Badge.GOLD_MULTIPLICATOR;
import static com.multiplication.social.gamification.domain.Badge.SILVER_MULTIPLICATOR;
import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class GameServiceImpl implements GameService {

    private ScoreCardRepository scoreCardRepository;
    private BadgeCardRepository badgeCardRepository;

    @Autowired
    public GameServiceImpl(ScoreCardRepository scoreCardRepository, BadgeCardRepository badgeCardRepository) {
        this.scoreCardRepository = scoreCardRepository;
        this.badgeCardRepository = badgeCardRepository;
    }

    @Override
    public GameStats newAttemptforUser(Long userId, Long attemptId, boolean correct) {
        if (correct) {
            ScoreCard scoreCard = new ScoreCard(userId, attemptId);
            scoreCardRepository.save(scoreCard);
            log.info("User with id {} scored {} points for attempt id {}", userId, scoreCard.getScore(), attemptId);
            List<BadgeCard> badgeCards = processForBadges(userId, attemptId);
            return new GameStats(userId, scoreCard.getScore(), badgeCards.stream().map(BadgeCard::getBadge).collect(toList()));
        }

        return GameStats.emptyStats(userId);
    }

    /**
     * Gets the stats for a user
     *
     * @param userId the user to get stats for
     * @return the game stats
     */
    @Override
    public GameStats retrieveStatsForUser(Long userId) {
        int score = scoreCardRepository.getTotalScoreForUser(userId);
        List<BadgeCard> badgeCards = badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId);
        return new GameStats(userId, score, badgeCards.stream().map(BadgeCard::getBadge).collect(toList()));
    }

    /**
     * Checks the total score and the different score cards obtained to give new badges in case their conditions are met.
     *
     * @param userId
     * @param attemptId
     * @return≤
     */
    private List<BadgeCard> processForBadges(final Long userId, final Long attemptId) {
        List<BadgeCard> badgeCards = new ArrayList<>();

        int totalScore = scoreCardRepository.getTotalScoreForUser(userId);
        log.info("New score for user {} is {}", userId, totalScore);

        List<ScoreCard> scoreCardList = scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId);
        List<BadgeCard> badgeCardList = badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId);

        // Badges depending on score
        checkAndGiveBadgeBasedOnScore(badgeCardList, BRONZE_MULTIPLICATOR, totalScore, 100, userId)
                .ifPresent(badgeCards::add);

        checkAndGiveBadgeBasedOnScore(badgeCardList, SILVER_MULTIPLICATOR, totalScore, 500, userId)
                .ifPresent(badgeCards::add);

        checkAndGiveBadgeBasedOnScore(badgeCardList, GOLD_MULTIPLICATOR, totalScore, 999, userId)
                .ifPresent(badgeCards::add);

        // first badge won
        if (scoreCardList.size() == 1 && !containsBadge(badgeCardList, FIRST_WIN)) {
            BadgeCard firstWinBadge = giveBadgeToUser(FIRST_WIN, userId);
            badgeCards.add(firstWinBadge);
        }

        return badgeCards;
    }

    private Optional<BadgeCard> checkAndGiveBadgeBasedOnScore(final List<BadgeCard> badgeCards, final Badge badge, final int score, final int scoreThreshold, final Long userId) {
        if (score >= scoreThreshold && !containsBadge(badgeCards, badge)) {
            return Optional.of(giveBadgeToUser(badge, userId));
        }
        return Optional.empty();
    }

    /**
     * Checks if the passed list of badges includes the one being checked
     */
    private boolean containsBadge(final List<BadgeCard> badgeCards, final Badge badge) {
        return badgeCards.stream().anyMatch(b -> b.getBadge().equals(badge));
    }

    /**
     * Assigns a new badge to the given user
     */
    private BadgeCard giveBadgeToUser(final Badge badge, final Long userId) {
        BadgeCard badgeCard = new BadgeCard(userId, badge);
        badgeCardRepository.save(badgeCard);
        log.info("User with id {} won a new badge: {}", userId, badge);
        return badgeCard;
    }
}
