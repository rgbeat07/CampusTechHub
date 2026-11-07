package com.visualprogramming.campustechhub

import com.visualprogramming.campustechhub.data.CampusRepository
import com.visualprogramming.campustechhub.model.Project
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CampusRepositoryTest {

    @Before
    fun setUp() {
        CampusRepository.resetToDefault()
    }

    @Test
    fun testInitialRepositoryState() {
        val profile = CampusRepository.studentProfile.value
        assertEquals("Aditya Pratama", profile.name)
        assertEquals("22001045", profile.nim)

        val communities = CampusRepository.communities.value
        assertTrue("Communities should have at least 6 items", communities.size >= 6)

        val joinedCount = communities.count { it.isJoined }
        assertEquals(3, joinedCount)

        val projects = CampusRepository.projects.value
        assertEquals(4, projects.size)
    }

    @Test
    fun testToggleCommunityJoin() {
        val initialStatus = CampusRepository.getCommunity("ai-society")?.isJoined
        assertEquals(false, initialStatus)

        CampusRepository.toggleCommunityJoin("ai-society")
        assertTrue(CampusRepository.getCommunity("ai-society")?.isJoined == true)

        CampusRepository.toggleCommunityJoin("ai-society")
        assertTrue(CampusRepository.getCommunity("ai-society")?.isJoined == false)
    }

    @Test
    fun testAddAndDeleteProject() {
        val initialCount = CampusRepository.projects.value.size

        val newProject = Project(
            id = "proj-test-01",
            communityId = "gdsc",
            communityName = "GDSC",
            title = "Test Project Compose",
            description = "Deskripsi test minimal 3 baris.",
            techStacks = listOf("Kotlin", "Jetpack Compose", "Room"),
            status = "In Progress"
        )

        CampusRepository.addProject(newProject)
        assertEquals(initialCount + 1, CampusRepository.projects.value.size)

        CampusRepository.deleteProject("proj-test-01")
        assertEquals(initialCount, CampusRepository.projects.value.size)
        assertNull(CampusRepository.projects.value.firstOrNull { it.id == "proj-test-01" })
    }
}

