package com.example.notesapplication.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.notesapplication.data.Note
import com.example.notesapplication.NoteViewModel
import com.example.notesapplication.ui.theme.NotesApplicationTheme
import com.example.notesapplication.ui.theme.NoteDetailScreen
import kotlinx.coroutines.launch

class NoteDetailActivity : ComponentActivity() {
    private val noteViewModel: NoteViewModel by viewModels()

    private val editNoteResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val noteId = intent.getIntExtra("noteId", -1)
        val noteTitle = intent.getStringExtra("noteTitle") ?: ""
        val noteContent = intent.getStringExtra("noteContent") ?: ""

        val note = Note(noteTitle, noteContent).apply {
            setId(noteId)
        }

        setContent {
            NotesApplicationTheme {
                NoteDetailScreen(
                    note = note,
                    onEditNote = { editedNote -> editNote(editedNote) },
                    onDeleteNote = { deleteNote(note) },
                    onBack = { finish() }
                )
            }
        }
    }

    // updates note in local database and server
    private fun editNote(note: Note) {
        val intent = Intent(this, EditNoteActivity::class.java).apply {
            putExtra("noteId", note.id)
            putExtra("noteTitle", note.title)
            putExtra("noteContent", note.content)
        }
        editNoteResult.launch(intent)
    }

    // deletes note from local database and server
    private fun deleteNote(note: Note) {
        lifecycleScope.launch {
            try {
                noteViewModel.deleteNote(note.getId())
                setResult(Activity.RESULT_OK)
                finish()
            } catch (e: Exception) {
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        }
    }
}



