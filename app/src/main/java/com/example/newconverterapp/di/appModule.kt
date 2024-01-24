package com.example.newconverterapp.di

import androidx.room.Room
import com.example.newconverterapp.db.MyDatabase
import com.example.newconverterapp.mapper.balance.BalanceMapper
import com.example.newconverterapp.repository.balance.BalanceRepository
import com.example.newconverterapp.repository.balance.BalanceRepositoryImpl
import com.example.newconverterapp.ui.home.HomeViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

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

    //Mappers
    single { BalanceMapper() }

    //Repositories
    single<BalanceRepository> { BalanceRepositoryImpl(get(), get()) }

    //ViewModels
    viewModel { HomeViewModel(get()) }
}
