package com.omnivoiceai.neuromirror

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
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

    private fun floatingActionClick(){
        Log.i("FloatingAction", "Clicked");
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar() {
    return TopAppBar(
        title = { Text("Title") },
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.Menu, "Menu")
            }
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.ShoppingCart, "Shopping Cart")
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.Favorite, "Favorites")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceDim
        )
    )
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
        Log.i(TAG2,
            "onCreate")
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        Log.i(TAG2,
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

    LazyColumn {
        items(items) {
            Text(it)
        }
    }
}
