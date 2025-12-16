CREATE TABLE plan (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,

    resume_limit INT NOT NULL,
    jd_limit INT NOT NULL,
    match_limit INT NOT NULL,

    price_monthly DECIMAL(10,2) NOT NULL,
    price_yearly DECIMAL(10,2) NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
