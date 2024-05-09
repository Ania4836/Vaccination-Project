package com.example.vaccination_project.db_connection.doctors

interface DoctorsDAO {
    fun getDoctorById(id: Int): Doctors?

    fun getDoctorId(name: String, surname: String): Int?

    fun getAllDoctors(): Set<Doctors?>?

    fun insertDoctor(doctor: Doctors) : Boolean

    fun updateDoctor(id: Int, doctor: Doctors) : Boolean

    fun deleteDoctor(id: Int) : Boolean
}