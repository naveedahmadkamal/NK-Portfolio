package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DataObject
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CaseStudy
import com.example.ui.components.GlassCard
import com.example.ui.components.MetricChip
import com.example.ui.components.ResponsiveContainer
import com.example.ui.components.TechBadge
import com.example.ui.theme.TechAmber
import com.example.ui.theme.TechCyan
import com.example.ui.theme.TechEmerald
import com.example.ui.theme.TechRose
import com.example.ui.viewmodel.MlSimulatorState
import com.example.ui.viewmodel.PowerBiSimulatorState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CaseStudyDetailScreen(
    caseStudy: CaseStudy,
    isBookmarked: Boolean,
    mlState: MlSimulatorState,
    biState: PowerBiSimulatorState,
    onBack: () -> Unit,
    onToggleBookmark: () -> Unit,
    onUpdateSimulatorAge: (Float) -> Unit,
    onUpdateSimulatorBp: (Float) -> Unit,
    onUpdateSimulatorGlucose: (Float) -> Unit,
    onUpdateSimulatorBmi: (Float) -> Unit,
    onUpdateSimulatorCholesterol: (Float) -> Unit,
    onUpdateBiStage: (String) -> Unit,
    onUpdateBiCohort: (String) -> Unit,
    onContactAboutProject: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("case_study_detail_screen"),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 16.dp,
            bottom = 96.dp
        )
    ) {
        // Navigation & Action Bar
        item {
            ResponsiveContainer {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { onBack() }
                            .padding(vertical = 4.dp)
                    ) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier.testTag("case_study_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back to list",
                                tint = TechCyan
                            )
                        }
                        Text(
                            text = "Back to Projects",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = TechCyan,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    IconButton(
                        onClick = onToggleBookmark,
                        modifier = Modifier.testTag("case_study_bookmark_toggle")
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = if (isBookmarked) "Remove bookmark" else "Bookmark case study",
                            tint = if (isBookmarked) TechAmber else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Title and Header Card
        item {
            ResponsiveContainer {
                GlassCard(
                    borderColor = TechCyan.copy(alpha = 0.4f),
                    backgroundColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            TechBadge(text = caseStudy.category.label)
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                            ) {
                                Text(
                                    text = caseStudy.duration,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = caseStudy.title,
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                        )

                        Text(
                            text = caseStudy.clientOrInstitution,
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = TechCyan,
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )

                        Text(
                            text = caseStudy.tagline,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )
                        )
                    }
                }
            }
        }

        // Executive Summary (Problem & Solution)
        item {
            ResponsiveContainer {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    GlassCard(
                        borderColor = TechRose.copy(alpha = 0.3f),
                        backgroundColor = MaterialTheme.colorScheme.surface
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(TechRose)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "THE PROBLEM & CHALLENGE",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = TechRose,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = caseStudy.problemStatement,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 22.sp
                                )
                            )
                        }
                    }

                    GlassCard(
                        borderColor = TechEmerald.copy(alpha = 0.35f),
                        backgroundColor = MaterialTheme.colorScheme.surface
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(TechEmerald)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "PROPOSED SOLUTION & ARCHITECTURE",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = TechEmerald,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = caseStudy.proposedSolution,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 22.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        // Architecture Pipeline Steps
        item {
            ResponsiveContainer {
                GlassCard(
                    borderColor = MaterialTheme.colorScheme.outline,
                    backgroundColor = MaterialTheme.colorScheme.surface
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.DataObject,
                                contentDescription = null,
                                tint = TechCyan,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Technical Implementation Pipeline",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        caseStudy.architectureComponents.forEachIndexed { index, step ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(TechCyan.copy(alpha = 0.15f))
                                        .border(1.dp, TechCyan.copy(alpha = 0.5f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${index + 1}",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            color = TechCyan,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = step.title,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = step.description,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            lineHeight = 20.sp
                                        ),
                                        modifier = Modifier.padding(vertical = 2.dp)
                                    )
                                    Text(
                                        text = "Tools: ${step.tools}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = TechCyan,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Machine Learning & Model Performance Benchmarks
        item {
            ResponsiveContainer {
                GlassCard(
                    borderColor = MaterialTheme.colorScheme.outline,
                    backgroundColor = MaterialTheme.colorScheme.surface
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = null,
                                tint = TechEmerald,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Model Architectures & Metrics",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        caseStudy.modelsUsed.forEach { model ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = model.modelName,
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                        )
                                        Text(
                                            text = model.metricValue,
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                color = TechEmerald,
                                                fontWeight = FontWeight.ExtraBold
                                            )
                                        )
                                    }
                                    Text(
                                        text = "Task: ${model.task} • Metric: ${model.metricLabel}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = TechCyan,
                                            fontWeight = FontWeight.Medium
                                        ),
                                        modifier = Modifier.padding(vertical = 2.dp)
                                    )
                                    Text(
                                        text = model.detail,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Interactive Live Simulators (Domain Specific!)
        if (caseStudy.id == "pulse_ai") {
            item {
                ResponsiveContainer {
                    PulseAiSimulatorCard(
                        mlState = mlState,
                        onUpdateAge = onUpdateSimulatorAge,
                        onUpdateBp = onUpdateSimulatorBp,
                        onUpdateGlucose = onUpdateSimulatorGlucose,
                        onUpdateBmi = onUpdateSimulatorBmi,
                        onUpdateCholesterol = onUpdateSimulatorCholesterol
                    )
                }
            }
        } else if (caseStudy.id == "lung_cancer_bi") {
            item {
                ResponsiveContainer {
                    LungCancerBiSimulatorCard(
                        biState = biState,
                        onUpdateStage = onUpdateBiStage,
                        onUpdateCohort = onUpdateBiCohort
                    )
                }
            }
        }

        // Key Results and Impact
        item {
            ResponsiveContainer {
                GlassCard(
                    borderColor = TechCyan.copy(alpha = 0.35f),
                    backgroundColor = MaterialTheme.colorScheme.surface
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = TechCyan,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Quantifiable Results & Deliverables",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        caseStudy.resultsAndImpact.forEach { result ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 5.dp)
                            ) {
                                Text(
                                    text = "▹",
                                    color = TechCyan,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Text(
                                    text = result,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = 21.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // Key Insights & Engineering Learnings
        item {
            ResponsiveContainer {
                GlassCard(
                    borderColor = TechAmber.copy(alpha = 0.35f),
                    backgroundColor = MaterialTheme.colorScheme.surface
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = TechAmber,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Key Insights & Takeaways",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        caseStudy.keyInsights.forEach { insight ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 5.dp)
                            ) {
                                Text(
                                    text = "💡",
                                    modifier = Modifier.padding(end = 8.dp),
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = insight,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 21.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // Tech Stack Tags
        item {
            ResponsiveContainer {
                Column {
                    Text(
                        text = "Technologies & Tools Used",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        caseStudy.techStack.forEach { tech ->
                            TechBadge(text = tech)
                        }
                    }
                }
            }
        }

        // Contact CTA for this project
        item {
            ResponsiveContainer {
                Button(
                    onClick = { onContactAboutProject(caseStudy.title) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TechCyan,
                        contentColor = Color(0xFF090D16)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("inquire_about_case_study_btn")
                ) {
                    Text(
                        text = "Inquire or Collaborate on This Project",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

// Interactive Multi-Disease Risk Simulator for Pulse-AI
@Composable
private fun PulseAiSimulatorCard(
    mlState: MlSimulatorState,
    onUpdateAge: (Float) -> Unit,
    onUpdateBp: (Float) -> Unit,
    onUpdateGlucose: (Float) -> Unit,
    onUpdateBmi: (Float) -> Unit,
    onUpdateCholesterol: (Float) -> Unit
) {
    GlassCard(
        borderColor = TechCyan,
        backgroundColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = null,
                        tint = TechCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Live ML Risk Inference Simulator",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = TechEmerald.copy(alpha = 0.2f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TechEmerald)
                ) {
                    Text(
                        text = "Active Inference",
                        color = TechEmerald,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            Text(
                text = "Adjust telemetry sliders below to simulate live feature inputs to the 4 trained classification models in Pulse-AI.",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.padding(vertical = 6.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Sliders
            SliderControl(
                label = "Patient Age",
                value = mlState.age,
                valueDisplay = "${mlState.age.toInt()} yrs",
                range = 20f..85f,
                onValueChange = onUpdateAge
            )

            SliderControl(
                label = "Systolic Blood Pressure",
                value = mlState.systolicBp,
                valueDisplay = "${mlState.systolicBp.toInt()} mmHg",
                range = 90f..190f,
                onValueChange = onUpdateBp
            )

            SliderControl(
                label = "Fasting Blood Glucose",
                value = mlState.fastingGlucose,
                valueDisplay = "${mlState.fastingGlucose.toInt()} mg/dL",
                range = 70f..220f,
                onValueChange = onUpdateGlucose
            )

            SliderControl(
                label = "Body Mass Index (BMI)",
                value = mlState.bmi,
                valueDisplay = String.format("%.1f", mlState.bmi),
                range = 18f..42f,
                onValueChange = onUpdateBmi
            )

            SliderControl(
                label = "Serum Cholesterol",
                value = mlState.cholesterol,
                valueDisplay = "${mlState.cholesterol.toInt()} mg/dL",
                range = 140f..320f,
                onValueChange = onUpdateCholesterol
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Model Prediction Output Grid
            Text(
                text = "Multi-Model Classification Probabilities",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RiskMetricBox(
                    label = "Heart Risk",
                    score = mlState.heartRisk,
                    modifier = Modifier.weight(1f)
                )
                RiskMetricBox(
                    label = "Diabetes",
                    score = mlState.diabetesRisk,
                    modifier = Modifier.weight(1f)
                )
                RiskMetricBox(
                    label = "Stroke Risk",
                    score = mlState.strokeRisk,
                    modifier = Modifier.weight(1f)
                )
                RiskMetricBox(
                    label = "Liver Disease",
                    score = mlState.liverRisk,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Primary Tier Output
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, TechCyan.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "DIAGNOSTIC STATUS:",
                            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                        Text(
                            text = "Confidence: ${String.format("%.1f", mlState.confidenceScore)}%",
                            style = MaterialTheme.typography.labelSmall.copy(color = TechEmerald, fontWeight = FontWeight.Bold)
                        )
                    }
                    Text(
                        text = mlState.primaryRiskTier,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = TechCyan,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SliderControl(
    label: String,
    value: Float,
    valueDisplay: String,
    range: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
            )
            Text(
                text = valueDisplay,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TechCyan
                )
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            colors = SliderDefaults.colors(
                thumbColor = TechCyan,
                activeTrackColor = TechCyan,
                inactiveTrackColor = MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier.height(28.dp)
        )
    }
}

@Composable
private fun RiskMetricBox(
    label: String,
    score: Float,
    modifier: Modifier = Modifier
) {
    val color = when {
        score < 35f -> TechEmerald
        score < 65f -> TechAmber
        else -> TechRose
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                maxLines = 1
            )
            Text(
                text = "${score.toInt()}%",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = color
                ),
                modifier = Modifier.padding(vertical = 2.dp)
            )
            LinearProgressIndicator(
                progress = { (score / 100f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = color,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}

// Interactive Power BI Simulation Card for Lung Cancer Case Study
@Composable
private fun LungCancerBiSimulatorCard(
    biState: PowerBiSimulatorState,
    onUpdateStage: (String) -> Unit,
    onUpdateCohort: (String) -> Unit
) {
    GlassCard(
        borderColor = TechAmber,
        backgroundColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Power BI Interactive Analytics Simulator",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = TechAmber.copy(alpha = 0.2f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TechAmber)
                ) {
                    Text(
                        text = "DAX Measures",
                        color = TechAmber,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Filter by Clinical Stage:",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("All Stages", "Stage I", "Stage II", "Stage III", "Stage IV").forEach { stage ->
                    val isSelected = biState.selectedStageFilter == stage
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onUpdateStage(stage) },
                        shape = RoundedCornerShape(6.dp),
                        color = if (isSelected) TechAmber else MaterialTheme.colorScheme.surface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) TechAmber else MaterialTheme.colorScheme.outline)
                    ) {
                        Text(
                            text = stage,
                            color = if (isSelected) Color(0xFF090D16) else MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 10.sp
                            ),
                            modifier = Modifier.padding(vertical = 6.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Filter by Exposure Cohort:",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("All Cohorts", "Smoker", "Passive", "Non-Smoker").forEach { cohort ->
                    val isSelected = biState.selectedCohort == cohort
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onUpdateCohort(cohort) },
                        shape = RoundedCornerShape(6.dp),
                        color = if (isSelected) TechCyan else MaterialTheme.colorScheme.surface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) TechCyan else MaterialTheme.colorScheme.outline)
                    ) {
                        Text(
                            text = cohort,
                            color = if (isSelected) Color(0xFF090D16) else MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 10.sp
                            ),
                            modifier = Modifier.padding(vertical = 6.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Calculated Output KPIs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricChip(
                    label = "Survival Projection",
                    value = "${String.format("%.1f", biState.survivalRate)}%",
                    modifier = Modifier.weight(1f),
                    highlightColor = if (biState.survivalRate > 50f) TechEmerald else TechRose
                )
                MetricChip(
                    label = "Avg Diagnosis Age",
                    value = "${String.format("%.1f", biState.averageOnsetAge)} yrs",
                    modifier = Modifier.weight(1f),
                    highlightColor = TechCyan
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Predominant Symptom Cluster: ",
                        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    Text(
                        text = biState.highRiskSymptom,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TechAmber
                        )
                    )
                }
            }
        }
    }
}
