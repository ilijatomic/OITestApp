package com.example.iotestapp.resources

import com.example.iotestapp.domain.model.User

object UserMocks {
    val validUser = User("admin", "admin")
    val invalidUser = User("bad", "bad")
}
