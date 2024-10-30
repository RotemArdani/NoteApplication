package com.example.notesapplication.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun AddNoteButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = Color(0xFF9575CD)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Note",
            tint = Color.White
        )
    }
}
