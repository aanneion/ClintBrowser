package com.jhaiian.clint.profiles

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhaiian.clint.R
import com.jhaiian.clint.ui.ClintDialog
import com.jhaiian.clint.ui.ClintDialogActionFooter
import com.jhaiian.clint.ui.ClintRadioButton
import com.jhaiian.clint.ui.theme.LocalClintColors

@Composable
internal fun ProfileIdentityDialog(
    profile: BrowserProfile,
    hideStatusBar: Boolean,
    hideSystemNavigation: Boolean,
    onDismiss: () -> Unit,
    onSave: (ProfileIdentityPreset, ProfilePresentationMode) -> Unit
) {
    val colors = LocalClintColors.current
    var identityPreset by remember(profile.id, profile.identityPreset) {
        mutableStateOf(profile.identityPreset)
    }
    var presentationMode by remember(profile.id, profile.presentationMode) {
        mutableStateOf(profile.presentationMode)
    }

    ClintDialog(
        title = stringResource(R.string.profile_identity_dialog_title, profile.name),
        hideStatusBar = hideStatusBar,
        hideSystemNavigation = hideSystemNavigation,
        onDismiss = onDismiss,
        footer = {
            ClintDialogActionFooter(
                onCancel = onDismiss,
                positiveLabel = stringResource(R.string.action_save),
                onPositive = { onSave(identityPreset, presentationMode) }
            )
        }
    ) {
        Column(Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
            Text(
                stringResource(R.string.profile_identity_disclaimer),
                color = colors.secondaryText,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
            )

            ProfileIdentityPreset.entries.forEach { preset ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable { identityPreset = preset }
                        .padding(horizontal = 4.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ClintRadioButton(
                        selected = identityPreset == preset,
                        modifier = Modifier.padding(start = 4.dp, end = 8.dp)
                    )
                    Column(Modifier.weight(1f).padding(end = 8.dp)) {
                        Text(
                            stringResource(preset.titleRes),
                            color = colors.onSurface,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            stringResource(preset.descriptionRes),
                            color = colors.secondaryText,
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }

            Text(
                stringResource(R.string.profile_presentation_title),
                color = colors.onSurface,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 12.dp, top = 10.dp, bottom = 2.dp)
            )
            Text(
                stringResource(R.string.profile_presentation_summary),
                color = colors.secondaryText,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
            )
            ProfilePresentationMode.entries.forEach { mode ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable { presentationMode = mode }
                        .padding(horizontal = 4.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ClintRadioButton(
                        selected = presentationMode == mode,
                        modifier = Modifier.padding(start = 4.dp, end = 8.dp)
                    )
                    Text(
                        stringResource(mode.titleRes),
                        color = colors.onSurface,
                        fontSize = 13.sp,
                        modifier = Modifier.weight(1f).padding(vertical = 7.dp)
                    )
                }
            }
            Text(
                stringResource(R.string.profile_identity_apply_hint),
                color = colors.secondaryText,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }
    }
}
