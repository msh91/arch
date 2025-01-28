package io.github.msh91.arcyto.core.design.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.msh91.arcyto.core.design.theme.ArcytoTheme

/**
 * A horizontal switcher component that receives N items and a selected item. the component displays all items in one
 * row and allows the user to select one of them.
 */
@Composable
fun <T : Any> ArcSwitcher(
    items: List<T>,
    selectedItem: T,
    itemTitle: (T) -> String,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    HorizontalSwitcher(items, selectedItem, itemTitle, onItemSelected, modifier)
}

@Composable
private fun <T : Any> HorizontalSwitcher(
    items: List<T>,
    selectedItem: T,
    itemTitle: (T) -> String,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Ensure the selected item is part of the list
    require(items.contains(selectedItem)) { "Selected item must be in the list of items" }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp),
    ) {
        items.forEach { item ->
            val isSelected = item == selectedItem
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isSelected) colorScheme.primary else colorScheme.secondary)
                    .clickable { onItemSelected(item) }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = itemTitle(item),
                    color = if (isSelected) Color.White else Color.Black,
                    style = typography.bodySmall
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArcSwitcherPreview() {
    ArcytoTheme {
        var items = remember { listOf("Item 1", "Item 2", "Item 3") }
        var selectedItem = remember { "Item 2" }
        ArcSwitcher(
            items = items,
            selectedItem = selectedItem,
            itemTitle = { it },
            onItemSelected = { selectedItem = it },
        )
    }
}