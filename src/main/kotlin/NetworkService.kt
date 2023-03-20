import api_models.chat.*
import api_models.diary.Diary
import api_models.lesson.Lesson
import api_models.mark.MarkDetails
import api_models.periodmarks.PeriodMarksResponse
import api_models.rating.RatingClass
import api_models.user.User
import api_models.userfeed.UserFeed
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

object NetworkService {

    enum class LoadType {
        Undefined,
        Past,
        Future
    }

    data class AuthResult(
        val credentials: AuthResultCredentials,
        val reason: String,
        val type: String
    )

    data class AuthResultCredentials(
        val accessToken: String,
        val userId: Long,
    )

    data class AuthRequestBody(
        val username: String,
        val password: String,
        val clientId: String,
        val clientSecret: String,
        val scope: String,
    )

    interface API {
        @POST("authorizations/byCredentials")
        fun auth(
            @Body authRequestBody: AuthRequestBody
        ): Call<AuthResult>

        @GET("persons/{person_id}/schools/{school_id}/groups/{group_id}/diary")
        fun diary(
            @Path("person_id") personId: Long,
            @Path("school_id") schoolId: Long,
            @Path("group_id") groupId: Long,
            @Header("Access-Token") accessToken: String?,
            @Query("id") id: String = "",
            @Query("loadType") loadType: String = LoadType.Undefined.name,
        ): Call<Diary>

        @GET("users/{user_id}/context")
        fun user(
            @Path("user_id") userId: Long,
            @Header("Access-Token") accessToken: String?,
        ): Call<User>

        @GET("persons/{person_id}/groups/{group_id}/rating")
        fun rating(
            @Path("person_id") personId: Long,
            @Path("group_id") groupId: Long,
            @Header("Access-Token") accessToken: String?,
        ): Call<RatingClass>

        @GET("persons/{person_id}/groups/{group_id}/lessons/{lesson_id}/lessonDetails")
        fun lessonDetails(
            @Path("person_id") personId: Long,
            @Path("group_id") groupId: Long,
            @Path("lesson_id") lessonId: Long,
            @Header("Access-Token") accessToken: String?,
        ): Call<Lesson>

        @GET("persons/{person_id}/groups/{group_id}/marks/{mark_id}/markDetails")
        fun markDetails(
            @Path("person_id") personId: Long,
            @Path("group_id") groupId: Long,
            @Path("mark_id") markId: Long,
            @Header("Access-Token") accessToken: String?,
        ): Call<MarkDetails>

        @GET("persons/{person_id}/groups/{group_id}/important")
        fun userFeed(
            @Path("person_id") personId: Long,
            @Path("group_id") groupId: Long,
            @Header("Access-Token") accessToken: String?,
        ): Call<UserFeed>

        @GET("persons/{person_id}/groups/{group_id}/periods/{period_id}/periodMarks")
        fun periodMarks(
            @Path("person_id") personId: Long,
            @Path("group_id") groupId: Long,
            @Path("period_id") periodId: Long,
            @Header("Access-Token") accessToken: String?,
        ): Call<PeriodMarksResponse>

        @GET("chat/context")
        fun chatContext(
            @Header("Access-Token") accessToken: String?
        ): Call<ChatContext>

        @POST("chat/credentials")
        fun chatCredentials(
            @Header("Access-Token") accessToken: String?
        ): Call<ChatCredentials>

        @POST("chat/contacts/enrich")
        fun chatEnrich(
            @Body body: ChatEnrichBody,
            @Header("Access-Token") accessToken: String?
        ): Call<ChatEnrich>

        @GET("chat/closeContacts")
        fun chatCloseContacts(
            @Header("Access-Token") accessToken: String?
        ): Call<ChatCloseContacts>
    }

    fun api(server: Server): API {
        val retrofit = Retrofit.Builder()
            .baseUrl(server.url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(API::class.java)
    }
}