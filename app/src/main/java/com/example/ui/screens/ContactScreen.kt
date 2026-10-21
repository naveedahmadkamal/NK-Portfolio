package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.components.ResponsiveContainer
import com.example.ui.components.SectionHeader
import com.example.ui.theme.TechCyan
import com.example.ui.theme.TechEmerald
import com.example.ui.viewmodel.ContactFormState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ContactScreen(
    formState: ContactFormState,
    inquiryCount: Int,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onSubjectChange: (String) -> Unit,
    onProjectTypeChange: (String) -> Unit,
    onTimelineChange: (String) -> Unit,
    onMessageChange: (String) -> Unit,
    onSaveInquiry: (String, () -> Unit) -> Unit,
    onOpenInquiryHistory: () -> Unit,
    onDismissStatus: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val projectTypes = listOf(
        "Full-Time Opportunity",
        "Machine Learning Project",
        "Power BI Dashboard",
        "Data Engineering / SQL",
        "Consultation"
    )

    val timelines = listOf(
        "Immediate",
        "Within 2 Weeks",
        "1 Month",
        "Flexible"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("contact_screen"),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 16.dp,
            bottom = 96.dp
        )
    ) {
        // Section Header
        item {
            ResponsiveContainer {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SectionHeader(
                        title = "Get In Touch",
                        subtitle = "Let's connect to turn raw data into decisions that matter.",
                        badgeText = "Contact Naveed",
                        modifier = Modifier.weight(1f)
                    )

                    if (inquiryCount > 0) {
                        OutlinedButton(
                            onClick = onOpenInquiryHistory,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, TechCyan.copy(alpha = 0.5f)),
                            modifier = Modifier.testTag("open_inquiry_history_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = null,
                                tint = TechCyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Saved ($inquiryCount)",
                                color = TechCyan,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }

        // Direct Contact Cards
        item {
            ResponsiveContainer {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    ContactMethodCard(
                        icon = Icons.Default.Email,
                        label = "Email Address",
                        value = "naveedahmadkhan804@gmail.com",
                        actionHint = "Tap to compose email",
                        onClick = {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:naveedahmadkhan804@gmail.com")
                            }
                            context.startActivity(Intent.createChooser(intent, "Send Email to Naveed"))
                        }
                    )

                    ContactMethodCard(
                        icon = Icons.Default.Phone,
                        label = "Phone / WhatsApp",
                        value = "+92 332 9050475",
                        actionHint = "Tap to call or message",
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:+923329050475")
                            }
                            context.startActivity(intent)
                        }
                    )

                    ContactMethodCard(
                        icon = Icons.Default.LocationOn,
                        label = "Location",
                        value = "KPK, Pakistan",
                        actionHint = "Open to remote & onsite relocations",
                        onClick = {
                            Toast.makeText(context, "Location: KPK, Pakistan", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }

        // Status Notification Card
        if (formState.submitSuccess && formState.statusMessage != null) {
            item {
                ResponsiveContainer {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = TechEmerald.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, TechEmerald)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = TechEmerald,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Inquiry Saved Successfully",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = TechEmerald,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = formState.statusMessage,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // Contact Form
        item {
            ResponsiveContainer {
                GlassCard(
                    borderColor = TechCyan.copy(alpha = 0.35f),
                    backgroundColor = MaterialTheme.colorScheme.surface
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Send an Inquiry or Project Proposal",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Fill in your requirements below. You can save it to the portfolio database and transmit via your email app.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.padding(top = 2.dp, bottom = 14.dp)
                        )

                        // Name
                        OutlinedTextField(
                            value = formState.name,
                            onValueChange = onNameChange,
                            label = { Text("Your Full Name *") },
                            isError = formState.nameError != null,
                            supportingText = { formState.nameError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("contact_input_name"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = TechCyan,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Email
                        OutlinedTextField(
                            value = formState.email,
                            onValueChange = onEmailChange,
                            label = { Text("Your Email Address *") },
                            isError = formState.emailError != null,
                            supportingText = { formState.emailError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("contact_input_email"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = TechCyan,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Subject
                        OutlinedTextField(
                            value = formState.subject,
                            onValueChange = onSubjectChange,
                            label = { Text("Subject / Project Title") },
                            placeholder = { Text("e.g. Data Scientist Role Discussion") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("contact_input_subject"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = TechCyan,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Project Type Selector
                        Text(
                            text = "Opportunity / Project Category",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(vertical = 6.dp)
                        ) {
                            projectTypes.forEach { type ->
                                val isSelected = formState.projectType == type
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { onProjectTypeChange(type) },
                                    label = { Text(type, fontSize = 12.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = TechCyan.copy(alpha = 0.2f),
                                        selectedLabelColor = TechCyan
                                    ),
                                    border = FilterChipDefaults.filterChipBorder(
                                        enabled = true,
                                        selected = isSelected,
                                        borderColor = MaterialTheme.colorScheme.outline,
                                        selectedBorderColor = TechCyan
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Timeline Selector
                        Text(
                            text = "Expected Timeline",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(vertical = 6.dp)
                        ) {
                            timelines.forEach { time ->
                                val isSelected = formState.timeline == time
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { onTimelineChange(time) },
                                    label = { Text(time, fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = TechEmerald.copy(alpha = 0.2f),
                                        selectedLabelColor = TechEmerald
                                    ),
                                    border = FilterChipDefaults.filterChipBorder(
                                        enabled = true,
                                        selected = isSelected,
                                        borderColor = MaterialTheme.colorScheme.outline,
                                        selectedBorderColor = TechEmerald
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Message Field
                        OutlinedTextField(
                            value = formState.message,
                            onValueChange = onMessageChange,
                            label = { Text("Message & Project Details *") },
                            placeholder = { Text("Describe the project scope, requirements, or role details...") },
                            isError = formState.messageError != null,
                            supportingText = { formState.messageError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                            minLines = 4,
                            maxLines = 8,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("contact_input_message"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = TechCyan,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        // Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    sendViaEmailApp(context, formState)
                                    onSaveInquiry("Sent via Email App") {}
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = TechCyan,
                                    contentColor = Color(0xFF090D16)
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("submit_inquiry_email_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Send via Email", fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = {
                                    onSaveInquiry("Logged in Portfolio") {
                                        Toast.makeText(context, "Inquiry saved to local portfolio!", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, TechEmerald),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("submit_inquiry_save_button")
                            ) {
                                Text("Save Inquiry", color = TechEmerald, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ContactMethodCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    actionHint: String,
    onClick: () -> Unit
) {
    GlassCard(
        borderColor = MaterialTheme.colorScheme.outline,
        backgroundColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .border(1.dp, TechCyan.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = TechCyan,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label.uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 10.sp
                    )
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = actionHint,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TechCyan,
                        fontSize = 11.sp
                    )
                )
            }
        }
    }
}

private fun sendViaEmailApp(context: Context, form: ContactFormState) {
    if (form.name.isBlank() || form.email.isBlank() || form.message.isBlank()) {
        Toast.makeText(context, "Please fill in your name, email, and message first", Toast.LENGTH_SHORT).show()
        return
    }

    val subject = if (form.subject.isNotBlank()) form.subject else "Inquiry from Portfolio: ${form.projectType}"
    val body = """
        Name: ${form.name}
        Sender Email: ${form.email}
        Opportunity / Project Type: ${form.projectType}
        Expected Timeline: ${form.timeline}
        
        Message:
        ${form.message}
        
        --
        Transmitted via NK-Portfolio Android Application
    """.trimIndent()

    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:naveedahmadkhan804@gmail.com")
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
    }

    try {
        context.startActivity(Intent.createChooser(intent, "Send Email via:"))
    } catch (e: Exception) {
        Toast.makeText(context, "No email client installed, message logged to app!", Toast.LENGTH_LONG).show()
    }
}
