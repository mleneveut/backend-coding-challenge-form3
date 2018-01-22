CREATE TABLE payment (
  id UUID NOT NULL,
  type VARCHAR(20) NOT NULL,
  version INTEGER,
  organisation_id UUID,
  attributes json NOT NULL,

  PRIMARY KEY (id)
);
