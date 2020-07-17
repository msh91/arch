package io.github.msh91.arch.util

// All regular expressions should be defined here
/**
 * Regex for mobile phone numbers
 */
const val MOBILE_REGEX = "^(09)[01239]\\d{8}\$"

/**
 * Regex for passwords
 */
const val PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=\\S+\$).{8,}\$"
