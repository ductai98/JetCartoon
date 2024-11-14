package com.taild.remote

import com.taild.domain.EpisodePage
import kotlinx.serialization.Serializable

@Serializable
data class RemoteEpisodePage(
    val info: Info,
    val results: List<RemoteEpisode>
) {
    @Serializable
    data class Info(
        val count: Int = 0,
        val pages: Int = 0,
        val next: String? = null,
        val prev: String? = null
    )

    fun toDomain(): EpisodePage {
        return EpisodePage(
            info = EpisodePage.Info(
                count = info.count,
                pages = info.pages,
                next = info.next,
                prev = info.prev
            ),
            results = results.map {
                it.toDomainEpisode()
            }
        )
    }
}