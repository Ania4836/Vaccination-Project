package com.example.vaccination_project.db_connection.vaccination

interface VaccinationDAO {
    fun getVaccinationById(name: String) : Vaccination?
    fun getVaccinationByName(name: String) : Vaccination?
    fun getVaccineIdByVaccineName(vaccineName: String): Int
    fun getAppointmentsCountForVaccine(vaccineName: String): Int
    fun updateVaccination(name: String, vaccination: Vaccination): Boolean
    fun deleteVaccination(name: String): Boolean
    fun insertVaccination(vaccination: Vaccination): Boolean
}