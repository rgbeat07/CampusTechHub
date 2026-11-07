package com.visualprogramming.campustechhub.ui.screen

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.visualprogramming.campustechhub.data.CampusRepository
import com.visualprogramming.campustechhub.ui.components.CommunityDirectoryCard
import com.visualprogramming.campustechhub.ui.theme.CampusTechHubTheme
import com.visualprogramming.campustechhub.ui.theme.Slate50
import com.visualprogramming.campustechhub.ui.theme.Slate500
import com.visualprogramming.campustechhub.ui.theme.Slate800
import com.visualprogramming.campustechhub.ui.theme.Slate900
import com.visualprogramming.campustechhub.viewmodel.CampusUiState
import com.visualprogramming.campustechhub.viewmodel.CampusViewModel

@Composable
fun CommunitiesScreen(
    viewModel: CampusViewModel,
    onOpenProjects: (communityId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(
                message = msg,
                actionLabel = "OK"
            )
            viewModel.clearSnackbar()
        }
    }

    CommunitiesContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onOpenProjects = onOpenProjects,
        onToggleJoin = viewModel::toggleCommunityJoin,
        onBlockedClick = viewModel::showGateAccessBlockedMessage,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        modifier = modifier
    )
}

@Composable
fun CommunitiesContent(
    uiState: CampusUiState,
    snackbarHostState: SnackbarHostState,
    onOpenProjects: (communityId: String) -> Unit,
    onToggleJoin: (communityId: String) -> Unit,
    onBlockedClick: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isSearchActive by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Slate50)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header: Communities Directory + Subtitle + Search Icon
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Communities Directory",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate900
                        )
                        Text(
                            text = "Pilih komunitas untuk membuka Club Projects",
                            fontSize = 12.sp,
                            color = Slate500
                        )
                    }

                    IconButton(onClick = { isSearchActive = !isSearchActive }) {
                        Icon(
                            imageVector = if (isSearchActive) Icons.Default.Close else Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Slate800
                        )
                    }
                }
            }

            // Search Bar (expandable)
            item {
                AnimatedVisibility(visible = isSearchActive) {
                    Column {
                        OutlinedTextField(
                            value = uiState.searchQuery,
                            onValueChange = onSearchQueryChange,
                            placeholder = { Text("Cari komunitas...", fontSize = 13.sp, color = Slate500) },
                            singleLine = true,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = Slate500
                                )
                            },
                            trailingIcon = {
                                if (uiState.searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { onSearchQueryChange("") }) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Clear",
                                            tint = Slate500
                                        )
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            // Communities List
            if (uiState.filteredCommunities.isEmpty()) {
                item {
                    FallbackCard(message = "Tidak ada komunitas yang sesuai dengan pencarian")
                }
            } else {
                items(uiState.filteredCommunities, key = { it.id }) { community ->
                    CommunityDirectoryCard(
                        community = community,
                        onOpenProjects = onOpenProjects,
                        onToggleJoin = onToggleJoin,
                        onBlockedClick = onBlockedClick
                    )
                }
            }
        }

        // Snackbar Host for Gate Access Alert
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CommunitiesScreenPreview() {
    CampusTechHubTheme {
        CommunitiesContent(
            uiState = CampusUiState(
                communities = CampusRepository.communities.value
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onOpenProjects = {},
            onToggleJoin = {},
            onBlockedClick = {},
            onSearchQueryChange = {}
        )
    }
}

