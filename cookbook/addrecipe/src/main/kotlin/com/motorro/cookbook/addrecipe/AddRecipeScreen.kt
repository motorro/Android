package com.motorro.cookbook.addrecipe

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.motorro.cookbook.addrecipe.data.AddRecipeGesture
import com.motorro.cookbook.addrecipe.data.AddRecipeViewState
import com.motorro.cookbook.appcore.compose.ui.theme.AppDimens
import com.motorro.cookbook.appcore.compose.ui.theme.CookbookTheme
import com.motorro.cookbook.appcore.compose.ui.theme.appBarColors
import com.motorro.cookbook.appcore.R as ACR

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddRecipeScreen(
    viewState: AddRecipeViewState,
    onGesture: (AddRecipeGesture) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_add_new)) },
                navigationIcon = {
                    IconButton(onClick = { onGesture(AddRecipeGesture.Back) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(ACR.string.btn_back)
                        )
                    }
                },
                colors = MaterialTheme.appBarColors()
            )
        }
    ) { innerPadding ->
        when(viewState) {
            is AddRecipeViewState.Form -> {
                Box(modifier = Modifier.padding(innerPadding).fillMaxWidth()) {
                    if (viewState.saving) {
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth().zIndex(2f).align(Alignment.TopCenter))
                    }
                    AddRecipeFormView(
                        formState = viewState,
                        onGesture = onGesture,
                        modifier = Modifier.zIndex(1f).align(Alignment.TopCenter)
                    )
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
private fun AddRecipeFormView(
    formState: AddRecipeViewState.Form,
    onGesture: (AddRecipeGesture) -> Unit,
    modifier: Modifier = Modifier
) {

    // Picture contract
    val selectPicture = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { onGesture(AddRecipeGesture.SetImage(it.toString())) }
    )

    // For the category dropdown
    val (allowExpanded, setExpanded) = remember { mutableStateOf(false) }
    val expanded = allowExpanded && formState.categories.isNotEmpty()

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Title Input
        OutlinedTextField(
            value = formState.title,
            onValueChange = { onGesture(AddRecipeGesture.SetTitle(it)) },
            label = { Text(stringResource(R.string.hint_title)) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = AppDimens.vertical_margin,
                    start = AppDimens.horizontal_margin,
                    end = AppDimens.horizontal_margin
                )
        )

        // Image Selection
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .padding(
                    top = AppDimens.vertical_margin_small,
                    start = AppDimens.horizontal_margin,
                    end = AppDimens.horizontal_margin
                )
                .clip(MaterialTheme.shapes.medium)
                .border(1.dp, Color.Gray, MaterialTheme.shapes.medium)
                .clickable {
                    selectPicture.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            val imageUri = formState.image
            if (null != imageUri) {
                GlideImage(
                    model = imageUri,
                    contentDescription = stringResource(R.string.desc_the_image_of_the_recipe),
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = stringResource(R.string.desc_the_image_of_the_recipe),
                    modifier = Modifier.fillMaxSize(0.5f),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // Category Dropdown
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = setExpanded,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = AppDimens.vertical_margin_small,
                    start = AppDimens.horizontal_margin,
                    end = AppDimens.horizontal_margin
                )
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = formState.category,
                onValueChange = {
                    onGesture(AddRecipeGesture.SetCategory(it))
                    setExpanded(true)
                },
                readOnly = false,
                singleLine = true,
                label = { Text(stringResource(R.string.hint_category)) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded,
                        modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.SecondaryEditable)
                    )
                }
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { setExpanded(false) }
            ) {
                formState.categories.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption, style = MaterialTheme.typography.bodyLarge) },
                        onClick = {
                            onGesture(AddRecipeGesture.SetCategory(selectionOption))
                            setExpanded(false)
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }

        // Recipe steps
        OutlinedTextField(
            value = formState.description,
            onValueChange = { onGesture(AddRecipeGesture.SetDescription(it)) },
            label = { Text(stringResource(R.string.hint_desc)) },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(
                    top = AppDimens.vertical_margin_small,
                    start = AppDimens.horizontal_margin,
                    end = AppDimens.horizontal_margin
                ),
            minLines = 5,
            maxLines = 10
        )

        // Save Button
        Button(
            onClick = {
                onGesture(AddRecipeGesture.Save)
            },
            enabled = formState.isValid,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = AppDimens.vertical_margin,
                    bottom = AppDimens.vertical_margin,
                    start = AppDimens.horizontal_margin,
                    end = AppDimens.horizontal_margin
                )
        ) {
            Text(stringResource(R.string.btn_save))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AddRecipeScreenPreview() {
    CookbookTheme {
        AddRecipeScreen(
            viewState = AddRecipeViewState.Form(
                title = "Recipe",
                image = "https://picsum.photos/id/22/200/200",
                category = "Category 1",
                categories = listOf("Category 1", "Category 2"),
                description = "Description",
                isValid = true,
                saving = true
            ),
            onGesture = { }
        )
    }
}