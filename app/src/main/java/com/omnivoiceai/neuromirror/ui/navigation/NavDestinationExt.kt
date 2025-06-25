package com.omnivoiceai.neuromirror.ui.navigation
import androidx.navigation.NavDestination

inline fun <reified T : NavigationRoute> NavDestination.hasRoute(): Boolean {
    return this.route == T::class.qualifiedName
}
