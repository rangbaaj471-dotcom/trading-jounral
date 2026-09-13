package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Rule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.CoreRuleEntity
import com.example.data.StrategyPlaybookEntity
import com.example.ui.ApexFxViewModel
import com.example.ui.theme.ApexCardBorder
import com.example.ui.theme.ApexError
import com.example.ui.theme.ApexOnPrimary
import com.example.ui.theme.ApexOnSurface
import com.example.ui.theme.ApexOnSurfaceVariant
import com.example.ui.theme.ApexOutline
import com.example.ui.theme.ApexOutlineVariant
import com.example.ui.theme.ApexPrimary
import com.example.ui.theme.ApexPrimaryContainer
import com.example.ui.theme.ApexSecondary
import com.example.ui.theme.ApexSurface
import com.example.ui.theme.ApexSurfaceContainer
import com.example.ui.theme.ApexSurfaceHigh

@Composable
fun PlaybookScreen(
    viewModel: ApexFxViewModel,
    modifier: Modifier = Modifier
) {
    val coreRules by viewModel.coreRules.collectAsStateWithLifecycle()
    val playbooks by viewModel.playbooks.collectAsStateWithLifecycle()
    val latestMindset by viewModel.latestMindsetLog.collectAsStateWithLifecycle()
    val traderNotes by viewModel.traderNotes.collectAsStateWithLifecycle()

    var selectedPlaybookCategory by remember { mutableStateOf("all") }
    var selectedPlaybookForDetail by remember { mutableStateOf<StrategyPlaybookEntity?>(null) }
    var showAddRuleDialog by remember { mutableStateOf(false) }

    // Mindset state
    var disciplineScore by remember { mutableFloatStateOf(8.5f) }
    var selectedEmotion by remember { mutableStateOf("Focused") }

    LaunchedEffect(latestMindset) {
        latestMindset?.let {
            disciplineScore = it.disciplineScore
            selectedEmotion = it.emotionalState
        }
    }

    // Trader notes editing state
    var notesContent by remember { mutableStateOf("") }
    LaunchedEffect(traderNotes) {
        traderNotes?.let {
            if (notesContent.isEmpty()) {
                notesContent = it.content
            }
        }
    }

    val checkedRulesCount = coreRules.count { it.isChecked }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ApexSurface)
            .testTag("playbook_screen")
    ) {
        // Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Strategic Playbooks & Rules",
                    color = ApexOnSurface,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Institutional execution models and personal edge refinement",
                    color = ApexOutline,
                    fontSize = 12.sp
                )
            }
        }

        // SECTION 1: Core Rules Checklist
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(ApexSurfaceContainer)
                    .border(1.dp, ApexCardBorder, RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Rule,
                                contentDescription = null,
                                tint = ApexSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Trading Discipline Checklist",
                                color = ApexOnSurface,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Progress badge
                        Box(
                            modifier = Modifier
                                .background(ApexSecondary.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "$checkedRulesCount/${coreRules.size} COMPLIANT",
                                color = ApexSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    coreRules.forEach { rule ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.toggleRule(rule) }
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Checkbox(
                                checked = rule.isChecked,
                                onCheckedChange = { viewModel.toggleRule(rule) },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = ApexSecondary,
                                    uncheckedColor = ApexOutline
                                ),
                                modifier = Modifier.testTag("rule_checkbox_${rule.id}")
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = rule.title,
                                    color = if (rule.isChecked) ApexOnSurface else ApexOutline,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = rule.description,
                                    color = ApexOutlineVariant,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(
                            onClick = { viewModel.resetRules() },
                            modifier = Modifier.testTag("reset_rules_button")
                        ) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp), tint = ApexOutline)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Reset for Today", color = ApexOutline, fontSize = 12.sp)
                        }

                        Button(
                            onClick = { showAddRuleDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = ApexSurfaceHigh),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.testTag("add_rule_button")
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp), tint = ApexOnSurface)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Add Rule", color = ApexOnSurface, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // SECTION 2: Mindset Logger
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(ApexSurfaceContainer)
                    .border(1.dp, ApexCardBorder, RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = null,
                                tint = ApexPrimaryContainer,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Daily Mindset & Psychology State",
                                color = ApexOnSurface,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = "${String.format("%.1f", disciplineScore)} / 10.0",
                            color = ApexSecondary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(text = "Discipline & Emotional Control Score", color = ApexOutline, fontSize = 11.sp)
                    Slider(
                        value = disciplineScore,
                        onValueChange = { disciplineScore = Math.round(it * 10f) / 10f },
                        valueRange = 1.0f..10.0f,
                        steps = 17,
                        colors = SliderDefaults.colors(
                            thumbColor = ApexSecondary,
                            activeTrackColor = ApexSecondary,
                            inactiveTrackColor = ApexSurfaceHigh
                        ),
                        modifier = Modifier.testTag("mindset_score_slider")
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(text = "CURRENT EMOTIONAL STATE", color = ApexOutline, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Calm", "Focused", "Anxious", "FOMO Prone").forEach { state ->
                            val isSelected = selectedEmotion == state
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isSelected) {
                                            if (state == "FOMO Prone" || state == "Anxious") ApexError.copy(alpha = 0.25f)
                                            else ApexSecondary.copy(alpha = 0.25f)
                                        } else ApexSurfaceHigh
                                    )
                                    .border(
                                        1.dp,
                                        if (isSelected) {
                                            if (state == "FOMO Prone" || state == "Anxious") ApexError
                                            else ApexSecondary
                                        } else ApexCardBorder,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { selectedEmotion = state }
                                    .padding(vertical = 8.dp)
                                    .testTag("emotion_chip_$state"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = state,
                                    color = if (isSelected) ApexOnSurface else ApexOutline,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { viewModel.saveMindset(disciplineScore, selectedEmotion) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ApexPrimaryContainer,
                            contentColor = ApexOnPrimary
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .testTag("log_mindset_button")
                    ) {
                        Text(text = "Log Mindset State", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // SECTION 3: Strategy Playbooks
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = ApexSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Institutional Strategy Playbooks",
                            color = ApexOnSurface,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Category tabs
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(ApexSurfaceContainer)
                            .padding(2.dp)
                    ) {
                        listOf("all" to "All", "fx" to "FX", "indices" to "Indices").forEach { (key, label) ->
                            val isSel = selectedPlaybookCategory == key
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (isSel) ApexPrimaryContainer else Color.Transparent)
                                    .clickable { selectedPlaybookCategory = key }
                                    .padding(horizontal = 6.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = label,
                                    color = if (isSel) ApexOnPrimary else ApexOutline,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        val filteredPlaybooks = playbooks.filter {
            selectedPlaybookCategory == "all" || it.category.equals(selectedPlaybookCategory, ignoreCase = true)
        }

        items(filteredPlaybooks) { playbook ->
            PlaybookCard(
                playbook = playbook,
                onClick = { selectedPlaybookForDetail = playbook },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }

        // SECTION 4: Trader Notes & Daily Review
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(ApexSurfaceContainer)
                    .border(1.dp, ApexCardBorder, RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Trader Notes & Daily Review",
                            color = ApexOnSurface,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Button(
                            onClick = { viewModel.saveNotes(notesContent) },
                            colors = ButtonDefaults.buttonColors(containerColor = ApexSecondary, contentColor = Color(0xFF003824)),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier
                                .height(32.dp)
                                .testTag("save_notes_button")
                        ) {
                            Text(text = "Save Notes", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = notesContent,
                        onValueChange = { notesContent = it },
                        minLines = 4,
                        maxLines = 8,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ApexSurfaceHigh,
                            unfocusedContainerColor = ApexSurfaceHigh,
                            focusedBorderColor = ApexPrimaryContainer,
                            unfocusedBorderColor = ApexCardBorder,
                            focusedTextColor = ApexOnSurface,
                            unfocusedTextColor = ApexOnSurface
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("trader_notes_text_area")
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Playbook Detail Modal Dialog
    selectedPlaybookForDetail?.let { pb ->
        AlertDialog(
            onDismissRequest = { selectedPlaybookForDetail = null },
            containerColor = ApexSurfaceContainer,
            title = {
                Column {
                    Text(text = pb.title, color = ApexOnSurface, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(text = "${pb.tag} • ${pb.timeframes} • ${pb.winRate}% Win Rate", color = ApexSecondary, fontSize = 12.sp)
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = pb.description, color = ApexOnSurfaceVariant, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Execution Checkpoints:", color = ApexPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = pb.step1, color = ApexOnSurface, fontSize = 12.sp)
                    Text(text = pb.step2, color = ApexOnSurface, fontSize = 12.sp)
                    Text(text = pb.step3, color = ApexOnSurface, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = pb.markdownDetails, color = ApexOutline, fontSize = 11.sp)
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedPlaybookForDetail = null }) {
                    Text("Close", color = ApexPrimaryContainer)
                }
            }
        )
    }

    // Add Rule Dialog
    if (showAddRuleDialog) {
        var ruleTitle by remember { mutableStateOf("") }
        var ruleDesc by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddRuleDialog = false },
            containerColor = ApexSurfaceContainer,
            title = { Text("Add Core Trading Rule", color = ApexOnSurface, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = ruleTitle,
                        onValueChange = { ruleTitle = it },
                        placeholder = { Text("Rule Title (e.g. Stop loss before order)", color = ApexOutline) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = ApexOnSurface, unfocusedTextColor = ApexOnSurface)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = ruleDesc,
                        onValueChange = { ruleDesc = it },
                        placeholder = { Text("Execution description...", color = ApexOutline) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = ApexOnSurface, unfocusedTextColor = ApexOnSurface)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (ruleTitle.isNotBlank()) {
                            viewModel.addRule(ruleTitle, ruleDesc)
                            showAddRuleDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ApexSecondary, contentColor = Color(0xFF003824))
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddRuleDialog = false }) {
                    Text("Cancel", color = ApexOutline)
                }
            }
        )
    }
}

@Composable
fun PlaybookCard(
    playbook: StrategyPlaybookEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(ApexSurfaceContainer)
            .border(1.dp, ApexCardBorder, RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(14.dp)
            .testTag("playbook_card_${playbook.id}")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .background(ApexPrimaryContainer.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(text = playbook.tag, color = ApexPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = playbook.timeframes, color = ApexOutline, fontSize = 11.sp)
                }

                Box(
                    modifier = Modifier
                        .background(ApexSecondary.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${playbook.winRate}% WIN RATE",
                        color = ApexSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = playbook.title,
                color = ApexOnSurface,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = playbook.description,
                color = ApexOutline,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Steps snippet
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(ApexSurfaceHigh)
                    .padding(8.dp)
            ) {
                Text(text = playbook.step1, color = ApexOnSurfaceVariant, fontSize = 11.sp)
                Text(text = playbook.step2, color = ApexOnSurfaceVariant, fontSize = 11.sp)
                Text(text = playbook.step3, color = ApexOnSurfaceVariant, fontSize = 11.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = playbook.updatedText, color = ApexOutlineVariant, fontSize = 10.sp)
                Text(
                    text = "View Playbook →",
                    color = ApexPrimaryContainer,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
