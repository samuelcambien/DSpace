--
-- The contents of this file are subject to the license and copyright
-- detailed in the LICENSE and NOTICE files at the root of the source
-- tree and available online at
--
-- http://www.dspace.org/license/
--

-- ===============================================================
-- WARNING WARNING WARNING WARNING WARNING WARNING WARNING WARNING
--
-- DO NOT MANUALLY RUN THIS DATABASE MIGRATION. IT WILL BE EXECUTED
-- AUTOMATICALLY (IF NEEDED) BY "FLYWAY" WHEN YOU STARTUP DSPACE.
-- http://flywaydb.org/
-- ===============================================================

CREATE TABLE album
(
    id                      UUID NOT NULL PRIMARY KEY,
    title                   VARCHAR(100) NOT NULL,
    artist                  VARCHAR(100) NOT NULL,
    release_date            TIMESTAMP,
    item_id                 UUID references item(uuid)
);

CREATE INDEX album_user_id_idx ON album(id);
CREATE INDEX album_status_idx ON album(title);
CREATE INDEX album_name_idx on album(artist);
CREATE INDEX album_start_time_idx on album(release_date);