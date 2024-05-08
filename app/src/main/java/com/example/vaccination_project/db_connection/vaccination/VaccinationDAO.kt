package com.example.vaccination_project.db_connection.vaccination

import android.accounts.Account

interface VaccinationDAO {
    fun getVaccination(name: String) : vaccination?
    fun getAllVaccinations() : Set<vaccination>?
    fun updateVaccination(name: String, vaccination: vaccination): Boolean
    fun deleteVaccination(name: String): Boolean
    fun insertVaccination(vaccination: Account): Boolean
}