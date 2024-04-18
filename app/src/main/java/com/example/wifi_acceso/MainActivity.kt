package com.example.wifi_acceso

import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.getSystemService

class MainActivity : ComponentActivity() {

    private var wifiList :ListView? = null
    private var wifiManager :WifiManager? = null
    private  val MY_PERMISSIONS_COARSE_LOCATION = 1

    var receiverWifi: WifiReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            wifiList = findViewById(R.id.wifiList)
            val btnScan = findViewById<Button>(R.id.scanBtn)

            wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            if (!wifiManager!!.isWifiEnabled){
                Toast.makeText(this, "Wifi On ...", Toast.LENGTH_LONG).show()
                wifiManager!!.setWifiEnabled(true)
            }
            if(ActivityCompat.checkSelfPermission((this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
            ){
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    MY_PERMISSIONS_COARSE_LOCATION
                )
            }
            btnScan.setOnClickListener{
                wifiManager!!.startScan()
            }

            app()
        }
    }

    override fun onPostResume() {
        super.onPostResume()
        receiverWifi = WifiReceiver(wifiManager!!,wifiList!!)
        val intentFilter = IntentFilter()
        intentFilter.addAction(
            WifiManager.SCAN_RESULTS_AVAILABLE_ACTION
        )
        registerReceiver(receiverWifi, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiverWifi)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == MY_PERMISSIONS_COARSE_LOCATION
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ){
            Toast.makeText(this, "Permiso concedido", Toast.LENGTH_LONG).show()
        }
        else{
            Toast.makeText(this, "Permiso denegado", Toast.LENGTH_LONG).show()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun app() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue)
            .padding(25.dp)
    ) {
        item {
            Row(horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)){
                Text(
                    text = "Wi-Fi",
                    fontSize = 32.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Image(
                    modifier = Modifier
                        .height(50.dp),
                    painter = painterResource(id = R.drawable.wifi),
                    contentDescription = "logo wifi"
                )

                /*
                */
            }
            Text(text = "Redes disponibles", color = Color.White)
            Row(modifier = Modifier.fillMaxWidth()){
                Button(onClick = { /*TODO*/ }) {
                    
                }
            }
        }
    }
}



