package com.tasyamalia.jetnote.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.tasyamalia.jetnote.R
import com.tasyamalia.jetnote.components.NoteButton
import com.tasyamalia.jetnote.components.NoteInputText
import com.tasyamalia.jetnote.data.NoteDataSource
import com.tasyamalia.jetnote.model.Note
import com.tasyamalia.jetnote.utils.formatDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    notes: List<Note>,
    onAddNote: (Note) -> Unit,
    onRemoveNote: (Note) -> Unit
) {
    var title by remember {
        mutableStateOf("")
    }
    var description by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                actions = {
                    Icon(imageVector = Icons.Rounded.Notifications, contentDescription = "Icon")
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFDADFE3)),
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(
                top = innerPadding.calculateTopPadding(),
                start = innerPadding.calculateLeftPadding(layoutDirection = LayoutDirection.Ltr) + 12.dp,
                end = innerPadding.calculateLeftPadding(layoutDirection = LayoutDirection.Rtl) + 12.dp
            )
        ) {
            //Content
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                NoteInputText(
                    text = title,
                    label = "Title",
                    modifier = Modifier.padding(top = 8.dp, bottom = 9.dp),
                    onTextChange = {
                        if (it.all { char ->
                                char.isLetter() || char.isWhitespace()
                            }) title = it
                    },
                )
                NoteInputText(
                    text = description,
                    label = "Add a note",
                    modifier = Modifier.padding(top = 8.dp, bottom = 9.dp),
                    onTextChange = {
                        if (it.all { char ->
                                char.isLetter() || char.isWhitespace()
                            }) description = it
                    },
                )
                NoteButton(
                    modifier = Modifier,
                    text = "Save",
                    onClick = {
                        if (title.isNotEmpty() && description.isNotEmpty()) {
                            onAddNote(Note(title = title, description = description))
                            title = ""
                            description = ""
                            Toast.makeText(context, "NoteAdd", Toast.LENGTH_SHORT).show()
                        }
                    },
                )
            }
            HorizontalDivider(modifier = Modifier.padding(10.dp))
            LazyColumn {
                items(notes) { note ->
                    NoteRow(
                        modifier = Modifier,
                        note = note,
                        onNoteClicked = {
                            onRemoveNote(note)
                        })
                }
            }

        }
    }
}

@Composable
fun NoteRow(
    modifier: Modifier,
    note: Note,
    onNoteClicked: (Note) -> Unit
) {
    Surface(
        modifier = modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(topEnd = 33.dp, bottomStart = 33.dp))
            .fillMaxWidth(),
        color = Color(0xFFDFE6EB),
        shadowElevation = 6.dp
    ) {
        Column(modifier = modifier
            .clickable {
                onNoteClicked(note)
            }
            .padding(horizontal = 14.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.Start) {
            Text(text = note.title, style = MaterialTheme.typography.labelLarge)
            Text(text = note.description, style = MaterialTheme.typography.labelMedium)
            Text(
                text = formatDate(note.entryDate.time),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}


@Preview
@Composable
fun NoteScreenPreview() {
    NoteScreen(
        notes = NoteDataSource().loadNotes(),
        onAddNote = {},
        onRemoveNote = {}
    )
}