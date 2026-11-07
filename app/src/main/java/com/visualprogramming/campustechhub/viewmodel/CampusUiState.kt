package com.visualprogramming.campustechhub.viewmodel

import com.visualprogramming.campustechhub.model.Community
import com.visualprogramming.campustechhub.model.Project
import com.visualprogramming.campustechhub.model.StudentProfile

data class CampusUiState(
    val studentProfile: StudentProfile = StudentProfile(),
    val communities: List<Community> = emptyList(),
    val projects: List<Project> = emptyList(),
    val searchQuery: String = "",
    val snackbarMessage: String? = null
) {
    val joinedClubsCount: Int
        get() = communities.count { it.isJoined }

    val totalProjectsCount: Int
        get() = projects.size

    val joinedCommunities: List<Community>
        get() = communities.filter { it.isJoined }

    val filteredCommunities: List<Community>
        get() = if (searchQuery.isBlank()) {
            communities
        } else {
            communities.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                        it.fullName.contains(searchQuery, ignoreCase = true) ||
                        it.description.contains(searchQuery, ignoreCase = true)
            }
        }

    fun getProjectsForCommunity(communityId: String): List<Project> {
        return projects.filter { it.communityId == communityId }
    }

    fun getCommunity(communityId: String): Community? {
        return communities.firstOrNull { it.id == communityId }
    }
}

