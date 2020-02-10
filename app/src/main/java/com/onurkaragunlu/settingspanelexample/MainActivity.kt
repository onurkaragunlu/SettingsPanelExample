package com.onurkaragunlu.settingspanelexample

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.provider.Settings
import android.util.Log
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    var settingsPanelRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnInernetConnection.setOnClickListener {
            checkInternetSettings()

        }
    }


   fun checkInternetSettings(){
       if (isNetworkAvailable(this)){
           webView.loadUrl("https://www.mobiler.dev/")
       }else{
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
               val intent = Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY)
               startActivityForResult(intent, settingsPanelRequestCode)
           } else {
               Toast.makeText(this, "To access this feature Internet must be connected", Toast.LENGTH_SHORT).show()
           }
       }
   }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == settingsPanelRequestCode){
            if (isNetworkAvailable(this)){
                webView.loadUrl("https://www.mobiler.dev/")
            }else{
                Toast.makeText(this, "To access this feature Internet must be connected", Toast.LENGTH_SHORT).show()
            }
        }
    }



    fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false


        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (connectivityManager != null) {


            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        return true
                    }
                }
            } else {
                try {
                    val activeNetworkInfo = connectivityManager.activeNetworkInfo
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                        return true
                    }
                } catch (e: Exception) {
                    Log.i("update_statut", "" + e.message)
                }

            }
        }
        return false
    }
}
