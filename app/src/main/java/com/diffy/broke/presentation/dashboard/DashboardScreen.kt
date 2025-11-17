package com.diffy.broke.presentation.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.diffy.broke.R
import com.diffy.broke.presentation.core.LocalNavController
import com.diffy.broke.presentation.core.templates.ScaffoldTemplate
import com.diffy.broke.presentation.core.Route
import com.diffy.broke.presentation.dashboard.components.BudgetCard
import com.diffy.broke.presentation.dashboard.components.CategoryCard
import com.diffy.broke.presentation.dashboard.components.MonthSwitcherCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onDrawerClick: () -> Unit,
    onEvent: (DashboardAction) -> Unit,
    state: DashboardState
) {
    ScaffoldTemplate(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.dashboard)) },
                navigationIcon = {
                    IconButton(
                        onClick = onDrawerClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = stringResource(R.string.navigation_icon)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            MonthSwitcherCard(
                currentMonth = "${state.currentMonth} ${state.currentYear}",
                onPrevious = { onEvent(DashboardAction.OnPreviousMonth) },
                onNext = { onEvent(DashboardAction.OnNextMonth) }
            )

            state.overView?.let {
                BudgetCard(
                    remaining = it.budget - it.totalSpend,
                    totalBudget = it.budget,
                    totalSpent = it.totalSpend
                )
            } ?: Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Loading...")
            }


            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
            ) {
                Box(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    state.todaySpend?.let{
                        Text(
                            text = "Today's Spends: ₹${"%.2f".format(it)}",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                        )
                    } ?: Text(
                        text = "Loading...",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Spending by Category",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                val navController = LocalNavController.current
                TextButton(onClick = { navController.navigate(Route.Transaction) }) {
                    Text(
                        text = "View All",
                    )
                }
            }

            state.categorySpendM.forEach { category ->
                CategoryCard(category)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}