package com.example.travelday_2

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.travelday_2.databinding.FragmentCommunityWriteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID
import android.Manifest



class CommunityWriteFragment : Fragment() {

    lateinit var binding: FragmentCommunityWriteBinding
    var selectedImageUri: Uri? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommunityWriteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()
        initBackStack()
    }



    fun initLayout(){
        binding.writeBtn.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            val userId = user?.email ?: "Anonymous"
            val title = binding.titleArea.text.toString()
            val content = binding.contentArea.text.toString()
            val time = getTime()
            val key = DBRef.contentRef.push().key ?: ""

            selectedImageUri?.let { uri ->
                val imageRef = FirebaseStorage.getInstance().getReference("images/${UUID.randomUUID()}")
                imageRef.putFile(uri).addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        val post = CommunityPost(userId, title, content, time, imageUrl = downloadUri.toString())
                        DBRef.contentRef.child(key).setValue(post).addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(context, "게시글 입력 완료", Toast.LENGTH_SHORT).show()
                                parentFragmentManager.popBackStack()
                            } else {
                                Toast.makeText(context, "Failed to post", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }.addOnFailureListener {
                        Toast.makeText(context, "Failed to get download url", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(context, "Failed to upload image", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                val post = CommunityPost(userId, title, content, time)
                DBRef.contentRef.child(key).setValue(post).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(context, "게시글 입력 완료", Toast.LENGTH_SHORT).show()
                        parentFragmentManager.popBackStack()
                    } else {
                        Toast.makeText(context, "Failed to post", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        binding.photoButton.setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    openGallery()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    Toast.makeText(context,"갤러리 권한이 거부되었습니다",Toast.LENGTH_SHORT).show()
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
        }

    }



    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                openGallery()
            } else {
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    private fun openGallery() {
        pickImagesResult.launch("image/*")
    }

    private val pickImagesResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            selectedImageUri = uri
            Toast.makeText(context, "Image file chosen", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "No file chosen", Toast.LENGTH_SHORT).show()
        }
    }
    private fun uploadImage(uri: Uri) {
        val storage=FirebaseStorage.getInstance()
        val storageRef=storage.getReference()
        val imageRef = storageRef.child("images/${UUID.randomUUID()}")
        imageRef.putFile(uri).addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                DBRef.contentRef
                    .child("imageUrls")
                    .setValue(downloadUri)
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to upload image", Toast.LENGTH_SHORT).show()
        }
    }


    fun getTime(): String {
        val currentDateTime = Calendar.getInstance().time
        val dateFormat =
            SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA).format(currentDateTime)
        return dateFormat
    }

    private fun initBackStack() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentFragmentManager.popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
}

