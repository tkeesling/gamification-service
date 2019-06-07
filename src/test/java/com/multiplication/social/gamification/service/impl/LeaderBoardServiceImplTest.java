package com.multiplication.social.gamification.service.impl;

import com.multiplication.social.gamification.domain.LeaderBoardRow;
import com.multiplication.social.gamification.repository.ScoreCardRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class LeaderBoardServiceImplTest {

    private LeaderBoardServiceImpl leaderBoardService;

    @Mock
    private ScoreCardRepository scoreCardRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        leaderBoardService = new LeaderBoardServiceImpl(scoreCardRepository);
    }

    @Test
    public void getCurrentLeaderBoard() {
        // given
        Long userId = 1L;
        LeaderBoardRow leaderBoardRow = new LeaderBoardRow(userId, 450L);
        List<LeaderBoardRow> expectedLeaderBoardRows = singletonList(leaderBoardRow);
        given(scoreCardRepository.findFirst10()).willReturn(expectedLeaderBoardRows);

        // when
        List<LeaderBoardRow> actual = leaderBoardService.getCurrentLeaderBoard();

        // then
        assertThat(actual).isEqualTo(expectedLeaderBoardRows);
    }

}