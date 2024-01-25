package com.example.newconverterapp.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BalanceEntity(
    @PrimaryKey
    val code: String,
    val balance: Double
)