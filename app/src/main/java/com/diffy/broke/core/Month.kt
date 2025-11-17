package com.diffy.broke.core

import java.util.Date

enum class Month(val position: Int) {
    JANUARY(1), FEBRUARY(2), MARCH(3), APRIL(4), MAY(5), JUNE(6), JULY(7), AUGUST(8), SEPTEMBER(9), OCTOBER(10), NOVEMBER(11), DECEMBER(12);

    fun next(): Month = when(this){
        JANUARY -> FEBRUARY
        FEBRUARY -> MARCH
        MARCH -> APRIL
        APRIL -> MAY
        MAY -> JUNE
        JUNE -> JULY
        JULY -> AUGUST
        AUGUST -> SEPTEMBER
        SEPTEMBER -> OCTOBER
        OCTOBER -> NOVEMBER
        NOVEMBER -> DECEMBER
        DECEMBER -> JANUARY
    }

    fun previous(): Month = when(this){
        JANUARY -> DECEMBER
        FEBRUARY -> JANUARY
        MARCH -> FEBRUARY
        APRIL -> MARCH
        MAY -> APRIL
        JUNE -> MAY
        JULY -> JUNE
        AUGUST -> JULY
        SEPTEMBER -> AUGUST
        OCTOBER -> SEPTEMBER
        NOVEMBER -> OCTOBER
        DECEMBER -> NOVEMBER
    }

    companion object {
        val current: Month
            get() = when (Date().month) {
                0 -> JANUARY
                1 -> FEBRUARY
                2 -> MARCH
                3 -> APRIL
                4 -> MAY
                5 -> JUNE
                6 -> JULY
                7 -> AUGUST
                8 -> SEPTEMBER
                9 -> OCTOBER
                10 -> NOVEMBER
                11 -> DECEMBER
                else -> JANUARY
            }
    }
}