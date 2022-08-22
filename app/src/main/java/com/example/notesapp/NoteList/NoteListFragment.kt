package com.example.notesapp.NoteList

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.notesapp.R
import com.example.notesapp.databinding.FragmentNoteListBinding
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class NoteListFragment : Fragment() {

    companion object {
        var notes = ArrayList<String>()
        lateinit var arrayAdapter: ArrayAdapter<String>
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentNoteListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_note_list, container, false
        )
        val listView: ListView = binding.listView

        //Create SharedPreferences to store the notes
        val prefs: SharedPreferences? = activity?.applicationContext
            ?.getSharedPreferences("com.example.notesapp", Context.MODE_PRIVATE)

        val empty = HashSet<String>(listOf("empty"))
        var set: HashSet<String> = prefs?.getStringSet("notes", empty) as HashSet<String>

        /*  If there is a "key" named "notes", it will not be null, therefore the else statement
         *  will execute, creating a new ArrayList with values from the set variable
         *  If it is the opposite, that is, it is null, add a new entry to notes variable
         *  In this case, it will be the only entry to the variable
         */
        if (set == empty) {
            notes.add("Example note")
        } else {
            notes = ArrayList(set)
        }

        arrayAdapter =
            context?.let { ArrayAdapter(it, R.layout.adapter_layout, R.id.text1, notes) }!!
        listView.adapter = arrayAdapter

        listView.setOnItemClickListener { _, _, position, _ ->
            //Using FragmentDirections class you can pass arguments into the NotesEditorFragment
            //Default arguments already stated in the navigation layout file
            findNavController().navigate(
                NoteListFragmentDirections.actionNoteListFragmentToNotesEditorFragment(
                    position
                )
            )
        }

        listView.setOnItemLongClickListener { _, _, position, _ ->

            AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are you sure")
                .setMessage("Do you want")
                .setPositiveButton("Yes") { _, _ ->
                    notes.removeAt(position)
                    arrayAdapter.notifyDataSetChanged()
                    set = HashSet(notes)
                    prefs.edit().putStringSet("notes", set).apply()
                }
                .setNegativeButton("No", null).show()

            return@setOnItemLongClickListener true
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_note -> {
                findNavController().navigate(
                    NoteListFragmentDirections.actionNoteListFragmentToNotesEditorFragment(
                        -1
                    )
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }
}