package com.example.myapplication.database

import com.example.myapplication.Database
import comexamplemyapplication.HockeyPlayer

class DatabaseProvider(databaseDriverFactory: DriverFactory) {
    private val database = Database(databaseDriverFactory.createDriver())

    private val dbQuery = database.databaseQueries


    fun doDatabaseStuff(): List<HockeyPlayer> {
        return dbQuery.selectAll().executeAsList()
    }


}