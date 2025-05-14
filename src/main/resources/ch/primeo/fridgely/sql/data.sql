INSERT INTO product (barcode, name, nameDE, nameFR, description, descriptionDE, descriptionFR,
                      is_default_product, is_bio, is_local, is_low_co2)
VALUES('001', 'Tomato', 'Tomate', 'Tomate', 'Local organic tomato', 'Lokale Bio-Tomate', 'Tomate bio locale', 0, 1, 1, 1),
       ('002', 'Egg', 'Ei', 'Œuf', 'Organic free-range egg', 'Bio-Freilandei', 'Œuf bio de plein air', 0, 1, 0, 1),
       ('003', 'Cheese', 'Käse', 'Fromage - France', 'Imported cheese block from France', 'Importierter Käseblock aus Frankreich','Bloc de fromage importé de France', 0, 0, 0, 0),
       ('004', 'Carrots', 'Karotten', 'Carottes Locales', 'Basket full of local carrots', 'Korb voller lokaler Karotten','Panier plein de carottes locales', 0, 0, 1, 1),
       ('005', 'Butter', 'Butter', 'Beurre', 'Organic butter made from grass-fed cows', 'Bio-Butter aus grasgefütterten Kühen', 'Beurre bio issu de vaches nourries à l''herbe', 0, 1, 0, 0),
       ('006', 'Beef', 'Rindfleisch', 'Bœuf', 'meat', 'Importiertes Rindfleisch aus Argentinien', 'Bœuf importé d''Argentine', 0, 0, 0, 0),
       ('007', 'Milk', 'Milch', 'Lait', 'Fresh organic milk from local farms', 'Frische Bio-Milch von lokalen Bauernhöfen', 'Lait bio frais des fermes locales', 0, 1, 1, 1),
       ('008', 'Spinach', 'Spinat', 'Épinard', 'Local fresh spinach leaves', 'Lokale frische Spinatblätter', 'Feuilles d''épinard fraîches locales', 0, 0, 1, 1),
       ('010', 'Apples', 'Äpfel', 'Pommes', 'Organic apples', 'Bio-Äpfel', 'Pommes biologiques', 0, 1, 0, 1),
       ('011', 'Potatoes', 'Kartoffeln', 'Terre Locales', 'Fresh local potatoes', 'Frische lokale Kartoffeln', 'Pommes de terre locales fraîches', 0, 0, 1, 1),
       ('012', 'Chicken', 'Poulet', 'Poulet', 'meat', 'Bio-Freilandhuhn', 'Poulet bio en liberté', 0, 1, 0, 0),
       ('016', 'Strawberries', 'Erdbeeren', 'Fraises', 'water_intensive', 'Süße lokale Erdbeeren', 'Fraises locales sucrées', 0, 0, 1, 1),
       ('017', 'Garlic', 'Knoblauch', 'Ail', 'Organic garlic bulbs', 'Bio-Knoblauchknollen', 'Bulbes d''ail biologiques', 0, 1, 0, 1),
       ('018', 'Lettuce', 'Salat', 'Laitue', 'Fresh organic locally grown lettuce', 'Frischer Bio-Salat aus lokalem Anbau', 'Laitue bio fraîche cultivée localement', 0, 1, 1, 1),
       ('019', 'Bananas', 'Bananen', 'Bananes', 'water_intensive', 'Importierte Bananen aus Ecuador', 'Bananes importées d''Équateur', 0, 0, 0, 1),
       ('020', 'Tofu', 'Tofu', 'Tofu', 'Organic firm tofu', 'Bio-Tofu fest', 'Tofu bio ferme', 0, 1, 0, 1),
       ('021', 'Orange', 'Orange', 'Orange', 'Sweet juicy oranges', 'Süße saftige Orangen', 'Oranges juteuses et sucrées', 0, 0, 0,0),
       ('022', 'Lemon', 'Zitrone', 'Citron', 'Fresh tangy lemons', 'Frische würzige Zitronen', 'Citrons frais et acidulés', 0, 0, 0,0),
       ('023', 'Onion', 'Zwiebel', 'Oignon', 'Fresh cooking onions', 'Frische Kochzwiebeln', 'Oignons frais de cuisine', 0, 0, 0,0),
       ('024', 'Avocado', 'Avocado', 'Avocat', 'Ripe creamy avocados', 'Reife cremige Avocados', 'Avocats mûrs et crémeux', 0, 0, 0,0),
       ('025', 'Bell Pepper', 'Paprika', 'Poivron', 'Colorful bell peppers', 'Bunte Paprikaschoten', 'Poivrons colorés', 0, 0, 0,0),
       ('026', 'Tuna', 'Thunfisch', 'Thon', 'Canned tuna in water', 'Dosenthunfisch in Wasser', 'Thon en conserve dans l''eau', 0, 0, 0,0),
       ('027', 'Ground Beef', 'Hackfleisch', 'Viande Hachée', 'Fresh ground beef', 'Frisches Hackfleisch', 'Viande hachée fraîche', 0, 0, 0,0),
       ('028', 'Beans', 'Bohnen', 'Haricots', 'Canned beans', 'Dosenbohnen', 'Haricots en conserve', 0, 0, 0,0),
       ('029', 'Corn', 'Mais', 'Maïs', 'Sweet corn kernels', 'Süße Maiskörner', 'Grains de maïs doux', 0, 0, 0,0),
       ('AAA', 'Bread', 'Brot', 'Pain', 'Bread', 'Brot', 'Pain', 1, 0, 0, 1),
       ('BBB', 'Rice', 'Reis', 'Riz', 'White rice', 'Weißer Reis', 'Riz blanc', 1, 0, 0,0),
       ('CCC', 'Pasta', 'Nudeln', 'Pâtes', 'Pasta', 'Nudeln', 'Pâtes', 1, 0, 0,0),
       ('DDD', 'Honey', 'Honig', 'Miel', 'Sweet honey', 'Süßer Honig', 'Miel doux', 1, 0, 0,0),
       ('EEE', 'Olive Oil', 'Olivenöl', 'Huile d''Olive', 'Premium organic extra virgin olive oil', 'Premium Bio-Olivenöl Extra Vergine', 'Huile d''olive bio extra vierge de qualité supérieure', 1, 1, 0, 1);

INSERT INTO recipe (name, nameDE, nameFR, description, descriptionDE, descriptionFR)
VALUES
    -- (1) Scrambled Eggs
    ('Scrambled Eggs', 'Rührei', 'Œufs Brouillés',
     'Fluffy scrambled eggs.', 'Fluffiges Rührei.', 'Œufs brouillés moelleux.'),
    -- (2) Grilled Cheese
    ('Grilled Cheese', 'Käse-Toast', 'Sandwich au Fromage Grillé',
     'Melted cheese sandwich.', 'Geschmolzener Käsesandwich.', 'Sandwich au fromage fondu.'),
    -- (3) Tomato Pasta
    ('Tomato Pasta', 'Tomaten-Pasta', 'Pâtes à la Tomate',
     'Pasta with tomato sauce.', 'Nudeln mit Tomatensoße.', 'Pâtes à la sauce tomate.'),
    -- (4) Chicken Rice
    ('Chicken Rice', 'Hühnchen mit Reis', 'Poulet et Riz',
     'Chicken served with rice.', 'Hühnchen mit Reis serviert.', 'Poulet servi avec du riz.'),
    -- (5) Carrot Soup
    ('Carrot Soup', 'Karottensuppe', 'Soupe de Carottes',
     'Creamy carrot soup.', 'Cremige Karottensuppe.', 'Soupe crémeuse de carottes.'),
    -- (6) Fruit Salad
    ('Fruit Salad', 'Fruchtsalat', 'Salade de Fruits',
     'Mixed fruit pieces.', 'Gemischte Obststücke.', 'Morceaux de fruits variés.'),
    -- (7) Strawberry Banana Smoothie
    ('Strawberry Banana Smoothie', 'Erdbeer-Bananen-Smoothie', 'Smoothie Fraise-Banane',
     'Smoothie with strawberries, bananas and milk.', 'Smoothie mit Erdbeeren, Bananen und Milch.', 'Smoothie aux fraises, bananes et lait.'),
    -- (8) Oatmeal with Apple & Honey
    ('Oatmeal with Apple & Honey', 'Haferbrei mit Apfel und Honig', 'Flocons d''Avoine Pomme & Miel',
   'Warm oats topped with apple and honey.', 'Warmer Haferbrei mit Apfel und Honig.', 'Flocons chauds au miel et à la pomme.'),
  -- (9) Garlic Bread
  ('Garlic Bread', 'Knoblauchbrot', 'Pain à l''Ail',
     'Toasted garlic bread.', 'Geröstetes Knoblauchbrot.', 'Pain à l''ail grillé.'),
  -- (10) Spinach Pasta
  ('Spinach Pasta', 'Spinat-Pasta', 'Pâtes aux Épinards',
   'Pasta with creamy spinach sauce.', 'Nudeln mit cremiger Spinatsauce.', 'Pâtes à la sauce crémeuse aux épinards.'),
  -- (11) Beef Stew
  ('Beef Stew', 'Rindfleischeintopf', 'Ragoût de Bœuf',
   'Hearty beef stew with vegetables.', 'Herzhafter Rindfleischeintopf mit Gemüse.', 'Ragoût de bœuf copieux aux légumes.'),
  -- (12) Tuna Salad
  ('Tuna Salad', 'Thunfischsalat', 'Salade de Thon',
   'Tuna with veggies and olive oil.', 'Thunfisch mit Gemüse und Olivenöl.', 'Thon avec légumes et huile d''olive.'),
    -- (13) Buttered Toast
    ('Buttered Toast', 'Buttertoast', 'Toast au Beurre',
     'Warm buttered toast slices.', 'Warme Buttertoast-Scheiben.', 'Tranches de pain au beurre chaud.'),
    -- (14) Cheese Omelette
    ('Cheese Omelette', 'Käse-Omelett', 'Omelette au Fromage',
     'Fluffy omelette with cheese.', 'Fluffiges Omelett mit Käse.', 'Omelette moelleuse au fromage.'),
    -- (15) Veggie Stir Fry
    ('Veggie Stir Fry', 'Gemüsepfanne', 'Poêlée de Légumes',
     'Stir-fry of carrots, spinach and garlic.', 'Pfannengerührtes Gemüse mit Karotten, Spinat und Knoblauch.', 'Sauté de carottes, épinards et ail.'),
    -- (16) Chicken Soup
    ('Chicken Soup', 'Hühnersuppe', 'Soupe au Poulet',
     'Chicken broth with veggies.', 'Hühnerbrühe mit Gemüse.', 'Bouillon de poulet aux légumes.'),
    -- (17) Potato Wedges
    ('Potato Wedges', 'Kartoffelspalten', 'Pommes de Terre Sautées',
     'Seasoned potato wedges.', 'Würzige Kartoffelspalten.', 'Pommes de terre assaisonnées.'),
    -- (18) Avocado Toast
    ('Avocado Toast', 'Avocado-Toast', 'Toast à l''Avocat',
   'Avocado on toast with lemon.', 'Avocado auf Toast mit Zitrone.', 'Avocat sur toast au citron.'),
  -- (19) Egg & Tomato Wrap
  ('Egg & Tomato Wrap', 'Ei-Tomaten-Wrap', 'Wrap Œuf et Tomate',
   'Wrap filled with egg and tomato.', 'Wrap gefüllt mit Ei und Tomate.', 'Wrap garni d''œuf et tomate.'),
    -- (20) Ultimate Breakfast Plate
    ('Ultimate Breakfast Plate', 'Ultimative Frühstücksplatte', 'Assiette Petit-Déjeuner Ultime',
     'Eggs, spinach and potatoes.', 'Eier, Spinat und Kartoffeln.', 'Œufs, épinards et pommes de terre.'),
    -- (21) Apple Toast (2 Zutaten)
    ('Apple Toast', 'Apfel-Toast', 'Toast à la Pomme',
     'Toasted bread with apple slices.', 'Toast mit Apfelscheiben.', 'Toast aux tranches de pomme.'),
    -- (22) Honey Butter Bread (2 Zutaten)
    ('Honey Butter Bread', 'Honig-Butter-Brot', 'Pain Miel-Beurre',
     'Bread with honey and butter.', 'Brot mit Honig und Butter.', 'Pain au miel et beurre.'),
    -- (23) Rice & Beans (2 Zutaten)
    ('Rice & Beans', 'Reis und Bohnen', 'Riz et Haricots',
     'Simple rice with beans.', 'Einfacher Reis mit Bohnen.', 'Riz simple aux haricots.'),
    -- (24) Cheese & Corn (2 Zutaten)
    ('Cheese & Corn', 'Käse und Mais', 'Fromage et Maïs',
     'Cheese with corn kernels.', 'Käse mit Maiskörnern.', 'Fromage aux grains de maïs.'),
    -- (25) Egg Fried Rice
    ('Egg Fried Rice', 'Eier gebratener Reis', 'Riz Frit aux Œufs',
     'Rice stir-fried with egg and spinach.', 'Gebratener Reis mit Ei und Spinat.', 'Riz frit avec œuf et épinards.'),
    -- (26) Pasta Aglio e Olio (2 Zutaten)
    ('Pasta Aglio e Olio', 'Pasta Aglio e Olio', 'Pâtes Aglio e Olio',
     'Pasta with garlic and olive oil.', 'Nudeln mit Knoblauch und Olivenöl.', 'Pâtes à l''ail et huile d''olive.'),
    -- (27) Banana Toast (2 Zutaten)
    ('Banana Toast', 'Bananen-Toast', 'Toast à la Banane',
     'Toast topped with banana slices.', 'Toast mit Bananenscheiben.', 'Toast aux tranches de banane.'),
    -- (28) Carrot & Butter (2 Zutaten)
    ('Carrot & Butter', 'Karotte und Butter', 'Carotte et Beurre',
     'Steamed carrots with butter.', 'Gedünstete Karotten mit Butter.', 'Carottes vapeur au beurre.'),
    -- (29) Spinach & Cheese (2 Zutaten)
    ('Spinach & Cheese', 'Spinat und Käse', 'Épinards et Fromage',
     'Spinach topped with melted cheese.', 'Spinat mit geschmolzenem Käse.', 'Épinards gratinés au fromage.'),
    -- (30) Tofu Stir Fry
    ('Tofu Stir Fry', 'Tofu-Pfanne', 'Sauté de Tofu',
     'Tofu with spinach, garlic and olive oil.', 'Tofu mit Spinat, Knoblauch und Olivenöl.', 'Tofu aux épinards, ail et huile d''olive.'),
  -- (31) Carrot & Peanut Butter (2 Zutaten)*
  ('Carrot & Peanut Butter', 'Karotte mit Erdnussbutter', 'Carotte au Beurre de Cacahuète',
   'Raw carrot sticks with peanut butter.', 'Rohkost-Karotten mit Erdnussbutter.', 'Bâtonnets de carotte au beurre de cacahuète.'),
  -- (32) Honey Yogurt (2 Zutaten)
  ('Honey Yogurt', 'Honig-Joghurt', 'Yaourt au Miel',
   'Yogurt drizzled with honey.', 'Joghurt mit Honig.', 'Yaourt au miel.'),
  -- (33) Cheese Rice Bake
  ('Cheese Rice Bake', 'Käse-Reis-Auflauf', 'Gratin de Riz au Fromage',
   'Baked rice with cheese and milk.', 'Überbackener Reis mit Käse und Milch.', 'Gratin de riz au fromage et lait.'),
  -- (34) Tomato Spinach Salad
  ('Tomato Spinach Salad', 'Tomaten-Spinat-Salat', 'Salade Tomate & Épinards',
   'Fresh spinach with tomato and lemon.', 'Frischer Spinat mit Tomate und Zitrone.', 'Épinards frais, tomate et citron.'),
  -- (35) Buttered Corn
  ('Buttered Corn', 'Mais mit Butter', 'Maïs au Beurre',
   'Sweet corn with melted butter.', 'Mais mit geschmolzener Butter.', 'Maïs avec beurre fondu.'),
  -- (36) Rice & Butter (2 Zutaten)
  ('Rice & Butter', 'Reis und Butter', 'Riz et Beurre',
   'Simple rice with butter.', 'Reis mit Butter.', 'Riz au beurre.'),
  -- (37) Egg & Cheese Toast
  ('Egg & Cheese Toast', 'Ei-Käse-Toast', 'Toast Œuf et Fromage',
   'Toast topped with egg and cheese.', 'Toast mit Ei und Käse.', 'Toast œuf et fromage.'),
  -- (38) Lemon Water (2 Zutaten)
  ('Lemon Water', 'Zitronenwasser', 'Eau Citronnée',
   'Water with fresh lemon.', 'Wasser mit frischer Zitrone.', 'Eau avec citron frais.'),
  -- (39) Potato & Garlic (2 Zutaten)
  ('Potato & Garlic', 'Kartoffel und Knoblauch', 'Pomme de Terre et Ail',
   'Roasted potatoes with garlic.', 'Geröstete Kartoffeln mit Knoblauch.', 'Pommes de terre rôties à l''ail.'),
    -- (40) Pepper & Tomato (2 Zutaten)
    ('Pepper & Tomato', 'Paprika und Tomate', 'Poivron et Tomate',
     'Sliced pepper & tomato.', 'Paprika- und Tomatenscheiben.', 'Poivron et tomate en tranches.'),
    -- (41) Beans on Toast (2 Zutaten)
    ('Beans on Toast', 'Bohnen-Toast', 'Toast aux Haricots',
     'Toast with canned beans.', 'Toast mit Dosenbohnen.', 'Toast aux haricots en conserve.'),
    -- (42) Classic Omelette (2 Zutaten)
    ('Classic Omelette', 'Klassisches Omelett', 'Omelette Classique',
     'Eggs with butter.', 'Eier mit Butter.', 'Œufs au beurre.'),
    -- (43) Pasta & Cheese (2 Zutaten)
    ('Pasta & Cheese', 'Pasta und Käse', 'Pâtes et Fromage',
     'Pasta topped with cheese.', 'Pasta mit Käse.', 'Pâtes au fromage.'),
    -- (44) Rice Porridge (2 Zutaten)
    ('Rice Porridge', 'Reisbrei', 'Bouillie de Riz',
     'Rice cooked in milk.', 'Reis in Milch gekocht.', 'Riz cuit au lait.'),
    -- (45) Honey Banana (2 Zutaten)
    ('Honey Banana', 'Honig-Banane', 'Banane au Miel',
     'Banana drizzled with honey.', 'Banane mit Honig.', 'Banane au miel.');

-- Zuordnung Zutaten
INSERT INTO recipe_ingredient (id, recipe_recipe_id, product_barcode)
VALUES
    -- 1
    (1, 1, '002'), (2, 1, '005'), (3, 1, '015'),
    -- 2
    (4, 2, 'AAA'), (5, 2, '003'), (6, 2, '005'),
    -- 3
    (7, 3, 'CCC'), (8, 3, '001'), (9, 3, '015'),
    -- 4
    (10, 4, '012'), (11, 4, 'BBB'), (12, 4, '017'),
    -- 5
    (13, 5, '004'), (14, 5, '011'), (15, 5, '017'),
    -- 6
    (16, 6, '010'), (17, 6, '019'), (18, 6, '016'),
    -- 7
    (19, 7, '016'), (20, 7, '019'), (21, 7, '007'),
    -- 8
    (22, 8, '013'), (23, 8, '010'), (24, 8, '007'),
    -- 9
    (25, 9, 'AAA'), (26, 9, '017'), (27, 9, '005'),
    -- 10
    (28, 10, 'CCC'), (29, 10, '008'), (30, 10, '003'), (31, 10, '007'),
    -- 11
    (32, 11, '006'), (33, 11, '004'), (34, 11, '011'),
    -- 12
    (35, 12, '026'), (36, 12, '001'), (37, 12, '015'),
    -- 13
    (38, 13, 'AAA'), (39, 13, '005'), (40, 13, '007'),
    -- 14
    (41, 14, '002'), (42, 14, '003'), (43, 14, '015'),
    -- 15
    (44, 15, '004'), (45, 15, '008'), (46, 15, '017'),
    -- 16
    (47, 16, '012'), (48, 16, '004'), (49, 16, '008'),
    -- 17
    (50, 17, '011'), (51, 17, '015'), (52, 17, '017'),
    -- 18
    (53, 18, 'AAA'), (54, 18, '024'), (55, 18, '022'),
    -- 19
    (56, 19, '002'), (57, 19, '001'), (58, 19, 'AAA'),
    -- 20
    (59, 20, '002'), (60, 20, '008'), (61, 20, '011'), (62, 20, '017'),
    -- 21
    (63, 21, 'AAA'), (64, 21, '010'),
    -- 22
    (65, 22, 'AAA'), (66, 22, '013'),
    -- 23
    (67, 23, 'BBB'), (68, 23, '028'),
    -- 24
    (69, 24, '003'), (70, 24, '029'),
    -- 25
    (71, 25, 'BBB'), (72, 25, '002'), (73, 25, '008'),
    -- 26
    (74, 26, 'CCC'), (75, 26, '017'),
    -- 27
    (76, 27, 'AAA'), (77, 27, '019'),
    -- 28
    (78, 28, '004'), (79, 28, '005'),
    -- 29
    (80, 29, '008'), (81, 29, '003'),
    -- 30
    (82, 30, '020'), (83, 30, '008'), (84, 30, '017'), (85, 30, '015'),
    -- 31*
    (86, 31, '004'), (87, 31, 'EEE'),
    -- 32
    (88, 32, '007'), (89, 32, '013'),
    -- 33
    (90, 33, 'BBB'), (91, 33, '003'), (92, 33, '007'),
    -- 34
    (93, 34, '008'), (94, 34, '001'), (95, 34, '022'),
    -- 35
    (96, 35, '029'), (97, 35, '005'),
    -- 36
    (98, 36, 'BBB'), (99, 36, '005'),
    -- 37
    (100, 37, 'AAA'), (101, 37, '002'), (102, 37, '003'),
    -- 38
    (103, 38, '022'), (104, 38, '015'),
    -- 39
    (105, 39, '011'), (106, 39, '017'),
    -- 40
    (107, 40, '025'), (108, 40, '001'),
    -- 41
    (109, 41, 'AAA'), (110, 41, '028'),
    -- 42
    (111, 42, '002'), (112, 42, '005'),
    -- 43
    (113, 43, 'CCC'), (114, 43, '003'),
    -- 44
    (115, 44, 'BBB'), (116, 44, '007'),
    -- 45
    (117, 45, '019'), (118, 45, '013');