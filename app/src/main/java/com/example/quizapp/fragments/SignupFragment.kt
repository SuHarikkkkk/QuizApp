package com.example.quizapp.fragments

import android.os.Bundle
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
import com.example.quizapp.databinding.FragmentSignupBinding
import com.google.firebase.auth.FirebaseAuth


class SignupFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnSignUp.setOnClickListener {
            it.findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }
        binding.btnSignUp2.setOnClickListener {
            val email = binding.etEmailAddress.text.toString()
            val pass = binding.etPassword.text.toString()
            val confirmPass = binding.etConfirmPassword.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass.equals(confirmPass)) {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener { result ->
                            if (result.isSuccessful) {
                                requireParentFragment().findNavController()
                                    .navigate(R.id.action_signupFragment_to_loginFragment)

                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    result.exception.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                } else {
                    Toast.makeText(requireContext(), "Пароль не совпадает", Toast.LENGTH_SHORT)
                        .show()
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
        return binding.root

    }
}