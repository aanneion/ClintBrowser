package com.jhaiian.clint.profiles

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

internal class ProfileDatabase(context: Context) :
    SQLiteOpenHelper(context.applicationContext, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """CREATE TABLE $TABLE (
                $COL_POSITION INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_ID       TEXT    NOT NULL UNIQUE,
                $COL_NAME     TEXT    NOT NULL,
                $COL_COLOR    INTEGER NOT NULL,
                $COL_IDENTITY_PRESET TEXT NOT NULL DEFAULT 'native_android',
                $COL_PRESENTATION_MODE TEXT NOT NULL DEFAULT 'auto'
            )"""
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE $TABLE ADD COLUMN $COL_IDENTITY_PRESET TEXT NOT NULL DEFAULT 'native_android'")
            db.execSQL("ALTER TABLE $TABLE ADD COLUMN $COL_PRESENTATION_MODE TEXT NOT NULL DEFAULT 'auto'")
        }
    }

    companion object {
        const val DB_NAME      = "clint_profiles.db"
        const val DB_VERSION   = 2
        const val TABLE        = "profiles"
        const val COL_POSITION = "position"
        const val COL_ID       = "id"
        const val COL_NAME     = "name"
        const val COL_COLOR    = "color"
        const val COL_IDENTITY_PRESET = "identity_preset"
        const val COL_PRESENTATION_MODE = "presentation_mode"
    }
}
