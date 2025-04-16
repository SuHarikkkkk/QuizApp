package com.example.quizapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentProfileBinding
import com.example.quizapp.databinding.FragmentSignupBinding
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {
    lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        val txtEmail = view?.findViewById<TextView>(R.id.txtEmail)
        firebaseAuth = FirebaseAuth.getInstance()
        if (txtEmail != null) {
            txtEmail.text = firebaseAuth.currentUser?.email
        }
        binding.btnLogout.setOnClickListener {
            it.findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }

        return inflater.inflate(R.layout.fragment_profile, container, false)
    }
}