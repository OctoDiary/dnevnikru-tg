import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class BaseCallback<T>(
    private val okHandler: (T) -> Unit,
    private val errorHandler: (NetworkService.DnevnikError?) -> Unit,
    private val failureHandler: (Throwable) -> Unit
) : Callback<T> {
    override fun onResponse(call: Call<T>, response: Response<T>) {
        val body = response.body()
        if (response.isSuccessful && body != null) {
            okHandler(body)
        } else {
            errorHandler(Gson().fromJson(response.errorBody()!!.charStream(), object : TypeToken<NetworkService.DnevnikError>() {}.type))
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        failureHandler(t)
    }
}