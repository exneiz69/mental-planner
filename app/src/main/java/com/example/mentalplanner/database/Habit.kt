package com.example.mentalplanner.database

import androidx.room.*
import com.google.gson.Gson
import java.util.*

@Entity(tableName = "Habits")
@TypeConverters(HabitConverters::class)
data class Habit(
    @ColumnInfo(name = "ID")
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "Preset")
    var preset: Preset,
    @ColumnInfo(name = "Name")
    var name: String,
    @ColumnInfo(name = "Icon")
    var icon: Int,
    @ColumnInfo(name = "HabitDays")
    var habitDays: HabitDays = HabitDays(),
    @ColumnInfo(name = "TimePeriod")
    var timePeriod: TimePeriod = TimePeriod.Anytime,
    @ColumnInfo(name = "Active")
    var active: Boolean = false,
    @ColumnInfo(name = "Completed")
    var completed: Boolean = false,
    @ColumnInfo(name = "CompletedLastTime")
    var completedLastTime: Date = Date(0)
)

class HabitConverters {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromHabitDays(habitDays: HabitDays): String = Gson().toJson(habitDays)

        @TypeConverter
        @JvmStatic
        fun toHabitDays(json: String): HabitDays = Gson().fromJson(json, HabitDays::class.java)

        @TypeConverter
        @JvmStatic
        fun fromDate(date: Date) = date.time

        @TypeConverter
        @JvmStatic
        fun toDate(date: Long) = Date(date)
    }
}

enum class Preset {
    Custom, Essential, EatDrinkHealthily, KeepActive, EaseStress, LeisureMoments;

    companion object {
        fun fromInt(value: Int) = Preset.values().firstOrNull { it.ordinal == value }
    }
}

data class HabitDays(
    var monday: Boolean = true,
    var tuesday: Boolean = true,
    var wednesday: Boolean = true,
    var thursday: Boolean = true,
    var friday: Boolean = true,
    var saturday: Boolean = true,
    var sunday: Boolean = true
)

enum class TimePeriod {
    Anytime, Morning, Afternoon, Evening;

    companion object {
        fun fromInt(value: Int) = TimePeriod.values().firstOrNull { it.ordinal == value }
    }
}