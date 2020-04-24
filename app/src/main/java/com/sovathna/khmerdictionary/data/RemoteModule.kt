package com.sovathna.khmerdictionary.data

import com.sovathna.khmerdictionary.data.remote.OkHttpModule
import com.sovathna.khmerdictionary.data.remote.RetrofitModule
import com.sovathna.khmerdictionary.data.remote.ServiceModule
import dagger.Module

@Module(includes = [OkHttpModule::class, RetrofitModule::class, ServiceModule::class])
class RemoteModule {
}