-- Disable foreign key checks to simplify table deletion
SET FOREIGN_KEY_CHECKS = 0;

-- Drop all tables if they exist
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS recruiting_companies;
DROP TABLE IF EXISTS employers;
DROP TABLE IF EXISTS certificate_registry;
DROP TABLE IF EXISTS graduates;
DROP TABLE IF EXISTS portfolio_file;
DROP TABLE IF EXISTS advertisements;
DROP TABLE IF EXISTS company_activities;
DROP TABLE IF EXISTS graduate_job_types;
DROP TABLE IF EXISTS graduate_employment_statuses;
DROP TABLE IF EXISTS users;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- Create users table
CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(191) NOT NULL,
                       email VARCHAR(191) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       phone_number VARCHAR(20),
                       last_login_role VARCHAR(50),
                       photo_path VARCHAR(255),
                       user_type VARCHAR(50) NOT NULL,
                       UNIQUE (email, user_type),
                       UNIQUE (phone_number, user_type)
) ENGINE=InnoDB;

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

-- Create recruiting_companies table
CREATE TABLE recruiting_companies (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      user_id BIGINT NOT NULL,
                                      role_type VARCHAR(50) NOT NULL,
                                      FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Create employers table
CREATE TABLE employers (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           user_id BIGINT NOT NULL,
                           company_name VARCHAR(191),
                           company_url VARCHAR(191),
                           role_type VARCHAR(50) NOT NULL,
                           FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Create certificate_registry table
CREATE TABLE certificate_registry (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      graduate_id BIGINT NOT NULL,
                                      certificate_number VARCHAR(191),
                                      certificate_date DATE,
                                      specialty VARCHAR(191),
                                      FOREIGN KEY (graduate_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Create graduates table
CREATE TABLE graduates (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           user_id BIGINT NOT NULL,
                           birth_date DATE,
                           rating INTEGER,
                           photo LONGBLOB,
                           photo_extension VARCHAR(10),
                           FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Create portfolio_file table
CREATE TABLE portfolio_file (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                file_data LONGBLOB,
                                file_name VARCHAR(255),
                                file_extension VARCHAR(10),
                                graduate_id BIGINT NOT NULL,
                                FOREIGN KEY (graduate_id) REFERENCES users(id) ON DELETE CASCADE
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
                                FOREIGN KEY (employer_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Create company_activities table
CREATE TABLE company_activities (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    employer_id BIGINT NOT NULL,
                                    activity TEXT,
                                    FOREIGN KEY (employer_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Create graduate_job_types table
CREATE TABLE graduate_job_types (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    graduate_id BIGINT NOT NULL,
                                    job_type VARCHAR(191),
                                    FOREIGN KEY (graduate_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Create graduate_employment_statuses table
CREATE TABLE graduate_employment_statuses (
                                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                              graduate_id BIGINT NOT NULL,
                                              employment_status VARCHAR(191),
                                              FOREIGN KEY (graduate_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;
