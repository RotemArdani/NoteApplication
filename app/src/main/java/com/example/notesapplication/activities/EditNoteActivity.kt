package com.example.notesapplication.activities

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.notesapplication.data.Note
import com.example.notesapplication.ui.theme.NotesApplicationTheme
import com.example.notesapplication.ui.theme.EditNoteScreen
import com.example.notesapplication.NoteViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditNoteActivity : ComponentActivity() {
    private val noteViewModel: NoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val noteId = intent.getIntExtra("noteId", -1)
        val noteTitle = intent.getStringExtra("noteTitle") ?: ""
        val noteContent = intent.getStringExtra("noteContent") ?: ""

        // create a Note object with the same ID
        val note = Note(noteTitle, noteContent).apply {
            setId(noteId)
        }

        setContent {
            NotesApplicationTheme {
                EditNoteScreen(
                    note = note,
                    onSaveNote = { updatedNote ->
                        lifecycleScope.launch {
                            withContext(Dispatchers.IO) {
                                noteViewModel.updateNote(updatedNote)
                            }
                            // returns RESULT to the previous activity
                            setResult(Activity.RESULT_OK)
                            finish()
                        }
                    }
                )
            }
        }
    }
}

