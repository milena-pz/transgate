package com.transdoor.app.network



import android.os.Build
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor

object RetrofitInstance {


    private val BASE_URL: String
        get() = if (Build.FINGERPRINT.lowercase().contains("generic")) {
            // Emulador
           "http://10.0.2.2/transdoor/app/"
        } else {
            // Móvil real (cambia por tu IP local)

          //  "http://192.168.1.21/transdoor/app/"
          "http://192.168.1.160/transdoor/app/"
           // "http://192.168.1.209/transdoor/app/"


        }

    // Interceptor para ver tráfico de red en Logcat
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Cliente HTTP con logging habilitado
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()




    val api: ApiService by lazy {
        Retrofit.Builder()

            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
