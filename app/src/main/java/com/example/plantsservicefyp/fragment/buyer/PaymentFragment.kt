package com.example.plantsservicefyp.fragment.buyer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.plantsservicefyp.R
import com.example.plantsservicefyp.databinding.FragmentPaymentBinding
import com.example.plantsservicefyp.util.*
import com.example.plantsservicefyp.util.constant.AppConstants
import com.example.plantsservicefyp.viewmodel.AuthenticationViewModel
import com.example.plantsservicefyp.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PaymentFragment : Fragment() {

    private lateinit var binding: FragmentPaymentBinding

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val authenticationViewModel: AuthenticationViewModel by viewModels()

    @Inject
    lateinit var customNotification: CustomNotification

    private var buyerEmail: String = ""

    private lateinit var broadCastReceiver: BroadcastReceiver

//    NOTE: USE ASSISTED FACTORY TO PASS IN CONTEXT IN CONSTRUCTOR OF JAZZ CASH PAYMENT CLASS
//    @Inject
//    private lateinit var jazzCashPayment: JazzCashPayment

    @Inject
    lateinit var customEmail: CustomEmail

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentBinding.inflate(layoutInflater, container, false)

//        create receipt and pass into send email method

//        NOTE: CREATE CUSTOM NOTIFICATION ALSO SEND THAT AFTER PAYMENT SUCCESS TRANSACTION

        authenticationViewModel._observeCurrentUser.observe(viewLifecycleOwner) {
            when (it) {
                is CurrentUserType.Buyer -> {
                    buyerEmail = it.user.email
                }
                else -> {
                    "only buyer user required"
                }
            }
        }

        sharedViewModel._observeTotalPrice.observe(viewLifecycleOwner) {
            JazzCashPayment(requireContext()).launch(binding.paymentFragmentWebView, it.toString())
        }

//        bought items
        sharedViewModel._observeBoughtItems.observe(viewLifecycleOwner) {
            it.forEach {
//                NOTE:
//                bought items iterated
//                make receipt
            }
        }

        broadCastReceiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                if (p1?.action.equals(AppConstants.JAZZ_CASH_PAYMENT_RESPONSE.value)) {

                    context?.toast("payment successfull!!!")

                    customNotification.notification(buyerEmail)

                    lifecycleScope.launch {
                        customEmail.sendEmail(
                            toEmail = buyerEmail,
                            emailSubject = "Your order has been palced will be delivered to your home soon :)",
                            emailBody = "->*** make a receipt!!! ***<-",
                        )
                    }


                }
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        context?.registerReceiver(
            broadCastReceiver,
            IntentFilter(AppConstants.JAZZ_CASH_PAYMENT_RESPONSE.value)
        )
    }

    override fun onDestroy() {
        super.onDestroy()

        context?.unregisterReceiver(broadCastReceiver)
    }
}