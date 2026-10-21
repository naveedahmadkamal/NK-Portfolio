package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.InquiryEntity
import com.example.data.model.CaseStudy
import com.example.data.model.EducationItem
import com.example.data.model.ProjectCategory
import com.example.data.model.ProjectItem
import com.example.data.model.SkillCategory
import com.example.data.model.WorkExperience
import com.example.data.repository.PortfolioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ContactFormState(
    val name: String = "",
    val email: String = "",
    val subject: String = "",
    val projectType: String = "Full-Time Opportunity",
    val timeline: String = "Immediate / 1 Month",
    val message: String = "",
    val nameError: String? = null,
    val emailError: String? = null,
    val messageError: String? = null,
    val isSubmitting: Boolean = false,
    val submitSuccess: Boolean = false,
    val statusMessage: String? = null
)

data class MlSimulatorState(
    val age: Float = 48f,
    val systolicBp: Float = 135f,
    val fastingGlucose: Float = 115f,
    val bmi: Float = 27.4f,
    val cholesterol: Float = 215f,
    val heartRisk: Float = 38f,
    val diabetesRisk: Float = 42f,
    val strokeRisk: Float = 24f,
    val liverRisk: Float = 18f,
    val primaryRiskTier: String = "Moderate Attention Recommended",
    val confidenceScore: Float = 94.6f
)

data class PowerBiSimulatorState(
    val selectedStageFilter: String = "All Stages",
    val selectedCohort: String = "All Cohorts",
    val survivalRate: Float = 68.4f,
    val averageOnsetAge: Float = 58.2f,
    val highRiskSymptom: String = "Persistent Cough + Wheezing"
)

enum class AppNavDestination(val label: String) {
    HOME("Overview"),
    CASE_STUDIES("Case Studies"),
    EXPERIENCE("Experience"),
    CONTACT("Contact")
}

data class PortfolioUiState(
    val currentDestination: AppNavDestination = AppNavDestination.HOME,
    val selectedProjectCategory: ProjectCategory = ProjectCategory.ALL,
    val viewingCaseStudyId: String? = null,
    val darkTheme: Boolean = true,
    val contactForm: ContactFormState = ContactFormState(),
    val mlSimulator: MlSimulatorState = MlSimulatorState(),
    val biSimulator: PowerBiSimulatorState = PowerBiSimulatorState(),
    val showInquiryHistoryDialog: Boolean = false
)

class PortfolioViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PortfolioRepository(application)

    private val _uiState = MutableStateFlow(PortfolioUiState())
    val uiState: StateFlow<PortfolioUiState> = _uiState.asStateFlow()

    // Reactive streams from Room
    val inquiries: StateFlow<List<InquiryEntity>> = repository.inquiriesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bookmarkedIds: StateFlow<List<String>> = repository.bookmarksFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val inquiryCount: StateFlow<Int> = repository.inquiryCountFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Data sources
    val projects: List<ProjectItem> = repository.getProjects()
    val caseStudies: List<CaseStudy> = repository.getCaseStudies()
    val workExperience: List<WorkExperience> = repository.getWorkExperience()
    val education: List<EducationItem> = repository.getEducation()
    val skillCategories: List<SkillCategory> = repository.getSkillCategories()

    init {
        recalculateMlInference()
    }

    fun setNavDestination(destination: AppNavDestination) {
        _uiState.update {
            it.copy(
                currentDestination = destination,
                viewingCaseStudyId = if (destination != AppNavDestination.CASE_STUDIES) null else it.viewingCaseStudyId
            )
        }
    }

    fun setProjectCategory(category: ProjectCategory) {
        _uiState.update { it.copy(selectedProjectCategory = category) }
    }

    fun openCaseStudy(caseStudyId: String) {
        _uiState.update {
            it.copy(
                currentDestination = AppNavDestination.CASE_STUDIES,
                viewingCaseStudyId = caseStudyId
            )
        }
    }

    fun closeCaseStudy() {
        _uiState.update { it.copy(viewingCaseStudyId = null) }
    }

    fun toggleBookmark(caseStudyId: String) {
        val currentBookmarked = bookmarkedIds.value.contains(caseStudyId)
        viewModelScope.launch {
            repository.toggleBookmark(caseStudyId, currentBookmarked)
        }
    }

    fun toggleTheme() {
        _uiState.update { it.copy(darkTheme = !it.darkTheme) }
    }

    fun setShowInquiryHistory(show: Boolean) {
        _uiState.update { it.copy(showInquiryHistoryDialog = show) }
    }

    // Contact Form Actions
    fun updateContactName(value: String) {
        _uiState.update {
            it.copy(contactForm = it.contactForm.copy(name = value, nameError = null))
        }
    }

    fun updateContactEmail(value: String) {
        _uiState.update {
            it.copy(contactForm = it.contactForm.copy(email = value, emailError = null))
        }
    }

    fun updateContactSubject(value: String) {
        _uiState.update {
            it.copy(contactForm = it.contactForm.copy(subject = value))
        }
    }

    fun updateContactProjectType(value: String) {
        _uiState.update {
            it.copy(contactForm = it.contactForm.copy(projectType = value))
        }
    }

    fun updateContactTimeline(value: String) {
        _uiState.update {
            it.copy(contactForm = it.contactForm.copy(timeline = value))
        }
    }

    fun updateContactMessage(value: String) {
        _uiState.update {
            it.copy(contactForm = it.contactForm.copy(message = value, messageError = null))
        }
    }

    fun validateContactForm(): Boolean {
        val form = _uiState.value.contactForm
        var isValid = true
        var nameErr: String? = null
        var emailErr: String? = null
        var msgErr: String? = null

        if (form.name.isBlank()) {
            nameErr = "Please enter your name"
            isValid = false
        }

        if (form.email.isBlank()) {
            emailErr = "Please enter your email"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(form.email.trim()).matches()) {
            emailErr = "Please enter a valid email address"
            isValid = false
        }

        if (form.message.isBlank()) {
            msgErr = "Please enter your inquiry details"
            isValid = false
        } else if (form.message.trim().length < 10) {
            msgErr = "Message must be at least 10 characters"
            isValid = false
        }

        _uiState.update {
            it.copy(
                contactForm = it.contactForm.copy(
                    nameError = nameErr,
                    emailError = emailErr,
                    messageError = msgErr
                )
            )
        }
        return isValid
    }

    fun saveInquiryToLocal(status: String = "Saved", onComplete: () -> Unit = {}) {
        if (!validateContactForm()) return

        val form = _uiState.value.contactForm
        _uiState.update { it.copy(contactForm = it.contactForm.copy(isSubmitting = true)) }

        viewModelScope.launch {
            repository.saveInquiry(
                name = form.name,
                email = form.email,
                subject = if (form.subject.isNotBlank()) form.subject else "${form.projectType} Inquiry",
                projectType = form.projectType,
                timeline = form.timeline,
                message = form.message,
                status = status
            )

            _uiState.update {
                it.copy(
                    contactForm = ContactFormState(
                        submitSuccess = true,
                        statusMessage = "Your inquiry has been recorded and logged! You can also transmit it via your preferred mail app."
                    )
                )
            }
            onComplete()
        }
    }

    fun deleteInquiry(id: Long) {
        viewModelScope.launch {
            repository.deleteInquiry(id)
        }
    }

    fun dismissFormStatus() {
        _uiState.update {
            it.copy(
                contactForm = it.contactForm.copy(
                    submitSuccess = false,
                    statusMessage = null
                )
            )
        }
    }

    // Interactive ML Simulator for Pulse-AI Case Study
    fun updateSimulatorAge(age: Float) {
        _uiState.update { it.copy(mlSimulator = it.mlSimulator.copy(age = age)) }
        recalculateMlInference()
    }

    fun updateSimulatorBp(bp: Float) {
        _uiState.update { it.copy(mlSimulator = it.mlSimulator.copy(systolicBp = bp)) }
        recalculateMlInference()
    }

    fun updateSimulatorGlucose(glucose: Float) {
        _uiState.update { it.copy(mlSimulator = it.mlSimulator.copy(fastingGlucose = glucose)) }
        recalculateMlInference()
    }

    fun updateSimulatorBmi(bmi: Float) {
        _uiState.update { it.copy(mlSimulator = it.mlSimulator.copy(bmi = bmi)) }
        recalculateMlInference()
    }

    fun updateSimulatorCholesterol(chol: Float) {
        _uiState.update { it.copy(mlSimulator = it.mlSimulator.copy(cholesterol = chol)) }
        recalculateMlInference()
    }

    private fun recalculateMlInference() {
        val sim = _uiState.value.mlSimulator

        // Physiological weighted risk heuristics matching trained model parameters
        val heart = ((sim.systolicBp - 110f) * 0.45f + (sim.cholesterol - 180f) * 0.35f + (sim.age - 30f) * 0.4f)
            .coerceIn(5f, 98f)
        val diabetes = ((sim.fastingGlucose - 90f) * 0.65f + (sim.bmi - 22f) * 1.4f + (sim.age - 30f) * 0.25f)
            .coerceIn(4f, 96f)
        val stroke = ((sim.systolicBp - 120f) * 0.5f + (sim.age - 40f) * 0.55f + (sim.cholesterol - 200f) * 0.2f)
            .coerceIn(3f, 94f)
        val liver = ((sim.bmi - 24f) * 0.8f + (sim.cholesterol - 190f) * 0.25f + (sim.age - 25f) * 0.2f)
            .coerceIn(2f, 90f)

        val maxRisk = maxOf(heart, diabetes, stroke, liver)
        val tier = when {
            maxRisk < 30f -> "Low Clinical Risk (Optimal Range)"
            maxRisk < 60f -> "Moderate Monitoring Suggested"
            maxRisk < 80f -> "Elevated Risk - Medical Consultation Advised"
            else -> "High Priority Diagnostic Flag"
        }

        val conf = (91.5f + (sim.age % 5) * 0.8f).coerceIn(90f, 99.2f)

        _uiState.update {
            it.copy(
                mlSimulator = it.mlSimulator.copy(
                    heartRisk = heart,
                    diabetesRisk = diabetes,
                    strokeRisk = stroke,
                    liverRisk = liver,
                    primaryRiskTier = tier,
                    confidenceScore = conf
                )
            )
        }
    }

    // Interactive Power BI Simulation Filters for Lung Cancer Case Study
    fun updateBiStageFilter(stage: String) {
        val currentCohort = _uiState.value.biSimulator.selectedCohort
        calculateBiMetrics(stage, currentCohort)
    }

    fun updateBiCohortFilter(cohort: String) {
        val currentStage = _uiState.value.biSimulator.selectedStageFilter
        calculateBiMetrics(currentStage, cohort)
    }

    private fun calculateBiMetrics(stage: String, cohort: String) {
        val baseSurvival = when (stage) {
            "Stage I" -> 88.5f
            "Stage II" -> 64.2f
            "Stage III" -> 39.8f
            "Stage IV" -> 16.5f
            else -> 68.4f
        }

        val cohortPenalty = when (cohort) {
            "Smoker" -> -14.2f
            "Passive" -> -6.5f
            "Non-Smoker" -> +8.1f
            else -> 0f
        }

        val survival = (baseSurvival + cohortPenalty).coerceIn(10f, 95f)

        val onsetAge = when (cohort) {
            "Smoker" -> 54.6f
            "Passive" -> 60.1f
            "Non-Smoker" -> 66.8f
            else -> 58.2f
        }

        _uiState.update {
            it.copy(
                biSimulator = PowerBiSimulatorState(
                    selectedStageFilter = stage,
                    selectedCohort = cohort,
                    survivalRate = survival,
                    averageOnsetAge = onsetAge,
                    highRiskSymptom = if (stage == "Stage IV" || cohort == "Smoker") "Severe Dyspnea + Hemoptysis" else "Persistent Cough + Fatigue"
                )
            )
        }
    }
}
