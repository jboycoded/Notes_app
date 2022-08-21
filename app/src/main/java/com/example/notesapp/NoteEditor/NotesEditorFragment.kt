package com.example.notesapp.NoteEditor

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.notesapp.NoteList.NoteListFragment
import com.example.notesapp.NoteList.NoteListFragmentDirections
import com.example.notesapp.R
import com.example.notesapp.databinding.FragmentNotesEditorBinding
import kotlin.properties.Delegates

class NotesEditorFragment : Fragment() {
    private lateinit var editText: EditText
    var noteId by Delegates.notNull<Int>()

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

        editText.addTextChangedListener {
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    NoteListFragment.notes[noteId] = charSequence.toString()
                    Log.i(
                        "Hello once more",
                        charSequence.toString() + "hello" + NoteListFragment.notes
                    )
                    NoteListFragment.arrayAdapter.notifyDataSetChanged()

                    //Create SharedPreferences to store the notes
                    val prefs: SharedPreferences? = activity?.applicationContext
                        ?.getSharedPreferences("com.example.notesapp", Context.MODE_PRIVATE)

                    val set: HashSet<String> = HashSet(NoteListFragment.notes)
                    prefs?.edit()?.putStringSet("notes", set)?.apply()
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            }
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    fun save() {
        NoteListFragment.notes[noteId] = editText.text.toString()
        Log.i(
            "Hello once more",
            editText.text.toString() + "hello" + NoteListFragment.notes
        )
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