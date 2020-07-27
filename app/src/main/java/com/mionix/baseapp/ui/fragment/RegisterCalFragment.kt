package com.mionix.baseapp.ui.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.archit.calendardaterangepicker.customviews.CalendarListener
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.mionix.baseapp.R
import com.mionix.baseapp.ui.custom.setTextColorRes
import kotlinx.android.synthetic.main.fragment_current_working_cal.*
import kotlinx.android.synthetic.main.fragment_register_cal.*
import kotlinx.android.synthetic.main.fragment_register_cal.calendarView
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*


class RegisterCalFragment : Fragment() {
    private val selectedDates = mutableSetOf<LocalDate>()
    private val today = LocalDate.now()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_cal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        handleDateSelected()
    }

    private fun setupView() {
        calendarView.dayBinder = object : DayBinder<CurrentWorkingCalFragment.DayViewContainer> {
            // Called only when a new container is needed.
            override fun create(view: View) = CurrentWorkingCalFragment.DayViewContainer(view)

            // Called every time we need to reuse a container.
            override fun bind(container: CurrentWorkingCalFragment.DayViewContainer, day: CalendarDay) {
                container.textView.text = day.date.dayOfMonth.toString()
            }
        }
        //itnit date of Month
        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(0)
        val lastMonth = currentMonth.plusMonths(0)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        calendarView.setup(firstMonth, lastMonth, firstDayOfWeek)
        calendarView.scrollToMonth(currentMonth)
        calendarView.dayBinder = object : DayBinder<CurrentWorkingCalFragment.DayViewContainer> {
            override fun create(view: View) = CurrentWorkingCalFragment.DayViewContainer(view)
            override fun bind(container: CurrentWorkingCalFragment.DayViewContainer, day: CalendarDay) {
                container.textView.text = day.date.dayOfMonth.toString()
                if (day.owner == DayOwner.THIS_MONTH) {
                    container.textView.setTextColor(Color.WHITE)
                } else {
                    container.textView.setTextColor(Color.GRAY)
                }
            }
        }
        //hide out date of month
        calendarView.dayBinder = object : DayBinder<CurrentWorkingCalFragment.DayViewContainer> {
            override fun create(view: View) = CurrentWorkingCalFragment.DayViewContainer(view)
            override fun bind(container: CurrentWorkingCalFragment.DayViewContainer, day: CalendarDay) {
                container.textView.text = day.date.dayOfMonth.toString()
                if (day.owner == DayOwner.THIS_MONTH) {
                    container.textView.setTextColor(Color.WHITE)
                } else {
                    container.textView.setTextColor(Color.GRAY)
                }
            }
        }
        //create class DayViewContainer
        class DayViewContainer(view: View) : ViewContainer(view) {
            // Will be set when this container is bound. See the dayBinder.
            lateinit var day: CalendarDay
            val textView = view.findViewById<TextView>(R.id.exOneDayText)

            init {
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        if (selectedDates.contains(day.date)) {
                            selectedDates.remove(day.date)
                        } else {
                            selectedDates.add(day.date)
                        }
                        calendarView.notifyDayChanged(day)
                    }
                }
            }
        }
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
                            textView.setTextColorRes(R.color.example_1_bg)
                            textView.setBackgroundResource(R.drawable.example_1_selected_bg)
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
            }
        }
        // calendar.setCurrentMonth(current)
        if(context!=null){
            val spinnerLeft = arrayOf("Full Working Day","Morning","Afternoon")
            val arrayAdapterLeft = ArrayAdapter(context!!,R.layout.support_simple_spinner_dropdown_item,spinnerLeft)
            spTypeOfTime.adapter = arrayAdapterLeft
        }
    }

    private fun handleDateSelected() {

    }

    companion object {
        fun newInstance(): RegisterCalFragment {
            val args = Bundle()
            val fragment = RegisterCalFragment()
            fragment.arguments = args
            return fragment
        }
    }
}