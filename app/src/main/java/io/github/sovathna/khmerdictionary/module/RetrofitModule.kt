package io.github.sovathna.khmerdictionary.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.github.sovathna.khmerdictionary.di.qualifier.Api
import io.github.sovathna.khmerdictionary.di.qualifier.Download
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object RetrofitModule {

    @Provides
    @ViewModelScoped
    @Download
    fun retrofit(@Download client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://example.com/")
            .client(client)
            .build()


    @Provides
    @ViewModelScoped
    @Api
    fun apiRetrofit(@Api client: OkHttpClient, converter: Converter.Factory): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://example.com/")
            .addConverterFactory(converter)
            .client(client)
            .build()
}