package com.example.mydiary.data.remote

import com.example.mydiary.data.model.*
import com.example.mydiary.ui.diary.DiaryViewModel
import retrofit2.Response
import retrofit2.http.*

interface DiaryApi {
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @POST("diary")
    suspend fun createDiary(@Body diary: Diary): Response<DiaryResponse>

    @GET("diary")
    suspend fun getDiaries(): Response<List<DiaryResponse>>

    @GET("diary/{diary_id}")
    suspend fun getDiaryById(@Path("diary_id") diaryId: Long): Response<DiaryResponse>

    @PUT("diary/{diary_id}")
    suspend fun updateDiary(@Path("diary_id") diaryId: Long, @Body diary: Diary): Response<DiaryResponse>

    @PUT("diary/{diary_id}/archieve")
    suspend fun archiveDiary(@Path("diary_id") diaryId: Long): Response<DiaryResponse>

    @PUT("diary/{diary_id}/unarchieve")
    suspend fun unarchiveDiary(@Path("diary_id") diaryId: Long): Response<DiaryResponse>

    @GET("diary/search")
    suspend fun searchDiaries(@Query("query") query: DiaryViewModel.LoadingStatus): Response<List<Diary>>

    @GET("diary/list")
    suspend fun diaryList(): Response<List<Diary>>

    @GET("diary/search_query")
    suspend fun searchQuery(@Query("query") query: String): Response<SearchQueryResponse>

    @DELETE("diary/{diary_id}")
    suspend fun deleteDiary(@Path("diary_id") diaryId: Long): Response<Unit>

}