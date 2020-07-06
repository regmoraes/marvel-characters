package br.com.regmoraes.marvelcharacters.presentation.characters

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import br.com.regmoraes.marvelcharacters.application.FetchCharacters.Companion.DEFAULT_LIMIT

/**
 * Adapted from https://medium.com/@anitaa_1990/pagination-in-recyclerview-without-paging-library-1c48e9328f81
 */
abstract class RecyclerViewPagination(private val layoutManager: RecyclerView.LayoutManager) :
    RecyclerView.OnScrollListener() {

    /*
     * Variable to keep track of the current page
     * */
    private var currentOffset: Int = DEFAULT_LIMIT

    /*
     * This variable is used to set
     * the threshold. For instance, if I have
     * set the page limit to 20, this will notify
     * the app to fetch more transactions when the
     * user scrolls to the 18th item of the list.
     * */
    private val threshold = 12

    /*
     * This is a hack to ensure that the app is notified
     * only once to fetch more data. Since we use
     * scrollListener, there is a possibility that the
     * app will be notified more than once when user is
     * scrolling. This means there is a chance that the
     * same data will be fetched from the backend again.
     * This variable is to ensure that this does NOT
     * happen.
     * */
    private var endWithAuto = false

    /*
     * I have created two abstract methods:
     * isLastPage() where the UI can specify if
     * this is the last page - this data usually comes from the backend.
     *
     * loadMore() where the UI can specify to load
     * more data when this method is called.
     *
     * We can also specify another method called
     * isLoading() - to let the UI display a loading View.
     * Since I did not need to display this, I have
     * commented it out.
     * */
    //public abstract boolean isLoading();

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == SCROLL_STATE_IDLE) {
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            var firstVisibleItemPosition = 0
            if (layoutManager is StaggeredGridLayoutManager) {
                firstVisibleItemPosition = layoutManager.findLastVisibleItemPositions(null).first()
            }

            if (visibleItemCount + firstVisibleItemPosition + threshold >= totalItemCount) {
                if (!endWithAuto) {
                    endWithAuto = true
                    loadMore(currentOffset, DEFAULT_LIMIT)
                    currentOffset += DEFAULT_LIMIT
                }
            } else {
                endWithAuto = false
            }
        }
    }

    abstract fun loadMore(offset: Int, limit: Int)
}