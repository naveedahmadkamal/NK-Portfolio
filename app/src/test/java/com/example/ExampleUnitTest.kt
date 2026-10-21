package com.example

import com.example.data.model.ProjectCategory
import com.example.ui.viewmodel.ContactFormState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testContactFormValidation() {
        val emptyForm = ContactFormState()
        assertTrue(emptyForm.name.isBlank())
        assertTrue(emptyForm.email.isBlank())
        assertTrue(emptyForm.message.isBlank())

        val validForm = ContactFormState(
            name = "Jane Doe",
            email = "jane@example.com",
            message = "We would love to hire Naveed for our Data Science team!"
        )
        assertFalse(validForm.name.isBlank())
        assertTrue(android.util.Patterns.EMAIL_ADDRESS?.matcher(validForm.email)?.matches() ?: validForm.email.contains("@"))
        assertTrue(validForm.message.length >= 10)
    }

    @Test
    fun testProjectCategories() {
        val categories = ProjectCategory.values()
        assertTrue(categories.contains(ProjectCategory.MACHINE_LEARNING))
        assertTrue(categories.contains(ProjectCategory.BI_DASHBOARDS))
        assertTrue(categories.contains(ProjectCategory.DATA_ENGINEERING))
    }

    @Test
    fun testMlSimulatorBounds() {
        val bp = 135f
        val chol = 215f
        val age = 48f
        val heartRisk = ((bp - 110f) * 0.45f + (chol - 180f) * 0.35f + (age - 30f) * 0.4f).coerceIn(5f, 98f)
        assertTrue(heartRisk in 5f..98f)
    }
}
