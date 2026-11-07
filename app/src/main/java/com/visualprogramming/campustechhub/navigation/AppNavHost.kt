package com.visualprogramming.campustechhub.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.visualprogramming.campustechhub.ui.screen.ClubProjectsScreen
import com.visualprogramming.campustechhub.ui.screen.CommunitiesScreen
import com.visualprogramming.campustechhub.ui.screen.DashboardScreen
import com.visualprogramming.campustechhub.ui.theme.BlueLight
import com.visualprogramming.campustechhub.ui.theme.BluePrimary
import com.visualprogramming.campustechhub.ui.theme.Slate400
import com.visualprogramming.campustechhub.ui.theme.Slate600
import com.visualprogramming.campustechhub.viewmodel.CampusViewModel

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    sharedViewModel: CampusViewModel = viewModel()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val isDashboard = currentDestination?.hasRoute<DashboardRoute>() == true
    val isCommunities = currentDestination?.hasRoute<CommunitiesRoute>() == true
    val isTopLevelRoute = isDashboard || isCommunities

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            if (isTopLevelRoute) {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 6.dp
                ) {
                    NavigationBarItem(
                        selected = isDashboard,
                        onClick = {
                            if (!isDashboard) {
                                navController.navigate(DashboardRoute) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (isDashboard) Icons.Default.Home else Icons.Outlined.Home,
                                contentDescription = "Dashboard"
                            )
                        },
                        label = {
                            Text(
                                text = "Dashboard",
                                fontSize = 12.sp,
                                fontWeight = if (isDashboard) FontWeight.SemiBold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BluePrimary,
                            selectedTextColor = BluePrimary,
                            indicatorColor = BlueLight,
                            unselectedIconColor = Slate400,
                            unselectedTextColor = Slate600
                        )
                    )

                    NavigationBarItem(
                        selected = isCommunities,
                        onClick = {
                            if (!isCommunities) {
                                navController.navigate(CommunitiesRoute) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (isCommunities) Icons.Default.Groups else Icons.Outlined.Groups,
                                contentDescription = "Communities"
                            )
                        },
                        label = {
                            Text(
                                text = "Communities",
                                fontSize = 12.sp,
                                fontWeight = if (isCommunities) FontWeight.SemiBold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BluePrimary,
                            selectedTextColor = BluePrimary,
                            indicatorColor = BlueLight,
                            unselectedIconColor = Slate400,
                            unselectedTextColor = Slate600
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = DashboardRoute,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<DashboardRoute> {
                DashboardScreen(
                    viewModel = sharedViewModel,
                    onOpenProjects = { communityId ->
                        navController.navigate(ClubProjectsRoute(communityId))
                    }
                )
            }

            composable<CommunitiesRoute> {
                CommunitiesScreen(
                    viewModel = sharedViewModel,
                    onOpenProjects = { communityId ->
                        navController.navigate(ClubProjectsRoute(communityId))
                    }
                )
            }

            composable<ClubProjectsRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<ClubProjectsRoute>()
                ClubProjectsScreen(
                    communityId = route.communityId,
                    viewModel = sharedViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}

