package com.mionix.baseapp.ui.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.mionix.baseapp.R
import com.mionix.baseapp.ui.base.BaseBackButtonActivity
import com.mionix.baseapp.ui.custom.setTextColorRes
import com.mionix.baseapp.utils.onClickThrottled
import kotlinx.android.synthetic.main.activity_register_time_working.*
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.*

class RegisterTimeWorkingActivity : BaseBackButtonActivity() {
    private var mAuth = FirebaseAuth.getInstance()
    private var currentUser = mAuth.currentUser
    private var workingRef = FirebaseDatabase.getInstance().reference.child("Users").child(currentUser?.uid.toString()).child("working_time")
//        .child("morning_time")
//    private var afternoonRef = FirebaseDatabase.getInstance().reference.child("Users").child(currentUser?.uid.toString()).child("working_time").child("afternoon_time")
//    private var fullTimeRef = FirebaseDatabase.getInstance().reference.child("Users").child(currentUser?.uid.toString()).child("working_time").child("full_time")
    private var listDateData = mutableListOf<DataWorkingDay>()
    private val selectedDates = mutableSetOf<LocalDate>()
    private val today = LocalDate.now()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_time_working)
        setupView()
        handleOnClick()
    }

    private fun handleOnClick() {
        btRegisterTimeWorking.onClickThrottled {
            var fullTime = ""
            var morningTime =""
            var afternoonTime = ""
            listDateData.forEach {
//                Log.d("DUY",it.date.dayOfMonth.toString())
//                Log.d("DUY",it.type.toString())
                    when(it.type){
                        1 -> {
                            fullTime += "," + it.date.dayOfMonth.toString()
                        }
                        2-> {
                            morningTime += "," + it.date.dayOfMonth.toString()
                        }
                        3-> {
                            afternoonTime += "," + it.date.dayOfMonth.toString()
                        }
                    }
            }
            val calendarDay =  Calendar.getInstance()
            workingRef.child((calendarDay.get(Calendar.MONTH)+2).toString()).child("full_time").setValue(fullTime)
            workingRef.child((calendarDay.get(Calendar.MONTH)+2).toString()).child("afternoon_time").setValue(afternoonTime)
            workingRef.child((calendarDay.get(Calendar.MONTH)+2).toString()).child("morning_time").setValue(morningTime).addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(this@RegisterTimeWorkingActivity,"Register Successful..",Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this@RegisterTimeWorkingActivity,it.exception?.message,Toast.LENGTH_SHORT).show()
                }
            }
            onBackPressed()
        }
    }
    private class DataWorkingDay(var type:Int,var date: LocalDate)
    private fun setupView() {
        //create class DayViewContainer
        class DayViewContainer(view: View) : ViewContainer(view) {
            // Will be set when this container is bound. See the dayBinder.
            lateinit var day: CalendarDay
            val textView = view.findViewById<TextView>(R.id.exOneDayText)

            init {
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        if (selectedDates.contains(day.date)) {
                            listDateData.removeIf { it.date == day.date}
                            selectedDates.remove(day.date)
                        } else {
                            selectedDates.add(day.date)
                        }
                        calendarView.notifyDayChanged(day)
                    }
                }
            }
        }
        //itnit date of Month
        val currentMonth = YearMonth.now()
//        val firstMonth = currentMonth.minusMonths(0)
        val lastMonth = currentMonth.plusMonths(1)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        calendarView.setup(lastMonth, lastMonth, firstDayOfWeek)
       // calendarView.scrollToMonth(currentMonth)

        //Using class DayViewContainer to handle event pick on calendar
        calendarView.dayBinder =object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.textView
                textView.text = day.date.dayOfMonth.toString()
                if (day.owner == DayOwner.THIS_MONTH) {
                    when {
                        selectedDates.contains(day.date) -> {
                            when (spTypeOfTime.selectedItemPosition) {
                                0 -> {
                                    listDateData.add(DataWorkingDay(1,day.date))
                                    textView.setTextColorRes(R.color.example_1_bg)
                                    textView.setBackgroundResource(R.drawable.fullday_selected)
                                }
                                1 -> {
                                    listDateData.add(DataWorkingDay(2,day.date))
                                    textView.setTextColorRes(R.color.example_1_bg)
                                    textView.setBackgroundResource(R.drawable.example_1_selected_bg)
                                }
                                else -> {
                                    listDateData.add(DataWorkingDay(3,day.date))
                                    textView.setTextColorRes(R.color.example_1_bg)
                                    textView.setBackgroundResource(R.drawable.affternoon_selected)
                                }
                            }

                        }
                        today == day.date -> {
                            textView.setTextColorRes(R.color.example_1_white)
                            textView.setBackgroundResource(R.drawable.example_1_today_bg)
                        }
                        else -> {
                            textView.setTextColorRes(R.color.example_1_white)
                            textView.background = null
                        }
                    }
                } else {
                    textView.setTextColorRes(R.color.example_1_white_light)
                    textView.background = null
                }
                val c = Calendar.getInstance()
                c.set(c.get(Calendar.YEAR),c.get(Calendar.MONTH)+1,day.day)
                if(day.owner == DayOwner.THIS_MONTH && (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)){
                    textView.setTextColorRes(R.color.colorPrimaryDark)
                    textView.background = null
                }
            }
        }
        // calendar.setCurrentMonth(current)
            val spinnerLeft = arrayOf("Full Working Day","Morning","Afternoon")
            val arrayAdapterLeft = ArrayAdapter(this@RegisterTimeWorkingActivity,R.layout.support_simple_spinner_dropdown_item,spinnerLeft)
            spTypeOfTime.adapter = arrayAdapterLeft
    }
    override fun setTitleToolbar(): String {
        return "Register Time Working"
    }
}