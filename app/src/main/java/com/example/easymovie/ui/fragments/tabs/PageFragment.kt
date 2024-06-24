import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.easymovie.R
import com.example.easymovie.ui.fragments.tabs.EpisodeFragment
import com.example.easymovie.ui.fragments.tabs.TabFragment

class PageFragment : Fragment() {
    companion object {
        private const val ARG_PAGE_NUMBER = "page_number"
        fun newInstance(pageNumber: Int): PageFragment {
            val fragment = PageFragment()
            val args = Bundle()
            args.putInt(ARG_PAGE_NUMBER, pageNumber)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pageNumber = arguments?.getInt(ARG_PAGE_NUMBER) ?: 1

        // Load different fragments based on page number
        val fragment = when (pageNumber) {
            1 -> TabFragment()
            2 -> EpisodeFragment()
            3 -> TabFragment()
            4 -> TabFragment()
            else -> TabFragment()
        }

        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.tab_fragment_container, fragment).commit()
    }
}
