package com.example.travelday_2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.travelday_2.databinding.FragmentCommunityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class CommunityLoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    lateinit var binding: FragmentCommunityLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentCommunityLoginBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()
    }

    private fun initLayout() {
        // Initialize Firebase Auth
        auth = Firebase.auth

        // Setup click listener for the login button
        binding.loginButton.setOnClickListener {
            val email = binding.emailField.text.toString()
            val password = binding.passwordField.text.toString()

            // Sign in with email and password
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign in success
                        val user = auth.currentUser
                        Log.i("유저이름",user.toString())
                        Toast.makeText(context,"로그인이 완료되었습니다",Toast.LENGTH_SHORT).show()
                        parentFragmentManager.beginTransaction().apply {
                            val communityMainFragment=CommunityMainFragment()
                            add(R.id.frag_container_community, communityMainFragment)
                            hide(this@CommunityLoginFragment)
                            addToBackStack(null)
                            commit()
                        }

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(context,"유효하지 않는 계정입니다",Toast.LENGTH_SHORT).show()
                    }
                }
        }

        binding.registerButton.setOnClickListener {
            val email = binding.emailField.text.toString()
            val password = binding.passwordField.text.toString()

            // Create user with email and password
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign up success
                        val user = auth.currentUser
                        Toast.makeText(context,"회원가입이 완료되었습니다",Toast.LENGTH_SHORT).show()
                    } else {
                        // If sign up fails, display a message to the user.
                        Toast.makeText(context,"회원가입에 실패하였습니다",Toast.LENGTH_SHORT).show()                    }
                }
        }
    }



}