package com.onepoint.kata.tennisgame.events;

import com.onepoint.kata.tennisgame.domain.GameScore;
import com.onepoint.kata.tennisgame.domain.Player;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class GameStartedEvent {

    private final String gameId;
    private final String tennisSetId;
    private final GameScore firstPlayerScore;
    private final GameScore secondPlayerScore;

}
