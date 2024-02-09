package com.codelab.basics;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.concurrent.Executor;

public class DBClass extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "POKEMON_LIST.db";

    public DBClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d("Save_v03", "DB onCreate()");

        db.execSQL("CREATE TABLE pokemon (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, pokemonName VARCHAR(256), type VARCHAR(256), pokedexNum INTEGER, pokeInfo VARCHAR(256), access_count INTEGER DEFAULT 0)");

        db.execSQL(
                "INSERT INTO pokemon(pokemonName, type, pokedexNum, pokeInfo) VALUES('Bulbasaur', 'Grass', 1, 'There is a plant seed on its back right from the day this Pokémon is born. The seed slowly grows larger.')");
        db.execSQL(
                "INSERT INTO pokemon(pokemonName, type, pokedexNum, pokeInfo) VALUES('Charmander', 'Fire', 4, 'It has a preference for hot things. When it rains, steam is said to spout from the tip of its tail.')");
        db.execSQL(
                "INSERT INTO pokemon(pokemonName, type, pokedexNum, pokeInfo) VALUES('Squirtle', 'Water', 7, 'When it retracts its long neck into its shell, it squirts out water with vigorous force.')");
        db.execSQL(
                "INSERT INTO pokemon(pokemonName, type, pokedexNum, pokeInfo) VALUES('Caterpie', 'Bug', 10, 'For protection, it releases a horrible stench from the antenna on its head to drive away enemies.')");
        db.execSQL(
                "INSERT INTO pokemon(pokemonName, type, pokedexNum, pokeInfo) VALUES('Pidgey', 'Flying', 16, 'Very docile. If attacked, it will often kick up sand to protect itself rather than fight back.')");
        db.execSQL(
                "INSERT INTO pokemon(pokemonName, type, pokedexNum, pokeInfo) VALUES('Ekans', 'Poison', 23, 'The older it gets, the longer it grows. At night, it wraps its long body around tree branches to rest.')");
        db.execSQL(
                "INSERT INTO pokemon(pokemonName, type, pokedexNum, pokeInfo) VALUES('Pikachu', 'Electric', 25, 'When it is angered, it immediately discharges the energy stored in the pouches in its cheeks. ')");
        db.execSQL(
                "INSERT INTO pokemon(pokemonName, type, pokedexNum, pokeInfo) VALUES('Diglett', 'Ground', 50, 'It lives about one yard underground, where it feeds on plant roots. It sometimes appears aboveground.')");
        db.execSQL(
                "INSERT INTO pokemon(pokemonName, type, pokedexNum, pokeInfo) VALUES('Machamp', 'Fighting', 68, 'It punches with its four arms at blinding speed. It can launch 1,000 punches in two seconds.')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
        Log.d("Save_v03", "DB onUpgrade() to version " + DATABASE_VERSION);
        db.execSQL("DROP TABLE IF EXISTS pokemon");
        onCreate(db);
    }

    // Return 2D String array of the records suitable to display
    // master-detail type list data
    public String[][] get2DRecords() {
        // Real code would select * from DB table and populate
        // the first string with a title, and the 2nd string
        // with details which could be a concat of remaining
        // fields

        Log.d("DBClass.get2DRecords", "Start===========================");
        SQLiteDatabase db = this.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {"id", "pokemonName", "type", "pokedexNum", "pokeInfo", "access_count"};
        String selection = "pokedexNum < ?";  // ? gets filled in by args
        String[] selectionArgs = {"150"};

        // How you want the results sorted in the resulting Cursor
        String sortOrder = "pokedexNum" + " ASC";   // sort by pokedexNum in ascending order

        Cursor c = db.query(
                "pokemon",  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        // Returned Array is size of ResultSet
        Log.d("DBClass.get2DRecords", "c.getCount()=" + c.getCount());
        String[][] newArray = new String[c.getCount()][5];

        c.moveToFirst();

        do {
            int pos = c.getPosition();
            Log.d("DBClass.get2DRecords", "pos=" + pos);
            long itemId = c.getLong(c.getColumnIndexOrThrow("id")); // Had to move this line in here to increment db access count

            // Initialize the array elements to display in MainActivity.kt
            newArray[pos][0] = c.getString(c.getColumnIndexOrThrow("pokemonName"));
            newArray[pos][1] = c.getString(c.getColumnIndexOrThrow("type"));
            newArray[pos][2] = String.valueOf(c.getInt(c.getColumnIndexOrThrow("pokedexNum")));
            newArray[pos][3] = c.getString(c.getColumnIndexOrThrow("pokeInfo"));
            int accessCount = c.getInt(c.getColumnIndexOrThrow("access_count"));
            newArray[pos][4] = String.valueOf(accessCount + 1);


            // Need to increment access_count on each 'read' or 'select'
            //int accessCount = c.getInt(c.getColumnIndexOrThrow("access_count"));
            ContentValues accessCountValue = new ContentValues();
            accessCountValue.put("access_count", accessCount + 1);
            db.update("pokemon", accessCountValue, "id=?", new String[]{String.valueOf(itemId)});

            // Log
            Log.d("DBClass.get2dRecords", "Access count for " + newArray[pos][0] + ": " + (accessCount + 1));

            Log.d("DBClass.get2DRecords", "Next Row");
        } while (c.moveToNext());  // Do while there is a next

        Log.d("DBClass.get2DRecords", "Dump array");
        for (String[] i : newArray) {
            for (String j : i) {
                Log.d("DBClass.get2DRecords", "j=>" + j);
            }
        }

        Log.d("DBClass.get2DRecords", "Sleep ..........................");

//        // Slow down just for fun to see what happens
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        Log.d("DBClass.get2DRecords", "End  ===========================");
        return newArray;
    }
}
