package android.project.auction.presentation.ui.common.bottomNav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNav(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination

    val items = listOf(
        BottomNavItem(
            "home_screen",
            "Home",
            Icons.Default.Home
        ),
        BottomNavItem(
            "categories_screen",
            "Categories",
            Icons.Default.Menu
        ),
        BottomNavItem(
            "favorites_screen",
            "Favorites",
            Icons.Default.Favorite
        ),
        BottomNavItem(
            "profile_screen",
            "Profile",
            Icons.Default.Person
        )
    )

    BottomNavigation(
        modifier = Modifier
            .padding(12.dp, 0.dp, 12.dp, 0.dp)
            .height(100.dp)
            .background(White),
        //backgroundColor = Color.White,
        elevation = 0.dp,
        backgroundColor = White
    ) {
        items.forEach {
            BottomNavigationItem(
                icon = {
                    it.icon.let {
                        Icon(
                            imageVector = it,
                            contentDescription = "",
                            modifier = Modifier.size(35.dp),
                            tint = Color.Gray
                        )
                    }
                },
                label = {
                    Text(
                        text = it.title,
                        color = Color.Gray
                    )
                },
                selected = currentRoute?.hierarchy?.any { it.route == it.route } == true,

                selectedContentColor = Color.Black,
                unselectedContentColor = Color.White,
                onClick = {}

            )
        }
    }
}