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
package org.sopt.official.stamp.feature.setting.nickname

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.ramcosta.composedestinations.result.EmptyResultBackNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import org.sopt.official.stamp.config.navigation.SettingNavGraph
import org.sopt.official.stamp.designsystem.component.layout.SoptColumn
import org.sopt.official.stamp.designsystem.component.toolbar.Toolbar
import org.sopt.official.stamp.designsystem.style.Gray50
import org.sopt.official.stamp.designsystem.style.Purple300
import org.sopt.official.stamp.designsystem.style.Red200
import org.sopt.official.stamp.designsystem.style.SoptTheme
import org.sopt.official.stamp.domain.fake.FakeUserRepository
import org.sopt.official.stamp.domain.usecase.user.CheckNicknameDuplicateUseCase
import org.sopt.official.stamp.domain.usecase.user.UpdateNicknameUseCase
import org.sopt.official.stamp.util.DefaultPreview
import org.sopt.official.stamp.util.addFocusCleaner

@SettingNavGraph
@Destination("nickname")
@Composable
fun UpdateNicknameScreen(
    navigator: DestinationsNavigator,
    resultNavigator: ResultBackNavigator<Boolean>,
    viewModel: UpdateNicknameViewModel = hiltViewModel()
) {
    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    val focusManager = LocalFocusManager.current
    val isFocused by viewModel.isFocused.collectAsState(false)
    val isSuccess by viewModel.isSuccess.collectAsState(false)
    val nickname by viewModel.nickname.collectAsState("")
    val error by viewModel.error.collectAsState(null)
    val isError by viewModel.isError.collectAsState(false)
    val message by viewModel.message.collectAsState("")
    val textFieldModifier = remember(isFocused, isError) {
        val modifier = Modifier
            .focusRequester(focusRequester)
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .onFocusChanged {
                viewModel.onUpdateFocusState(it.isFocused)
            }

        if (!isFocused) {
            modifier
        } else {
            modifier.border(
                width = 1.dp,
                color = if (!isError) Purple300 else Red200,
                shape = RoundedCornerShape(10.dp)
            )
        }
    }
    val backgroundColor = remember(isFocused) {
        if (isFocused) Color.White else Gray50
    }
    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            resultNavigator.navigateBack(true)
        }
    }

    SoptTheme {
        SoptColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(SoptTheme.colors.white)
                .addFocusCleaner(focusManager) {
                    viewModel.onUpdateFocusState(false)
                }
        ) {
            Toolbar(
                modifier = Modifier.padding(bottom = 10.dp),
                title = {
                    Text(
                        text = "닉네임 변경",
                        style = SoptTheme.typography.h2,
                        modifier = Modifier.padding(start = 4.dp),
                        color = SoptTheme.colors.onSurface
                    )
                },
                onBack = { navigator.popBackStack() }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = nickname,
                onValueChange = { viewModel.onUpdateNickname(it) },
                modifier = textFieldModifier,
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = backgroundColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    textColor = SoptTheme.colors.onSurface90,
                    placeholderColor = SoptTheme.colors.onSurface60
                ),
                textStyle = SoptTheme.typography.caption1,
                placeholder = {
                    Text(
                        text = "한글/영문 10자 이하로 입력해주세요.",
                        style = SoptTheme.typography.caption1
                    )
                },
                isError = error != null
            )
            Spacer(modifier = Modifier.height(12.dp))
            if (message.isNotBlank() && isFocused) {
                Text(
                    text = message,
                    style = SoptTheme.typography.caption2,
                    color = if (isError) SoptTheme.colors.error300 else SoptTheme.colors.access300
                )
            }
            Spacer(modifier = Modifier.height(if (message.isNotBlank() && isFocused) 26.dp else 52.dp))
            Button(
                onClick = { viewModel.onPress() },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = SoptTheme.colors.purple300,
                    disabledBackgroundColor = SoptTheme.colors.purple200
                ),
                contentPadding = PaddingValues(vertical = 16.dp),
                enabled = !isError && nickname.isNotBlank()
            ) {
                Text(
                    text = "닉네임 변경",
                    style = SoptTheme.typography.h2,
                    color = SoptTheme.colors.white
                )
            }
        }
    }
}

@DefaultPreview
@Composable
private fun UpdateNicknameScreenPreview() {
    UpdateNicknameScreen(
        navigator = EmptyDestinationsNavigator,
        viewModel = UpdateNicknameViewModel(
            CheckNicknameDuplicateUseCase(FakeUserRepository),
            UpdateNicknameUseCase(
                FakeUserRepository
            )
        ),
        resultNavigator = EmptyResultBackNavigator()
    )
}
