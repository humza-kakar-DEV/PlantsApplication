package com.example.plantsservicefyp.fragment.buyer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.plantsservicefyp.databinding.FragmentPaymentBinding
import com.example.plantsservicefyp.model.firebase.Plant
import com.example.plantsservicefyp.util.*
import com.example.plantsservicefyp.util.constant.AppConstants
import com.example.plantsservicefyp.viewmodel.AuthenticationViewModel
import com.example.plantsservicefyp.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
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

    private lateinit var receipt: String

    private lateinit var totalPrice: String

    private lateinit var mailCallback: () -> Unit

//    NOTE: USE ASSISTED FACTORY TO PASS IN CONTEXT IN CONSTRUCTOR OF JAZZ CASH PAYMENT CLASS
//    @Inject
//    private lateinit var jazzCashPayment: JazzCashPayment l

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentBinding.inflate(layoutInflater, container, false)

//        create receipt and pass into send email method

//        NOTE: CREATE CUSTOM NOTIFICATION ALSO SEND THAT AFTER PAYMENT SUCCESS TRANSACTION

//        bought items
        sharedViewModel._observeBoughtItems.observe(viewLifecycleOwner) { boughtPlants ->
            authenticationViewModel._observeCurrentUser.observe(viewLifecycleOwner) {
                when (it) {
                    is CurrentUserType.Buyer -> {
                        buyerEmail = it.user.email
                        sharedViewModel._observeTotalPrice.observe(viewLifecycleOwner) { totalPrice ->
                            JazzCashPayment(requireContext()).launch(
                                binding.paymentFragmentWebView,
                                totalPrice.toString()
                            )
                            receipt = boughtPlants.createReceipt(totalPrice.toString()).toString()
                            context?.log(receipt)

                            mailCallback()
                        }
                    }
                    else -> {}
                }
            }
        }

        mailCallback = {
            requireContext().toast("mail v1.1")
            GlobalScope.launch (Dispatchers.IO) {
                CustomEmail(requireContext()).sendEmail(
                    toEmail = buyerEmail,
                    emailSubject = "Thank you for placing order 😊",
                    emailBody = receipt.toString()
                ) {
                    requireContext().log("MAIL HAS BEEN SENT!!!")
                }
            }
        }

        broadCastReceiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                if (p1?.action.equals(AppConstants.JAZZ_CASH_PAYMENT_RESPONSE.value)) {

                    context?.toast("payment successfull 😊")

                    customNotification.notification(buyerEmail)

                    Thread(Runnable {
                        Handler(
                            Looper.getMainLooper()
                        ).postDelayed(
                            Runnable {
                                     requireActivity().showPaymentAlert().show()
                            }, 1000
                        )
                    }).start()

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