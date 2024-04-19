package com.example.vaccination_project.db_connection.account

import android.accounts.Account

interface AccountDAO {
    fun getAccount(name: String) : Account?
    fun setAllAccounts() : Set<Account>?
}