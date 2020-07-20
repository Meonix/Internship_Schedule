package com.mionix.baseapp.ui.custom

import android.os.Build
import android.widget.DatePicker
import com.mionix.baseapp.model.DateModel

open class CustomCalendar {
    private var startMontn: Int = 0
    private var startDay: Int = 0
    private var startYear: Int = 0

    private var endMontn: Int = 0
    private var endDay: Int = 0
    private var endYear: Int = 0

    fun startDate(datePicker : DatePicker, dateModel: DateModel):DateModel{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (dateModel != null){
                endDay = dateModel.day
                endMontn = dateModel.month
                endYear = dateModel.year
            }
            startDay = datePicker.dayOfMonth
            startMontn = datePicker.month
            startYear = datePicker.year
            datePicker.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
                if (!(endDay == 0 || endMontn == 0 || endYear == 0)) {
                    if (year == endYear) {
                        /*Start Year Inter = End Year Intern*/
                        startYear = year
                        if (monthOfYear == endMontn) {
                            /*Start Month Intern = End Month Intern*/
                            startMontn = monthOfYear
                            if (dayOfMonth <= endDay) {
                                /*Start Day Intern <= End Day Intern*/
                                startDay = dayOfMonth
                            } else {
                                /*Start Day Intern > End Day Intern*/
                                datePicker.updateDate(startYear, startMontn, endDay)
                            }
                        } else if (monthOfYear < endMontn) {
                            /*Start Month Intern < End Month Intern*/
                            startMontn = monthOfYear
                            startDay = dayOfMonth
                        } else if (monthOfYear > endMontn) {
                            /*Start Month Intern > End Month Intern*/
                            datePicker.updateDate(startYear, endMontn, dayOfMonth)
                        }
                    } else if (year < endYear) {
                        /*Start Year Inter < End Year Intern*/
                        startDay = dayOfMonth
                        startMontn = monthOfYear
                        startYear = year
                    } else if (year > endYear) {
                        /*Start Year Inter > End Year Intern*/
                        datePicker.updateDate(endYear, monthOfYear, dayOfMonth)
                    }
                } else {
                    startDay = dayOfMonth
                    startMontn = monthOfYear
                    startYear = year
                }
            }
        }
        return DateModel(startDay, startMontn, startYear)
    }

    fun endDate(datePicker: DatePicker, dateModel:DateModel):DateModel{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (dateModel != null){
                startDay = dateModel.day
                startMontn = dateModel.month
                startYear = dateModel.year
            }
            endDay = datePicker.dayOfMonth
            endMontn = datePicker.month
            endYear = datePicker.year
            datePicker.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
                if (!(startDay == 0 || startMontn == 0 || startYear == 0)) {
                    if (year == startYear) {
                        /*End Year Intern = Star Year Intern*/
                        endYear = year
                        if (monthOfYear == startMontn) {
                            /*End Motnh Intern = Star Month Intern*/
                            endMontn = monthOfYear
                            if (dayOfMonth >= startDay) {
                                /*End Day Intern >= Star Day Intern*/
                                endDay = dayOfMonth
                            } else {
                                /*End Day Intern < Star Day Intern*/
                                datePicker.updateDate(endYear, endMontn, startDay)
                            }
                        } else if (monthOfYear > startMontn) {
                            /*End Motnh Intern > Star Month Intern*/
                            endMontn = monthOfYear
                            endDay = dayOfMonth
                        } else if (monthOfYear < startMontn) {
                            /*End Motnh Intern < Star Month Intern*/
                            datePicker.updateDate(endYear, startMontn, dayOfMonth)
                        }
                    } else if (year > startYear) {
                        /*End Year Intern > Star Year Intern*/
                        endDay = dayOfMonth
                        endMontn = monthOfYear
                        endYear = year
                    } else if (year < startYear) {
                        /*End Year Intern < Star Year Intern*/
                        datePicker.updateDate(startYear, monthOfYear, dayOfMonth)
                    }
                } else {
                    endDay = dayOfMonth
                    endMontn = monthOfYear
                    endYear = year
                }
            }
        }
        return DateModel(endDay, endMontn, endYear)
    }

    fun setupDate(day:Int, month:Int, year:Int, datePicker: DatePicker){
        datePicker.updateDate(year, month, day)
    }
}