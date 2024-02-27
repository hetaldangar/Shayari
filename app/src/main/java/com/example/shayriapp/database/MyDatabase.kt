package com.example.shayriapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.util.Log
import com.example.shayriapp.modeclass.CategoryModelClass
import com.example.shayriapp.modeclass.DisplayCategoryModelClass
import com.example.shayriapp.modeclass.FavouriteModelClass
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class MyDatabase(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    private val mDataBase: SQLiteDatabase? = null
    private var mNeedUpdate = false
    private val mContext: Context

    init {
        if (Build.VERSION.SDK_INT >= 17) DB_PATH =
            context.applicationInfo.dataDir + "/databases/" else DB_PATH =
            "/data/data/" + context.packageName + "/databases/"
        mContext = context
        copy()
        updateDataBase()

        this.readableDatabase
    }

    private fun copy() {
        if (!checkDataBase()) {
            this.readableDatabase
            close()
            try {
                copyDBFile()
            } catch (mIOException: IOException) {
                //  throw Error("ErrorCopyingDataBase")
            }
        }
    }

    private fun checkDataBase(): Boolean {
        val dbFile = File(DB_PATH + DB_NAME)
        return dbFile.exists()
    }

    @Throws(IOException::class)
    private fun copyDBFile() {
        val mInput = mContext.assets.open(DB_NAME)
        val mOutput: OutputStream = FileOutputStream(DB_PATH + DB_NAME)
        val mBuffer = ByteArray(1024)
        var mLength: Int
        while (mInput.read(mBuffer).also { mLength = it } > 0) mOutput.write(mBuffer, 0, mLength)
        mOutput.flush()
        mOutput.close()
        mInput.close()
    }

    override fun onCreate(db: SQLiteDatabase) {}
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (newVersion > oldVersion) mNeedUpdate = true
    }

    //    TODO update database
    @Throws(IOException::class)
    fun updateDataBase() {
        if (mNeedUpdate) {
            val dbFile = File(DB_PATH + DB_NAME)
            if (dbFile.exists()) dbFile.delete()
            copy()
            mNeedUpdate = false
        }
    }

    @Synchronized
    override fun close() {
        mDataBase?.close()
        super.close()
    }

    fun categoryData(): ArrayList<CategoryModelClass> {
        var list = ArrayList<CategoryModelClass>()

        val db = readableDatabase
        val sql = "select * from categoryTB"
        val c = db.rawQuery(sql, null)
        if (c.count > 0) {
            c.moveToFirst()
            do {
                val id = c.getInt(0)
                val categoryName = c.getString(1)

                Log.e(TAG, "readData:==> $id   $categoryName ")

                var model = CategoryModelClass(id, categoryName)
                list.add(model)

            } while (c.moveToNext())
        }
        return list

    }

    fun shayriData(c_ID: Int): ArrayList<DisplayCategoryModelClass> {
        var shayriList = ArrayList<DisplayCategoryModelClass>()

        val dbS = readableDatabase
        val sqlS = "select * from shayriTB where shayri_CategoryID='$c_ID'"
        val c = dbS.rawQuery(sqlS, null)
        if (c.count > 0) {
            c.moveToFirst()
            do {
                var shayri_id = c.getInt(0)
                var shayri = c.getString(1)
                var category_Id = c.getInt(2)
                var fav = c.getInt(3)

                Log.e(TAG, "shayriData:==> $shayri_id   $shayri ")
                var shayrimodel = DisplayCategoryModelClass(shayri_id, shayri, category_Id, fav)

                shayriList.add(shayrimodel)
            } while (c.moveToNext())
        }
        return shayriList

    }

    fun Fav_updateRecord(fav: Int, shayri_id: Int) {
        val update = writableDatabase
        val updateSql =
            "update shayriTB set shayri_Save='$fav'  where shayriText_Id='$shayri_id' "
        update.execSQL(updateSql)
    }

    fun Fav_DisplayRecord(): ArrayList<FavouriteModelClass> {
        var DisplayList = ArrayList<FavouriteModelClass>()

        val dbS = readableDatabase
        val sqlS = "select * from shayriTB where shayri_Save = 1 "
        val c = dbS.rawQuery(sqlS, null)
        if (c.moveToFirst()) {

            do {
                var shayri_id = c.getInt(0)
                var shayri = c.getString(1)
                var fav = c.getInt(2)

                Log.e(TAG, "shayriData:==> $shayri_id   $shayri ")
                var shayrimodel = FavouriteModelClass(shayri_id, shayri, fav)

                DisplayList.add(shayrimodel)
            } while (c.moveToNext())
        }
        return DisplayList

    }


    companion object {
        private const val TAG = "MyDatabase"
        private const val DB_NAME = "shayriDb.db"
        private var DB_PATH = ""
        private const val DB_VERSION = 1
    }
}