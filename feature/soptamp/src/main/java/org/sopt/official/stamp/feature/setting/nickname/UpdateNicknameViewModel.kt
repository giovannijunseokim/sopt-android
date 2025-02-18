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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sopt.official.stamp.domain.usecase.user.CheckNicknameDuplicateUseCase
import org.sopt.official.stamp.domain.usecase.user.UpdateNicknameUseCase
import retrofit2.HttpException
import javax.inject.Inject

enum class UpdateNicknameErrorState {
    INPUT_EMPTY,
    IS_NOT_NEW,
    REQUEST_ERROR;
}

data class UpdateNicknameUiState(
    val nickname: String = "",
    val isFocused: Boolean = false,
    val isSuccess: Boolean = false,
    val error: UpdateNicknameErrorState? = null,
    val message: String = ""
)

@HiltViewModel
class UpdateNicknameViewModel @Inject constructor(
    private val checkNicknameDuplicateUseCase: CheckNicknameDuplicateUseCase,
    private val updateNicknameUseCase: UpdateNicknameUseCase
) : ViewModel() {
    private val uiState = MutableStateFlow(UpdateNicknameUiState())
    val nickname = uiState.map { it.nickname }
    val isFocused = uiState.map { it.isFocused }
    val isSuccess = uiState.map { it.isSuccess }
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed(), 1)
    val error = uiState.map { it.error }
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed(), 1)
    val isError = error.map { it != null }
    val message = uiState.map { it.message }

    init {
        viewModelScope.launch {
            nickname.debounce(500L)
                .filter { it.isNotBlank() }
                .collectLatest {
                    checkNicknameDuplicateUseCase(it)
                        .onSuccess {
                            uiState.value = uiState.value.copy(
                                message = "사용가능한 닉네임입니다.",
                                error = null
                            )
                        }.onFailure { error ->
                            if (error is HttpException) {
                                uiState.value = uiState.value.copy(
                                    message = "사용 중인 이름입니다.",
                                    error = UpdateNicknameErrorState.IS_NOT_NEW
                                )
                            } else {
                                uiState.value = uiState.value.copy(
                                    message = "사용 중인 이름입니다.",
                                    error = UpdateNicknameErrorState.REQUEST_ERROR
                                )
                            }
                        }
                }
        }
    }

    fun onUpdateNickname(new: String) {
        if (new.isBlank() && uiState.value.isFocused) {
            uiState.value = uiState.value.copy(
                error = UpdateNicknameErrorState.INPUT_EMPTY,
                message = "한글/영문 10자 이하로 입력해주세요."
            )
        }
        if (new.length <= 10) {
            uiState.value = uiState.value.copy(nickname = new)
        }
    }

    fun onUpdateFocusState(isFocused: Boolean) {
        uiState.value = uiState.value.copy(
            isFocused = isFocused,
            error = if (!isFocused && uiState.value.error != UpdateNicknameErrorState.IS_NOT_NEW) {
                null
            } else {
                uiState.value.error
            }
        )
    }

    fun onPress() {
        viewModelScope.launch {
            updateNicknameUseCase(uiState.value.nickname)
                .onSuccess {
                    uiState.update {
                        it.copy(
                            isSuccess = true,
                            error = null
                        )
                    }
                }.onFailure {
                    uiState.update {
                        it.copy(
                            error = UpdateNicknameErrorState.REQUEST_ERROR,
                            message = "요청 시에 에러가 발생했습니다.",
                            isSuccess = false
                        )
                    }
                }
        }
    }
}
