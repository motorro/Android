package com.motorro.cookbook.recipelist.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.motorro.cookbook.appcore.compose.ui.theme.AppDimens
import com.motorro.cookbook.appcore.compose.ui.theme.CookbookTheme
import com.motorro.cookbook.model.ListRecipe
import com.motorro.cookbook.model.RecipeCategory
import com.motorro.cookbook.recipelist.data.RecipeListItem
import kotlin.time.Instant
import kotlin.uuid.Uuid
import com.motorro.cookbook.appcore.R as ACR

@Composable
@OptIn(ExperimentalGlideComposeApi::class)
fun RecipeItemView(recipeItem: RecipeListItem.RecipeItem, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(
                    top = AppDimens.vertical_margin_small,
                    bottom = AppDimens.vertical_margin_small,
                    start = AppDimens.horizontal_margin,
                    end = AppDimens.horizontal_margin
                )
                .size(AppDimens.list_image_size)
                .clip(MaterialTheme.shapes.medium)
                .border(1.dp, Color.Gray, MaterialTheme.shapes.medium),
            contentAlignment = Alignment.Center
        ) {
            val imageUrl = recipeItem.imageUrl?.url
            val requestManager = LocalGlideRequestManager.current
            val size = with(LocalDensity.current) {
                AppDimens.list_image_size.toPx().toInt()
            }
            if (null != imageUrl) {
                GlideImage(
                    model = imageUrl,
                    contentDescription = recipeItem.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    requestBuilderTransform = {
                        requestManager.asDrawable().load(imageUrl).override(size, size)
                    }
                )
            } else {
                Icon(
                    painter = painterResource(id = ACR.drawable.ic_no_image),
                    contentDescription = recipeItem.title,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(AppDimens.list_image_size)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp)) // Corresponds to horizontal_margin

        // Title
        Text(
            text = recipeItem.title,
            style = MaterialTheme.typography.headlineSmall, // Approximating ?attr/textAppearanceHeadline4
            modifier = Modifier.weight(1f), // Takes remaining space
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRecipeItemView() {
    val sampleRecipe = RecipeListItem.RecipeItem(
        ListRecipe(
            id = Uuid.random(),
            title = "Delicious Pasta Dish",
            category = RecipeCategory("Pasta"),
            image = com.motorro.cookbook.model.Image("https://picsum.photos/id/22/200/200"),
            dateTimeCreated = Instant.parse("2023-10-01T00:00:00Z")
        )
    )

    CookbookTheme {
        WithLocalGlide {
            RecipeItemView(sampleRecipe)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewRecipeItemViewNoImage() {
    val sampleRecipe = RecipeListItem.RecipeItem(
        ListRecipe(
            id = Uuid.random(),
            title = "Delicious Pasta Dish",
            category = RecipeCategory("Pasta"),
            image = null,
            dateTimeCreated = Instant.parse("2023-10-01T00:00:00Z")
        )
    )

    CookbookTheme {
        WithLocalGlide {
            RecipeItemView(sampleRecipe)
        }
    }
}

