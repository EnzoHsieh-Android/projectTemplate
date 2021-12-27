package com.citrus.projecttemplate.di


import androidx.viewbinding.BuildConfig
import com.citrus.projecttemplate.remote.ApiService
import com.citrus.projecttemplate.remote.MemeRepositoryImpl
import com.citrus.projecttemplate.util.Constants
import com.citrus.projecttemplate.view.main.MemeUseCase
import com.citrus.projecttemplate.view.main.adapter.DemoItemAdapter
import com.citrus.projecttemplate.view.main.adapter.PuzzleAdapter
import com.google.gson.Gson
import com.skydoves.sandwich.coroutines.CoroutinesResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface AppModule {
    companion object {
        private const val DEFAULT_CONNECT_TIME = 10L
        private const val DEFAULT_WRITE_TIME = 30L
        private const val DEFAULT_READ_TIME = 30L

        @Provides
        @Singleton
        fun okHttpClient(): OkHttpClient {
            val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()

            if (BuildConfig.DEBUG) {
                val loggingInterceptor =
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                okHttpClientBuilder.addInterceptor(loggingInterceptor)
            }

            return okHttpClientBuilder
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(DEFAULT_CONNECT_TIME, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_WRITE_TIME, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_READ_TIME, TimeUnit.SECONDS)
                .build()
        }


        @Provides
        @Singleton
        fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(CoroutinesResponseCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .build()

        @Provides
        @Singleton
        fun provideApiService(retrofit: Retrofit): ApiService =
            retrofit.create(ApiService::class.java)
    }
}

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @Provides
    fun provideMemeUseCase(repository: MemeRepositoryImpl) =
        MemeUseCase(repository)
}

@Module
@InstallIn(FragmentComponent::class)
object FragmentModule {
    @Provides
    fun provideGoodsItemAdapter() =
        DemoItemAdapter()

    @Provides
    fun providePuzzleAdapter() =
        PuzzleAdapter()

}
