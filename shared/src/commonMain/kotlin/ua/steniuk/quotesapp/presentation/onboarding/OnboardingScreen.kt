package ua.steniuk.quotesapp.presentation.onboarding

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import ua.steniuk.quotesapp.presentation.common.ObserveEvents
import ua.steniuk.quotesapp.resources.*

@Composable
fun OnboardingScreen(
    onNavigateToAuth: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveEvents(viewModel.events) { event ->
        when (event) {
            OnboardingEvent.NavigateToAuth -> onNavigateToAuth()
        }
    }

    OnboardingContent(state = state, onAction = viewModel::onAction)
}

@Composable
private fun OnboardingContent(
    state: OnboardingState,
    onAction: (OnboardingAction) -> Unit,
) {
    val colors = MaterialTheme.colorScheme
    val pagerState = rememberPagerState(pageCount = { state.pages.size })
    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            onAction(OnboardingAction.OnPageChanged(page))
        }
    }

    val backgroundBrush = Brush.verticalGradient(
        listOf(
            colors.background,
            colors.primaryContainer.copy(alpha = 0.12f),
            colors.tertiaryContainer.copy(alpha = 0.35f),
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
            .systemBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        OnboardingTopBar(
            currentPage = state.currentPage,
            pageCount = state.pages.size,
            isLastPage = state.isLastPage,
            onSkip = { onAction(OnboardingAction.OnSkipClick) }
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            OnboardingPageContent(state.pages[page])
        }

        PageIndicator(
            pageCount = state.pages.size,
            currentPage = state.currentPage
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val backEnabled = state.currentPage > 0

            Box(
                modifier = Modifier
                    .weight(0.4f)
                    .height(56.dp)
                    .shadow(6.dp, CircleShape, spotColor = colors.primary)
                    .clip(CircleShape)
                    .background(colors.surfaceContainerLowest)
                    .clickable(enabled = backEnabled) {
                        scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "← " + stringResource(Res.string.btn_back),
                    style = MaterialTheme.typography.labelLarge,
                    color = colors.onSurface.copy(alpha = if (backEnabled) 1f else 0.35f)
                )
            }

            Spacer(Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .weight(0.6f)
                    .height(56.dp)
                    .shadow(10.dp, CircleShape, spotColor = colors.primaryContainer)
                    .clip(CircleShape)
                    .background(accentGradient())
                    .clickable {
                        if (state.isLastPage) onAction(OnboardingAction.OnGetStartedClick)
                        else scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(
                        if (state.isLastPage) Res.string.btn_start else Res.string.btn_next
                    ) + "  →",
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun OnboardingTopBar(
    currentPage: Int,
    pageCount: Int,
    isLastPage: Boolean,
    onSkip: () -> Unit,
) {
    val colors = MaterialTheme.colorScheme
    Row(
        modifier = Modifier.fillMaxWidth().height(48.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Pill(background = colors.primaryContainer.copy(alpha = 0.18f)) {
            Text("♥", color = colors.primary, fontSize = 14.sp)
            Spacer(Modifier.width(6.dp))
            Text(
                text = stringResource(Res.string.onboarding_step, currentPage + 1, pageCount).uppercase(),
                style = chipTextStyle(),
                color = colors.primary
            )
        }

        if (!isLastPage) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(colors.surfaceContainerLowest)
                    .clickable(onClick = onSkip)
                    .padding(horizontal = 18.dp, vertical = 10.dp)
            ) {
                Text(
                    text = stringResource(Res.string.btn_skip) + "  ›",
                    style = MaterialTheme.typography.labelLarge,
                    color = colors.onSurface
                )
            }
        }
    }
}

@Composable
private fun OnboardingPageContent(page: OnboardingPage) {
    val colors = MaterialTheme.colorScheme
    val title = stringResource(page.title)
    val accent = stringResource(page.titleAccent)

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OnboardingIllustration(page)

        Spacer(Modifier.height(24.dp))

        Pill(background = colors.primaryContainer.copy(alpha = 0.18f)) {
            Text(page.chipEmoji, fontSize = 12.sp)
            Spacer(Modifier.width(6.dp))
            Text(
                text = stringResource(page.chip).uppercase(),
                style = chipTextStyle(),
                color = colors.primary
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = buildAnnotatedString {
                append(title)
                append(" ")
                withStyle(SpanStyle(color = colors.primary)) { append(accent) }
            },
            style = MaterialTheme.typography.headlineMedium,
            color = colors.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = stringResource(page.description),
            style = MaterialTheme.typography.bodyLarge,
            color = colors.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(20.dp))

        QuoteCard(text = stringResource(page.quote))
    }
}

@Composable
private fun OnboardingIllustration(page: OnboardingPage) {
    val colors = MaterialTheme.colorScheme
    Box(
        modifier = Modifier.size(270.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .matchParentSize()
                .background(
                    Brush.radialGradient(
                        listOf(colors.primaryContainer.copy(alpha = 0.35f), Color.Transparent)
                    ),
                    CircleShape
                )
        )
        Box(
            Modifier
                .size(230.dp)
                .shadow(20.dp, CircleShape, spotColor = colors.primary)
                .background(colors.surfaceContainerLowest, CircleShape)
                .padding(8.dp)
                .clip(CircleShape)
        ) {
            Image(
                painter = painterResource(page.image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        FloatingBadge(
            emoji = "✨",
            modifier = Modifier.align(Alignment.TopEnd).padding(top = 18.dp, end = 10.dp)
        )
        FloatingBadge(
            emoji = page.badgeEmoji,
            modifier = Modifier.align(Alignment.BottomStart).padding(bottom = 22.dp, start = 10.dp)
        )
    }
}

@Composable
private fun FloatingBadge(emoji: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(48.dp)
            .shadow(8.dp, CircleShape, spotColor = MaterialTheme.colorScheme.primary)
            .background(MaterialTheme.colorScheme.surfaceContainerLowest, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(emoji, fontSize = 22.sp)
    }
}

@Composable
private fun QuoteCard(text: String) {
    val colors = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(20.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, shape, spotColor = colors.primary)
            .background(colors.surfaceContainerLowest, shape)
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "❝",
            color = colors.primaryContainer,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
            color = colors.onSurfaceVariant,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun PageIndicator(pageCount: Int, currentPage: Int) {
    val colors = MaterialTheme.colorScheme
    val activeBrush = accentGradient()
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 18.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            val selected = index == currentPage
            val width by animateDpAsState(if (selected) 28.dp else 8.dp)
            Box(
                Modifier
                    .padding(horizontal = 4.dp)
                    .size(width = width, height = 8.dp)
                    .clip(CircleShape)
                    .background(
                        if (selected) activeBrush
                        else SolidColor(colors.primaryContainer.copy(alpha = 0.3f))
                    )
            )
        }
    }
}

@Composable
private fun Pill(
    background: Color,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(background)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}

@Composable
private fun accentGradient(): Brush = Brush.horizontalGradient(
    listOf(
        MaterialTheme.colorScheme.primaryContainer,
        MaterialTheme.colorScheme.secondaryContainer,
    )
)

@Composable
private fun chipTextStyle() = MaterialTheme.typography.labelLarge.copy(
    fontSize = 12.sp,
    letterSpacing = 1.2.sp
)