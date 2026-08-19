package com.example.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AppBottomNavigationBar
import com.example.ui.components.AppHeader
import com.example.ui.screens.BookmarksAndHistoryScreen
import com.example.ui.screens.BotSenderScreen
import com.example.ui.screens.DeepSearchToolsScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.viewmodel.MainViewModel

@Composable
fun MainApp(
    viewModel: MainViewModel = viewModel()
) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()

    // Persian RTL layout
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                contentWindowInsets = WindowInsets.safeDrawing,
                topBar = {
                    AppHeader(
                        title = "تله‌فایندر | TeleFinder",
                        subtitle = when (currentTab) {
                            0 -> "جستجو و فیلتر پیشرفته کانال‌ها و ویدیوها"
                            1 -> "ارسال مستقیم عکس، ویدیو، فایل و پیام به ربات"
                            2 -> "کانال‌های ذخیره‌شده و سابقه جستجوها"
                            else -> "موتور جستجوی عمیق و گوگل دورک"
                        },
                        onSearchToolClick = {
                            viewModel.setTab(3)
                        }
                    )
                },
                bottomBar = {
                    AppBottomNavigationBar(
                        currentTab = currentTab,
                        onTabSelected = { viewModel.setTab(it) }
                    )
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    AnimatedContent(
                        targetState = currentTab,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "TabContent"
                    ) { tabIndex ->
                        when (tabIndex) {
                            0 -> SearchScreen(
                                viewModel = viewModel,
                                onNavigateToBotSender = { viewModel.setTab(1) }
                            )
                            1 -> BotSenderScreen(
                                viewModel = viewModel
                            )
                            2 -> BookmarksAndHistoryScreen(
                                viewModel = viewModel
                            )
                            3 -> DeepSearchToolsScreen(
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }
        }
    }
}
