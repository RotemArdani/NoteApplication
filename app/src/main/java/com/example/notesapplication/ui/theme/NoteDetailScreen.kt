package com.example.notesapplication.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notesapplication.data.Note
import androidx.compose.material3.ButtonDefaults

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NoteDetailScreen(
    note: Note,
    onEditNote: (Note) -> Unit,
    onDeleteNote: (Note) -> Unit,
    onBack: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3E5F5))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = note.title,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF311B92),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = note.content,
                fontSize = 18.sp,
                color = Color(0xFF5E35B1),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // bottoms for edit, delete, and back
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Button(
                    onClick = { onEditNote(note) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7E57C2), contentColor = Color.White)
                ) {
                    Text("Edit")
                }

                Button(
                    onClick = { showDeleteDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAB47BC), contentColor = Color.White)
                ) {
                    Text("Delete")
                }

                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9575CD), contentColor = Color.White)
                ) {
                    Text("Back")
                }
            }
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Confirm Delete", color = Color(0xFF6200EE)) },
                text = { Text("Are you sure you want to delete this note?", color = Color(0xFF5E35B1)) },
                confirmButton = {
                    Button(
                        onClick = {
                            onDeleteNote(note)
                            showDeleteDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F), contentColor = Color.White)
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showDeleteDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray, contentColor = Color.White)
                    ) {
                        Text("Cancel")
                    }
                },
                containerColor = Color(0xFFF3E5F5)
            )
        }
    }
}





