-- Disable foreign key checks to simplify table deletion
SET FOREIGN_KEY_CHECKS = 0;

-- Drop all tables if they exist
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS certificate_registry;
DROP TABLE IF EXISTS portfolio_file;
DROP TABLE IF EXISTS advertisements;
DROP TABLE IF EXISTS graduate;
DROP TABLE IF EXISTS employer;
DROP TABLE IF EXISTS recruiting_company;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS advertisement;
DROP TABLE IF EXISTS advertisements;
DROP TABLE IF EXISTS certificate_registry_specialties;
DROP TABLE IF EXISTS company_activities;
DROP TABLE IF EXISTS employers;
DROP TABLE IF EXISTS graduate_employment_statuses;
DROP TABLE IF EXISTS graduate_job_types;
DROP TABLE IF EXISTS graduates;
DROP TABLE IF EXISTS recruiting_companies;
DROP TABLE IF EXISTS specialties;


-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- Create users table (superclass)
CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(191) NOT NULL,
                       email VARCHAR(191) NOT NULL,
                       phone_number VARCHAR(20),
                       password VARCHAR(255) NOT NULL,
                       last_login_role VARCHAR(50),
                       role_type VARCHAR(50)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create graduate table (subclass)
CREATE TABLE graduate (
                          id BIGINT PRIMARY KEY,
                          birth_date DATE,
                          rating DOUBLE,
                          photo_file VARCHAR(255) NOT NULL,
                          job_type VARCHAR(191),
                          employment_status VARCHAR(191),
                          FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create employer table (subclass)
CREATE TABLE employer (
                          id BIGINT PRIMARY KEY,
                          company_name VARCHAR(191),
                          company_url VARCHAR(191),
                          company_activity TEXT,
                          FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create recruiting_company table (subclass)
CREATE TABLE recruiting_company (
                                    id BIGINT PRIMARY KEY,
                                    FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create roles table
CREATE TABLE roles (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(191) NOT NULL UNIQUE,
                       type VARCHAR(50) NOT NULL
) ENGINE=InnoDB;

-- Create user_roles table for many-to-many relationship between users and roles
CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL,
                            role_id BIGINT NOT NULL,
                            PRIMARY KEY (user_id, role_id),
                            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                            FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Create certificate_registry table
CREATE TABLE certificate_registry (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      graduate_id BIGINT NOT NULL,
                                      certificate_number VARCHAR(191),
                                      certificate_date DATE,
                                      specialty VARCHAR(191),
                                      FOREIGN KEY (graduate_id) REFERENCES graduate(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Create portfolio_file table
CREATE TABLE portfolio_file (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                file_path VARCHAR(255) NOT NULL,
                                file_name VARCHAR(255) NOT NULL,
                                graduate_id BIGINT NOT NULL,
                                FOREIGN KEY (graduate_id) REFERENCES graduate(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Create advertisements table
CREATE TABLE advertisements (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                title VARCHAR(255) NOT NULL,
                                work_schedule VARCHAR(255),
                                age_range VARCHAR(255),
                                requirements TEXT,
                                conditions TEXT,
                                description TEXT,
                                type VARCHAR(50),
                                salary INTEGER,
                                employer_id BIGINT,
                                recruiting_company_id BIGINT,
                                FOREIGN KEY (employer_id) REFERENCES employer(id) ON DELETE CASCADE,
                                FOREIGN KEY (recruiting_company_id) REFERENCES recruiting_company(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Inserting roles into the roles table
INSERT INTO roles (name, type) VALUES ('GRADUATE', 'GRADUATE');
INSERT INTO roles (name, type) VALUES ('RECRUITING_COMPANY', 'RECRUITING_COMPANY');
INSERT INTO roles (name, type) VALUES ('EMPLOYER', 'EMPLOYER');
INSERT INTO roles (name, type) VALUES ('ADMINISTRATOR', 'ADMINISTRATOR');
