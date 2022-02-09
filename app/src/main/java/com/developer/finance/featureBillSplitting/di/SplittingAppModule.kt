package com.developer.finance.featureBillSplitting.di

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.developer.finance.featureBillSplitting.data.remote.UserApiRepository
import com.developer.finance.featureBillSplitting.data.remote.interfaces.UserApi
import com.developer.finance.featureBillSplitting.domain.repository.UserApiRepositoryImpl
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.connection.ConnectionEventListener
import com.pusher.client.connection.ConnectionState
import com.pusher.client.connection.ConnectionStateChange
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object BillSplittingAppModule {

    @Singleton
    @Provides
    fun providePusherInstance(): Pusher {
        val options = PusherOptions()
        options.setCluster("ap2")
        val pusher = Pusher("5c5201def765e1b873e4", options)

        pusher.connect(object : ConnectionEventListener {
            override fun onConnectionStateChange(change: ConnectionStateChange?) {
                Log.i(
                    "Pusher",
                    "State changed from ${change?.previousState} to ${change?.currentState}"
                )
            }

            override fun onError(message: String?, code: String?, e: Exception?) {
                Log.i(
                    "Pusher",
                    "There was a problem connecting! code ($code), message ($message), exception($e)"
                )
            }

        }, ConnectionState.ALL)

        return pusher
    }

    @Singleton
    @Provides
    fun provideUserTokenPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("user_token", Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideUserApiRetrofitInstance(): UserApi {
        return Retrofit.Builder().baseUrl("http://192.168.0.32:3000/api/users/")
            .addConverterFactory(
                GsonConverterFactory.create()
            ).build().create(UserApi::class.java)
    }

    @Singleton
    @Provides
    fun provideUserApiRepository(userApi: UserApi): UserApiRepository {
        return UserApiRepositoryImpl(userApi)
    }

}