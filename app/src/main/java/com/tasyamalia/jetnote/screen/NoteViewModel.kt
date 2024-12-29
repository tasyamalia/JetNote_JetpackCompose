package com.tasyamalia.jetnote.screen

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tasyamalia.jetnote.data.NoteDataSource
import com.tasyamalia.jetnote.model.Note
import com.tasyamalia.jetnote.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteRepository: NoteRepository) : ViewModel() {
    private var _noteList = MutableStateFlow<List<Note>>(emptyList())
    val noteList = _noteList.asStateFlow()

     fun addNote(note: Note) = viewModelScope.launch { noteRepository.addNote(note) }
     fun removeNote(note: Note) = viewModelScope.launch { noteRepository.deleteNote(note) }
     fun updateNote(note: Note) = viewModelScope.launch { noteRepository.updateNote(note) }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.getAllNotes().distinctUntilChanged()
                .collect { listOfNotes ->
                    _noteList.value = listOfNotes
                }
        }
    }


}