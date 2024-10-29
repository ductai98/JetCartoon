package com.taild.domain

sealed class CharacterGender(val displayName: String) {
    object Male: CharacterGender("Male")
    object Female: CharacterGender("Female")
    object Genderless: CharacterGender("Genderless")
    object Unknown: CharacterGender("Not specified")
}