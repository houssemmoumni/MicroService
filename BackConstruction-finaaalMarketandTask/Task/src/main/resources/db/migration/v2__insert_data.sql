INSERT INTO role (name)
VALUES
    ('PROJECT_MANAGER');

INSERT INTO user (username, password)
VALUES
    ('admin_user', 'password123'),
    ('client_user', 'password456'),
    ('ouvrier_user', 'password789');

INSERT INTO user_roles (user_id, role_id)
VALUES
    ((SELECT id FROM user WHERE username = 'admin_user'), (SELECT id FROM role WHERE name = 'ADMIN')),
    ((SELECT id FROM user WHERE username = 'client_user'), (SELECT id FROM role WHERE name = 'CLIENT')),
    ((SELECT id FROM user WHERE username = 'ouvrier_user'), (SELECT id FROM role WHERE name = 'OUVRIER'));

INSERT INTO user (username, password)
VALUES
    ('project_manager_user', 'password123');

INSERT INTO user_roles (user_id, role_id)
VALUES
    ((SELECT id FROM user WHERE username = 'project_manager_user'), (SELECT id FROM role WHERE name = 'PROJECT_MANAGER'));