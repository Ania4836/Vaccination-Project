package com.example.vaccination_project.db_connection.doctors

/**
 * Defines the data access operations for managing doctors within the database.
 * This interface is intended to be implemented by classes that handle database interactions,
 * ensuring consistency in how doctor data is accessed and manipulated across the application.
 *
 * Implementing classes are expected to manage connections and handle any necessary
 * transformation between database results and [Doctors] objects.
 */
interface DoctorsDAO {
    /**
     * Retrieves a doctor by their unique identifier from the database.
     *
     * @param id The unique identifier of the doctor.
     * @return A [Doctors] instance representing the doctor, or null if no doctor is found.
     */
    fun getDoctorById(id: Int): Doctors?

    /**
     * Retrieves the unique identifier of a doctor based on their name and surname.
     *
     * @param name The first name of the doctor.
     * @param surname The surname of the doctor.
     * @return The unique identifier of the doctor, or null if no such doctor exists.
     */
    fun getDoctorId(name: String, surname: String): Int?

    /**
     * Retrieves all doctors stored in the database.
     *
     * @return A set of [Doctors] objects, each representing a doctor; the set is null if no doctors are available.
     */
    fun getAllDoctors(): Set<Doctors?>?

    /**
     * Inserts a new doctor into the database.
     *
     * @param doctor A [Doctors] object containing the doctor's details.
     * @return True if the insertion was successful, false otherwise.
     */
    fun insertDoctor(doctor: Doctors) : Boolean

    /**
     * Updates the details of an existing doctor in the database.
     *
     * @param id The unique identifier of the doctor to update.
     * @param doctor A [Doctors] object containing the updated details.
     * @return True if the update was successful, false otherwise.
     */
    fun updateDoctor(id: Int, doctor: Doctors) : Boolean

    /**
     * Deletes a doctor from the database using their unique identifier.
     *
     * @param id The unique identifier of the doctor to be deleted.
     * @return True if the doctor was successfully deleted, false otherwise.
     */
    fun deleteDoctor(id: Int) : Boolean
}