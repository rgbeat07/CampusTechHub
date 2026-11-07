package com.visualprogramming.campustechhub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.visualprogramming.campustechhub.data.CampusRepository
import com.visualprogramming.campustechhub.model.Community
import com.visualprogramming.campustechhub.model.Project
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class CampusViewModel(
    private val repository: CampusRepository = CampusRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _snackbarMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<CampusUiState> = combine(
        repository.studentProfile,
        repository.communities,
        repository.projects,
        _searchQuery,
        _snackbarMessage
    ) { profile, communities, projects, searchQuery, snackbarMessage ->
        CampusUiState(
            studentProfile = profile,
            communities = communities,
            projects = projects,
            searchQuery = searchQuery,
            snackbarMessage = snackbarMessage
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CampusUiState(
            studentProfile = repository.studentProfile.value,
            communities = repository.communities.value,
            projects = repository.projects.value
        )
    )

    fun toggleCommunityJoin(communityId: String) {
        repository.toggleCommunityJoin(communityId)
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun showGateAccessBlockedMessage() {
        _snackbarMessage.value = "Harap bergabung ke komunitas terlebih dahulu!"
    }

    fun showSnackbar(message: String) {
        _snackbarMessage.value = message
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    fun addProject(
        communityId: String,
        title: String,
        description: String,
        techStacks: List<String>
    ) {
        val community = repository.getCommunity(communityId)
        val communityName = community?.name ?: "Community"
        val nextIndex = repository.projects.value.count { it.communityId == communityId } + 1
        val projectId = "proj-${communityId.take(4).lowercase()}-${String.format("%02d", nextIndex)}"

        val iconType = when {
            techStacks.any { it.contains("Compose", ignoreCase = true) || it.contains("Android", ignoreCase = true) } -> "android"
            techStacks.any { it.contains("Cloud", ignoreCase = true) || it.contains("Ktor", ignoreCase = true) || it.contains("Firebase", ignoreCase = true) } -> "cloud"
            techStacks.any { it.contains("Security", ignoreCase = true) || it.contains("Cyber", ignoreCase = true) } -> "security"
            techStacks.any { it.contains("AI", ignoreCase = true) || it.contains("Python", ignoreCase = true) } -> "ai"
            else -> "code"
        }

        val newProject = Project(
            id = projectId,
            communityId = communityId,
            communityName = communityName,
            title = title.trim(),
            description = description.trim(),
            techStacks = techStacks,
            status = "In Progress",
            iconType = iconType
        )

        repository.addProject(newProject)
    }

    fun deleteProject(projectId: String) {
        repository.deleteProject(projectId)
    }

    fun getCommunity(communityId: String): Community? {
        return repository.getCommunity(communityId)
    }

    fun isCommunityJoined(communityId: String): Boolean {
        return repository.getCommunity(communityId)?.isJoined == true
    }
}
