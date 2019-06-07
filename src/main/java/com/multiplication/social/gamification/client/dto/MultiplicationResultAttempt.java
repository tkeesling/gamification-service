package com.multiplication.social.gamification.client.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.multiplication.social.gamification.client.MultiplicationResultAttemptDeserializer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Identifies the attempt from a user to solve a multiplication.
 */
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@JsonDeserialize(using = MultiplicationResultAttemptDeserializer.class)
public final class MultiplicationResultAttempt {

    private final String userAlias;
    private final int multiplicationFactorA;
    private final int multiplicationFactorB;
    private final int resultAttempt;
    private final boolean correct;

    MultiplicationResultAttempt() {
        this.userAlias = null;
        this.multiplicationFactorA = -1;
        this.multiplicationFactorB = -1;
        this.resultAttempt = -1;
        this.correct = false;
    }
}
