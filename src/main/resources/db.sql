CREATE TABLE players (
    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE matches (
    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    player1_id INT NOT NULL,
    player2_id INT NOT NULL,
    winner_id INT,

    FOREIGN KEY (player1_id) REFERENCES players(id),
    FOREIGN KEY (player2_id) REFERENCES players(id),
    FOREIGN KEY (winner_id) REFERENCES players(id)
);
