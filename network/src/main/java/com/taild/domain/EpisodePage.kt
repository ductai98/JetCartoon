package com.taild.domain

data class EpisodePage(
    val info: Info,
    val result: List<Episode>
) {
    data class Info(
        val count: Int = 0,
        val pages: Int = 0,
        val next: String? = null,
        val prev: String? = null
    )
}