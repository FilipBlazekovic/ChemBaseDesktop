
-- CHEMBASE DATABASE STRUCTURE
-- ---------------------------

CREATE TABLE locations
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    location TEXT
);

CREATE TABLE chemicals
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    chemical_name TEXT,
    brutto_formula TEXT,
    molar_mass TEXT,
    quantity_amount REAL,
    quantity_unit TEXT,
    storage_location INTEGER,
    manufacturer TEXT,
    supplier TEXT,
    date_of_entry TEXT,
    additional_info TEXT,
    FOREIGN KEY(storage_location) REFERENCES locations(id)
);

CREATE INDEX ix_chemical_name ON chemicals(chemical_name);
CREATE INDEX ix_brutto_formula ON chemicals(brutto_formula);
CREATE INDEX ix_molar_mass ON chemicals(molar_mass);
CREATE INDEX ix_storage_location ON chemicals(storage_location);
CREATE INDEX ix_manufacturer ON chemicals(manufacturer);
CREATE INDEX ix_supplier ON chemicals(supplier);


-- test inserts
-- ------------
INSERT INTO locations(location) VALUES ("Lab A, Closet 1, Shelf 1A");
INSERT INTO locations(location) VALUES ("Lab A, Closet 1, Shelf 1B");
INSERT INTO locations(location) VALUES ("Lab A, Closet 1, Shelf 2A");
INSERT INTO locations(location) VALUES ("Lab A, Closet 1, Shelf 2B");
INSERT INTO locations(location) VALUES ("Lab A, Closet 2, Shelf 1A");
INSERT INTO locations(location) VALUES ("Lab A, Closet 2, Shelf 2B");
INSERT INTO locations(location) VALUES ("Lab B, Closet 1, Shelf 1A");
INSERT INTO locations(location) VALUES ("Lab B, Closet 1, Shelf 1B");
INSERT INTO locations(location) VALUES ("Lab B, Closet 1, Shelf 2A");
INSERT INTO locations(location) VALUES ("Lab B, Closet 1, Shelf 2B");

INSERT INTO chemicals(chemical_name, brutto_formula, molar_mass, quantity_amount, quantity_unit, storage_location, manufacturer, supplier, date_of_entry, additional_info)
VALUES("Methanol", "CH4O", "32.04", "4", "l", 1, "Sigma Aldrich", "Unknown", "2020/10/12", "Test chemical");

INSERT INTO chemicals(chemical_name, brutto_formula, molar_mass, quantity_amount, quantity_unit, storage_location, manufacturer, supplier, date_of_entry, additional_info)
VALUES("Ethanol", "C2H6O", "46.07", "2", "l", 1, "Sigma Aldrich", "Unknown", "2020/10/12", "Test chemical");

INSERT INTO chemicals(chemical_name, brutto_formula, molar_mass, quantity_amount, quantity_unit, storage_location, manufacturer, supplier, date_of_entry, additional_info)
VALUES("Ethanol", "C2H6O", "46.07", "2", "l", 1, "Sigma Aldrich", "Unknown", "2020/10/13", "Test chemical");

INSERT INTO chemicals(chemical_name, brutto_formula, molar_mass, quantity_amount, quantity_unit, storage_location, manufacturer, supplier, date_of_entry, additional_info)
VALUES("Ethanol", "C2H6O", "46.07", "2", "l", 2, "Sigma Aldrich", "Unknown", "2020/10/13", "Test chemical");

INSERT INTO chemicals(chemical_name, brutto_formula, molar_mass, quantity_amount, quantity_unit, storage_location, manufacturer, supplier, date_of_entry, additional_info)
VALUES("Potassium Chloride", "KCl", "74.55", "500", "g", 5, "Sigma Aldrich", "Unknown", "2020/10/14", "Test chemical");

INSERT INTO chemicals(chemical_name, brutto_formula, molar_mass, quantity_amount, quantity_unit, storage_location, manufacturer, supplier, date_of_entry, additional_info)
VALUES("Potassium Chloride", "KCl", "74.55", "500", "g", 5, "Sigma Aldrich", "Unknown", "2020/10/14", "Test chemical");

INSERT INTO chemicals(chemical_name, brutto_formula, molar_mass, quantity_amount, quantity_unit, storage_location, manufacturer, supplier, date_of_entry, additional_info)
VALUES("Sodium Chloride", "NaCl", "58.44", "1", "kg", 5, "Sigma Aldrich", "Unknown", "2020/10/14", "Test chemical");

INSERT INTO chemicals(chemical_name, brutto_formula, molar_mass, quantity_amount, quantity_unit, storage_location, manufacturer, supplier, date_of_entry, additional_info)
VALUES("Sodium Chloride", "NaCl", "58.44", "1", "kg", 5, "Sigma Aldrich", "Unknown", "2020/10/14", "Test chemical");

INSERT INTO chemicals(chemical_name, brutto_formula, molar_mass, quantity_amount, quantity_unit, storage_location, manufacturer, supplier, date_of_entry, additional_info)
VALUES("Sodium sulfate", "Na2SO4", "142.04", "250", "g", 6, "Sigma Aldrich", "Unknown", "2020/10/14", "Test chemical");

INSERT INTO chemicals(chemical_name, brutto_formula, molar_mass, quantity_amount, quantity_unit, storage_location, manufacturer, supplier, date_of_entry, additional_info)
VALUES("Sodium sulfate", "Na2SO4", "142.04", "250", "g", 6, "Sigma Aldrich", "Unknown", "2020/10/14", "Test chemical");

INSERT INTO chemicals(chemical_name, brutto_formula, molar_mass, quantity_amount, quantity_unit, storage_location, manufacturer, supplier, date_of_entry, additional_info)
VALUES("Sodium sulfate", "Na2SO4", "142.04", "250", "g", 6, "Sigma Aldrich", "Unknown", "2020/10/14", "Test chemical");

INSERT INTO chemicals(chemical_name, brutto_formula, molar_mass, quantity_amount, quantity_unit, storage_location, manufacturer, supplier, date_of_entry, additional_info)
VALUES("Magnesium sulfate", "Mg2SO4", "120.37", "500", "g", 6, "Sigma Aldrich", "Unknown", "2020/10/14", "Test chemical");

INSERT INTO chemicals(chemical_name, brutto_formula, molar_mass, quantity_amount, quantity_unit, storage_location, manufacturer, supplier, date_of_entry, additional_info)
VALUES("Magnesium sulfate", "Mg2SO4", "120.37", "500", "g", 6, "Sigma Aldrich", "Unknown", "2020/10/14", "Test chemical");

INSERT INTO chemicals(chemical_name, brutto_formula, molar_mass, quantity_amount, quantity_unit, storage_location, manufacturer, supplier, date_of_entry, additional_info)
VALUES("Isopropanol", "C3H8O", "60.10", "5", "l", 1, "Sigma Aldrich", "Unknown", "2020/10/12", "Test chemical");

INSERT INTO chemicals(chemical_name, brutto_formula, molar_mass, quantity_amount, quantity_unit, storage_location, manufacturer, supplier, date_of_entry, additional_info)
VALUES("Isopropanol", "C3H8O", "60.10", "1", "l", 1, "Sigma Aldrich", "Unknown", "2020/10/12", "Test chemical");
