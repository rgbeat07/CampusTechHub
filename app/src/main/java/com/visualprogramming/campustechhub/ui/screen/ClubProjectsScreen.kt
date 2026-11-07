package com.visualprogramming.campustechhub.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.visualprogramming.campustechhub.ui.components.AddProjectDialog
import com.visualprogramming.campustechhub.ui.components.DetailProjectCard
import com.visualprogramming.campustechhub.ui.theme.BluePrimary
import com.visualprogramming.campustechhub.ui.theme.CampusTechHubTheme
import com.visualprogramming.campustechhub.ui.theme.Slate200
import com.visualprogramming.campustechhub.ui.theme.Slate50
import com.visualprogramming.campustechhub.ui.theme.Slate500
import com.visualprogramming.campustechhub.ui.theme.Slate800
import com.visualprogramming.campustechhub.ui.theme.Slate900
import com.visualprogramming.campustechhub.ui.theme.SoftRed
import com.visualprogramming.campustechhub.ui.theme.SoftRedLight
import com.visualprogramming.campustechhub.viewmodel.CampusViewModel

@Composable
fun ClubProjectsScreen(
    communityId: String,
    viewModel: CampusViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val community = uiState.getCommunity(communityId)
    val communityProjects = uiState.getProjectsForCommunity(communityId)

    var showAddDialog by remember { mutableStateOf(false) }

    ClubProjectsContent(
        community = community,
        projects = communityProjects,
        onBackClick = onBackClick,
        onDeleteProject = viewModel::deleteProject,
        onAddProjectClick = { showAddDialog = true },
        modifier = modifier
    )

    if (showAddDialog && community != null) {
        AddProjectDialog(
            communityName = community.name,
            communityFullName = community.fullName,
            onDismiss = { showAddDialog = false },
            onSave = { title, description, techStacks ->
                viewModel.addProject(
                    communityId = communityId,
                    title = title,
                    description = description,
                    techStacks = techStacks
                )
                showAddDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClubProjectsContent(
    community: Community?,
    projects: List<Project>,
    onBackClick: () -> Unit,
    onDeleteProject: (String) -> Unit,
    onAddProjectClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val communityName = community?.name ?: "Community"
    val communityFullName = community?.fullName ?: ""

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Slate50,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "$communityName Projects",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate900
                        )
                        if (communityFullName.isNotBlank()) {
                            Text(
                                text = communityFullName,
                                fontSize = 11.sp,
                                color = Slate500
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali ke Communities",
                            tint = Slate800
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddProjectClick,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Add Project", fontWeight = FontWeight.SemiBold) },
                containerColor = BluePrimary,
                contentColor = Color.White
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Community Info Header Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Slate200),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = communityFullName.ifBlank { communityName },
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate800
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${community?.memberCount ?: 0} Anggota • ${projects.size} Proyek Aktif",
                                fontSize = 12.sp,
                                color = Slate500
                            )
                        }

                        Card(
                            shape = RoundedCornerShape(50),
                            colors = CardDefaults.cardColors(containerColor = SoftRedLight),
                            border = BorderStroke(1.dp, SoftRed.copy(alpha = 0.5f))
                        ) {
                            Text(
                                text = "Joined ✓",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SoftRed,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // Section: Daftar Proyek Mahasiswa (X)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "▢ Daftar Proyek Mahasiswa (${projects.size})",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate800
                    )
                }
            }

            // Projects List
            if (projects.isEmpty()) {
                item {
                    FallbackCard(message = "Belum ada proyek yang dicatat")
                }
            } else {
                items(projects, key = { it.id }) { project ->
                    DetailProjectCard(
                        project = project,
                        onDelete = onDeleteProject
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ClubProjectsScreenPreview() {
    CampusTechHubTheme {
        ClubProjectsContent(
            community = Community(
                id = "gdsc",
                name = "GDSC",
                fullName = "Google Developer Student Club",
                category = "Google Developer Student Club • 142 Mahasiswa",
                memberCount = 142,
                description = "Fokus pada ekosistem Google",
                isJoined = true
            ),
            projects = listOf(
                Project(
                    id = "proj-gdsc-01",
                    communityId = "gdsc",
                    communityName = "GDSC",
                    title = "Smart Campus Navigation",
                    description = "Navigasi peta gedung perkuliahan, lab komputer, dan fasilitas kampus real-time.",
                    techStacks = listOf("Kotlin", "Jetpack Compose", "Room", "Maps SDK"),
                    status = "In Progress",
                    iconType = "android"
                )
            ),
            onBackClick = {},
            onDeleteProject = {},
            onAddProjectClick = {}
        )
    }
}

