package com.example.dataemail

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.dataemail.admin.DashboardActivity
import com.example.dataemail.databinding.ActivityRegisterBinding
import com.example.dataemail.model.Users
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var db: AppDatabase
    private lateinit var prefManager: PrefManager
    private var isPasswordVisible: Boolean = false

    private var selectedRole: String = "User"  // Default role

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Room database and PrefManager
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app_db")
            .fallbackToDestructiveMigration()
            .build()
        prefManager = PrefManager(this)

        // Setup Spinner with roles
        val roles = listOf("User", "Admin")  // List of roles
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerRoleRegister.adapter = adapter

        // Set a listener for spinner item selection
        binding.spinnerRoleRegister.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedRole = parentView.getItemAtPosition(position) as String
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // Handle no selection if needed
            }
        }

        setupPasswordToggle()
        setLoginText()

        binding.btnRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val username = binding.rusername.text.toString()
        val email = binding.remail.text.toString()
        val phone = binding.rphone.text.toString()
        val password = binding.rpassword.text.toString()
        val profilePhotoUrl = binding.rprofilePhotoUrl.text.toString()

        if (username.isBlank() || email.isBlank() || phone.isBlank() || password.isBlank() || profilePhotoUrl.isBlank()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                // Save user data to Room database
                val user = Users(
                    username = username,
                    email = email,
                    phone = phone,
                    password = password,
                    profilePhotoUrl = profilePhotoUrl,
                    role = selectedRole
                )
                db.userDao().insert(user)

                // Save user data to PrefManager
                prefManager.saveUser(username, email, phone, password, profilePhotoUrl, selectedRole)
                prefManager.saveLoggedInUsername(username)

                Toast.makeText(this@RegisterActivity, "Registration successful as $selectedRole", Toast.LENGTH_SHORT).show()


                // Navigate to the corresponding activity based on role
                val intent = if (selectedRole == "Admin") {
                    Intent(this@RegisterActivity, DashboardActivity::class.java)
                } else {
                    Intent(this@RegisterActivity, HomeActivity::class.java)
                }
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                Toast.makeText(this@RegisterActivity, "Registration failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupPasswordToggle() {
        binding.rpassword.setOnTouchListener { v, event ->
            val drawableEnd = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (binding.rpassword.right - binding.rpassword.compoundDrawables[drawableEnd].bounds.width())) {
                    togglePasswordVisibility()
                    return@setOnTouchListener true
                }
            }
            false
        }
    }

    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            binding.rpassword.transformationMethod = PasswordTransformationMethod.getInstance()
            binding.rpassword.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_lock, 0, R.drawable.ic_eye_close, 0
            )
        } else {
            binding.rpassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            binding.rpassword.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_lock, 0, R.drawable.ic_eye_open, 0
            )
        }

        binding.rpassword.setSelection(binding.rpassword.text.length)
        isPasswordVisible = !isPasswordVisible
    }

    private fun setLoginText() {
        val text = "Already Have an Account? Log In"
        val spannableString = SpannableString(text)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        spannableString.setSpan(
            ForegroundColorSpan(Color.BLUE),
            25, 31,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannableString.setSpan(
            clickableSpan,
            25, 31,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        with(binding) {
            tvlogin.text = spannableString
            tvlogin.movementMethod = LinkMovementMethod.getInstance()
        }
    }
}

