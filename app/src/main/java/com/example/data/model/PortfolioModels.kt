package com.example.data.model

data class ProjectItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: ProjectCategory,
    val period: String,
    val institutionOrCompany: String,
    val summary: String,
    val keyMetrics: List<Pair<String, String>>,
    val techStack: List<String>,
    val highlights: List<String>,
    val imageResName: String? = null
)

enum class ProjectCategory(val label: String) {
    ALL("All"),
    MACHINE_LEARNING("Machine Learning"),
    BI_DASHBOARDS("BI & Analytics"),
    DATA_ENGINEERING("Data Engineering")
}

data class CaseStudy(
    val id: String,
    val title: String,
    val tagline: String,
    val category: ProjectCategory,
    val duration: String,
    val clientOrInstitution: String,
    val problemStatement: String,
    val proposedSolution: String,
    val architectureComponents: List<ArchitectureStep>,
    val modelsUsed: List<ModelPerformance>,
    val resultsAndImpact: List<String>,
    val keyInsights: List<String>,
    val techStack: List<String>
)

data class ArchitectureStep(
    val title: String,
    val description: String,
    val tools: String
)

data class ModelPerformance(
    val modelName: String,
    val task: String,
    val metricLabel: String,
    val metricValue: String,
    val detail: String
)

data class WorkExperience(
    val role: String,
    val organization: String,
    val location: String,
    val period: String,
    val bulletPoints: List<String>,
    val skillsUsed: List<String>
)

data class EducationItem(
    val degree: String,
    val institution: String,
    val grade: String,
    val period: String,
    val description: String
)

data class SkillCategory(
    val name: String,
    val iconEmoji: String,
    val skills: List<SkillItem>
)

data class SkillItem(
    val name: String,
    val proficiency: Int, // 0 - 100
    val level: String
)
