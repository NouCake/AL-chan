package com.zen.alchan.data.datasource

import com.apollographql.apollo.api.Response
import io.reactivex.Observable
import type.UserStatisticsSort

interface UserDataSource {
    fun getProfileQuery(userId: Int, sort: List<UserStatisticsSort>): Observable<Response<ProfileDataQuery.Data>>
}