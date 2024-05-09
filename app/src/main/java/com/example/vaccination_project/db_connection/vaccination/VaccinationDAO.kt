package com.example.vaccination_project.db_connection.vaccination

interface VaccinationDAO {
    fun getVaccinationById(id: Int) : Vaccination?
    fun getVaccinationByName(vaccineName: String) : Vaccination?
    fun getVaccinationIdByVaccineName(vaccineName: String): Int
    fun getAllVaccinations() : Set<Vaccination?>?
    fun getAppointmentsCountForVaccination(vaccineName: String): Int
    fun getDosesCountByVaccineName(vaccineName: String): Int
    fun updateVaccination(id: Int, vaccination: Vaccination): Boolean
    fun deleteVaccination(id: Int): Boolean
    fun insertVaccination(vaccination: Vaccination): Boolean
}