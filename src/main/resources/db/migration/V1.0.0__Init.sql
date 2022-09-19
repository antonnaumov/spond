CREATE TABLE asteroids(
    id UUID PRIMARY KEY,
    approach_date DATE NOT NULL,
    neo_id TEXT NOT NULL,
    name TEXT NOT NULL,
    estimated_diameter_min DOUBLE PRECISION DEFAULT 0,
    estimated_diameter_max DOUBLE PRECISION DEFAULT 0,
    miss_distance DOUBLE PRECISION DEFAULT 0
);

CREATE UNIQUE INDEX asteroid_neo_id_uidx ON asteroids(neo_id);
CREATE INDEX asteroid_estimated_approach_date_idx ON asteroids(approach_date);
CREATE INDEX asteroid_estimated_approach_date_desc_idx ON asteroids(approach_date DESC);
CREATE INDEX asteroid_estimated_diameter_min_idx ON asteroids(estimated_diameter_min);
CREATE INDEX asteroid_estimated_diameter_max_idx ON asteroids(estimated_diameter_max);