package com.example.data.repository

import android.content.Context
import androidx.room.Room
import com.example.data.local.AppDatabase
import com.example.data.local.BookmarkEntity
import com.example.data.local.InquiryEntity
import com.example.data.model.ArchitectureStep
import com.example.data.model.CaseStudy
import com.example.data.model.EducationItem
import com.example.data.model.ModelPerformance
import com.example.data.model.ProjectCategory
import com.example.data.model.ProjectItem
import com.example.data.model.SkillCategory
import com.example.data.model.SkillItem
import com.example.data.model.WorkExperience
import kotlinx.coroutines.flow.Flow

class PortfolioRepository(context: Context) {

    private val db: AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "nk_portfolio.db"
    ).fallbackToDestructiveMigration().build()

    private val inquiryDao = db.inquiryDao()
    private val bookmarkDao = db.bookmarkDao()

    // Room Flow APIs
    val inquiriesFlow: Flow<List<InquiryEntity>> = inquiryDao.getAllInquiries()
    val bookmarksFlow: Flow<List<String>> = bookmarkDao.getAllBookmarks()
    val inquiryCountFlow: Flow<Int> = inquiryDao.getInquiryCount()

    suspend fun saveInquiry(
        name: String,
        email: String,
        subject: String,
        projectType: String,
        timeline: String,
        message: String,
        status: String = "Saved"
    ): Long {
        return inquiryDao.insertInquiry(
            InquiryEntity(
                name = name.trim(),
                email = email.trim(),
                subject = subject.trim(),
                projectType = projectType,
                timeline = timeline,
                message = message.trim(),
                status = status
            )
        )
    }

    suspend fun deleteInquiry(id: Long) {
        inquiryDao.deleteInquiry(id)
    }

    suspend fun toggleBookmark(caseStudyId: String, isBookmarked: Boolean) {
        if (isBookmarked) {
            bookmarkDao.removeBookmark(caseStudyId)
        } else {
            bookmarkDao.addBookmark(BookmarkEntity(caseStudyId))
        }
    }

    // Projects data
    fun getProjects(): List<ProjectItem> = listOf(
        ProjectItem(
            id = "pulse_ai",
            title = "Pulse-AI — Multi-Disease Healthcare Diagnostics",
            subtitle = "Full-stack AI healthcare platform with 4 ML classification models",
            category = ProjectCategory.MACHINE_LEARNING,
            period = "01/2026 – 06/2026",
            institutionOrCompany = "GCUF (Final Year Project)",
            summary = "Comprehensive web & AI medical diagnostics system analyzing multidimensional patient telemetry to detect early risks for Heart Disease, Diabetes, Stroke, and Liver Disease.",
            keyMetrics = listOf(
                "Accuracy" to "98.4%",
                "Models" to "4 Ensembles",
                "Latency" to "<120ms",
                "Dataset" to "12,000+ Records"
            ),
            techStack = listOf("Python", "Scikit-Learn", "Flask", "TensorFlow", "Pandas", "NumPy"),
            highlights = listOf(
                "Trained 4 distinct ML classifiers with cross-validated hyperparameter tuning",
                "Integrated automated data preprocessing with outlier removal & feature scaling",
                "Constructed high-speed RESTful inference endpoints with real-time risk indicators",
                "Designed clinical interpretability reports highlighting primary risk contributors"
            ),
            imageResName = "img_pulse_ai"
        ),
        ProjectItem(
            id = "lung_cancer_bi",
            title = "Lung Cancer Power BI & Epidemiological Analytics",
            subtitle = "Interactive dynamic analytics dashboard evaluating risk patterns",
            category = ProjectCategory.BI_DASHBOARDS,
            period = "01/2024 – 05/2024",
            institutionOrCompany = "GCUF Research & Analytics",
            summary = "Designed and deployed dynamic Power BI executive dashboards uncovering correlations between environmental exposures, genetic markers, smoking history, and patient survival rates.",
            keyMetrics = listOf(
                "DAX Measures" to "35+",
                "Patient Records" to "3,500+",
                "Filter Latency" to "Instant",
                "Key Insights" to "12 Published"
            ),
            techStack = listOf("Power BI", "DAX", "Python", "Seaborn", "Data Modeling", "SQL"),
            highlights = listOf(
                "Built dimensional star-schema data models optimizing multi-source medical tables",
                "Authored complex DAX calculations for survival progression and stage severity index",
                "Formulated dynamic tooltips and cross-filtering visual drill-downs for oncology clinicians",
                "Synthesized clinical insights into structured summaries for rapid decision-making"
            ),
            imageResName = "img_cancer_bi"
        ),
        ProjectItem(
            id = "softicera_pipeline",
            title = "Enterprise SQL & Predictive Modeling Pipeline",
            subtitle = "Data extraction, feature engineering, and model evaluation pipeline",
            category = ProjectCategory.DATA_ENGINEERING,
            period = "03/2025 – 06/2025",
            institutionOrCompany = "SofticEra Institute & Software House",
            summary = "End-to-end data science lifecycle implementation cleaning high-throughput operational data, performing SQL extractions, building classification pipelines, and delivering BI dashboards.",
            keyMetrics = listOf(
                "Data Cleaned" to "500k+ Rows",
                "Query Speedup" to "42%",
                "Dashboards" to "6 Deployed",
                "F1 Score" to "0.93"
            ),
            techStack = listOf("SQL", "Python", "Pandas", "Scikit-Learn", "Power BI", "SciPy"),
            highlights = listOf(
                "Automated missing value imputations and categorical encoding across complex schemas",
                "Formulated indexed SQL queries reducing extract execution duration by 42%",
                "Evaluated Random Forest vs Gradient Boosting models with confusion matrices & ROC curves",
                "Collaborated with senior software architects in sprint reporting and data governance"
            )
        ),
        ProjectItem(
            id = "customer_churn_ai",
            title = "Customer Churn & Behavioral Analytics System",
            subtitle = "Predictive retention analytics engine with risk scoring",
            category = ProjectCategory.MACHINE_LEARNING,
            period = "2024 – 2025",
            institutionOrCompany = "Independent Applied ML Project",
            summary = "Advanced supervised learning system classifying subscriber churn propensity and recommending proactive retention incentives using feature importance attribution.",
            keyMetrics = listOf(
                "AUC-ROC" to "0.95",
                "Precision" to "91.2%",
                "Retention Boost" to "+18%",
                "Inference" to "Batch & Real-time"
            ),
            techStack = listOf("Python", "XGBoost", "Scikit-Learn", "Matplotlib", "Joblib"),
            highlights = listOf(
                "Implemented SMOTE balancing to mitigate severe class imbalance",
                "Identified key tenure and service interaction drivers through SHAP value analysis",
                "Created risk decile segmentation enabling targeted marketing campaigns"
            )
        )
    )

    fun getCaseStudies(): List<CaseStudy> = listOf(
        CaseStudy(
            id = "pulse_ai",
            title = "Pulse-AI: Full-Stack Multi-Disease Diagnostic System",
            tagline = "Empowering clinicians with real-time multi-disease risk assessments through integrated machine learning.",
            category = ProjectCategory.MACHINE_LEARNING,
            duration = "6 Months (01/2026 – 06/2026)",
            clientOrInstitution = "Government College University Faisalabad (GCUF)",
            problemStatement = "Early disease detection in rural and under-resourced healthcare facilities is hindered by delayed lab analyses and complex multifactorial patient parameters. Diagnosing cardiovascular risks, diabetes progression, stroke likelihood, and liver impairments often requires separate visits and manual review, leading to missed intervention windows.",
            proposedSolution = "Pulse-AI was conceived as a unified web-based predictive engine. By taking standard non-invasive physiological readings (blood pressure, fasting glucose, BMI, age, enzyme levels), the platform executes 4 parallel trained ML classifiers and renders instantaneous probabilistic risk profiles with clinical explainability.",
            architectureComponents = listOf(
                ArchitectureStep(
                    title = "Data Ingestion & Standardized Preprocessing",
                    description = "Automated pipeline validating telemetry boundaries, imputing null values via KNNImputer, and scaling continuous variables using StandardScaler.",
                    tools = "Pandas, NumPy, Scikit-Learn"
                ),
                ArchitectureStep(
                    title = "Ensemble ML Classification Layer",
                    description = "Specialized classification models per pathology: Random Forest for Heart Disease, Gradient Boosting for Diabetes, SMOTE + Logistic Regression for Stroke, and Multi-Layer Perceptrons for Liver Disease.",
                    tools = "Scikit-Learn, TensorFlow/Keras, Joblib"
                ),
                ArchitectureStep(
                    title = "API Gateway & Inference Service",
                    description = "Lightweight REST API orchestrating model inputs, applying calibration curves, and outputting JSON risk scores and confidence intervals in <120ms.",
                    tools = "Flask, Python, Gunicorn"
                ),
                ArchitectureStep(
                    title = "Interactive Clinical Visualization",
                    description = "Front-end dashboard with dynamic gauges, risk tiers (Low, Moderate, High), and actionable health lifestyle interventions.",
                    tools = "JavaScript, Chart.js, HTML5/CSS3"
                )
            ),
            modelsUsed = listOf(
                ModelPerformance(
                    modelName = "Heart Disease Classifier",
                    task = "Binary Risk Prediction",
                    metricLabel = "Accuracy / Recall",
                    metricValue = "98.4% / 97.9%",
                    detail = "Random Forest (150 estimators, max depth 12) trained on Cleveland & Framingham cohorts."
                ),
                ModelPerformance(
                    modelName = "Diabetes Risk Predictor",
                    task = "Insulin & Glucose Profiling",
                    metricLabel = "F1-Score / Accuracy",
                    metricValue = "0.96 / 95.8%",
                    detail = "Gradient Boosting with engineered BMI-to-age interaction features."
                ),
                ModelPerformance(
                    modelName = "Stroke Probability Model",
                    task = "Imbalanced Event Detection",
                    metricLabel = "AUC-ROC",
                    metricValue = "0.946",
                    detail = "SMOTE oversampling + Regularized Logistic Regression with threshold tuning."
                ),
                ModelPerformance(
                    modelName = "Liver Disease Classifier",
                    task = "Hepatic Enzyme Biomarker Analysis",
                    metricLabel = "Accuracy",
                    metricValue = "96.2%",
                    detail = "Artificial Neural Network (Dense 64 -> 32 -> 1 with Dropout 0.3)."
                )
            ),
            resultsAndImpact = listOf(
                "Successfully classified multi-pathology cases with cross-validated mean accuracy exceeding 96.5%",
                "Reduced preliminary triage assessment preparation time from 45 minutes to under 2 minutes",
                "Engineered a zero-dependency local scoring module allowing offline deployment in clinics",
                "Received First Division distinction by the GCUF Faculty of Computer Science & Data Science"
            ),
            keyInsights = listOf(
                "Systolic BP combined with age and cholesterol ratio presented the strongest predictive weight in cardiac models",
                "Feature scaling was pivotal for neural network convergence across divergent liver enzyme ranges",
                "Explainable AI metrics (feature contributions) significantly increased clinician trust during trial tests"
            ),
            techStack = listOf("Python", "Scikit-Learn", "TensorFlow", "Pandas", "Flask", "NumPy", "Matplotlib")
        ),
        CaseStudy(
            id = "lung_cancer_bi",
            title = "Lung Cancer Epidemiological & Clinical BI Dashboard",
            tagline = "Transforming complex multi-variable clinical records into intuitive Power BI decision matrices.",
            category = ProjectCategory.BI_DASHBOARDS,
            duration = "5 Months (01/2024 – 05/2024)",
            clientOrInstitution = "GCUF Department of Statistics & Data Science",
            problemStatement = "Oncology researchers and health departments struggled with fragmented spreadsheets tracking patient lifestyle factors (smoking duration, occupational hazards, chronic coughing, chest pain) and staging outcomes. Lack of cross-filtering made identifying demographic risk intersections cumbersome.",
            proposedSolution = "Constructed an enterprise-grade interactive Power BI analytics suite featuring automated ETL, star-schema data structures, over 35 customized DAX metrics, and drill-through visual dashboards for clinical researchers.",
            architectureComponents = listOf(
                ArchitectureStep(
                    title = "Data Cleansing & Normalization",
                    description = "Python and Power Query M scripts to scrub raw patient logs, resolve discrepancies, and harmonize symptom intensity scores.",
                    tools = "Python, Power Query, Pandas"
                ),
                ArchitectureStep(
                    title = "Relational Data Modeling",
                    description = "Engineered fact and dimension tables (Dim_Patient, Dim_Symptoms, Dim_Demographics, Fact_Diagnosis) with 1-to-many relationship cardinality.",
                    tools = "Power BI Desktop, Star Schema"
                ),
                ArchitectureStep(
                    title = "Advanced DAX Metric Engineering",
                    description = "Calculated cumulative survival rates, stage progression velocity, and smoking intensity coefficient matrices.",
                    tools = "DAX (Data Analysis Expressions)"
                ),
                ArchitectureStep(
                    title = "Dynamic User Interface & Storytelling",
                    description = "Implemented bookmarking, conditional matrix formatting, synchronized slicers, and automated narrative summaries.",
                    tools = "Power BI Visuals, Custom Themes"
                )
            ),
            modelsUsed = listOf(
                ModelPerformance(
                    modelName = "Symptom Severity Index",
                    task = "Composite Risk Weighting",
                    metricLabel = "Correlation Coefficient",
                    metricValue = "r = 0.81",
                    detail = "DAX weighted formula aggregating shortness of breath, wheezing, and coughing frequency."
                ),
                ModelPerformance(
                    modelName = "Stage IV Progression Probability",
                    task = "Survival Statistical Analysis",
                    metricLabel = "Model Confidence",
                    metricValue = "94.1%",
                    detail = "Multivariate logistic analysis identifying high-risk clinical clusters."
                )
            ),
            resultsAndImpact = listOf(
                "Aggregated over 3,500 patient records into a single unified analytical dashboard",
                "Uncovered a 3.4x elevated likelihood of late-stage diagnosis among patients with combined smoking and passive smoke exposure",
                "Enabled oncology staff to filter patient cohorts across 6 dimensions with zero query lag",
                "Featured as a model analytics project in university academic symposiums"
            ),
            keyInsights = listOf(
                "Visual drill-through paths reduced time-to-insight for non-technical medical personnel by 75%",
                "Star schema modeling cut Power BI dataset memory footprint by 38% compared to a flat table",
                "Interactive what-if parameters helped students simulate preventative lifestyle reduction targets"
            ),
            techStack = listOf("Power BI", "DAX", "Python", "Power Query", "Seaborn", "SQL")
        ),
        CaseStudy(
            id = "softicera_pipeline",
            title = "SofticEra: Production Data Science & SQL Optimization",
            tagline = "Designing resilient data cleaning frameworks and operational classification pipelines in a fast-paced software house.",
            category = ProjectCategory.DATA_ENGINEERING,
            duration = "4 Months (03/2025 – 06/2025)",
            clientOrInstitution = "SofticEra Institute & Software House, Faisalabad",
            problemStatement = "Client enterprise systems had disparate relational databases with redundant records, unstructured inputs, and sluggish query execution times that slowed down daily reporting and downstream model training.",
            proposedSolution = "Conducted extensive database audits, authored optimized SQL queries with composite indexing, built automated Python preprocessing scripts, and trained predictive models with comprehensive evaluation benchmarks.",
            architectureComponents = listOf(
                ArchitectureStep(
                    title = "Database Indexing & Query Tuning",
                    description = "Refactored multi-table JOINs, added covering indices, and eliminated unindexed subqueries in production database instances.",
                    tools = "PostgreSQL, MySQL, SQL EXPLAIN"
                ),
                ArchitectureStep(
                    title = "Scalable Python Preprocessing",
                    description = "Built reusable pipelines handling IQR outlier trimming, categorical encoding, and automated schema validation.",
                    tools = "Python, Pandas, NumPy, SciPy"
                ),
                ArchitectureStep(
                    title = "Classification & Benchmark Evaluation",
                    description = "Evaluated multiple algorithms (Logistic Regression, Decision Trees, Random Forests) across cross-validation folds.",
                    tools = "Scikit-Learn, Joblib"
                ),
                ArchitectureStep(
                    title = "Executive Decision Dashboards",
                    description = "Designed interactive Power BI reports providing real-time visibility into weekly business indicators.",
                    tools = "Power BI, SQL DirectQuery"
                )
            ),
            modelsUsed = listOf(
                ModelPerformance(
                    modelName = "Operational Predictive Classifier",
                    task = "Business Event Prediction",
                    metricLabel = "F1-Score / Precision",
                    metricValue = "0.93 / 91.8%",
                    detail = "Random Forest optimized via GridSearchCV with balanced class weighting."
                ),
                ModelPerformance(
                    modelName = "Data Extraction Engine",
                    task = "High-Volume ETL",
                    metricLabel = "Execution Speedup",
                    metricValue = "-42% Latency",
                    detail = "Optimized SQL views and materialized query patterns over 500k+ rows."
                )
            ),
            resultsAndImpact = listOf(
                "Extracted and preprocessed over 500,000 real-world records across client engagements",
                "Accelerated critical daily reporting queries from 18 seconds down to 4.2 seconds",
                "Built and delivered 6 functional Power BI dashboards directly consumed by business stakeholders",
                "Commended by senior engineering management for high documentation rigor and clean code practices"
            ),
            keyInsights = listOf(
                "Early data validation prevents silent bugs that compound exponentially in ML pipelines",
                "Collaborative stakeholder reviews early in dashboard design ensure metrics map directly to business KPIs",
                "Documentation and modular pipeline scripts enabled seamless handover to internal dev teams"
            ),
            techStack = listOf("Python", "SQL", "Pandas", "Scikit-Learn", "Power BI", "SciPy", "Git")
        )
    )

    fun getWorkExperience(): List<WorkExperience> = listOf(
        WorkExperience(
            role = "Data Science Intern",
            organization = "SofticEra Institute & Software House",
            location = "Faisalabad, Pakistan",
            period = "03/2025 – 06/2025",
            bulletPoints = listOf(
                "Cleaned, preprocessed, and analyzed real-world datasets using Python (Pandas, NumPy) to prepare high-quality data for predictive modeling.",
                "Built machine learning models for prediction and classification tasks, performing rigorous hyperparameter tuning and model performance evaluation.",
                "Designed dynamic Power BI visualizations and dashboards to support data-driven decision-making for executive teams.",
                "Performed complex SQL-based data extractions, query optimizations, and supported relational database management tasks.",
                "Collaborated with senior developers and data analysts on agile team projects, gaining end-to-end experience in data reporting and technical documentation."
            ),
            skillsUsed = listOf("Python", "Pandas", "NumPy", "SQL", "Power BI", "Scikit-Learn", "EDA", "Git")
        )
    )

    fun getEducation(): List<EducationItem> = listOf(
        EducationItem(
            degree = "BS Data Science (BSDS)",
            institution = "Government College University Faisalabad (GCUF), Pakistan",
            grade = "First Division",
            period = "2022 – 2026",
            description = "Rigorous 4-year curriculum covering Machine Learning, Deep Learning, Statistical Analysis, Database Systems, Data Mining, Big Data Analytics, and Linear Algebra."
        ),
        EducationItem(
            degree = "FSc Pre-Medical",
            institution = "B.I.S.E Bannu Board, Pakistan",
            grade = "Grade A+",
            period = "2018 – 2020",
            description = "Solid foundation in biological sciences, physics, and analytical problem solving."
        ),
        EducationItem(
            degree = "Matriculation in Science",
            institution = "B.I.S.E Federal Board, Pakistan",
            grade = "Grade A+",
            period = "2016 – 2018",
            description = "Distinction in general science, mathematics, and computer literacy."
        )
    )

    fun getSkillCategories(): List<SkillCategory> = listOf(
        SkillCategory(
            name = "Programming & Data Analysis",
            iconEmoji = "💻",
            skills = listOf(
                SkillItem("Python (Pandas, NumPy, SciPy)", 95, "Advanced"),
                SkillItem("SQL (PostgreSQL, MySQL)", 90, "Advanced"),
                SkillItem("Data Cleaning & Preprocessing", 95, "Expert"),
                SkillItem("Exploratory Data Analysis (EDA)", 92, "Advanced"),
                SkillItem("R (Plotly)", 80, "Proficient"),
                SkillItem("MS Excel (Pivot, Formulas)", 88, "Advanced")
            )
        ),
        SkillCategory(
            name = "Machine Learning & AI",
            iconEmoji = "🤖",
            skills = listOf(
                SkillItem("Scikit-Learn (Classification, Trees)", 94, "Advanced"),
                SkillItem("Predictive Modeling & Regression", 92, "Advanced"),
                SkillItem("Deep Learning (TensorFlow / Keras)", 86, "Proficient"),
                SkillItem("Feature Engineering & Selection", 90, "Advanced"),
                SkillItem("PyTorch (Neural Nets)", 82, "Proficient"),
                SkillItem("Model Evaluation & ROC Curves", 92, "Advanced")
            )
        ),
        SkillCategory(
            name = "BI & Data Visualization",
            iconEmoji = "📊",
            skills = listOf(
                SkillItem("Power BI (DAX, Star Schema)", 92, "Advanced"),
                SkillItem("Matplotlib & Seaborn", 94, "Advanced"),
                SkillItem("Tableau Dashboards", 84, "Proficient"),
                SkillItem("Plotly Interactive Charts", 88, "Advanced"),
                SkillItem("SPSS Statistical Modeling", 80, "Proficient")
            )
        ),
        SkillCategory(
            name = "Statistics & Professional",
            iconEmoji = "🎯",
            skills = listOf(
                SkillItem("Statistical Hypothesis Testing", 90, "Advanced"),
                SkillItem("Analytical Problem Solving", 95, "Expert"),
                SkillItem("Cross-Functional Teamwork", 92, "Advanced"),
                SkillItem("Technical Communication & Reports", 90, "Advanced"),
                SkillItem("Project Management & Agile", 85, "Proficient")
            )
        )
    )
}
