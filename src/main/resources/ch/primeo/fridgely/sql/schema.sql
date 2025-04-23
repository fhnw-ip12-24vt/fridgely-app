-- Drop tables if they exist to ensure a clean state
DROP TABLE IF EXISTS RecipeIngredients;
DROP TABLE IF EXISTS Recipes;
DROP TABLE IF EXISTS Products;
DROP TABLE IF EXISTS FridgeStock; -- Assuming FridgeStock table exists based on model classes

-- Create Products table
CREATE TABLE Products (
    Barcode TEXT PRIMARY KEY NOT NULL,
    Name TEXT,
    NameDE TEXT,
    NameFR TEXT,
    Description TEXT,
    DescriptionDE TEXT,
    DescriptionFR TEXT,
    IsDefaultProduct BOOLEAN DEFAULT 0,
    IsBio BOOLEAN DEFAULT 0,
    IsLocal BOOLEAN DEFAULT 0
);

-- Create Recipes table
CREATE TABLE Recipes (
    Id INTEGER PRIMARY KEY AUTOINCREMENT,
    Name TEXT,
    NameDE TEXT,
    NameFR TEXT,
    Description TEXT,
    DescriptionDE TEXT,
    DescriptionFR TEXT
);

-- Create RecipeIngredients table (Join table)
CREATE TABLE RecipeIngredients (
    RecipeId INTEGER NOT NULL,
    Barcode TEXT NOT NULL,
    FOREIGN KEY (RecipeId) REFERENCES Recipes(Id),
    FOREIGN KEY (Barcode) REFERENCES Products(Barcode),
    PRIMARY KEY (RecipeId, Barcode)
);

-- Create FridgeStock table (Assuming structure based on common patterns)
CREATE TABLE FridgeStock (
    Id INTEGER PRIMARY KEY AUTOINCREMENT,
    Barcode TEXT NOT NULL,
    Quantity INTEGER DEFAULT 1,
    ExpiryDate DATE, -- Or TEXT if storing as ISO string
    FOREIGN KEY (Barcode) REFERENCES Products(Barcode)
);

