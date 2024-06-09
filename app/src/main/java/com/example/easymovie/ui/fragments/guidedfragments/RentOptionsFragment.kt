package com.example.easymovie.ui.fragments.guidedfragments

import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import androidx.leanback.app.GuidedStepSupportFragment
import androidx.leanback.widget.GuidanceStylist
import androidx.leanback.widget.GuidedAction
import com.example.easymovie.R

class RentOptionsFragment : GuidedStepSupportFragment() {

    companion object {
        private const val ACTION_ID_HD = 1L
        private const val ACTION_ID_SD = 2L
    }

    override fun onCreateGuidance(savedInstanceState: Bundle?): GuidanceStylist.Guidance {
        val context = requireContext()
        val icon = AppCompatResources.getDrawable(context, R.drawable.ic_guidance)

        return GuidanceStylist.Guidance(
            getString(R.string.rent_options_title),
            getString(R.string.rent_options_description),
            "",
            icon
        )
    }

    override fun onCreateActions(actions: MutableList<GuidedAction>, savedInstanceState: Bundle?) {
        val hdAction = GuidedAction.Builder(requireContext())
            .id(ACTION_ID_HD)
            .title(getString(R.string.rent_hd))
            .description(getString(R.string.rent_hd_price))
            .build()
        actions.add(hdAction)

        val sdAction = GuidedAction.Builder(requireContext())
            .id(ACTION_ID_SD)
            .title(getString(R.string.rent_sd))
            .description(getString(R.string.rent_sd_price))
            .build()
        actions.add(sdAction)
    }

    override fun onGuidedActionClicked(action: GuidedAction) {
        val fragment = PaymentMethodFragment()
        add(parentFragmentManager, fragment)
    }
}