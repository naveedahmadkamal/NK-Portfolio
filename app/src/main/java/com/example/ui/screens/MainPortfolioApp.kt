package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TechCyan
import com.example.ui.viewmodel.AppNavDestination
import com.example.ui.viewmodel.PortfolioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPortfolioApp(
    viewModel: PortfolioViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val bookmarkedIds by viewModel.bookmarkedIds.collectAsState()
    val inquiryCount by viewModel.inquiryCount.collectAsState()
    val inquiries by viewModel.inquiries.collectAsState()

    val currentDestination = uiState.currentDestination
    val isViewingCaseStudy = uiState.viewingCaseStudyId != null
    val currentCaseStudy = viewModel.caseStudies.firstOrNull { it.id == uiState.viewingCaseStudyId }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val isWideScreen = maxWidth >= 680.dp

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            Text(
                                text = "NK-PORTFOLIO",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp,
                                    color = TechCyan
                                )
                            )
                        }
                    },
                    actions = {
                        if (inquiryCount > 0) {
                            IconButton(
                                onClick = { viewModel.setShowInquiryHistory(true) },
                                modifier = Modifier.testTag("appbar_inquiry_history_btn")
                            ) {
                                BadgedBox(badge = { Badge { Text("$inquiryCount") } }) {
                                    Icon(
                                        imageVector = Icons.Default.History,
                                        contentDescription = "Saved inquiries",
                                        tint = TechCyan
                                    )
                                }
                            }
                        }

                        IconButton(
                            onClick = { viewModel.toggleTheme() },
                            modifier = Modifier.testTag("theme_toggle_button")
                        ) {
                            Icon(
                                imageVector = if (uiState.darkTheme) Icons.Default.Brightness7 else Icons.Default.Brightness4,
                                contentDescription = "Toggle Theme",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                    )
                )
            },
            bottomBar = {
                if (!isWideScreen) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .windowInsetsPadding(WindowInsets.navigationBars)
                            .testTag("bottom_nav_bar")
                    ) {
                        AppNavDestination.values().forEach { destination ->
                            val isSelected = currentDestination == destination && !isViewingCaseStudy
                            val iconSelected = when (destination) {
                                AppNavDestination.HOME -> Icons.Default.Home
                                AppNavDestination.CASE_STUDIES -> Icons.Default.Insights
                                AppNavDestination.EXPERIENCE -> Icons.Default.Work
                                AppNavDestination.CONTACT -> Icons.Default.Email
                            }
                            val iconUnselected = when (destination) {
                                AppNavDestination.HOME -> Icons.Outlined.Home
                                AppNavDestination.CASE_STUDIES -> Icons.Outlined.Insights
                                AppNavDestination.EXPERIENCE -> Icons.Outlined.Work
                                AppNavDestination.CONTACT -> Icons.Outlined.Email
                            }

                            NavigationBarItem(
                                selected = isSelected,
                                onClick = { viewModel.setNavDestination(destination) },
                                icon = {
                                    Icon(
                                        imageVector = if (isSelected) iconSelected else iconUnselected,
                                        contentDescription = destination.label
                                    )
                                },
                                label = { Text(destination.label) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.background,
                                    indicatorColor = TechCyan,
                                    selectedTextColor = TechCyan,
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                modifier = Modifier.testTag("nav_item_${destination.name}")
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Adaptive Navigation Rail on Wide screens
                if (isWideScreen) {
                    NavigationRail(
                        containerColor = MaterialTheme.colorScheme.surface,
                        modifier = Modifier
                            .fillMaxHeight()
                            .testTag("navigation_rail")
                    ) {
                        AppNavDestination.values().forEach { destination ->
                            val isSelected = currentDestination == destination && !isViewingCaseStudy
                            val iconSelected = when (destination) {
                                AppNavDestination.HOME -> Icons.Default.Home
                                AppNavDestination.CASE_STUDIES -> Icons.Default.Insights
                                AppNavDestination.EXPERIENCE -> Icons.Default.Work
                                AppNavDestination.CONTACT -> Icons.Default.Email
                            }
                            val iconUnselected = when (destination) {
                                AppNavDestination.HOME -> Icons.Outlined.Home
                                AppNavDestination.CASE_STUDIES -> Icons.Outlined.Insights
                                AppNavDestination.EXPERIENCE -> Icons.Outlined.Work
                                AppNavDestination.CONTACT -> Icons.Outlined.Email
                            }

                            NavigationRailItem(
                                selected = isSelected,
                                onClick = { viewModel.setNavDestination(destination) },
                                icon = {
                                    Icon(
                                        imageVector = if (isSelected) iconSelected else iconUnselected,
                                        contentDescription = destination.label
                                    )
                                },
                                label = { Text(destination.label) },
                                colors = NavigationRailItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.background,
                                    indicatorColor = TechCyan,
                                    selectedTextColor = TechCyan,
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }
                }

                // Main Screen Content Area
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    if (isViewingCaseStudy && currentCaseStudy != null) {
                        CaseStudyDetailScreen(
                            caseStudy = currentCaseStudy,
                            isBookmarked = bookmarkedIds.contains(currentCaseStudy.id),
                            mlState = uiState.mlSimulator,
                            biState = uiState.biSimulator,
                            onBack = { viewModel.closeCaseStudy() },
                            onToggleBookmark = { viewModel.toggleBookmark(currentCaseStudy.id) },
                            onUpdateSimulatorAge = { viewModel.updateSimulatorAge(it) },
                            onUpdateSimulatorBp = { viewModel.updateSimulatorBp(it) },
                            onUpdateSimulatorGlucose = { viewModel.updateSimulatorGlucose(it) },
                            onUpdateSimulatorBmi = { viewModel.updateSimulatorBmi(it) },
                            onUpdateSimulatorCholesterol = { viewModel.updateSimulatorCholesterol(it) },
                            onUpdateBiStage = { viewModel.updateBiStageFilter(it) },
                            onUpdateBiCohort = { viewModel.updateBiCohortFilter(it) },
                            onContactAboutProject = { projectTitle ->
                                viewModel.updateContactSubject("Inquiry regarding $projectTitle")
                                viewModel.setNavDestination(AppNavDestination.CONTACT)
                            }
                        )
                    } else {
                        when (currentDestination) {
                            AppNavDestination.HOME -> {
                                HomeScreen(
                                    projects = viewModel.projects,
                                    selectedCategory = uiState.selectedProjectCategory,
                                    bookmarkedIds = bookmarkedIds,
                                    onSelectCategory = { viewModel.setProjectCategory(it) },
                                    onOpenCaseStudy = { viewModel.openCaseStudy(it) },
                                    onToggleBookmark = { viewModel.toggleBookmark(it) },
                                    onNavigate = { viewModel.setNavDestination(it) }
                                )
                            }
                            AppNavDestination.CASE_STUDIES -> {
                                CaseStudiesScreen(
                                    caseStudies = viewModel.caseStudies,
                                    bookmarkedIds = bookmarkedIds,
                                    onOpenCaseStudy = { viewModel.openCaseStudy(it) },
                                    onToggleBookmark = { viewModel.toggleBookmark(it) }
                                )
                            }
                            AppNavDestination.EXPERIENCE -> {
                                ExperienceScreen(
                                    workExperience = viewModel.workExperience,
                                    education = viewModel.education,
                                    skillCategories = viewModel.skillCategories
                                )
                            }
                            AppNavDestination.CONTACT -> {
                                ContactScreen(
                                    formState = uiState.contactForm,
                                    inquiryCount = inquiryCount,
                                    onNameChange = { viewModel.updateContactName(it) },
                                    onEmailChange = { viewModel.updateContactEmail(it) },
                                    onSubjectChange = { viewModel.updateContactSubject(it) },
                                    onProjectTypeChange = { viewModel.updateContactProjectType(it) },
                                    onTimelineChange = { viewModel.updateContactTimeline(it) },
                                    onMessageChange = { viewModel.updateContactMessage(it) },
                                    onSaveInquiry = { status, onDone ->
                                        viewModel.saveInquiryToLocal(status, onDone)
                                    },
                                    onOpenInquiryHistory = { viewModel.setShowInquiryHistory(true) },
                                    onDismissStatus = { viewModel.dismissFormStatus() }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Dialog for saved inquiry history
        if (uiState.showInquiryHistoryDialog) {
            InquiryHistoryDialog(
                inquiries = inquiries,
                onDismiss = { viewModel.setShowInquiryHistory(false) },
                onDeleteInquiry = { viewModel.deleteInquiry(it) }
            )
        }
    }
}
