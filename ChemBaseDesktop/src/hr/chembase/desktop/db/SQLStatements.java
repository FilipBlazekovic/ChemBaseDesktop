package hr.chembase.desktop.db;

public class SQLStatements {

	public static final String TABLE_TRUNCATE_LOCATIONS_SQL = "DELETE FROM locations";
	public static final String TABLE_TRUNCATE_CHEMICALS_SQL = "DELETE FROM chemicals";

	public static final String TABLE_CREATE_LOCATIONS_SQL = "CREATE TABLE locations(" +
															"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
															"location TEXT)";

	public static final String TABLE_CREATE_CHEMICALS_SQL = "CREATE TABLE chemicals(" +
															"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
															"chemical_name TEXT, " +
															"brutto_formula TEXT, " +
															"molar_mass TEXT, " +
															"quantity_amount REAL, " +
															"quantity_unit TEXT, " +
															"storage_location INTEGER, " +
															"manufacturer TEXT, " +
															"supplier TEXT, " +
															"date_of_entry TEXT, " +
															"additional_info TEXT, " +
															"FOREIGN KEY(storage_location) REFERENCES locations(id))";

	public static final String CONSTRUCT_IX_CHEMICAL_NAME_SQL 	 = "CREATE INDEX ix_chemical_name ON chemicals(chemical_name)";
	public static final String CONSTRUCT_IX_BRUTTO_FORMULA_SQL 	 = "CREATE INDEX ix_brutto_formula ON chemicals(brutto_formula)";
	public static final String CONSTRUCT_IX_MOLAR_MASS_SQL 		 = "CREATE INDEX ix_molar_mass ON chemicals(molar_mass)";
	public static final String CONSTRUCT_IX_STORAGE_LOCATION_SQL = "CREATE INDEX ix_storage_location ON chemicals(storage_location)";
	public static final String CONSTRUCT_IX_MANUFACTURER_SQL 	 = "CREATE INDEX ix_manufacturer ON chemicals(manufacturer)";
	public static final String CONSTRUCT_IX_SUPPLIER_SQL 		 = "CREATE INDEX ix_supplier ON chemicals(supplier)";
	
	/* ------------------------------------------------------------------------------------------ */

	public static final String GET_LOCATIONS_SQL = "SELECT location FROM locations ORDER BY location";
	
	public static final String INSERT_LOCATION_SQL = "INSERT INTO locations(location) VALUES (?)";
	
	public static final String DELETE_LOCATION_SQL = "DELETE FROM locations WHERE location = ?";

	public static final String CHECK_IF_RECORDS_EXIST_FOR_LOCATION_SQL = "SELECT id FROM chemicals WHERE storage_location = (SELECT id FROM locations WHERE location = ?) LIMIT 1";
	
	/* ------------------------------------------------------------------------------------------ */

	public static final String INITIAL_SQL = "SELECT " +
											 "x.id, " +
											 "x.chemical_name, " +
											 "x.brutto_formula, " +
											 "x.molar_mass, " +
											 "x.quantity_amount, " +
											 "x.quantity_unit, " +
											 "y.location, " +
											 "x.manufacturer, " +
											 "x.supplier, " +
											 "x.date_of_entry, " +
											 "x.additional_info " +											 
											 "FROM chemicals x " +
											 "INNER JOIN locations y ON x.storage_location = y.id " +
											 "ORDER BY x.id";
	
	public static final String CORE_SEARCH_SQL = "SELECT " +
			 									 "x.id, " +
			 									 "x.chemical_name, " +
			 									 "x.brutto_formula, " +
			 									 "x.molar_mass, " +
			 									 "x.quantity_amount, " +
			 									 "x.quantity_unit, " +
												 "y.location, " +
												 "x.manufacturer, " +
												 "x.supplier, " +
												 "x.date_of_entry, " +
												 "x.additional_info " +		
			 									 "FROM chemicals x " +
			 									 "INNER JOIN locations y ON x.storage_location = y.id";

	/* ------------------------------------------------------------------------------------------ */
	
	public static final String VIEW_RECORD_SQL = "SELECT " +
			 									 "x.chemical_name, " +
			 									 "x.brutto_formula, " +
			 									 "x.molar_mass, " +
			 									 "x.quantity_amount, " +
			 									 "x.quantity_unit, " +
			 									 "y.location, " +
			 									 "x.manufacturer, " +
			 									 "x.supplier, " +
			 									 "x.date_of_entry, " +
			 									 "x.additional_info " +
			 									 "FROM chemicals x " +
			 									 "INNER JOIN locations y ON x.storage_location = y.id " +
			 									 "WHERE x.id = ?";

	public static final String DELETE_RECORD_SQL = "DELETE FROM chemicals WHERE id = ?";

	public static final String ADD_RECORD_SQL = "INSERT INTO chemicals(" +
												"chemical_name," +
												"brutto_formula, " +
												"molar_mass, " +
												"quantity_amount, " +
												"quantity_unit, " +
												"storage_location, " +
												"manufacturer, " +
												"supplier, " +
												"date_of_entry, " +
												"additional_info) " +
												"VALUES (?,?,?,?,?,(SELECT id FROM locations WHERE location = ?),?,?,?,?)";

	public static final String EDIT_RECORD_SQL = "UPDATE chemicals SET " + 
												 "chemical_name = ?, " +
												 "brutto_formula = ?, " +
												 "molar_mass = ?, " +
												 "quantity_amount = ?, " +
												 "quantity_unit = ?, " +
												 "storage_location = (SELECT id FROM locations WHERE location = ?), " +
												 "manufacturer = ?, " +
												 "supplier = ?, " +
												 "date_of_entry = ?, " +
												 "additional_info = ? " +
												 "WHERE id = ?";
	
	/* ------------------------------------------------------------------------------------------ */

}
