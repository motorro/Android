package com.motorro.cookbook.recipelist.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.motorro.cookbook.appcore.compose.ui.theme.AppDimens
import com.motorro.cookbook.appcore.compose.ui.theme.CookbookTheme
import com.motorro.cookbook.model.RecipeCategory
import com.motorro.cookbook.recipelist.data.RecipeListItem

@Composable
fun CategoryItemView(categoryItem: RecipeListItem.CategoryItem) {
    Text(
        text = categoryItem.name,
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.onSecondaryContainer,
        modifier = Modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(horizontal = AppDimens.horizontal_margin_small),
        overflow = TextOverflow.Ellipsis,
        maxLines = 1
    )
}

@Composable
@Preview(showBackground = true)
fun PreviewCategoryItemView() {
    val sampleCategory = RecipeCategory(
        name = "Drinks"
    )
    CookbookTheme {
        CategoryItemView(RecipeListItem.CategoryItem(sampleCategory))
    }
}