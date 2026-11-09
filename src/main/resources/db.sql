CREATE TABLE players (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE matches (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    player1_id BIGINT NOT NULL REFERENCES players(id),
    player2_id BIGINT NOT NULL REFERENCES players(id),
    winner_id BIGINT NOT NULL REFERENCES players(id),

    CONSTRAINT different_players CHECK (player1_id <> player2_id),
    CONSTRAINT winner_is_player CHECK (winner_id IN (player1_id, player2_id))
);

-- Зачем явное объявление индекса, если при UNIQUE он создается по умолчанию? (из чеклиста)
CREATE INDEX player_name ON players(name);
CREATE INDEX matches_player1 ON matches(player1_id);
CREATE INDEX matches_player2 ON matches(player2_id);
