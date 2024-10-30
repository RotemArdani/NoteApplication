package com.example.notesapplication.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notesapplication.data.Note

@Composable
fun NoteListScreen(
    modifier: Modifier = Modifier,
    notes: List<Note>,
    onClickNote: (Note) -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }

    // if there is no query - shows the list of all notes. otherwise, shows the list of notes that match the query
    val notesToShow = if (searchQuery.isEmpty()) {
        notes
    } else {
        notes.filter { note ->
            note.title.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFEDE7F6))
    ) {
        Text(
            text = "MY NOTES",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF311B92),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            placeholder = { Text("Search notes...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color(0xFF311B92)
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF311B92),
                unfocusedBorderColor = Color(0xFF311B92).copy(alpha = 0.7f)
            ),
            singleLine = true
        )

        if (searchQuery.isNotEmpty() && notesToShow.isEmpty()) {
            // shows message if there are no notes that match the query
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No notes found for '$searchQuery'",
                    color = Color(0xFF311B92),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        } else {
            // shows the list of notes that match the query
            LazyColumn {
                items(notesToShow) { note ->
                    NoteItem(note = note, onClickNote = onClickNote)
                    HorizontalDivider(color = Color.Black.copy(alpha = 0.1f))
                }
            }
        }
    }
}

@Composable
fun NoteItem(note: Note, onClickNote: (Note) -> Unit) {
    Row(
        modifier = Modifier
            .clickable { onClickNote(note) }
            .fillMaxWidth()
            .padding(12.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Text(
            text = note.title,
            color = Color.Black,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            modifier = Modifier.padding(8.dp)
        )
    }
}



