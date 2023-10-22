package com.example.plantsservicefyp.fragment.buyer

import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.core.view.isEmpty
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plantsservicefyp.R
import com.example.plantsservicefyp.adapter.PlantsReminderRecyclerViewAdapter
import com.example.plantsservicefyp.databinding.FragmentSetReminderBinding
import com.example.plantsservicefyp.model.BoughtDate
import com.example.plantsservicefyp.model.NotificationData
import com.example.plantsservicefyp.util.ReminderAlarmManager
import com.example.plantsservicefyp.util.convertTo12Hr
import com.example.plantsservicefyp.util.log
import com.example.plantsservicefyp.util.toast
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.util.InternalAPI
import io.ktor.util.date.toDate
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class SetReminderFragment : Fragment() {

    private lateinit var binding: FragmentSetReminderBinding

    private lateinit var plantsReminderRecyclerViewAdapter: PlantsReminderRecyclerViewAdapter

    @Inject
    lateinit var reminderAlarmManager: ReminderAlarmManager

    private var userSelectedCalendar = Calendar.getInstance(Locale.ENGLISH)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSetReminderBinding.inflate(layoutInflater, container, false)

        plantsReminderRecyclerViewAdapter = PlantsReminderRecyclerViewAdapter(requireContext())

        ObjectAnimator.ofFloat(binding.contentContainerLayout, "translationY", 0f).apply {
            duration = 1000
            start()
        }

        ObjectAnimator.ofFloat(binding.contentContainerLayout, "alpha", 1f).apply {
            duration = 1000
            start()
            doOnEnd {
                ObjectAnimator.ofFloat(binding.wallClockRightImageView, "translationY", 0f).apply {
                    duration = 500
                    start()
                }
                ObjectAnimator.ofFloat(binding.wallClockRightImageView, "alpha", 1f).apply {
                    duration = 500
                    start()
                    doOnEnd {
                        ObjectAnimator.ofFloat(
                            binding.wallClockLeftImageView,
                            "translationY",
                            0f
                        ).apply {
                            duration = 500
                            start()
                        }
                        ObjectAnimator.ofFloat(binding.wallClockLeftImageView, "alpha", 1f)
                            .apply {
                                duration = 500
                                start()
                            }
                    }
                }
            }
        }

        binding.selectDateButton.setOnClickListener {
            openDateAndTimePicker {}
        }

        binding.createReminderButton.setOnClickListener {
            if (binding.textInputHeading.editText!!.text.isEmpty() && binding.textInputDescription.editText!!.text.isEmpty()) {
                context?.toast("fields are empty!")
                return@setOnClickListener
            }
            reminderAlarmManager.startReminder(
                reminderCalendar = userSelectedCalendar,
                notificationData = NotificationData(
                    binding.textInputHeading.editText?.text?.trim().toString(),
                    binding.textInputDescription.editText?.text?.trim().toString()
                )
            )
            Snackbar.make(binding.parentContainer, "Your plant health reminder is set! ❤", 3500)
                .apply {
                    setTextColor(Color.WHITE)
                }.show()
        }

        return binding.root
    }

    @OptIn(InternalAPI::class)
    private fun openDateAndTimePicker(callback: () -> Unit) {
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select appointment date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(constraintsBuilder.build())
                .build()
        datePicker.show(requireActivity().supportFragmentManager, "Date.Picker")
        datePicker.addOnPositiveButtonClickListener {
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).calendar.toDate(it).apply {
                userSelectedCalendar.apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                }
            }
            openTime() {
                callback()
            }
        }
    }

    private fun openTime(callback: () -> Unit) {
        val timePicker =
            MaterialTimePicker
                .Builder()
                .build()

        timePicker.show(requireActivity().supportFragmentManager, "Time.Picker")

        timePicker.addOnPositiveButtonClickListener {
            userSelectedCalendar.set(Calendar.HOUR, timePicker.hour)
            userSelectedCalendar.set(Calendar.MINUTE, timePicker.minute)
            if (timePicker.inputMode == 0) {
                userSelectedCalendar.set(Calendar.AM_PM, Calendar.PM)
            } else if (timePicker.inputMode == 1) {
                userSelectedCalendar.set(Calendar.AM_PM, Calendar.AM)
            }
            callback()
        }
    }
}