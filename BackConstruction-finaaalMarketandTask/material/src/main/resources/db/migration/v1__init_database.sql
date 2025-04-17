USE material;

CREATE TABLE IF NOT EXISTS category (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        description VARCHAR(255),
                                        name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS material (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        name VARCHAR(255),
                                        description VARCHAR(255),
                                        available_quantity DOUBLE NOT NULL,
                                        price DECIMAL(38, 2),
                                        category_id INT,  -- Make sure this column matches the field name in the entity
                                        status ENUM('A_LOUER', 'DISPONIBLE', 'NON_DISPONIBLE'),
                                        FOREIGN KEY (category_id) REFERENCES category(id)
);
CREATE TABLE IF NOT EXISTS user (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    username VARCHAR(255) NOT NULL,
                                    password VARCHAR(255) NOT NULL
);
CREATE TABLE IF NOT EXISTS role (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    name ENUM('ADMIN', 'CLIENT', 'OUVRIER') NOT NULL
);
CREATE TABLE IF NOT EXISTS user_roles (
                                          user_id INT,
                                          role_id INT,
                                          PRIMARY KEY (user_id, role_id),
                                          FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
                                          FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE
);
ALTER TABLE material ADD COLUMN created_by_id INT NOT NULL;
ALTER TABLE material DROP COLUMN created_by_id;


