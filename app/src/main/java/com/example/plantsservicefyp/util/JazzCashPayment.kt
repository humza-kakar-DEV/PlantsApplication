package com.example.plantsservicefyp.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


open class JazzCashPayment(
    private var context: Context,
) {

    private val Jazz_MerchantID = "MC52757"
    private val Jazz_Password = "v3875bxv89"
    private val Jazz_IntegritySalt = "uw18485s40"
    private var postData = ""
    private val paymentReturnUrl = "https://www.youtube.com/"

    private lateinit var mWebView: WebView

    fun launch(webView: WebView, price: String) {
        mWebView = webView
        // Enable Javascript
        // Enable Javascript
        val webSettings: WebSettings = mWebView.getSettings()
        webSettings.javaScriptEnabled = true

        mWebView.setWebViewClient(MyWebViewClient(context))
        webSettings.domStorageEnabled = true
        mWebView.addJavascriptInterface(FormDataInterface(context), "FORMOUT")

//        Intent intentData = getIntent();
//        String price = intentData.getStringExtra("price");
//        System.out.println("AhmadLogs: price_before : " +price);

//        String[] values = price.split("\\.");
//        price = values[0];

//        Intent intentData = getIntent();
//        String price = intentData.getStringExtra("price");
//        System.out.println("AhmadLogs: price_before : " +price);

//        String[] values = price.split("\\.");
//        price = values[0];

        val Date = Date()
        val dateFormat = SimpleDateFormat("yyyyMMddHHmmss")
        val DateString = dateFormat.format(Date)
        println("AhmadLogs: DateString : $DateString")

        // Convert Date to Calendar

        // Convert Date to Calendar
        val c = Calendar.getInstance()
        c.time = Date
        c.add(Calendar.HOUR, 1)

        // Convert calendar back to Date

        // Convert calendar back to Date
        val currentDateHourPlusOne = c.time
        val expiryDateString = dateFormat.format(currentDateHourPlusOne)
        println("AhmadLogs: expiryDateString : $expiryDateString")

        val TransactionIdString = "T$DateString"
        println("AhmadLogs: TransactionIdString : $TransactionIdString")

        val pp_MerchantID = Jazz_MerchantID
        val pp_Password = Jazz_Password
        val IntegritySalt = Jazz_IntegritySalt
        val pp_ReturnURL = paymentReturnUrl
        val pp_Version = "1.1"
        val pp_TxnType = ""
        val pp_Language = "EN"
        val pp_SubMerchantID = ""
        val pp_BankID = "TBANK"
        val pp_ProductID = "RETL"
        val pp_TxnCurrency = "PKR"
        val pp_BillReference = "billRef"
        val pp_Description = "Description of transaction"
        var pp_SecureHash = ""
        val pp_mpf_1 = "1"
        val pp_mpf_2 = "2"
        val pp_mpf_3 = "3"
        val pp_mpf_4 = "4"
        val pp_mpf_5 = "5"

        //sortedString = "cwu55225t6&1000&TBANK&billRef&Description of transaction&EN&MC10487&z740xw7fu0&RETL&http://localhost/jazzcash_part_3/order_placed.php&PKR&20201223202501&20201223212501&T20201223202501&1.1&1&2&3&4&5";
        //pp_SecureHash = php_hash_hmac(sortedString, IntegritySalt);


        //sortedString = "cwu55225t6&1000&TBANK&billRef&Description of transaction&EN&MC10487&z740xw7fu0&RETL&http://localhost/jazzcash_part_3/order_placed.php&PKR&20201223202501&20201223212501&T20201223202501&1.1&1&2&3&4&5";
        //pp_SecureHash = php_hash_hmac(sortedString, IntegritySalt);
        var sortedString = ""
        sortedString += "$IntegritySalt&"
        sortedString += "$price&"
        sortedString += "$pp_BankID&"
        sortedString += "$pp_BillReference&"
        sortedString += "$pp_Description&"
        sortedString += "$pp_Language&"
        sortedString += "$pp_MerchantID&"
        sortedString += "$pp_Password&"
        sortedString += "$pp_ProductID&"
        sortedString += "$pp_ReturnURL&"
        //sortedString += pp_SubMerchantID + "&";
        //sortedString += pp_SubMerchantID + "&";
        sortedString += "$pp_TxnCurrency&"
        sortedString += "$DateString&"
        sortedString += "$expiryDateString&"
        //sortedString += pp_TxnType + "&";
        //sortedString += pp_TxnType + "&";
        sortedString += "$TransactionIdString&"
        sortedString += "$pp_Version&"
        sortedString += "$pp_mpf_1&"
        sortedString += "$pp_mpf_2&"
        sortedString += "$pp_mpf_3&"
        sortedString += "$pp_mpf_4&"
        sortedString += pp_mpf_5

        pp_SecureHash = php_hash_hmac(sortedString, IntegritySalt)!!
        println("AhmadLogs: sortedString : $sortedString")
        println("AhmadLogs: pp_SecureHash : $pp_SecureHash")

        try {
            postData += (URLEncoder.encode("pp_Version", "UTF-8")
                    + "=" + URLEncoder.encode(pp_Version, "UTF-8") + "&")
            postData += (URLEncoder.encode("pp_TxnType", "UTF-8")
                    + "=" + pp_TxnType + "&")
            postData += (URLEncoder.encode("pp_Language", "UTF-8")
                    + "=" + URLEncoder.encode(pp_Language, "UTF-8") + "&")
            postData += (URLEncoder.encode("pp_MerchantID", "UTF-8")
                    + "=" + URLEncoder.encode(pp_MerchantID, "UTF-8") + "&")
            postData += (URLEncoder.encode("pp_SubMerchantID", "UTF-8")
                    + "=" + pp_SubMerchantID + "&")
            postData += (URLEncoder.encode("pp_Password", "UTF-8")
                    + "=" + URLEncoder.encode(pp_Password, "UTF-8") + "&")
            postData += (URLEncoder.encode("pp_BankID", "UTF-8")
                    + "=" + URLEncoder.encode(pp_BankID, "UTF-8") + "&")
            postData += (URLEncoder.encode("pp_ProductID", "UTF-8")
                    + "=" + URLEncoder.encode(pp_ProductID, "UTF-8") + "&")
            postData += (URLEncoder.encode("pp_TxnRefNo", "UTF-8")
                    + "=" + URLEncoder.encode(TransactionIdString, "UTF-8") + "&")
            postData += (URLEncoder.encode("pp_Amount", "UTF-8")
                    + "=" + URLEncoder.encode(price, "UTF-8") + "&")
            postData += (URLEncoder.encode("pp_TxnCurrency", "UTF-8")
                    + "=" + URLEncoder.encode(pp_TxnCurrency, "UTF-8") + "&")
            postData += (URLEncoder.encode("pp_TxnDateTime", "UTF-8")
                    + "=" + URLEncoder.encode(DateString, "UTF-8") + "&")
            postData += (URLEncoder.encode("pp_BillReference", "UTF-8")
                    + "=" + URLEncoder.encode(pp_BillReference, "UTF-8") + "&")
            postData += (URLEncoder.encode("pp_Description", "UTF-8")
                    + "=" + URLEncoder.encode(pp_Description, "UTF-8") + "&")
            postData += (URLEncoder.encode("pp_TxnExpiryDateTime", "UTF-8")
                    + "=" + URLEncoder.encode(expiryDateString, "UTF-8") + "&")
            postData += (URLEncoder.encode("pp_ReturnURL", "UTF-8")
                    + "=" + URLEncoder.encode(pp_ReturnURL, "UTF-8") + "&")
            postData += (URLEncoder.encode("pp_SecureHash", "UTF-8")
                    + "=" + pp_SecureHash + "&")
            postData += (URLEncoder.encode("ppmpf_1", "UTF-8")
                    + "=" + URLEncoder.encode(pp_mpf_1, "UTF-8") + "&")
            postData += (URLEncoder.encode("ppmpf_2", "UTF-8")
                    + "=" + URLEncoder.encode(pp_mpf_2, "UTF-8") + "&")
            postData += (URLEncoder.encode("ppmpf_3", "UTF-8")
                    + "=" + URLEncoder.encode(pp_mpf_3, "UTF-8") + "&")
            postData += (URLEncoder.encode("ppmpf_4", "UTF-8")
                    + "=" + URLEncoder.encode(pp_mpf_4, "UTF-8") + "&")
            postData += (URLEncoder.encode("ppmpf_5", "UTF-8")
                    + "=" + URLEncoder.encode(pp_mpf_5, "UTF-8"))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        System.out.println("AhmadLogs: postData : $postData")

        mWebView.postUrl(
            "https://sandbox.jazzcash.com.pk/CustomerPortal/transactionmanagement/merchantform/",
            postData.toByteArray()
        )
    }

    private class MyWebViewClient (
        private var context: Context,
        private var paymentReturnUrl: String = "https://www.youtube.com/"
    ) : WebViewClient() {
        private val jsCode = "" + "function parseForm(form){" +
                "var values='';" +
                "for(var i=0 ; i< form.elements.length; i++){" +
                "   values+=form.elements[i].name+'='+form.elements[i].value+'&'" +
                "}" +
                "var url=form.action;" +
                "console.log('parse form fired');" +
                "window.FORMOUT.processFormData(url,values);" +
                "   }" +
                "for(var i=0 ; i< document.forms.length ; i++){" +
                "   parseForm(document.forms[i]);" +
                "};"

        //private static final String DEBUG_TAG = "CustomWebClient";
        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            if (url == paymentReturnUrl) {
                println("AhmadLogs: return url cancelling")
                view.stopLoading()
                return
            }
            super.onPageStarted(view, url, favicon)
        }

        override fun onPageFinished(view: WebView, url: String) {
            //Log.d(DEBUG_TAG, "Url: "+url);
            if (url == paymentReturnUrl) {
                return
            }
            view.loadUrl("javascript:(function() { $jsCode})()")
            super.onPageFinished(view, url)
        }
    }

    private class FormDataInterface (
        private var context: Context,
        private val paymentReturnUrl: String = "https://www.youtube.com/"
    ) {
        @JavascriptInterface
        fun processFormData(url: String, formData: String) {
            val i = Intent("com.example.firebasepractise.myCustom")
            println("AhmadLogs: Url:$url form data $formData")
            if (url == paymentReturnUrl) {
                val values = formData.split("&".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                for (pair in values) {
                    val nameValue = pair.split("=".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                    if (nameValue.size == 2) {
                        println("AhmadLogs: Name:" + nameValue[0] + " value:" + nameValue[1])
                        i.putExtra(nameValue[0], nameValue[1])
                    }
                }

                context.toast("JazzCashPayment: payment succeeded!!!")
                context.log("JazzCashPayment: payment succeeded!!!")
//                sending success broadcast
                context.sendBroadcast(i)
                return
            }
        }
    }

    private fun php_hash_hmac(data: String, secret: String): String? {
        var returnString: String? = ""
        try {
            val sha256_HMAC: Mac = Mac.getInstance("HmacSHA256")
            val secret_key = SecretKeySpec(secret.toByteArray(), "HmacSHA256")
            sha256_HMAC.init(secret_key)
            val res: ByteArray = sha256_HMAC.doFinal(data.toByteArray())
            returnString = bytesToHex(res)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return returnString
    }

    private fun bytesToHex(bytes: ByteArray): String? {
        val hexArray = "0123456789abcdef".toCharArray()
        val hexChars = CharArray(bytes.size * 2)
        var j = 0
        var v: Int
        while (j < bytes.size) {
            v = bytes[j].toInt() and 0xFF
            hexChars[j * 2] = hexArray[v ushr 4]
            hexChars[j * 2 + 1] = hexArray[v and 0x0F]
            j++
        }
        return String(hexChars)
    }

}