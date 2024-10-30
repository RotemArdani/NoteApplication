package com.example.notesapplication.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.notesapplication.ui.theme.NotesApplicationTheme
import com.example.notesapplication.NoteViewModel
import com.example.notesapplication.ui.theme.AddNoteScreen

class AddNoteActivity : ComponentActivity() {
    private val noteViewModel: NoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotesApplicationTheme {
                AddNoteScreen(
                    onSaveNote = { note ->
                        noteViewModel.addNote(note)
                        finish() // returns to the main page
                    },
                    onBack = { finish() }
                )
            }
        }
    }
}
