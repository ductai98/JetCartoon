package com.taild.remote

import com.taild.domain.CharacterPage
import kotlinx.serialization.Serializable

@Serializable
data class RemoteCharacterPage(
    val info: Info,
    val results: List<RemoteCharacter>
) {
    @Serializable
    data class Info(
        val count: Int = 0,
        val pages: Int = 0,
        val next: String? = null,
        val prev: String? = null
    )

    fun toDomain(): CharacterPage {
        return CharacterPage(
            info = CharacterPage.Info(
                count = info.count,
                pages = info.pages,
                next = info.next,
                prev = info.prev
            ),
            results = results.map {
                it.toDomainCharacter()
            }
        )
    }
}
