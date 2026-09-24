# Production database migration and schema validation

The application uses `JPA_DDL_AUTO=update` as a local-development default. Production deployments should set:

```bash
JPA_DDL_AUTO=validate
```

and should use a reviewed, versioned migration process before starting the application. This repository does not include Flyway or Liquibase migrations, and this document is a migration checklist/template rather than a complete fresh-install schema.

## Before changing a production database

1. Take and test a backup.
2. Confirm the current table and column names with `INFORMATION_SCHEMA`.
3. Check for duplicate data that would prevent a unique constraint.
4. Review the exact schema diff produced by the current entity mappings.
5. Apply changes in a staging environment first.
6. Run the verification queries below before enabling `JPA_DDL_AUTO=validate`.

## Required entity constraints

The current entities require the following constraints:

- `user_details.email` is unique.
- `shows_table.screen_id` allows many shows for one screen; the legacy one-to-one index must not remain.
- `seat.screen_id` is non-null and the pair `(screen_id, seat_name)` is unique.
- `feedback` is unique for the pair `(user_id, movie_id)`.

Constraint names used by the entities are:

- `uk_user_details_email`
- `uk_seat_screen_name`
- `uk_feedback_user_movie`

Constraint names must be checked against the actual production schema before applying a manually written migration.

## Data checks before adding constraints

Check duplicate emails after normalizing case and surrounding whitespace:

```sql
SELECT LOWER(TRIM(email)) AS normalized_email, COUNT(*) AS row_count
FROM user_details
GROUP BY LOWER(TRIM(email))
HAVING COUNT(*) > 1;
```

Check duplicate seats per screen:

```sql
SELECT screen_id, seat_name, COUNT(*) AS row_count
FROM seat
GROUP BY screen_id, seat_name
HAVING COUNT(*) > 1;
```

Check duplicate feedback per user and movie:

```sql
SELECT user_id, movie_id, COUNT(*) AS row_count
FROM feedback
GROUP BY user_id, movie_id
HAVING COUNT(*) > 1;
```

Resolve duplicate rows using a reviewed data-cleanup plan before adding unique constraints.

## Legacy schema upgrade template

The following statements are a template for an existing database that was created from an older schema. They are intentionally not safe to run unchanged. Confirm every table, column, and index name first.

### 1. Normalize user email values

After resolving duplicate normalized emails, apply the normalization and constraint separately in a maintenance window:

```sql
UPDATE user_details
SET email = LOWER(TRIM(email));

ALTER TABLE user_details
    ADD CONSTRAINT uk_user_details_email UNIQUE (email);
```

If the constraint already exists, do not add it again. Inspect `INFORMATION_SCHEMA.TABLE_CONSTRAINTS` first.

### 2. Remove the legacy one-show-per-screen constraint

The current `Shows.screen` mapping is many-to-one. Find indexes on the `shows_table.screen_id` column:

```sql
SELECT INDEX_NAME, NON_UNIQUE
FROM INFORMATION_SCHEMA.STATISTICS
WHERE TABLE_SCHEMA = DATABASE()
  AND TABLE_NAME = 'shows_table'
  AND COLUMN_NAME = 'screen_id';
```

A legacy unique index has `NON_UNIQUE = 0`. Drop only the verified legacy index, using the actual name returned by the query:

```sql
-- Replace the placeholder with the verified legacy index name.
ALTER TABLE shows_table
    DROP INDEX <verified_legacy_screen_index>;
```

Do not run this statement against a schema where no such legacy index exists.

### 3. Normalize the legacy seat association column

Older schemas may have used the implicit association column `screen_screen_id`; the current entity expects `screen_id`. First inspect the actual columns:

```sql
SELECT COLUMN_NAME, COLUMN_TYPE, IS_NULLABLE
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = DATABASE()
  AND TABLE_NAME = 'seat'
  AND COLUMN_NAME IN ('screen_id', 'screen_screen_id');
```

If `screen_screen_id` exists and `screen_id` does not, verify the data and then run a reviewed rename:

```sql
-- Run only when the verification above confirms this legacy column.
ALTER TABLE seat
    CHANGE screen_screen_id screen_id VARCHAR(36) NOT NULL;
```

If both columns exist, do not run the rename. Reconcile the columns and data first.

### 4. Add seat and feedback constraints

After duplicate rows are resolved:

```sql
ALTER TABLE seat
    ADD CONSTRAINT uk_seat_screen_name UNIQUE (screen_id, seat_name);

ALTER TABLE feedback
    ADD CONSTRAINT uk_feedback_user_movie UNIQUE (user_id, movie_id);
```

Skip any statement whose constraint already exists.

## Post-migration verification

Verify the expected constraints and columns before starting with schema validation:

```sql
SHOW CREATE TABLE user_details;
SHOW CREATE TABLE shows_table;
SHOW CREATE TABLE seat;
SHOW CREATE TABLE feedback;
```

Also confirm that:

- `user_details.email` is unique and normalized.
- `shows_table.screen_id` is not a unique one-to-one relationship.
- `seat.screen_id` exists, is non-null, and pairs with `seat_name` uniquely.
- `feedback(user_id, movie_id)` is unique.
- All foreign keys and non-null columns match the current entities.

Only then start the application with:

```bash
JPA_DDL_AUTO=validate
```

A validation failure indicates that the reviewed schema and current entity mappings are not aligned; do not switch back to `update` in production.

## Production configuration

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
