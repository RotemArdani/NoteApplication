package com.example.notesapplication.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.notesapplication.NoteViewModel
import com.example.notesapplication.ui.theme.NoteListScreen
import com.example.notesapplication.ui.theme.NotesApplicationTheme
import com.example.notesapplication.data.Note
import com.example.notesapplication.ui.theme.AddNoteButton

class MainActivity : ComponentActivity() {
    private val noteViewModel: NoteViewModel by viewModels()

    // refresh notes after returning from AddNoteActivity or NoteDetailActivity
    private val noteActivityResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            noteViewModel.refreshNotes()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotesApplicationTheme {
                val lifecycleOwner = LocalLifecycleOwner.current

                DisposableEffect(lifecycleOwner) {
                    val observer = LifecycleEventObserver { _, event ->
                        if (event == Lifecycle.Event.ON_RESUME) {
                            noteViewModel.refreshNotes()
                        }
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)
                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(observer)
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = {
                        AddNoteButton {
                            openAddNoteScreen()
                        }
                    },
                    content = { innerPadding ->
                        val notes by noteViewModel.getAllNotes().observeAsState(emptyList())

                        NoteListScreen(
                            notes = notes,
                            modifier = Modifier.padding(innerPadding),
                            onClickNote = { note -> openNoteDetail(note) }
                        )
                    }
                )
            }
        }
    }

    private fun openNoteDetail(note: Note) {
        val intent = Intent(this, NoteDetailActivity::class.java).apply {
            putExtra("noteId", note.id)
            putExtra("noteTitle", note.title)
            putExtra("noteContent", note.content)
        }
        noteActivityResult.launch(intent)
    }

    private fun openAddNoteScreen() {
        val intent = Intent(this, AddNoteActivity::class.java)
        noteActivityResult.launch(intent)
    }
}