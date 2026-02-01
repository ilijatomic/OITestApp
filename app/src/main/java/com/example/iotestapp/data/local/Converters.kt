package com.example.iotestapp.data.local

import androidx.room.TypeConverter
import com.example.testapp.domain.model.TransactionType

class Converters {

    @TypeConverter
    fun fromTransactionType(value: TransactionType): String = value.name

    @TypeConverter
    fun toTransactionType(value: String): TransactionType = TransactionType.valueOf(value)
}