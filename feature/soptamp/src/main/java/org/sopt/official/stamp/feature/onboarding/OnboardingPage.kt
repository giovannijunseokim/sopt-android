/*
 * Copyright 2023 SOPT - Shout Our Passion Together
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sopt.official.stamp.feature.onboarding

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.sopt.official.stamp.R
import org.sopt.official.stamp.designsystem.component.layout.SoptColumn
import org.sopt.official.stamp.designsystem.style.SoptTheme
import org.sopt.official.stamp.util.DefaultPreview

@Composable
fun OnboardingPage(
    @DrawableRes image: Int,
    title: String,
    content: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            painter = painterResource(id = image),
            contentDescription = null
        )
        Spacer(modifier = Modifier.size(16.dp))
        OnboardingPageTitle(text = title)
        Spacer(modifier = Modifier.size(12.dp))
        OnboardingPageContent(text = content)
    }
}

@Composable
fun OnboardingPageTitle(text: String) {
    Text(
        text = text,
        style = SoptTheme.typography.h1,
        color = SoptTheme.colors.onSurface90
    )
}

@Composable
fun OnboardingPageContent(text: String) {
    Text(
        text = text,
        style = SoptTheme.typography.sub2,
        color = SoptTheme.colors.onSurface50
    )
}

@DefaultPreview
@Composable
private fun OnboardingPreview() {
    SoptTheme {
        SoptColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            OnboardingPage(
                R.drawable.ic_onboarding_1,
                "title",
                "content"
            )
        }
    }
}
