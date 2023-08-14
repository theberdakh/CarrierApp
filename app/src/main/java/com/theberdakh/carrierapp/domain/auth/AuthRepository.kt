package com.theberdakh.carrierapp.domain.auth

import com.theberdakh.carrierapp.data.model.User

interface AuthRepository {

    suspend fun login(user: User)

}