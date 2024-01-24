package com.example.newconverterapp.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newconverterapp.db.entity.BalanceEntity

@Dao
interface BalanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBalance(entity: BalanceEntity)

    @Query("SELECT * FROM BalanceEntity")
    suspend fun getBalance(): List<BalanceEntity>

    @Query("SELECT * FROM BalanceEntity WHERE code = :code")
    suspend fun getSelectedBalance(code: String): BalanceEntity?

    @Query("DELETE FROM BalanceEntity WHERE code = :code")
    suspend fun removeBalance(code: String)
}