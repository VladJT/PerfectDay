package jt.projects.repository.network.facts

import retrofit2.http.*

interface FactsApi {
    @GET("{month}/{day}/date?json")
    suspend fun getFactOfTheDay(
        @Path("month") month: Int,
        @Path("day") day: Int
    ): FactResponse
}