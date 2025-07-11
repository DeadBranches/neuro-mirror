package com.omnivoiceai.neuromirror.ui.screens.settings.components

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.omnivoiceai.neuromirror.R
import com.omnivoiceai.neuromirror.domain.model.Operation
import com.omnivoiceai.neuromirror.utils.Logger
import com.omnivoiceai.neuromirror.worker.ExportImportWorker


@Composable
fun BackupButtons(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val filePickerSaver = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json"),
        onResult = { uri ->
            Logger.d("Uri: $uri")
            uri?.let {
                Logger.d("Uri: $uri")
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

    Column() {

        Text(
            stringResource(R.string.data_operation) ,
            modifier = Modifier.padding(top = 16.dp ),
            style = MaterialTheme.typography.headlineSmall,
        )

        SettingLineItem(title = stringResource(R.string.export), onClick = {
            filePickerSaver.launch("export.json")
        })

        SettingLineItem(title = stringResource(R.string.import_data), onClick = {
            filePickerLauncher.launch(arrayOf("application/json"))
        })
    }
}
