package com.visualprogramming.campustechhub.model

import kotlinx.serialization.Serializable

@Serializable
data class Project(
    val id: String,
    val communityId: String,
    val communityName: String,
    val title: String,
    val description: String,
    val techStacks: List<String>,
    val status: String = "In Progress", // "In Progress" or "Completed"
    val iconType: String = "code"
)

