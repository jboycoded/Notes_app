package com.example.notesapp.NoteEditor

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.notesapp.NoteList.NoteListFragment
import com.example.notesapp.R
import com.example.notesapp.databinding.FragmentNotesEditorBinding
import kotlin.properties.Delegates

class NotesEditorFragment : Fragment() {
    private lateinit var editText: EditText
    private var noteId by Delegates.notNull<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentNotesEditorBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_notes_editor, container, false
        )

        editText = binding.editText
        // Retrieve argument passed from NoteListFragment
        val args = NotesEditorFragmentArgs.fromBundle(requireArguments())
        noteId = args.noteId
        if (noteId != -1) {
            editText.setText(NoteListFragment.notes[noteId])
        } else {
            NoteListFragment.notes.add("")
            noteId = NoteListFragment.notes.size - 1
            NoteListFragment.arrayAdapter.notifyDataSetChanged()
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun save() {
        NoteListFragment.notes[noteId] = editText.text.toString()
        NoteListFragment.arrayAdapter.notifyDataSetChanged()

        //Create SharedPreferences to store the notes
        val prefs: SharedPreferences? = activity?.applicationContext
            ?.getSharedPreferences("com.example.notesapp", Context.MODE_PRIVATE)

        val set: HashSet<String> = HashSet(NoteListFragment.notes)
        prefs?.edit()?.putStringSet("notes", set)?.apply()
        Toast.makeText(context, "Saved successfully", Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        save()
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.editor_menu, menu)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        save()
        activity?.onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}