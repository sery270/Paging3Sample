/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.codelabs.paging.model

import java.lang.Exception

// TODO deprecated 제거하기 !
// Paging 3을 사용하면 라이브러리가 LoadResult로 성공 및 오류 사례를 모두 모델링하므로 RepoSearchResult가 더 이상 필요하지 않습니다.
// RepoSearchResult는 다음 단계에서 대체되므로 삭제해도 됩니다.

/**
 * RepoSearchResult from a search, which contains List<Repo> holding query data,
 * and a String of network error state.
 */
//sealed class RepoSearchResult {
//    data class Success(val data: List<Repo>) : RepoSearchResult()
//    data class Error(val error: Exception) : RepoSearchResult()
//}
