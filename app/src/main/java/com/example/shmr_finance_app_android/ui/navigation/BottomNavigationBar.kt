package com.example.shmr_finance_app_android.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.navigation.RootScreen

@Composable
fun BottomNavigationBar(
    navController: NavController,
    screens: List<RootScreen>
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    NavigationBar {
        screens.forEach { screen ->
            NavigationBarItem(
                selected = currentDestination == screen.route,
                label = {
                    Text(text = stringResource(id = screen.label))
                },
                icon = {
                    Icon(
                        modifier = Modifier
                            .padding(all = dimensionResource(R.dimen.extra_small_padding))
                            .size(dimensionResource(R.dimen.medium_icon_size)),
                        painter = painterResource(screen.icon),
                        contentDescription = stringResource(screen.label)
                    )
                },
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    selectedIconColor = MaterialTheme.colorScheme.tertiary,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}