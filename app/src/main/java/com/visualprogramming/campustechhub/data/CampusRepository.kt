package com.visualprogramming.campustechhub.data

import com.visualprogramming.campustechhub.model.Community
import com.visualprogramming.campustechhub.model.Project
import com.visualprogramming.campustechhub.model.StudentProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object CampusRepository {

    private val initialCommunities = listOf(
        Community(
            id = "gdsc",
            name = "GDSC",
            fullName = "Google Developer Student Club",
            category = "Google Developer Student Club • 142 Mahasiswa",
            memberCount = 142,
            description = "Fokus pada ekosistem Google: Android Compose, Flutter, Firebase, dan Google Cloud Platform.",
            isJoined = true,
            iconType = "code"
        ),
        Community(
            id = "cybersecurity",
            name = "CyberSecurity Guild",
            fullName = "CyberSecurity Guild",
            category = "Security & Pentesting • 88 Mahasiswa",
            memberCount = 88,
            description = "Eksplorasi ethical hacking, CTF challenges, reverse engineering, dan pertahanan jaringan kampus.",
            isJoined = true,
            iconType = "security"
        ),
        Community(
            id = "ai-society",
            name = "AI Society",
            fullName = "AI Society",
            category = "Machine Learning & LLM • 115 Mahasiswa",
            memberCount = 115,
            description = "Riset terapan deep learning, computer vision, agen cerdas, dan fine-tuning large language models.",
            isJoined = false,
            iconType = "ai"
        ),
        Community(
            id = "mobile-dev",
            name = "Mobile Dev Community",
            fullName = "Mobile Dev Community",
            category = "Native Jetpack Compose & iOS • 95 Mahasiswa",
            memberCount = 95,
            description = "Pengembangan aplikasi mobile modern lintas platform dengan fokus clean architecture dan UI/UX.",
            isJoined = true,
            iconType = "mobile"
        ),
        Community(
            id = "cloud-devops",
            name = "Cloud & DevOps Circle",
            fullName = "Cloud & DevOps Circle",
            category = "Cloud Infrastructure & CI/CD • 76 Mahasiswa",
            memberCount = 76,
            description = "Otomasi deployment, containerization Docker/Kubernetes, monitoring sistem, dan AWS/GCP cloud services.",
            isJoined = false,
            iconType = "cloud"
        ),
        Community(
            id = "web-tech",
            name = "Web Tech Innovators",
            fullName = "Web Tech Innovators",
            category = "Fullstack Web & APIs • 120 Mahasiswa",
            memberCount = 120,
            description = "Membangun aplikasi web performa tinggi dengan arsitektur microservices, RESTful & GraphQL APIs.",
            isJoined = false,
            iconType = "web"
        )
    )

    private val initialProjects = listOf(
        Project(
            id = "proj-gdsc-01",
            communityId = "gdsc",
            communityName = "GDSC",
            title = "Smart Campus Navigation",
            description = "Navigasi peta gedung perkuliahan, lab komputer, dan fasilitas kampus real-time.",
            techStacks = listOf("Kotlin", "Jetpack Compose", "Room", "Maps SDK"),
            status = "In Progress",
            iconType = "android"
        ),
        Project(
            id = "proj-gdsc-02",
            communityId = "gdsc",
            communityName = "GDSC",
            title = "Student Attendance QR Scanner",
            description = "Pemindai barcode & QR presensi kehadiran workshop berbasis CameraX & Firebase.",
            techStacks = listOf("Jetpack Compose", "Firebase", "CameraX"),
            status = "Completed",
            iconType = "code"
        ),
        Project(
            id = "proj-gdsc-03",
            communityId = "gdsc",
            communityName = "GDSC",
            title = "Developer RSVP Portal",
            description = "Portal pendaftaran webinar dan bootcamp teknologi sinkronisasi Ktor API server.",
            techStacks = listOf("Kotlin", "Ktor", "Compose"),
            status = "In Progress",
            iconType = "cloud"
        ),
        Project(
            id = "proj-cs-01",
            communityId = "cybersecurity",
            communityName = "CyberSecurity Guild",
            title = "Campus Network Vulnerability Auditor",
            description = "Toolkit analisis celah keamanan dan pemindaian port jaringan intranet laboratorium kampus.",
            techStacks = listOf("Python", "Bash", "Linux"),
            status = "In Progress",
            iconType = "security"
        )
    )

    private val _communities = MutableStateFlow<List<Community>>(initialCommunities)
    val communities: StateFlow<List<Community>> = _communities.asStateFlow()

    private val _projects = MutableStateFlow<List<Project>>(initialProjects)
    val projects: StateFlow<List<Project>> = _projects.asStateFlow()

    private val _studentProfile = MutableStateFlow(StudentProfile())
    val studentProfile: StateFlow<StudentProfile> = _studentProfile.asStateFlow()

    fun toggleCommunityJoin(communityId: String) {
        _communities.update { list ->
            list.map { community ->
                if (community.id == communityId) {
                    val newJoined = !community.isJoined
                    val updatedMemberCount = if (newJoined) community.memberCount + 1 else community.memberCount - 1
                    community.copy(
                        isJoined = newJoined,
                        memberCount = updatedMemberCount.coerceAtLeast(0)
                    )
                } else {
                    community
                }
            }
        }
    }

    fun addProject(project: Project) {
        _projects.update { list ->
            listOf(project) + list
        }
    }

    fun deleteProject(projectId: String) {
        _projects.update { list ->
            list.filterNot { it.id == projectId }
        }
    }

    fun getCommunity(communityId: String): Community? {
        return _communities.value.firstOrNull { it.id == communityId }
    }

    fun resetToDefault() {
        _communities.value = initialCommunities
        _projects.value = initialProjects
        _studentProfile.value = StudentProfile()
    }
}
