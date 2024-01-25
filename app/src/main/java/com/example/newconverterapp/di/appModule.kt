package com.example.newconverterapp.di

import androidx.room.Room
import com.example.newconverterapp.db.MyDatabase
import com.example.newconverterapp.mapper.balance.BalanceMapper
import com.example.newconverterapp.mapper.currency.CurrencyMapper
import com.example.newconverterapp.network.ApiService
import com.example.newconverterapp.network.currency.CurrencyDataFetcher
import com.example.newconverterapp.repository.balance.BalanceRepository
import com.example.newconverterapp.repository.balance.BalanceRepositoryImpl
import com.example.newconverterapp.repository.currency.CurrencyRepository
import com.example.newconverterapp.repository.currency.CurrencyRepositoryImpl
import com.example.newconverterapp.ui.home.HomeViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

private const val dbName = "MyConversionDb"

val applicationModule = module {
    //Room
    single {
        Room.databaseBuilder(
            androidApplication(),
            MyDatabase::class.java,
            dbName
        ).build()
    }

    //OkHttp3
    single {
        OkHttpClient.Builder()
            .callTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .build()
    }
    single { ApiService(get()) }

    //DataFetchers
    single { CurrencyDataFetcher(get()) }

    //Mappers
    single { BalanceMapper() }
    single { CurrencyMapper() }

    //Repositories
    single<BalanceRepository> { BalanceRepositoryImpl(get(), get()) }
    single<CurrencyRepository> { CurrencyRepositoryImpl(get(), get()) }

    //ViewModels
    viewModel { HomeViewModel(get(), get()) }
}