package com.omnivoiceai.neuromirror

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.omnivoiceai.neuromirror.ui.theme.NeuroMirrorTheme

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "On Create called")
        Toast.makeText(this, "$TAG onCreate", Toast.LENGTH_LONG).show()
        setContent {
            NeuroMirrorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize()
                ){
                    GoToHomeButton()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "On start called")
        Toast.makeText(this, "$TAG onStart", Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "On resume called")
        Toast.makeText(this, "$TAG onResume", Toast.LENGTH_LONG).show()
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "On pause called")
        Toast.makeText(this, "$TAG onPause", Toast.LENGTH_LONG).show()
    }
}

@Composable
fun GoToHomeButton() {
    val ctx = LocalContext.current

    Button(
        modifier = Modifier.requiredSize(150.dp, 50.dp),
        onClick = {
            val intent = Intent(ctx, HomeActivity::class.java)
            ctx.startActivity(intent)
        }
    ){
        Text("Go to homepage")
    }
}