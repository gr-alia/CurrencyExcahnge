package com.example.currencyexchange.data.db

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.math.BigDecimal
import javax.inject.Inject

@ProvidedTypeConverter
class ModelConverter @Inject constructor() {
    @TypeConverter
    fun fromBigDecimal(value: BigDecimal): String {
        return value.toString()
    }

    @TypeConverter
    fun toBigDecimal(value: String): BigDecimal {
        return BigDecimal(value)
    }

}