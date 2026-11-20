package com.example.e_commerce_app.ui.core.utils

object AppValidation {
    fun validateName(name: String): String? {
        return if (name.trim().isEmpty()) "Please Enter Name" else null
    }

    fun validateEmail(email: String): String? {
        val emailRegex = """^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$""".toRegex()
        return when {
            email.trim().isEmpty() -> "Please Enter Email"
            !email.matches(emailRegex) -> "Enter Valid Email"
            else -> null
        }
    }
    fun validatePhone(phone: String): String? {
        return when {
            phone.trim().isEmpty() -> "Please Enter Phone"
            phone.length < 11 -> "Phone should be at least 11 digits"
            else -> null
        }
    }

    fun validatePassword(password: String): String? {
        return when {
            password.trim().isEmpty() -> "Please Enter Phone"
            password.length < 6 -> "Phone should be at least 6 chars"
            else -> null
        }
    }

    fun validateConfirmPassword(password: String, rePassword: String): String? {
        return when {
            rePassword.trim().isEmpty() -> "Please Enter Re-Password"
            rePassword!= password ->  "Re-Password doesn't match password"
            else -> null
        }
    }
}