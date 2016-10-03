package eu.dziadosz.shoppinglist;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Radosław on 02.10.2016.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "shopping_database";
    public static final String DATABASE_TABLE = "products";
    private final String NAME = "name";
    private final String COUNT = "count";
    private final String PRICE = "price";

    public DatabaseOpenHelper(Context context) {
        // Context, database name, optional cursor factory, database version
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create a new table
        db.execSQL("CREATE TABLE "+DATABASE_TABLE+" (_id INTEGER PRIMARY KEY AUTOINCREMENT, "+NAME+" TEXT, "+COUNT+" INT, "+PRICE+" REAL);");
        // create sample data
        ContentValues values = new ContentValues();
        values.put(NAME, "Potatoes");
        values.put(COUNT, 50);
        values.put(PRICE, 2.5);
        // insert data to database, name of table, "Nullcolumnhack", values
        db.insert(DATABASE_TABLE, null, values);
        // a more data...
        values.put(NAME, "Raspberrys");
        values.put(COUNT, 100);
        values.put(PRICE, 12.35);
        db.insert(DATABASE_TABLE, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
        onCreate(db);
    }
}
