-- Insert categories
INSERT INTO category (description, name) VALUES
                                                      ('Construction metals', 'Steel'),
                                                      ('Building materials', 'Bricks'),
                                                      ('Concrete & cement', 'Cement'),
                                                      ('Pipes & plumbing', 'Pipes'),
                                                      ('Electrical components', 'Wires');

-- Insert materials for 'Steel' category
INSERT INTO material (available_quantity, description, name, price, category_id, status) VALUES
                                                                                             (100, 'Reinforced steel rods for structural support', 'Rebar Steel', 120.00, (SELECT id FROM category WHERE name = 'Bricks'), 'DISPONIBLE'),
                                                                                             (50, 'Steel sheets used in roofing', 'Galvanized Steel Sheets', 300.00, (SELECT id FROM category WHERE name = 'Bricks'), 'A_LOUER'),
                                                                                             (75, 'Structural steel beams for heavy construction', 'Steel Beams', 500.00, (SELECT id FROM category WHERE name = 'Steel'), 'DISPONIBLE'),
                                                                                             (60, 'Corrugated steel used for fencing and construction', 'Corrugated Steel', 180.00, (SELECT id FROM category WHERE name = 'Steel'), 'DISPONIBLE'),
                                                                                             (90, 'Steel pipes for plumbing and construction use', 'Steel Pipes', 220.00, (SELECT id FROM category WHERE name = 'Steel'), 'NON_DISPONIBLE');

-- Insert materials for 'Bricks' category
INSERT INTO material (available_quantity, description, name, price, category_id, status)VALUES
                                                                                             (500, 'Standard red clay bricks for construction', 'Clay Bricks', 0.50, (SELECT id FROM category WHERE name = 'Bricks'), 'DISPONIBLE'),
                                                                                             (400, 'Concrete bricks used for load-bearing walls', 'Concrete Blocks', 1.20, (SELECT id FROM category WHERE name = 'Bricks'), 'A_LOUER'),
                                                                                             (300, 'Fire-resistant bricks for industrial use', 'Fire Bricks', 2.50, (SELECT id FROM category WHERE name = 'Bricks'), 'DISPONIBLE'),
                                                                                             (600, 'Hollow bricks for lightweight walls', 'Hollow Bricks', 0.80, (SELECT id FROM category WHERE name = 'Bricks'), 'NON_DISPONIBLE'),
                                                                                             (200, 'Paving bricks for roads and driveways', 'Paving Bricks', 1.00, (SELECT id FROM category WHERE name = 'Bricks'), 'DISPONIBLE');

-- Insert materials for 'Cement' category
INSERT INTO material (available_quantity, description, name, price, category_id, status) VALUES
                                                                                             (200, 'Ordinary Portland Cement for general construction', 'Portland Cement', 8.00, (SELECT id FROM category WHERE name = 'Cement'), 'DISPONIBLE'),
                                                                                             (150, 'Quick-dry cement for fast construction work', 'Quick-dry Cement', 12.00, (SELECT id FROM category WHERE name = 'Cement'), 'NON_DISPONIBLE'),
                                                                                             (100, 'High-strength cement for heavy structures', 'High-strength Cement', 15.00, (SELECT id FROM category WHERE name = 'Cement'), 'A_LOUER'),
                                                                                             (180, 'White cement used for aesthetic applications', 'White Cement', 10.00, (SELECT id FROM category WHERE name = 'Cement'), 'DISPONIBLE'),
                                                                                             (120, 'Masonry cement used in brickwork', 'Masonry Cement', 9.00, (SELECT id FROM category WHERE name = 'Cement'), 'DISPONIBLE');

-- Insert materials for 'Pipes' category
INSERT INTO material (available_quantity, description, name, price, category_id, status) VALUES
                                                                                             (300, 'PVC pipes for residential plumbing', 'PVC Pipes', 3.50, (SELECT id FROM category WHERE name = 'Pipes'), 'DISPONIBLE'),
                                                                                             (250, 'Copper pipes for water distribution', 'Copper Pipes', 7.00, (SELECT id FROM category WHERE name = 'Pipes'), 'NON_DISPONIBLE'),
                                                                                             (200, 'Galvanized iron pipes for durability', 'Galvanized Iron Pipes', 5.50, (SELECT id FROM category WHERE name = 'Pipes'), 'A_LOUER'),
                                                                                             (180, 'Concrete pipes for sewage systems', 'Concrete Pipes', 10.00, (SELECT id FROM category WHERE name = 'Pipes'), 'DISPONIBLE'),
                                                                                             (220, 'Flexible PEX pipes for plumbing', 'PEX Pipes', 4.00, (SELECT id FROM category WHERE name = 'Pipes'), 'DISPONIBLE');

-- Insert materials for 'Wires' category
INSERT INTO material (available_quantity, description, name, price, category_id, status) VALUES
                                                                                             (500, 'Copper electrical wires for residential wiring', 'Copper Wires', 1.50, (SELECT id FROM category WHERE name = 'Wires'), 'DISPONIBLE'),
                                                                                             (400, 'Aluminum electrical wires for commercial wiring', 'Aluminum Wires', 1.00, (SELECT id FROM category WHERE name = 'Wires'), 'DISPONIBLE'),
                                                                                             (300, 'Fiber optic cables for internet connectivity', 'Fiber Optic Cables', 5.00, (SELECT id FROM category WHERE name = 'Wires'), 'NON_DISPONIBLE'),
                                                                                             (350, 'Coaxial cables for TV and networking', 'Coaxial Cables', 2.00, (SELECT id FROM category WHERE name = 'Wires'), 'DISPONIBLE'),
                                                                                             (450, 'Insulated wiring for industrial use', 'Insulated Wires', 3.00, (SELECT id FROM category WHERE name = 'Wires'), 'A_LOUER');

INSERT INTO role (name)
VALUES
    ('ADMIN'),
    ('CLIENT'),
    ('OUVRIER');

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