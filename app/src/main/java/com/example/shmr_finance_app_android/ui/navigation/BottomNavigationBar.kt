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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.navigation.RootScreen

@Composable
fun BottomNavigationBar(
    currentDestination: String?,
    onNavigate: (String) -> Unit,
    screens: List<RootScreen>
) {

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
                onClick = { onNavigate(screen.route) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    selectedIconColor = MaterialTheme.colorScheme.tertiary,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}