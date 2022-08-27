package com.example.expandablelistcomposeexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<ExpandableListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(viewModel)
        }
    }
}

@Composable
fun MainScreen(viewModel: ExpandableListViewModel) {

    val itemIds by viewModel.itemIds.collectAsState()

    Scaffold(
        topBar = { TopBar() }
    ) { padding ->  // We need to pass scaffold's inner padding to the content
        LazyColumn(modifier = Modifier.padding(padding)) {
            itemsIndexed(viewModel.items.value) { index, item ->
                ExpandableContainerView(
                    itemModel = item,
                    onClickItem = { viewModel.onItemClicked(index) },
                    expanded = itemIds.contains(index)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    val viewModel = ExpandableListViewModel()
    MainScreen(viewModel = viewModel)
}

@Composable
fun TopBar() {
    TopAppBar(
        title = { Text(text = stringResource(R.string.app_name), fontSize = 18.sp) },
        backgroundColor = colorResource(id = R.color.colorPrimary),
        contentColor = Color.White
    )
}

@Preview(showBackground = false)
@Composable
fun TopBarPreview() {
    TopBar()
}

@Composable
fun ExpandableContainerView(itemModel: DataModel, onClickItem: () -> Unit, expanded: Boolean) {
    Box(
        modifier = Modifier
            .background(colorResource(R.color.colorPrimaryDark))
    ) {
        Column {
            HeaderView(questionText = itemModel.question, onClickItem = onClickItem)
            ExpandableView(answerText = itemModel.answer, isExpanded = expanded)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExpandableContainerViewPreview() {
    ExpandableContainerView(
        itemModel = DataModel("Question", "Answer"),
        onClickItem = {},
        expanded = true
    )
}

@Composable
fun HeaderView(questionText: String, onClickItem: () -> Unit) {
    Box(
        modifier = Modifier
            .background(colorResource(R.color.colorPrimary))
            .clickable(
                indication = null, // Removes the ripple effect on tap
                interactionSource = remember { MutableInteractionSource() }, // Removes the ripple effect on tap
                onClick = onClickItem
            )
            .padding(8.dp)
    ) {
        Text(
            text = questionText,
            fontSize = 17.sp,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderViewPreview() {
    HeaderView("Question") {}
}

@Composable
fun ExpandableView(answerText: String, isExpanded: Boolean) {
    // Opening Animation
    val expandTransition = remember {
        expandVertically(
            expandFrom = Alignment.Top,
            animationSpec = tween(300)
        ) + fadeIn(
            animationSpec = tween(300)
        )
    }

    // Closing Animation
    val collapseTransition = remember {
        shrinkVertically(
            shrinkTowards = Alignment.Top,
            animationSpec = tween(300)
        ) + fadeOut(
            animationSpec = tween(300)
        )
    }

    AnimatedVisibility(
        visible = isExpanded,
        enter = expandTransition,
        exit = collapseTransition
    ) {
        Box(modifier = Modifier.padding(15.dp)) {
            Text(
                text = answerText,
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExpandableViewPreview() {
    ExpandableView("Answer", true)
}