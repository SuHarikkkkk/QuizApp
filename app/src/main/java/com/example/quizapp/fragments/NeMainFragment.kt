package com.example.quizapp.fragments


import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.R
import com.example.quizapp.R.*
import com.example.quizapp.adapters.QuizAdapter
import com.example.quizapp.databinding.FragmentLoginBinding
import com.example.quizapp.databinding.FragmentNeMainBinding
import com.example.quizapp.databinding.FragmentProfileBinding
import com.example.quizapp.models.Quiz
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date
import java.util.Locale

class NeMainFragment : Fragment() {
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var binding: FragmentNeMainBinding
    lateinit var adapter: QuizAdapter
    private var quizList = mutableListOf<Quiz>()
    private lateinit var quizRecyclerView: RecyclerView
    lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNeMainBinding.inflate(layoutInflater)

        populateDummyData()

        val rootView = inflater.inflate(layout.fragment_ne_main, container, false)

        // Инициализация DrawerLayout, NavigationView и Toolbar
        val appBar = rootView.findViewById<MaterialToolbar>(R.id.appBar)
        val mainDrawer = rootView.findViewById<DrawerLayout>(R.id.mainDrawer)
        val navigationView = rootView.findViewById<NavigationView>(R.id.navigationView)

        // Инициализация Toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(appBar)

        // Включение кнопки гамбургер меню
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }

        // Создание ActionBarDrawerToggle
        actionBarDrawerToggle = ActionBarDrawerToggle(
            requireActivity(),
            mainDrawer,
            appBar,
            string.open_drawer,
            string.close_drawer
        ).apply {
            syncState()  // Синхронизация состояния иконки
            mainDrawer.addDrawerListener(this)  // Добавление слушателя
        }

        // Обработка кликов по элементам меню
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.btnProfile -> {
                    findNavController().navigate(R.id.action_neMainFragment_to_profileFragment)
                }
            }
            mainDrawer.closeDrawers()
            true
        }

        // Инициализация RecyclerView и Firestore
        quizRecyclerView = rootView.findViewById(R.id.quizRecycleView)
        setUpRecyclerView()
        setUpFireStore()

        return rootView
    }

    private fun setUpRecyclerView() {
        adapter = QuizAdapter(requireContext(), quizList)
        quizRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        quizRecyclerView.adapter = adapter
    }

    fun setUpViews() {
        setUpFireStore()
        setUpRecyclerView()
        setUpDatePicker()
    }

    private fun setUpDatePicker() {
        view?.findViewById<Button>(R.id.btnDatePicker)?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(requireActivity().supportFragmentManager, "DatePicker")
            datePicker.addOnPositiveButtonClickListener { selectedDate ->
                Log.d("DATEPICKER", datePicker.headerText)
                val dateFormatter = SimpleDateFormat("dd-mm-yyyy", Locale.getDefault())
                val date = dateFormatter.format(Date(selectedDate))
                findNavController().navigate(R.id.action_neMainFragment_to_questionFragment)
            }
            datePicker.addOnNegativeButtonClickListener {
                Log.d("DATEPICKER", "Date selection cancelled")
            }
            datePicker.addOnCancelListener {
                Log.d("DATEPICKER", "Date Picker Cancelled")
            }
        }
    }

    private fun setUpFireStore() {
        firestore = FirebaseFirestore.getInstance()
        val collectionReference = firestore.collection("quizzes")
        collectionReference.addSnapshotListener { value, error ->
            if ((value == null) || (error != null)) {
                Toast.makeText(requireContext(), "Ошибка извлечения данных", Toast.LENGTH_SHORT)
                    .show()
                return@addSnapshotListener
            }
            quizList.clear()
            quizList.addAll(value.toObjects(Quiz::class.java))
            adapter.notifyDataSetChanged()
        }
    }



    // Обработка нажатий на элементы меню
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun populateDummyData() {
        quizList.add(Quiz(drawable.bell_vector, "bell"))
        quizList.add(Quiz(drawable.calculator_vector, "calculator"))
        quizList.add(Quiz(drawable.ruler_vector, "building"))
        quizList.add(Quiz(drawable.school_bag_vector, "pencil"))
        quizList.add(Quiz(drawable.line_building_vector, "ruler"))
        quizList.add(Quiz(drawable.university_book_vector, "bag"))
    }
}