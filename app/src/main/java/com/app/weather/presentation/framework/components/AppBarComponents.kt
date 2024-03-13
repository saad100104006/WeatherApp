package com.app.weather.presentation.framework.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.app.weather.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CenterContentTopAppBar(
    title: @Composable () -> Unit,
    startIcon: ImageVector,
    startIconContentDescription: String = stringResource(id = R.string.content_description_none),
    onStartIconClicked: () -> Unit,
    endIcon: ImageVector? = null,
    endIconContentDescription: String = stringResource(id = R.string.content_description_none),
    onEndIconClicked: (() -> Unit)? = null

) {
    CenterAlignedTopAppBar(
        title = title,
        navigationIcon = {
            IconButton(onClick = onStartIconClicked) {
                Icon(imageVector = startIcon, contentDescription = startIconContentDescription)
            }
        },
        actions = {
            endIcon?.let { icon ->
                IconButton(onClick = onEndIconClicked ?: {}, content = {
                    Icon(imageVector = icon, contentDescription = endIconContentDescription)
                })
            }

        }
    )
}

@Composable
@Preview
private fun CenterContentTopAppBarPreview() {
    CenterContentTopAppBar(
        title = { Text("Pokhara", color = Color.Gray) },
        startIcon = ImageVector.vectorResource(id = R.drawable.ic_map),
        startIconContentDescription = stringResource(id = R.string.content_description_saved_regions),
        onStartIconClicked = { },
        endIcon = ImageVector.vectorResource(id = R.drawable.ic_search),
        endIconContentDescription = stringResource(id = R.string.content_description_search_regions)
    ) {

    }
}