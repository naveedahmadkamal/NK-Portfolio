package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CaseStudy
import com.example.data.model.ProjectCategory
import com.example.ui.components.GlassCard
import com.example.ui.components.ResponsiveContainer
import com.example.ui.components.SectionHeader
import com.example.ui.components.TechBadge
import com.example.ui.theme.TechAmber
import com.example.ui.theme.TechCyan

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CaseStudiesScreen(
    caseStudies: List<CaseStudy>,
    bookmarkedIds: List<String>,
    onOpenCaseStudy: (String) -> Unit,
    onToggleBookmark: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf<ProjectCategory?>(null) }
    var onlyBookmarks by remember { mutableStateOf(false) }

    val filteredList = caseStudies.filter { study ->
        val matchesCategory = selectedCategory == null || study.category == selectedCategory
        val matchesBookmark = !onlyBookmarks || bookmarkedIds.contains(study.id)
        matchesCategory && matchesBookmark
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("case_studies_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 16.dp,
            bottom = 96.dp
        )
    ) {
        // Header
        item {
            ResponsiveContainer {
                Column {
                    SectionHeader(
                        title = "In-Depth Case Studies",
                        subtitle = "Technical architecture, model validation, and business outcomes",
                        badgeText = "Research & Production"
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Filters
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedCategory == null && !onlyBookmarks,
                            onClick = {
                                selectedCategory = null
                                onlyBookmarks = false
                            },
                            label = { Text("All Studies") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = TechCyan.copy(alpha = 0.2f),
                                selectedLabelColor = TechCyan
                            )
                        )

                        ProjectCategory.values().filter { it != ProjectCategory.ALL }.forEach { cat ->
                            FilterChip(
                                selected = selectedCategory == cat && !onlyBookmarks,
                                onClick = {
                                    selectedCategory = cat
                                    onlyBookmarks = false
                                },
                                label = { Text(cat.label) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = TechCyan.copy(alpha = 0.2f),
                                    selectedLabelColor = TechCyan
                                )
                            )
                        }

                        FilterChip(
                            selected = onlyBookmarks,
                            onClick = {
                                onlyBookmarks = !onlyBookmarks
                            },
                            label = { Text("Saved (${bookmarkedIds.size})") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Bookmark,
                                    contentDescription = null,
                                    tint = if (onlyBookmarks) TechAmber else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = TechAmber.copy(alpha = 0.2f),
                                selectedLabelColor = TechAmber
                            )
                        )
                    }
                }
            }
        }

        // Case Study Cards
        items(filteredList, key = { it.id }) { study ->
            ResponsiveContainer {
                val isBookmarked = bookmarkedIds.contains(study.id)
                GlassCard(
                    borderColor = if (isBookmarked) TechAmber.copy(alpha = 0.4f) else MaterialTheme.colorScheme.outline,
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.clickable { onOpenCaseStudy(study.id) }
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TechBadge(text = study.category.label)
                            IconButton(
                                onClick = { onToggleBookmark(study.id) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = null,
                                    tint = if (isBookmarked) TechAmber else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = study.title,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )

                        Text(
                            text = "${study.clientOrInstitution} • ${study.duration}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TechCyan,
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )

                        Text(
                            text = study.tagline,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 20.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Models summary
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Models / Modules: ${study.modelsUsed.size}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "Interactive Demo Available",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TechCyan,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { onOpenCaseStudy(study.id) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = TechCyan,
                                contentColor = Color(0xFF090D16)
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("open_case_study_btn_${study.id}")
                        ) {
                            Text(
                                text = "Read Complete Case Study",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
