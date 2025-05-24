INSERT INTO product (barcode, name, nameDE, nameFR, description, descriptionDE, descriptionFR,
                     is_default_product, is_bio, is_local, is_low_co2)
VALUES ('001', 'Tomato', 'Tomate', 'Tomate', 'Local organic tomato', 'Lokale Bio-Tomate', 'Tomate bio locale', 0, 1, 1,
        1),
       ('002', 'Egg', 'Ei', 'Œuf', 'Organic free-range egg', 'Bio-Freilandei', 'Œuf bio de plein air', 0, 1, 1, 1),
       ('003', 'Cheese', 'Käse', 'Fromage - France', 'Imported cheese block from France',
        'Importierter Käseblock aus Frankreich', 'Bloc de fromage importé de France', 0, 0, 1, 0),
       ('004', 'Carrots', 'Karotten', 'Carottes Locales', 'Basket full of local carrots',
        'Korb voller lokaler Karotten', 'Panier plein de carottes locales', 0, 0, 1, 1),
       ('005', 'Butter', 'Butter', 'Beurre', 'Organic butter made from grass-fed cows',
        'Bio-Butter aus grasgefütterten Kühen', 'Beurre bio issu de vaches nourries à l''herbe', 0, 1, 1, 0),
       ('006', 'Beef', 'Rindfleisch', 'Bœuf', 'meat', 'Importiertes Rindfleisch aus Argentinien',
        'Bœuf importé d''Argentine', 0, 0, 0, 0),
       ('007', 'Milk', 'Milch', 'Lait', 'Fresh organic milk from local farms',
        'Frische Bio-Milch von lokalen Bauernhöfen', 'Lait bio frais des fermes locales', 0, 1, 1, 1),
       ('008', 'Spinach', 'Spinat', 'Épinard', 'Local fresh spinach leaves', 'Lokale frische Spinatblätter',
        'Feuilles d''épinard fraîches locales', 0, 0, 1, 1),
       ('010', 'Apples', 'Äpfel', 'Pommes', 'Organic apples', 'Bio-Äpfel', 'Pommes biologiques', 0, 1, 0, 1),
       ('011', 'Potatoes', 'Kartoffeln', 'Terre Locales', 'Fresh local potatoes', 'Frische lokale Kartoffeln',
        'Pommes de terre locales fraîches', 0, 0, 1, 1),
       ('012', 'Chicken', 'Poulet', 'Poulet', 'meat', 'Bio-Freilandhuhn', 'Poulet bio en liberté', 0, 1, 0, 0),
       ('016', 'Strawberries', 'Erdbeeren', 'Fraises', 'water_intensive', 'Süße lokale Erdbeeren',
        'Fraises locales sucrées', 0, 0, 1, 1),
       ('017', 'Garlic', 'Knoblauch', 'Ail', 'Organic garlic bulbs', 'Bio-Knoblauchknollen',
        'Bulbes d''ail biologiques', 0, 1, 0, 1),
       ('018', 'Lettuce', 'Salat', 'Laitue', 'Fresh organic locally grown lettuce',
        'Frischer Bio-Salat aus lokalem Anbau', 'Laitue bio fraîche cultivée localement', 0, 1, 1, 1),
       ('019', 'Bananas', 'Bananen', 'Bananes', 'water_intensive', 'Importierte Bananen aus Ecuador',
        'Bananes importées d''Équateur', 0, 0, 0, 1),
       ('020', 'Tofu', 'Tofu', 'Tofu', 'Organic firm tofu', 'Bio-Tofu fest', 'Tofu bio ferme', 0, 1, 0, 1),
       ('021', 'Orange', 'Orange', 'Orange', 'Sweet juicy oranges', 'Süße saftige Orangen',
        'Oranges juteuses et sucrées', 0, 0, 0, 0),
       ('022', 'Lemon', 'Zitrone', 'Citron', 'Fresh tangy lemons', 'Frische würzige Zitronen',
        'Citrons frais et acidulés', 0, 0, 0, 0),
       ('023', 'Onion', 'Zwiebel', 'Oignon', 'Fresh cooking onions', 'Frische Kochzwiebeln', 'Oignons frais de cuisine',
        0, 0, 0, 0),
       ('024', 'Avocado', 'Avocado', 'Avocat', 'Ripe creamy avocados', 'Reife cremige Avocados',
        'Avocats mûrs et crémeux', 0, 0, 0, 0),
       ('025', 'Bell Pepper', 'Paprika', 'Poivron', 'Colorful bell peppers', 'Bunte Paprikaschoten', 'Poivrons colorés',
        0, 0, 0, 0),
       ('026', 'Tuna', 'Thunfisch', 'Thon', 'Canned tuna in water', 'Dosenthunfisch in Wasser',
        'Thon en conserve dans l''eau', 0, 0, 0, 0),
       ('027', 'Ground Beef', 'Hackfleisch', 'Viande Hachée', 'Fresh ground beef', 'Frisches Hackfleisch',
        'Viande hachée fraîche', 0, 0, 0, 0),
       ('028', 'Beans', 'Bohnen', 'Haricots', 'Canned beans', 'Dosenbohnen', 'Haricots en conserve', 0, 0, 0, 0),
       ('029', 'Corn', 'Mais', 'Maïs', 'Sweet corn kernels', 'Süße Maiskörner', 'Grains de maïs doux', 0, 0, 0, 0),
       ('AAA', 'Bread', 'Brot', 'Pain', 'Bread', 'Brot', 'Pain', 1, 0, 0, 1),
       ('BBB', 'Rice', 'Reis', 'Riz', 'White rice', 'Weißer Reis', 'Riz blanc', 1, 0, 0, 0),
       ('CCC', 'Pasta', 'Nudeln', 'Pâtes', 'Pasta', 'Nudeln', 'Pâtes', 1, 0, 0, 0),
       ('DDD', 'Honey', 'Honig', 'Miel', 'Sweet honey', 'Süßer Honig', 'Miel doux', 1, 0, 0, 0),
       ('EEE', 'Olive Oil', 'Olivenöl', 'Huile d''Olive', 'Premium organic extra virgin olive oil',
        'Premium Bio-Olivenöl Extra Vergine', 'Huile d''olive bio extra vierge de qualité supérieure', 1, 1, 0, 1);


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
     'Smoothie with strawberries, bananas and milk.', 'Smoothie mit Erdbeeren, Bananen und Milch.',
     'Smoothie aux fraises, bananes et lait.'),
    -- (8) Fruit Breakfast & Honey
    ('Fruit Breakfast & Honey', 'Obst-Frühtstück und Honig', 'Petit-Déjeuner aux Fruits et Miel',
     'A breakfast with fruits and honey.', 'eine Obst-Frühstück mit Honig.',
     'Un petit déjeuner avec des fruits et du miel.'),
    -- (9) Garlic Bread
    ('Garlic Bread', 'Knoblauchbrot', 'Pain à l''Ail',
     'Toasted garlic bread.', 'Geröstetes Knoblauchbrot.', 'Pain à l''ail grillé.'),
    -- (10) Spinach Pasta
    ('Spinach Pasta', 'Spinat-Pasta', 'Pâtes aux Épinards',
     'Pasta with creamy spinach sauce.', 'Nudeln mit cremiger Spinatsauce.', 'Pâtes à la sauce crémeuse aux épinards.'),
    -- (11) Beef Stew
    ('Beef Stew', 'Rindfleischeintopf', 'Ragoût de Bœuf',
     'Hearty beef stew with vegetables.', 'Herzhafter Rindfleischeintopf mit Gemüse.',
     'Ragoût de bœuf copieux aux légumes.'),
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
     'Stir-fry of carrots, spinach and garlic.', 'Pfannengerührtes Gemüse mit Karotten, Spinat und Knoblauch.',
     'Sauté de carottes, épinards et ail.'),
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
    -- (26) Pasta Aglio e Olio
    ('Pasta Aglio e Olio', 'Pasta Aglio e Olio', 'Pâtes Aglio e Olio',
     'Pasta with garlic and olive oil.', 'Nudeln mit Knoblauch und Olivenöl.', 'Pâtes à l''ail et huile d''olive.'),
    -- (27) Banana Toast
    ('Banana Toast', 'Bananen-Toast', 'Toast à la Banane',
     'Toast topped with banana slices.', 'Toast mit Bananenscheiben.', 'Toast aux tranches de banane.'),
    -- (28) Carrot & Butter
    ('Carrot & Butter', 'Karotte und Butter', 'Carotte et Beurre',
     'Steamed carrots with butter.', 'Gedünstete Karotten mit Butter.', 'Carottes vapeur au beurre.'),
    -- (29) Spinach & Cheese
    ('Spinach & Cheese', 'Spinat und Käse', 'Épinards et Fromage',
     'Spinach topped with melted cheese.', 'Spinat mit geschmolzenem Käse.', 'Épinards gratinés au fromage.'),
    -- (30) Tofu Stir Fry
    ('Tofu Stir Fry', 'Tofu-Pfanne', 'Sauté de Tofu',
     'Tofu with spinach, garlic and olive oil.', 'Tofu mit Spinat, Knoblauch und Olivenöl.',
     'Tofu aux épinards, ail et huile d''olive.'),
    -- (31) Guacamole & Toastx
    ('Guacamole & toast', 'Guacamole und Toast', 'Guacamole et pain grillé',
     'Toast with guacamole ontop.', 'Toastbrot mit Guacamole-aufstrich .',
     'Toast avec guacamole.'),
    -- (32) Cheeseburger
    ('Cheeseburger', 'Cheeseburger', 'Cheeseburger',
     'A classic cheeseburger.', 'einen klassischen Cheeseburger.', 'un cheeseburger classique.'),
    -- (33) Cheese Rice Bake
    ('Cheese Rice Bake', 'Käse-Reis-Auflauf', 'Gratin de Riz au Fromage',
     'Baked rice with cheese and milk.', 'Überbackener Reis mit Käse und Milch.', 'Gratin de riz au fromage et lait.'),
    -- (34) Tomato Spinach Salad
    ('Tomato Spinach Salad', 'Tomaten-Spinat-Salat', 'Salade Tomate & Épinards',
     'Fresh spinach with tomato and lemon.', 'Frischer Spinat mit Tomate und Zitrone.',
     'Épinards frais, tomate et citron.'),
    -- (35) Buttered Corn
    ('Buttered Corn', 'Mais mit Butter', 'Maïs au Beurre',
     'Sweet corn with melted butter.', 'Mais mit geschmolzener Butter.', 'Maïs avec beurre fondu.'),
    -- (36) Rice & Butter (2 Zutaten)
    ('Rice & Butter', 'Reis und Butter', 'Riz et Beurre',
     'Simple rice with butter.', 'Reis mit Butter.', 'Riz au beurre.'),
    -- (37) Egg & Cheese Toast
    ('Egg & Cheese Toast', 'Ei-Käse-Toast', 'Toast Œuf et Fromage',
     'Toast topped with egg and cheese.', 'Toast mit Ei und Käse.', 'Toast œuf et fromage.'),
    -- (38) Scrambled eggs with fruits and Toast.
    ('Scrambled eggs with fruits and toast', 'Rührei mit Früchten und Toast', 'Œufs brouillés avec fruits et toasts',
     'A simple breakfast with eggs,fruits and toast.', 'ein einfaches Frühstück mit Eiern, Obst und Toast.',
     'un petit déjeuner simple avec des œufs, des fruits et du pain grillé.'),
    -- (39) Potato & Garlic (2 Zutaten)
    ('Potato & Garlic', 'Kartoffel und Knoblauch', 'Pomme de Terre et Ail',
     'Roasted potatoes with garlic.', 'Geröstete Kartoffeln mit Knoblauch.', 'Pommes de terre rôties à l''ail.'),
    -- (40) Mexican Rice (5 Zutaten)
    ('Mexican Rice', 'Mexikanischer Reis', 'Riz Mexicain',
     'Rice with Vegetables and beans.', 'Reis mit Gemüse und Bohnen.', 'Riz avec légumes et haricots.'),
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
    -- (45) Hardboiled Eggs wih avocado and toast.
    ('Hardboiled Eggs with Avocado and Toast', 'Hartgekochte Eier mit Avocado und Toast',
     'Œufs durs avec avocat et pain grillé',
     'A simple breakfast with hardboiled eggs, avocado and toast.', 'ein einfaches Frühstück mit hartgekochten Eiern, Avocado und Toast.',
     'un petit déjeuner simple avec des œufs durs, de l''avocat et du pain grillé.'),
    -- (46) Raclette with potatoes and bread.
    ('Raclette with Potatoes and Bread', 'Raclette mit Kartoffeln und Brot',
     'Raclette avec Pommes de Terre et Pain',
     'A simple raclette with potatoes and bread.', 'eine einfache Raclette mit Kartoffeln und Brot.',
     'une raclette simple avec des pommes de terre et du pain.'),
    -- (47) Steak with carrots.
    ('Steak with Carrots', 'Steak mit Karotten', 'Steak avec Carottes',
     'Steak seasoned with butter and served with carrots.', 'Steak gewürzt mit Butter und serviert mit Karotten.',
     'Steak assaisonné de beurre et servi avec des carottes.'),
    -- (48) Meat Platter with Bread and Cheese
    ('Meat Platter with Bread and Cheese', 'Fleischplatte mit Brot und Käse', 'Plateau de viande avec pain et fromage',
     'A meat platter with Bread and Cheese.', 'Eine Fleischplatte mit Brot und Käse.','Un plateau de viande avec pain et fromage.'),
    -- (49) Steak with fries and garlic bread
    ('Steak with Fries and Garlic Bread', 'Steak mit Pommes und Knoblauchbrot', 'Steak avec Frites et Pain à l''Ail',
     'A steak served with fries and garlic bread.', 'Ein Steak serviert mit Pommes und Knoblauchbrot.',
     'Un steak servi avec des frites et du pain à l''ail.'),
    -- (50) Apple and Banana Smoothie
    ('Apple and Banana Smoothie', 'Apfel-Bananen-Smoothie', 'Smoothie Pomme-Banane',
     'A smoothie made with apples and bananas.', 'Ein Smoothie aus Äpfeln und Bananen.',
     'Un smoothie à base de pommes et de bananes.'),
    -- (51) Orange and lemon smoothie
    ('Orange and Lemon Smoothie', 'Orangen-Zitronen-Smoothie', 'Smoothie Orange et Citron',
     'A refreshing smoothie made with oranges and lemons.', 'Ein erfrischender Smoothie aus Orangen und Zitronen.',
     'Un smoothie rafraîchissant à base d''oranges et de citrons.'),
    -- (52) Appletart with Strawberries
    ('Appletart with Strawberries', 'Apfeltarte mit Erdbeeren', 'Tarte aux Pommes et Fraises',
     'A delicious appletart topped with strawberries.', 'Eine köstliche Apfeltarte mit Erdbeeren.',
     'Une délicieuse tarte aux pommes garnie de fraises.'),
    -- (53) candied Apples with homemade orange ice cream
    ('Candied Apples with Homemade orange ice-cream', 'Kandierte Äpfel mit hausgemachtem Orangen-eis',
     'Pommes confites avec glace à l orange maison',
     'A dessert of candied apples served with homemade ice cream.', 'Ein Dessert aus kandierten Äpfeln mit hausgemachtem Eis.',
     'Un dessert de pommes caramélisées servi avec de la glace maison de orange.'),
    -- (54) Chicken and Rice mexican style
    ('Chicken and Rice Mexican Style', 'Hühnchen und Reis im mexikanischen Stil', 'Poulet et Riz à la Mexicaine',
     'A dish of chicken and rice with Mexican spices.', 'Ein Gericht aus Hühnchen und Reis mit mexikanischen Gewürzen.',
     'Un plat de poulet et de riz aux épices mexicaines.'),
    -- (55) Egg salad with lettuce and tomatoes
    ('Egg Salad with Lettuce and Tomatoes', 'Eiersalat mit Salat und Tomaten', 'Salade d''Œufs avec Laitue et Tomates',
     'A refreshing egg salad served on a bed of lettuce with tomatoes.', 'Ein erfrischender Eiersalat auf einem Bett aus Salat mit Tomaten.',
     'Une salade d''œufs rafraîchissante servie sur un lit de laitue avec des tomates.'),
    -- (56) Chicken salad Wrap
    ('Chicken Salad Wrap', 'Hühnchen-Salat-Wrap', 'Wrap de Salade de Poulet',
     'A Lettuce Wrap filled with Chicken.', 'Ein Wrap aus Kopfsallat gefüllt mit Hünchen.', 'Wrap de laitue fourré au poulet.'),
    -- (57) Fried Tofu with rice and vegetables
    ('Fried Tofu with Rice and Vegetables', 'Gebratener Tofu mit Reis und Gemüse', 'Tofu Frit avec Riz et Légumes',
     'A dish of fried tofu served with rice and vegetables.', 'Ein Gericht aus gebratenem Tofu mit Reis und Gemüse.',
     'Un plat de tofu frit servi avec du riz et des légumes.'),
    -- (58) Steamed Tofu with Spinach and Garlic
    ('Steamed Tofu with Spinach and Garlic', 'Gedämpfter Tofu mit Spinat und Knoblauch',
     'Tofu Vapeur avec Épinards et Ail',
     'A dish of steamed tofu served with spinach and garlic.', 'Ein Gericht aus gedämpftem Tofu mit Spinat und Knoblauch.',
     'Un plat de tofu vapeur servi avec des épinards et de l''ail.'),
    -- (59) Enchilada Power Bowl
    ('Enchilada Power Bowl', 'Enchilada Power Bowl', 'Bol de Pouvoir Enchilada',
     'A bowl of rice, beans, and vegetables with enchilada sauce.', 'Eine Schüssel Reis, Bohnen und Gemüse mit Enchilada-Sauce.',
     'Un bol de riz, haricots et légumes avec sauce enchilada.'),
    -- (60) Homemade Ice Cream from Fruits
    ('Homemade Ice Cream from Fruits', 'Hausgemachtes Eis aus Früchten', 'Glace Maison aux Fruits',
     'A refreshing homemade ice cream made from fruits.', 'Ein erfrischendes hausgemachtes Eis aus Früchten.',
     'Une glace maison rafraîchissante à base de fruits.'),
    -- (61) Tuna Seasoned with Lemon and Orange
    ('Tuna Seasoned with Lemon and Orange', 'Thunfisch gewürzt mit Zitrone und Orange',
     'Thon assaisonné de Citron et Orange',
     'A dish of tuna seasoned with lemon and orange.', 'Ein Gericht aus Thunfisch gewürzt mit Zitrone und Orange.',
     'Un plat de thon assaisonné de citron et d''orange.'),
    -- (62) Mega Veggie Bowl
    ('Mega Veggie Bowl', 'Mega Gemüse-Schüssel', 'Bol de Légumes Méga',
     'A bowl of mixed vegetables with a tangy dressing.', 'Eine Schüssel mit gemischtem Gemüse und einer würzigen Sauce.',
     'Un bol de légumes mélangés avec une vinaigrette acidulée.'),
    -- (63) Avocado and Tomato Salad
    ('Avocado and Tomato Salad', 'Avocado und Tomatensalat', 'Salade d''Avocat et Tomate',
     'A refreshing salad of avocado and tomato.', 'Ein erfrischender Salat aus Avocado und Tomate.',
     'Une salade rafraîchissante à l''avocat et à la tomate.'),
    -- (64) Stuffed Bell Peppers
    ('Stuffed Bell Peppers', 'Gefüllte Paprika', 'Poivrons Farcis',
     'Bell peppers stuffed with rice and vegetables.', 'Paprikaschoten gefüllt mit Reis und Gemüse.',
     'Poivrons farcis de riz et de légumes.'),
    -- (65) Fish and Chips
    ('Fish and Chips', 'Fisch und Pommes', 'Poisson et Frites',
     'A classic dish of fish and chips.', 'Ein klassisches Gericht aus Fisch und Pommes.',
     'Un plat classique de poisson et frites.'),
    -- (66) Tuna salad with Rice
    ('Tuna Salad with Rice', 'Thunfischsalat mit Reis', 'Salade de Thon avec Riz',
     'A refreshing tuna salad served with rice.', 'Ein erfrischender Thunfischsalat mit Reis serviert.',
     'Une salade de thon rafraîchissante servie avec du riz.'),
    -- (67) Sushi Rolls
    ('Sushi Rolls', 'Sushi-Rollen', 'Rouleaux de Sushi',
     'A dish of sushi rolls with various fillings.', 'Ein Gericht aus Sushi-Rollen mit verschiedenen Füllungen.',
     'Un plat de rouleaux de sushi avec différentes garnitures.'),
    -- (68) Sphaghetti Bolognese
    ('Spaghetti Bolognese', 'Spaghetti Bolognese', 'Spaghetti Bolognaise',
     'A classic dish of spaghetti with Bolognese sauce.', 'Ein klassisches Gericht aus Spaghetti mit Bolognese-Sauce.',
     'Un plat classique de spaghetti à la sauce bolognaise.'),
    -- (69) Ground Beef rice bowl
    ('Ground Beef Rice Bowl', 'Hackfleisch-Reisschüssel', 'Bol de Riz au Viande Hachée',
     'A bowl of rice topped with ground beef and vegetables.', 'Eine Schüssel Reis mit Hackfleisch und Gemüse.',
     'Un bol de riz garni de viande hachée et de légumes.'),
    -- (70) Corn salad with cheese
    ('Corn Salad with Cheese', 'Mais-Salat mit Käse', 'Salade de Maïs avec Fromage',
     'A refreshing corn salad topped with cheese.', 'Ein erfrischender Maissalat mit Käse.', 'Une salade de maïs rafraîchissante garnie de fromage.'),
    -- (71) Steak with Corn and Rice
    ('Steak with Corn and Rice', 'Steak mit Mais und Reis', 'Steak avec Maïs et Riz',
     'A dish of steak served with corn and rice.', 'Ein Gericht aus Steak mit Mais und Reis serviert.',
     'Un plat de steak servi avec du maïs et du riz.');




-- Zuordnung Zutaten
INSERT INTO recipe_ingredient (id, recipe_recipe_id, product_barcode)
VALUES
    -- 1
    (1, 1, '002'),
    (2, 1, '005'),
    (3, 1, 'EEE'),
    -- 2
    (4, 2, 'AAA'),
    (5, 2, '003'),
    (6, 2, '005'),
    -- 3
    (7, 3, 'CCC'),
    (8, 3, '001'),
    (9, 3, 'EEE'),
    (10, 3, '003'),
    (11, 3, '017'),
    -- 4
    (12, 4, '012'),
    (13, 4, 'BBB'),
    (14, 4, '017'),
    (15, 4, '005'),
    -- 5
    (16, 5, '004'),
    (17, 5, '011'),
    (18, 5, '017'),
    -- 6
    (19, 6, '010'),
    (20, 6, '019'),
    (21, 6, '016'),
    (22, 6, '021'),
    -- 7
    (23, 7, '016'),
    (24, 7, '019'),
    (25, 7, '007'),
    -- 8
    (26, 8, 'DDD'),
    (27, 8, '010'),
    (28, 8, '007'),
    -- 9
    (29, 9, 'AAA'),
    (30, 9, '017'),
    (31, 9, '005'),
    -- 10
    (32, 10, 'CCC'),
    (33, 10, '008'),
    (34, 10, '003'),
    (35, 10, '005'),
    -- 11
    (36, 11, '006'),
    (37, 11, '004'),
    (38, 11, '011'),
    -- 12
    (39, 12, '026'),
    (40, 12, '001'),
    (41, 12, 'EEE'),
    -- 13
    (42, 13, 'AAA'),
    (43, 13, '005'),
    (44, 13, '007'),
    -- 14
    (45, 14, '002'),
    (46, 14, '003'),
    (47, 14, 'EEE'),
    -- 15
    (48, 15, '004'),
    (49, 15, '008'),
    (50, 15, '017'),
    -- 16
    (51, 16, '012'),
    (52, 16, '004'),
    (53, 16, '008'),
    -- 17
    (54, 17, '011'),
    (55, 17, 'EEE'),
    (56, 17, '017'),
    -- 18
    (57, 18, 'AAA'),
    (58, 18, '024'),
    (59, 18, '022'),
    -- 19
    (60, 19, '002'),
    (61, 19, '001'),
    (62, 19, 'AAA'),
    -- 20
    (63, 20, '002'),
    (64, 20, '003'),
    (65, 20, '005'),
    (66, 20, '007'),
    (67, 20, 'AAA'),
    -- 21
    (68, 21, 'AAA'),
    (69, 21, '010'),
    -- 22
    (70, 22, 'AAA'),
    (71, 22, '013'),
    -- 23
    (72, 23, 'BBB'),
    (73, 23, '028'),
    -- 24
    (74, 24, '003'),
    (75, 24, '029'),
    -- 25
    (76, 25, 'BBB'),
    (77, 25, '002'),
    (78, 25, '008'),
    -- 26
    (79, 26, 'CCC'),
    (80, 26, '017'),
    (81, 26, 'EEE'),
    -- 27
    (82, 27, 'AAA'),
    (83, 27, '019'),
    -- 28
    (84, 28, '004'),
    (85, 28, '005'),
    -- 29
    (86, 29, '008'),
    (87, 29, '003'),
    -- 30
    (88, 30, '020'),
    (89, 30, '008'),
    (90, 30, '017'),
    (91, 30, 'EEE'),
    -- 31
    (92, 31, '001'),
    (93, 31, 'EEE'),
    (94, 31, '024'),
    (95, 31, '023'),
    -- 32
    (96, 32, '018'),
    (97, 32, '002'),
    (98, 32, '003'),
    (99, 32, '027'),
    (100, 32, '001'),
    (101, 32, 'AAA'),
    -- 33
    (102, 33, 'BBB'),
    (103, 33, '003'),
    (104, 33, '007'),
    -- 34
    (105, 34, '008'),
    (106, 34, '001'),
    (107, 34, '018'),
    -- 35
    (108, 35, '029'),
    (109, 35, '005'),
    -- 36
    (110, 36, 'BBB'),
    (111, 36, '005'),
    -- 37
    (112, 37, 'AAA'),
    (113, 37, '002'),
    (114, 37, '003'),
    -- 38
    (115, 38, '002'),
    (116, 38, 'AAA'),
    (117, 38, '016'),
    (118, 38, '019'),
    -- 39
    (119, 39, '011'),
    (120, 39, '017'),
    -- 40
    (121, 40, '025'),
    (122, 40, '001'),
    (123, 40, '023'),
    (124, 40, '028'),
    (125, 40, 'BBB'),
    -- 41
    (126, 41, 'AAA'),
    (127, 41, '028'),
    -- 42
    (128, 42, '002'),
    (129, 42, '005'),
    -- 43
    (130, 43, 'CCC'),
    (131, 43, '003'),
    -- 44
    (132, 44, 'BBB'),
    (133, 44, '007'),
    -- 45
    (134, 45, '002'),
    (135, 45, '024'),
    (136, 45, '001'),
    (137, 45, 'AAA'),
    -- 46
    (138, 46, '003'),
    (139, 46, '011'),
    (140, 46, '023'),
    (141, 46, 'AAA'),
    -- 47
    (142, 47, '006'),
    (143, 47, '004'),
    (144, 47, '005'),
    (145, 47, '017'),
    -- 48
    (146, 48, '006'),
    (147, 48, '003'),
    (148, 48, '005'),
    (149, 48, '012'),
    (150, 48, 'AAA'),
    -- 49
    (151, 49, '006'),
    (152, 49, '011'),
    (153, 49, '017'),
    (154, 49, 'AAA'),
    (155, 49, 'EEE'),
    -- 50
    (156, 50, '010'),
    (157, 50, '019'),
    (158, 50, '016'),
    (159, 50, '007'),
    -- 51
    (160, 51, '016'),
    (161, 51, '021'),
    (162, 51, '022'),
    (163, 51, '007'),
    -- 52
    (164, 52, '011'),
    (165, 52, '016'),
    (166, 52, 'DDD'),
    (167, 52, '022'),
    -- 53
    (168, 53, '010'),
    (169, 53, 'DDD'),
    (170, 53, '007'),
    (171, 53, '021'),
    -- 54
    (172, 54, '012'),
    (173, 54, 'BBB'),
    (174, 54, '028'),
    (175, 54, '025'),
    (176, 54, 'EEE'),
    -- 55
    (177, 55, '002'),
    (178, 55, '018'),
    (179, 55, '001'),
    (180, 55, '022'),
    (181, 55, 'AAA'),
    -- 56
    (182, 56, '001'),
    (183, 56, '012'),
    (184, 56, '018'),
    (185, 56, '025'),
    -- 57
    (186, 57, '020'),
    (187, 57, '025'),
    (188, 57, '023'),
    (189, 57, 'BBB'),
    -- 58
    (190, 58, '020'),
    (191, 58, '008'),
    (192, 58, '017'),
    -- 59
    (193, 59, '024'),
    (194, 59, '028'),
    (195, 59, '020'),
    (196, 59, '022'),
    -- 60
    (197, 60, '007'),
    (198, 60, '016'),
    (199, 60, '021'),
    (200, 60, '022'),
    -- 61
    (201, 61, '026'),
    (202, 61, '021'),
    (203, 61, '022'),
    (204, 61, 'EEE'),
    -- 62
    (205, 62, '020'),
    (206, 62, '023'),
    (207, 62, '029'),
    (208, 62, '001'),
    (209, 62, '018'),
    -- 63
    (210, 63, '001'),
    (211, 63, '018'),
    (212, 63, '024'),
    (213, 63, '029'),
    -- 64
    (214, 64, '025'),
    (215, 64, '003'),
    (216, 64, '027'),
    (217, 64, 'EEE'),
    -- 65
    (218, 65, '022'),
    (219, 65, '011'),
    (220, 65, '026'),
    (221, 65, 'EEE'),
    -- 66
    (222, 66, '026'),
    (223, 66, 'BBB'),
    (224, 66, '022'),
    (225, 66, '018'),
    -- 67
    (226, 67, '017'),
    (227, 67, '001'),
    (228, 67, 'EEE'),
    (229, 67, '022'),
    -- 68
    (230, 68, 'CCC'),
    (231, 68, 'EEE'),
    (232, 68, '027'),
    (233, 68, '001'),
    (234, 68, '003'),
    -- 69
    (235, 69, 'BBB'),
    (236, 69, '025'),
    (237, 69, '027'),
    (238, 69, '028'),
    -- 70
    (239, 70, '029'),
    (240, 70, '003'),
    (241, 70, '001'),
    -- 71
    (242, 71, '006'),
    (243, 71, '029'),
    (244, 72, 'EEE');












