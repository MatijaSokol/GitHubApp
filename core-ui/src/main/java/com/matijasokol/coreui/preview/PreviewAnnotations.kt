package com.matijasokol.coreui.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
@Preview(
    name = "Compact phone",
    group = "Devices",
    widthDp = COMPACT_PHONE_PREVIEW_WIDTH_DP,
    heightDp = COMPACT_PHONE_PREVIEW_HEIGHT_DP,
    showBackground = true,
)
@Preview(
    name = "Standard phone",
    group = "Devices",
    widthDp = STANDARD_PHONE_PREVIEW_WIDTH_DP,
    heightDp = STANDARD_PHONE_PREVIEW_HEIGHT_DP,
    showBackground = true,
)
@Preview(
    name = "Foldable",
    group = "Devices",
    widthDp = FOLDABLE_PREVIEW_WIDTH_DP,
    heightDp = FOLDABLE_PREVIEW_HEIGHT_DP,
    showBackground = true,
)
@Preview(
    name = "Tablet",
    group = "Devices",
    widthDp = TABLET_PREVIEW_WIDTH_DP,
    heightDp = TABLET_PREVIEW_HEIGHT_DP,
    showBackground = true,
)
annotation class GitHubAppDevicePreviews

@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
@Preview(
    name = "Light",
    group = "Themes",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    name = "Dark",
    group = "Themes",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
annotation class GitHubAppThemePreviews

@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
@Preview(
    name = "Large font",
    group = "Accessibility",
    widthDp = STANDARD_PHONE_PREVIEW_WIDTH_DP,
    heightDp = LARGE_FONT_PREVIEW_HEIGHT_DP,
    fontScale = 1.8f,
    showBackground = true,
)
annotation class GitHubAppLargeFontPreview

const val STANDARD_PHONE_PREVIEW_WIDTH_DP = 412
const val STANDARD_PHONE_PREVIEW_HEIGHT_DP = 915
const val COMPACT_PHONE_PREVIEW_WIDTH_DP = 320
private const val COMPACT_PHONE_PREVIEW_HEIGHT_DP = 640
private const val FOLDABLE_PREVIEW_WIDTH_DP = 673
private const val FOLDABLE_PREVIEW_HEIGHT_DP = 841
private const val TABLET_PREVIEW_WIDTH_DP = 1280
private const val TABLET_PREVIEW_HEIGHT_DP = 800
private const val LARGE_FONT_PREVIEW_HEIGHT_DP = 1200
