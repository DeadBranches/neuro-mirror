package com.omnivoiceai.neuromirror.ui.screens.home

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.omnivoiceai.neuromirror.R
import com.omnivoiceai.neuromirror.ui.components.layout.AppBar
import com.omnivoiceai.neuromirror.ui.theme.NeuroMirrorTheme
import com.omnivoiceai.neuromirror.utils.Logger

private const val TAG = "MainActivity"
class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.d("On Create called")
        Toast.makeText(this, "$TAG onCreate", Toast.LENGTH_LONG).show()
        setContent {
            NeuroMirrorTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(
                    containerColor = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxSize(),
                    topBar = { AppBar() },
                    floatingActionButton = {
                        FloatingActionButton(onClick = ::floatingActionClick ) {
                            Icon(Icons.Filled.Add, "Add item")
                        }
                    }
                ) { innerPadding ->
                    Surface(
                        color = Color.Transparent,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        Greeting("Android")
                    }
                }

            }
        }
    }

    private fun floatingActionClick() {
        Logger.d("Clicked")
    }

    override fun onStart() {
        super.onStart()
        Logger.d("On start called")
        Toast.makeText(this, "$TAG onStart", Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        Logger.d("On resume called")
        Toast.makeText(this, "$TAG onResume", Toast.LENGTH_LONG).show()
    }

    override fun onPause() {
            super.onPause()
            Logger.d("On pause called")
            Toast.makeText(this, "$TAG onPause", Toast.LENGTH_LONG).show()
        }
}



private const val TAG2 = "GreetingComposable"
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Toast.makeText(LocalContext.current, "$TAG onPause", Toast.LENGTH_LONG).show()
    Column {
        Row {
            Text(text = "Hello $name!")
            Text(text = "Hello $name!")
            Text(text = "Hello $name!")
        }
        ScrollableList()
    }

    LifecycleEventEffect(Lifecycle.Event.ON_CREATE) {
        Log.i(
            TAG2,
            "onCreate")
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        Log.i(
            TAG2,
            "onResume")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NeuroMirrorTheme {
        Greeting("Android")
    }
}

@Composable
fun ScrollableList() {
    val items = (0..100).map { "Elem $it" }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(items) {
            MaterialListItem(it)
        }
    }
}

@Composable
fun MaterialListItem(item: String, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 16.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = "Android Logo",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant),
            modifier = Modifier.size(48.dp)
        )
        Spacer(Modifier.size(16.dp))
        Text(
            item,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
