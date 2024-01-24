package com.example.newconverterapp.util

interface Mapper<T, M> {
    fun mapToModel(t: T): M
    fun mapToModel(list: List<T>) = list.map { mapToModel(it) }
}