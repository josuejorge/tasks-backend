-- V1: schema inicial (reproduz exatamente o schema gerado pelo Hibernate)
-- Tabelas: task, course, audit_log
--
-- Em banco que JA possui essas tabelas (prod), o Flyway faz baseline e NAO roda esta migration
-- (spring.flyway.baseline-on-migrate=true). Em banco vazio (local), ela cria todo o schema.

-- ============================================================
-- Sequence compartilhada do Hibernate
-- Usada por Task (@GeneratedValue sem strategy -> AUTO -> sequence)
-- ============================================================
CREATE SEQUENCE hibernate_sequence
    START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;

-- ============================================================
-- task  (id atribuido pela aplicacao via hibernate_sequence)
-- ============================================================
CREATE TABLE task (
    id        bigint NOT NULL,
    task      varchar(255),
    due_date  date,
    CONSTRAINT task_pkey PRIMARY KEY (id)
);

-- ============================================================
-- course  (id IDENTITY -> default nextval da sequence)
-- ============================================================
CREATE SEQUENCE course_id_seq
    START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;

CREATE TABLE course (
    id          bigint NOT NULL DEFAULT nextval('course_id_seq'),
    name        varchar(255) NOT NULL,
    description text,
    category    varchar(100),
    created_at  timestamp without time zone,
    CONSTRAINT course_pkey PRIMARY KEY (id)
);

ALTER SEQUENCE course_id_seq OWNED BY course.id;

-- ============================================================
-- audit_log  (id IDENTITY -> default nextval da sequence)
-- ============================================================
CREATE SEQUENCE audit_log_id_seq
    START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;

CREATE TABLE audit_log (
    id               bigint NOT NULL DEFAULT nextval('audit_log_id_seq'),
    action           varchar(255) NOT NULL,
    task_id          bigint,
    task_description varchar(255),
    occurred_at      timestamp without time zone NOT NULL,
    CONSTRAINT audit_log_pkey PRIMARY KEY (id)
);

ALTER SEQUENCE audit_log_id_seq OWNED BY audit_log.id;
