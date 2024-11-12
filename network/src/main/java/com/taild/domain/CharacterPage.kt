package com.taild.domain

data class CharacterPage(
    val info: Info,
    val results: List<Character>
) {
    data class Info(
        val count: Int = 0,
        val pages: Int = 0,
        val next: String? = null,
        val prev: String? = null
    )
}