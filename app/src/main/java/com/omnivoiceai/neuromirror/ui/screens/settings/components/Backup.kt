package com.omnivoiceai.neuromirror.ui.screens.settings.components

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.omnivoiceai.neuromirror.domain.model.Operation
import com.omnivoiceai.neuromirror.utils.Logger
import com.omnivoiceai.neuromirror.worker.ExportImportWorker


@Composable
fun BackupButtons() {
    val context = LocalContext.current

    val filePickerSaver = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json"),
        onResult = { uri ->
            Logger.d("Uri: ${uri}")
            uri?.let {
                Logger.d("Uri: ${uri}")
                val workRequest = OneTimeWorkRequestBuilder<ExportImportWorker>()
                    .setInputData(workDataOf(
                        "action" to Operation.EXPORT.name,
                        "export_uri" to it.toString()
                    ))
                    .build()

                WorkManager.getInstance(context).enqueue(workRequest)
            }
        }
    )

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                val workRequest = OneTimeWorkRequestBuilder<ExportImportWorker>()
                    .setInputData(
                        workDataOf(
                            "action" to Operation.IMPORT.name,
                            "import_uri" to it.toString()
                        )
                    )
                    .build()

                WorkManager.getInstance(context).enqueue(workRequest)
            }
        }
    )

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(onClick = {
            filePickerSaver.launch("export.json")
        }) {
            Text("Esporta dati in JSON")
        }

        Button(onClick = {
            // IMPORT tramite file picker
            filePickerLauncher.launch(arrayOf("application/json"))
        }) {
            Text("Importa dati da JSON")
        }
    }
}
