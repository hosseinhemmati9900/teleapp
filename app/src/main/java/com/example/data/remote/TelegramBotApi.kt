package com.example.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class TelegramApiResponse<T>(
    @Json(name = "ok") val ok: Boolean,
    @Json(name = "result") val result: T? = null,
    @Json(name = "description") val description: String? = null,
    @Json(name = "error_code") val errorCode: Int? = null
)

@JsonClass(generateAdapter = true)
data class TelegramUser(
    @Json(name = "id") val id: Long,
    @Json(name = "is_bot") val isBot: Boolean,
    @Json(name = "first_name") val firstName: String,
    @Json(name = "username") val username: String? = null
)

@JsonClass(generateAdapter = true)
data class TelegramMessage(
    @Json(name = "message_id") val messageId: Long,
    @Json(name = "date") val date: Long
)

interface TelegramBotService {

    @GET("getMe")
    suspend fun getMe(): Response<TelegramApiResponse<TelegramUser>>

    @FormUrlEncoded
    @POST("sendMessage")
    suspend fun sendMessage(
        @Field("chat_id") chatId: String,
        @Field("text") text: String,
        @Field("parse_mode") parseMode: String? = "HTML"
    ): Response<TelegramApiResponse<TelegramMessage>>

    @Multipart
    @POST("sendPhoto")
    suspend fun sendPhoto(
        @Part("chat_id") chatId: RequestBody,
        @Part photo: MultipartBody.Part,
        @Part("caption") caption: RequestBody?,
        @Part("parse_mode") parseMode: RequestBody?
    ): Response<TelegramApiResponse<TelegramMessage>>

    @Multipart
    @POST("sendVideo")
    suspend fun sendVideo(
        @Part("chat_id") chatId: RequestBody,
        @Part video: MultipartBody.Part,
        @Part("caption") caption: RequestBody?,
        @Part("parse_mode") parseMode: RequestBody?
    ): Response<TelegramApiResponse<TelegramMessage>>

    @Multipart
    @POST("sendDocument")
    suspend fun sendDocument(
        @Part("chat_id") chatId: RequestBody,
        @Part document: MultipartBody.Part,
        @Part("caption") caption: RequestBody?,
        @Part("parse_mode") parseMode: RequestBody?
    ): Response<TelegramApiResponse<TelegramMessage>>

    @Multipart
    @POST("sendAudio")
    suspend fun sendAudio(
        @Part("chat_id") chatId: RequestBody,
        @Part audio: MultipartBody.Part,
        @Part("caption") caption: RequestBody?,
        @Part("parse_mode") parseMode: RequestBody?
    ): Response<TelegramApiResponse<TelegramMessage>>
}

object TelegramBotConstants {
    const val BOT_TOKEN = "8802924488:AAHU2xeqYwxJvG8gA3a0V_ntjFteUks8NuE"
    const val DEFAULT_CHAT_ID = "6661823492"
    const val BASE_URL = "https://api.telegram.org/bot$BOT_TOKEN/"
}

object TelegramBotClientProvider {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .build()

    val service: TelegramBotService by lazy {
        Retrofit.Builder()
            .baseUrl(TelegramBotConstants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(TelegramBotService::class.java)
    }
}
