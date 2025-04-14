package com.example.quizapp.fragments
import android.os.Bundle
import android.provider.ContactsContract.RawContacts.Data
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.R
import com.example.quizapp.adapters.QuizAdapter
import com.example.quizapp.databinding.FragmentNeMainBinding
import com.example.quizapp.models.Quiz
import com.google.firebase.firestore.FirebaseFirestore


class NeMainFragment : Fragment() {
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var adapter: QuizAdapter
    private var quizList = mutableListOf<Quiz>()
    private lateinit var quizRecyclerView: RecyclerView
    lateinit var firestore: FirebaseFirestore
    //private var _binding: FragmentNeMainBinding? = null
    //private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        populateDummyData()

        fun setUpDrawerLayout() {
            if(activity is AppCompatActivity){
                (activity as AppCompatActivity).setSupportActionBar(view?.findViewById(R.id.appBar))
            }
            actionBarDrawerToggle = ActionBarDrawerToggle(requireActivity(),(view?.findViewById(R.id.mainDrawer)),
                R.string.app_name,
                R.string.app_name
            )
            actionBarDrawerToggle.syncState()
        }

        fun setUpRecyclerView() {
            adapter = QuizAdapter(requireContext(), quizList)
            quizRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
            quizRecyclerView.adapter = adapter
        }

        fun setUpFireStore() {
            firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("quizzes")
            collectionReference.addSnapshotListener { value, error ->
                if((value == null) || (error != null)) {
                    Toast.makeText(requireContext(), "Ошибка извлечения данных", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }
                Log.d("DATA", value.toObjects(Quiz::class.java).toString())
            }
        }

        fun setUpViews() {
            setUpDrawerLayout()
            setUpRecyclerView()
            setUpFireStore()
        }

        fun onOptionsItemSelected(item: MenuItem): Boolean {
            if(actionBarDrawerToggle.onOptionsItemSelected(item)) {
                return true
            }
            return super.onOptionsItemSelected(item)
        }
        return inflater.inflate(R.layout.fragment_ne_main, container, false)
    }

    private fun populateDummyData() {
        quizList.add(Quiz("bell", "bell"))
        quizList.add(Quiz("calculator", "calculator"))
        quizList.add(Quiz("building", "building"))
        quizList.add(Quiz("pencil", "pencil"))
        quizList.add(Quiz("ruler", "ruler"))
        quizList.add(Quiz("bag", "bag"))
    }

}