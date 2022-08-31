package com.example.waterreminder.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.*

class DateUtils {
    companion object {

        fun getToday():LocalDate{
            val instant = Clock.System.now()
            return instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
        }

        fun getMaxOfDayOfMonth(monthNumber:Int,year:Int):Int {
            return when (monthNumber) {
                1, 3, 5, 7, 8, 10, 12 -> 31
                4, 6, 9, 11 -> 30
                2 -> if (isLeapYear(year)) 29 else 28
                else -> 30
            }
        }

        private fun isLeapYear(year:Int):Boolean{
            val isDividedBy4AndNot100 = isDividedBy4(year) && !isDividedBy100(year)
            return if (isDividedBy4AndNot100) {
                true
            } else isDividedBy400(year)

        }

        private fun isDividedBy4(number : Int) : Boolean{
            return number % 4 == 0
        }

        private fun isDividedBy100(number : Int) : Boolean{
            return number % 100 == 0
        }

        private fun isDividedBy400(number : Int) : Boolean{
            return number % 400 == 0
        }
    }


    }
