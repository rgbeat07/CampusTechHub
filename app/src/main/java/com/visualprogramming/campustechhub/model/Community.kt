package com.visualprogramming.campustechhub.model

import kotlinx.serialization.Serializable

@Serializable
data class Community(
    val id: String,
    val name: String,
    val fullName: String,
    val category: String,
    val memberCount: Int,
    val description: String,
    val isJoined: Boolean = false,
    val iconType: String = "code"
)

