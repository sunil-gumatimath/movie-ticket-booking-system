# Production database migration

The application still supports `ddl-auto: update` for local development through the default configuration. Production deployments must set:

```bash
JPA_DDL_AUTO=validate
```

and apply reviewed SQL migrations before starting the application.

## Required migration

Back up the database and check for duplicates before adding constraints. The following changes are required for the current entities:

```sql
-- Normalize duplicate/casing data before adding the unique constraint.
UPDATE user_details
SET email = LOWER(TRIM(email));

ALTER TABLE user_details
    ADD CONSTRAINT uk_user_details_email UNIQUE (email);

-- A screen may have many shows. Remove the old one-to-one unique index
-- created by the previous Shows.screen mapping before adding this constraint.
-- Use the actual index name reported by INFORMATION_SCHEMA for the database.
ALTER TABLE shows_table
    DROP INDEX <old_screen_unique_index>;

-- Rename the legacy implicit association column if it exists.
-- Verify the actual type before applying this statement.
ALTER TABLE seat
    CHANGE screen_screen_id screen_id VARCHAR(36) NOT NULL;

ALTER TABLE seat
    ADD CONSTRAINT uk_seat_screen_name UNIQUE (screen_id, seat_name);

ALTER TABLE feedback
    ADD CONSTRAINT uk_feedback_user_movie UNIQUE (user_id, movie_id);
```

Before dropping the old show index, identify it with:

```sql
SELECT INDEX_NAME, NON_UNIQUE
FROM INFORMATION_SCHEMA.STATISTICS
WHERE TABLE_SCHEMA = DATABASE()
  AND TABLE_NAME = 'shows_table'
  AND COLUMN_NAME = 'screen_id';
```

The `NON_UNIQUE` value must be `0` for the legacy one-to-one constraint. Do not run the migration until all duplicate feedback and seat rows have been resolved.

## Deployment configuration

Required production environment variables:

```bash
DB_HOST=<managed-mysql-host>
DB_PORT=3306
DB_NAME=<database>
DB_USERNAME=<least-privilege-user>
DB_PASSWORD=<secret>
JWT_SECRET=<base64-secret-at-least-64-bytes>
JWT_EXPIRATION=86400000
CORS_ALLOWED_ORIGINS=https://app.example.com
JPA_DDL_AUTO=validate
```

The application deliberately fails startup when `JWT_SECRET` is absent or too short for HS512.
