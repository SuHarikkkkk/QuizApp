package com.example.quizapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        firebaseAuth = FirebaseAuth.getInstance()

        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            findNavController().navigate(R.id.action_loginFragment_to_neMainFragment)
        }

        binding.btnSignUp.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginFragment_to_signupFragment)

        }
        binding.btnSignUp2.setOnClickListener {
            val email = binding.etEmailAddress.text.toString()
            val pass = binding.etPassword.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email.trim(), pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        requireParentFragment().findNavController()
                            .navigate(R.id.action_loginFragment_to_neMainFragment)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            it.exception.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("afsfassaf", it.exception.toString())
                    }
                }


            } else {
                Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show()
            }
        }


        view?.let {
            ViewCompat.setOnApplyWindowInsetsListener(it.findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        return binding.root
    }
}
