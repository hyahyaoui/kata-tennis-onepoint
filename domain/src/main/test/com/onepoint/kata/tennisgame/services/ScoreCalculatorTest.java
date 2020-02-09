package com.onepoint.kata.tennisgame.services;

import com.onepoint.kata.tennisgame.command.StartGameCommand;
import com.onepoint.kata.tennisgame.command.WinPointCommand;
import com.onepoint.kata.tennisgame.domain.Game;
import com.onepoint.kata.tennisgame.domain.Player;
import com.onepoint.kata.tennisgame.domain.Score;
import com.onepoint.kata.tennisgame.events.GameStartedEvent;
import com.onepoint.kata.tennisgame.events.PointWonEvent;
import com.onepoint.kata.tennisgame.exceptions.BusinessRuleViolatedException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ScoreCalculatorTest {

    final Player player1 = new Player().setName("player1");
    final Player player2 = new Player().setName("player2");

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void should_compute_score_for_game_started_event() {
        StartGameCommand cmd = mock(StartGameCommand.class);
        when(cmd.getId()).thenReturn("id");
        when(cmd.getFirstPlayer()).thenReturn(player1);
        when(cmd.getSecondPlayer()).thenReturn(player2);
        final GameStartedEvent event = ScoreCalculator.computeAndCreateEvent(cmd);
        assertEquals(event.getFirstPlayer().getName(), "player1");
        assertEquals(event.getSecondPlayer().getName(), "player2");
        assertEquals(event.getFirstPlayerScore(), Score.LOVE);
        assertEquals(event.getSecondPlayerScore(), Score.LOVE);
        assertEquals(event.getId(), "id");
    }

    @Test
    public void should_throw_business_rule_violated_exception_when_wining_point_after_game_won() {
        expectedException.expect(BusinessRuleViolatedException.class);
        expectedException.expectMessage("Game already finished !");
        WinPointCommand cmd = mock(WinPointCommand.class);
        Game game = mock(Game.class);
        when(game.getGameWinner()).thenReturn(new Player());
        ScoreCalculator.computeAndCreateEvent(cmd, game);
    }

    @Test
    public void should_update_score_when_a_player_win_a_point() {
        WinPointCommand cmd = mock(WinPointCommand.class);
        Game game = mock(Game.class);
        when(cmd.getPointWinner()).thenReturn(player1);
        when(game.getFirstPlayer()).thenReturn(player1);
        when(game.getSecondPlayer()).thenReturn(player2);
        when(game.getFirstPlayerScore()).thenReturn(Score.FIFTEEN);
        when(game.getSecondPlayerScore()).thenReturn(Score.FORTY);
        final PointWonEvent pointWonEvent = ScoreCalculator.computeAndCreateEvent(cmd, game);
        assertEquals(pointWonEvent.getFirstPlayerScore(), Score.THIRTY);
        assertEquals(pointWonEvent.getSecondPlayerScore(), Score.FORTY);
    }

    @Test
    public void should_declare_game_winner() {
        WinPointCommand cmd = mock(WinPointCommand.class);
        Game game = mock(Game.class);
        when(game.getFirstPlayerScore()).thenReturn(Score.FORTY);
        when(cmd.getPointWinner()).thenReturn(player1);
        when(game.getFirstPlayer()).thenReturn(player1);
        final PointWonEvent pointWonEvent = ScoreCalculator.computeAndCreateEvent(cmd, game);
        assertEquals(pointWonEvent.getGameWinner().getName(), player1.getName());
    }
}
