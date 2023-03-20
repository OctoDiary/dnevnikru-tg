import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class BaseCallback<T>(
    private val okHandler: (T) -> Unit,
    private val errorHandler: (ResponseBody?) -> Unit,
    private val failureHandler: (Throwable) -> Unit
) : Callback<T> {
    override fun onResponse(call: Call<T>, response: Response<T>) {
        val body = response.body()
        if (response.isSuccessful && body != null) {
            okHandler(body)
        } else {
            errorHandler(response.errorBody())
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        failureHandler(t)
    }
}