package com.tennisscoreboard.model;

import com.tennisscoreboard.exception.InvalidValueException;

public record Players(String firstPlayerName, String secondPlayerName) {
    public Players {
        firstPlayerName = normalize(firstPlayerName);
        secondPlayerName = normalize(secondPlayerName);

        if (firstPlayerName.equals(secondPlayerName)) {
            throw new InvalidValueException("Имена игроков не могут совпадать.");
        }
    }
    public String nameOf(PlayerSide side) {
        return side == PlayerSide.FIRST ? firstPlayerName : secondPlayerName;
    }

    public PlayerSide sideOf(String name) {
        name = normalize(name);
        if (name.equals(firstPlayerName)) {
            return PlayerSide.FIRST;
        } else if (name.equals(secondPlayerName)) {
            return PlayerSide.SECOND;
        }
        throw new InvalidValueException("Игрок с таким именем не найден.");
    }

    private static String normalize(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidValueException("Имя игрока не может быть пустым.");
        }
        return name.strip();
    }
}
