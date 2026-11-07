package com.visualprogramming.campustechhub.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.visualprogramming.campustechhub.model.Community
import com.visualprogramming.campustechhub.model.Project
import com.visualprogramming.campustechhub.model.StudentProfile
import com.visualprogramming.campustechhub.ui.components.DashboardProjectCard
import com.visualprogramming.campustechhub.ui.components.JoinedCommunityItemCard
import com.visualprogramming.campustechhub.ui.components.StatBadgeRow
import com.visualprogramming.campustechhub.ui.components.StudentProfileCard
import com.visualprogramming.campustechhub.ui.components.TopHeaderSection
import com.visualprogramming.campustechhub.ui.theme.BluePrimary
import com.visualprogramming.campustechhub.ui.theme.CampusTechHubTheme
import com.visualprogramming.campustechhub.ui.theme.Slate50
import com.visualprogramming.campustechhub.ui.theme.Slate500
import com.visualprogramming.campustechhub.ui.theme.Slate800
import com.visualprogramming.campustechhub.viewmodel.CampusUiState
import com.visualprogramming.campustechhub.viewmodel.CampusViewModel

@Composable
fun DashboardScreen(
    viewModel: CampusViewModel,
    onOpenProjects: (communityId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DashboardContent(
        uiState = uiState,
        onOpenProjects = onOpenProjects,
        modifier = modifier
    )
}

@Composable
fun DashboardContent(
    uiState: CampusUiState,
    onOpenProjects: (communityId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Slate50)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // App Bar / Top Header
            item {
                TopHeaderSection(studentProfile = uiState.studentProfile)
            }

            // Student Profile Card
            item {
                StudentProfileCard(studentProfile = uiState.studentProfile)
            }

            // Reactive Counter Badges (Joined Clubs & Total Projects)
            item {
                StatBadgeRow(
                    joinedClubsCount = uiState.joinedClubsCount,
                    totalProjectsCount = uiState.totalProjectsCount
                )
            }

            // Section 1: Semua Komunitas yang Diikuti
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "★ Semua Komunitas yang Diikuti (${uiState.joinedClubsCount})",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate800
                    )
                    Text(
                        text = "Status: Joined",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BluePrimary
                    )
                }
            }

            if (uiState.joinedCommunities.isEmpty()) {
                item {
                    FallbackCard(message = "Belum bergabung dengan komunitas")
                }
            } else {
                items(uiState.joinedCommunities, key = { it.id }) { community ->
                    val clubProjectCount = uiState.getProjectsForCommunity(community.id).size
                    JoinedCommunityItemCard(
                        community = community,
                        projectCount = clubProjectCount,
                        onClick = { onOpenProjects(community.id) }
                    )
                }
            }

            // Section 2: Semua Proyek Mahasiswa
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "▢ Semua Proyek Mahasiswa (${uiState.totalProjectsCount})",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate800
                    )
                    Text(
                        text = "Total: ${uiState.totalProjectsCount} Proyek",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BluePrimary
                    )
                }
            }

            if (uiState.projects.isEmpty()) {
                item {
                    FallbackCard(message = "Belum ada proyek yang dicatat")
                }
            } else {
                items(uiState.projects, key = { it.id }) { project ->
                    DashboardProjectCard(project = project)
                }
            }
        }
    }
}

@Composable
fun FallbackCard(
    message: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message,
                fontSize = 13.sp,
                color = Slate500,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardScreenPreview() {
    CampusTechHubTheme {
        DashboardContent(
            uiState = CampusUiState(
                studentProfile = StudentProfile(),
                communities = listOf(
                    Community(
                        id = "gdsc",
                        name = "GDSC",
                        fullName = "Google Developer Student Club",
                        category = "Google Developer Student Club • 2 Proyek",
                        memberCount = 142,
                        description = "Fokus pada ekosistem Google",
                        isJoined = true,
                        iconType = "code"
                    )
                ),
                projects = listOf(
                    Project(
                        id = "proj-gdsc-01",
                        communityId = "gdsc",
                        communityName = "GDSC",
                        title = "Smart Campus Navigation",
                        description = "Navigasi kampus",
                        techStacks = listOf("Kotlin", "Jetpack Compose", "Room"),
                        status = "In Progress",
                        iconType = "android"
                    )
                )
            ),
            onOpenProjects = {}
        )
    }
}

