package com.lelo.notetaking.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lelo.notetaking.models.Note

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesApp() {
    // state: our in-memory list of notes
    val notes = remember { mutableStateListOf<Note>() }

    // state: dialog controls + input fields
    var dialogOpen by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf("") }
    var newDesc by remember { mutableStateOf("") }
    var selectedNote by remember { mutableStateOf<Note?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("My Notes") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { dialogOpen = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add note")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier.fillMaxSize()
        ) {
            items(notes.size) { note ->
                NoteItem(notes[note], onClick = { selectedNote = notes[note]})
            }
        }
        selectedNote?.let { note ->
            AlertDialog(
                onDismissRequest = { selectedNote = null },
                title = { Text(text = note.title) },
                text = { Text(text = note.description) },
                confirmButton = {
                    TextButton(onClick = { selectedNote = null }) {
                        Text("OK")
                    }
                }
            )
        }

        // 4) The “Add Note” dialog
        if (dialogOpen) {
            AlertDialog(
                onDismissRequest = { dialogOpen = false },
                title = { Text("Add a new note") },
                text = {
                    Column {
                        TextField(
                            value = newTitle,
                            onValueChange = { newTitle = it },
                            label = { Text("Title") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = newDesc,
                            onValueChange = { newDesc = it },
                            label = { Text("Description") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            // only add if both fields are non-empty
                            if (newTitle.isNotBlank() && newDesc.isNotBlank()) {
                                notes += Note(newTitle.trim(), newDesc.trim())
                                newTitle = ""
                                newDesc = ""
                                dialogOpen = false
                            }
                        },
                        enabled = newTitle.isNotBlank() && newDesc.isNotBlank()
                    ) {
                        Text("Add")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { dialogOpen = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}