package com.example.android.codelabs.paging.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.android.codelabs.paging.api.GithubService
import com.example.android.codelabs.paging.api.IN_QUALIFIER
import com.example.android.codelabs.paging.data.GithubRepository.Companion.NETWORK_PAGE_SIZE
import com.example.android.codelabs.paging.model.Repo
import retrofit2.HttpException
import java.io.IOException

// GitHub page API is 1 based: https://developer.github.com/v3/#pagination
private const val GITHUB_STARTING_PAGE_INDEX = 1

class GithubPagingSource(
    private val service: GithubService,
    private val query: String
) : PagingSource<Int, Repo>() {

    /**
     * 첫 로드시 LoadParams.key 는 null
     * 초기 페이지 키 정의해야 함 -> GITHUB_STARTING_PAGE_INDEX 로 지정하기
     */

    /**
     * load 의 리턴인 LoadResult
     * LoadResult.Page: 로드에 성공한 경우
     *      - 더 이상 로드할 수 없는 경우 (네트워크 응답 성공 && 목록이 빔)
     *          -> nextKey 또는 prevKey 는 null
     * LoadResult.Error: 오류가 발생한 경우
     */

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {
        val position = params.key ?: GITHUB_STARTING_PAGE_INDEX
        val apiQuery = query + IN_QUALIFIER
        return try {
            val response = service.searchRepos(apiQuery, position, params.loadSize)
            val repos = response.items
            val nextKey = if (repos.isEmpty()) {
                null
            } else {
                // initial load size = 3 * NETWORK_PAGE_SIZE
                // ensure we're not requesting duplicating items, at the 2nd request
                position + (params.loadSize / NETWORK_PAGE_SIZE)
            }
            LoadResult.Page(
                data = repos,
                prevKey = if (position == GITHUB_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    /**
     * getRefreshKey 는 후속 새로 고침 호출에 사용 (후속이 아닌 첫 번째 새로 고침 호출은 initialKey 를 사용)
     * invalidate 되어 Paging 라이브러리가 현재 목록을 대체할 새 데이터를 로드하려고 할 때마다 발생
     * - 스와이프하여 새로고침
     * - 데이터베이스 업데이트
     * - 구성 변경
     * - 프로세스 중단
     */

    /**
     * 후속 새로 고침 호출은 PagingState.anchorPosition 주변 데이터의 로드를 다시 시작
     * - PagingState.anchorPosition : 가장 최근에 액세스한 인덱스
     */

    // The refresh key is used for subsequent refresh calls to PagingSource.load after the initial load
    override fun getRefreshKey(state: PagingState<Int, Repo>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}