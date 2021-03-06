package com.onepoint.kata.tennisgame.commands;

import com.onepoint.kata.tennisgame.command.WinPointCommand;
import com.onepoint.kata.tennisgame.domain.Player;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class WinPointCmd implements WinPointCommand {
    @TargetAggregateIdentifier
    @NonNull
    private String tennisSetId;
    @NonNull
    private String gameId;
    @NonNull
    private Player winner;

    public static WinPointCmd from(WinPointCommand cmd) {
        return new WinPointCmd()
                .setGameId(cmd.getGameId())
                .setTennisSetId(cmd.getTennisSetId())
                .setWinner(cmd.getWinner());
    }
}
