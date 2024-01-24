package com.example.newconverterapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newconverterapp.db.dao.BalanceDao
import com.example.newconverterapp.db.entity.BalanceEntity

@Database(entities = [BalanceEntity::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {
    abstract fun getBalanceDao(): BalanceDao
}