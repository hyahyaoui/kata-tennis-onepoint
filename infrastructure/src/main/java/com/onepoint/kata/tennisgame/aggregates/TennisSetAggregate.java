package com.onepoint.kata.tennisgame.aggregates;

import com.onepoint.kata.tennisgame.domain.Player;
import com.onepoint.kata.tennisgame.domain.TennisSet;
import com.onepoint.kata.tennisgame.domain.TennisSetScore;
import com.onepoint.kata.tennisgame.events.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.HashMap;
import java.util.Map;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;


@Getter
@NoArgsConstructor
@Aggregate
public class TennisSetAggregate implements TennisSet {

    @AggregateIdentifier
    public String id;
    public Map<String, GameAggregate> games = new HashMap<>();
    private Player firstPlayer;
    private Player secondPlayer;
    private TennisSetScore firstPlayerScore = TennisSetScore.ZERO;
    private TennisSetScore secondPlayerScore = TennisSetScore.ZERO;

    public TennisSetAggregate(String id) {
        this.id = id;
    }

    public void handle(TennisSetStartedEvent event) {
        apply(event);
    }


    public void handle(GameStartedEvent event) {
        apply(event);
    }

    public void handle(PointWonEvent event) {
        apply(event);
    }


    public void handle(GameWonEvent event) {
        apply(event);
    }


    @EventSourcingHandler
    public void on(TennisSetStartedEvent event) {
        this.id = event.getId();
        this.firstPlayer = event.getFirstPlayer();
        this.secondPlayer = event.getSecondPlayer();
    }

    @EventSourcingHandler
    public void on(GameStartedEvent event) {
        GameAggregate game = new GameAggregate(this.id);
        game.on(event);
        this.games.put(game.getId(), game);
    }

    @EventSourcingHandler
    public void on(PointWonEvent event) {
        GameAggregate game = this.games.get(event.getGameId());
        game.on(event);
    }

    @EventSourcingHandler
    public void on(GameWonEvent event) {
        GameAggregate game = this.games.get(event.getGameId());
        game.on(event);
    }


    public void handle(WonEvent event) {
        if ((event instanceof GameWonEvent)) {
            handle((GameWonEvent) event);
        } else {
            handle((PointWonEvent) event);
        }
    }
}
