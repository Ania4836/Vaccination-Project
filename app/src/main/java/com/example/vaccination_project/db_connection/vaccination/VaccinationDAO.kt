package com.example.vaccination_project.db_connection.vaccination

/**
 * Defines the data access operations for managing vaccination records in the database.
 * This interface standardizes the CRUD operations (create, read, update, delete) for vaccination data,
 * ensuring that implementing classes provide consistent functionality for interacting with vaccination records.
 *
 * Implementing classes are expected to manage the database connections and handle the conversion
 * of data between the database format and the [Vaccination] class.
 */
interface VaccinationDAO {

    /**
     * Retrieves a vaccination record by its unique identifier.
     *
     * @param id The unique identifier of the vaccination.
     * @return A [Vaccination] object representing the specific vaccination, or null if no record is found.
     */
    fun getVaccinationById(id: Int) : Vaccination?

    /**
     * Retrieves a vaccination record by the vaccine name.
     *
     * @param vaccineName The name of the vaccine.
     * @return A [Vaccination] object representing the specific vaccination, or null if no record is found.
     */
    fun getVaccinationByName(vaccineName: String) : Vaccination?

    /**
     * Retrieves the unique identifier of a vaccination by the name of the vaccine.
     *
     * @param vaccineName The name of the vaccine.
     * @return The unique identifier of the vaccination, or 0 if no such vaccination exists.
     */
    fun getVaccinationIdByVaccineName(vaccineName: String): Int

    /**
     * Retrieves all vaccination records stored in the database.
     *
     * @return A set containing [Vaccination] objects, potentially empty if no records are found.
     */
    fun getAllVaccinations() : Set<Vaccination?>?

    /**
     * Retrieves the total number of scheduled appointments for a specific vaccine based on its name.
     *
     * @param vaccineName The name of the vaccine.
     * @return The total number of appointments scheduled for the specified vaccine.
     */
    fun getAppointmentsCountForVaccination(vaccineName: String): Int

    /**
     * Retrieves the total number of doses administered for a specific vaccine based on its name.
     *
     * @param vaccineName The name of the vaccine.
     * @return The total number of doses administered for the specified vaccine.
     */
    fun getDosesCountByVaccineName(vaccineName: String): Int

    /**
     * Updates a vaccination record in the database.
     *
     * @param id The unique identifier of the vaccination to update.
     * @param vaccination A [Vaccination] object containing the updated details.
     * @return True if the update was successful, false otherwise.
     */
    fun updateVaccination(id: Int, vaccination: Vaccination): Boolean

    /**
     * Deletes a vaccination record from the database using its unique identifier.
     *
     * @param id The unique identifier of the vaccination to be deleted.
     * @return True if the deletion was successful, false otherwise.
     */
    fun deleteVaccination(id: Int): Boolean

    /**
     * Inserts a new vaccination record into the database.
     *
     * @param vaccination A [Vaccination] object containing the details to be inserted.
     * @return True if the insertion was successful, false otherwise.
     */
    fun insertVaccination(vaccination: Vaccination): Boolean
}