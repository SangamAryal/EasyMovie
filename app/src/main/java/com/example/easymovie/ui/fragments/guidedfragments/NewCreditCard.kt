package com.example.easymovie.ui.fragments.guidedfragments

import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import androidx.leanback.app.GuidedStepSupportFragment
import androidx.leanback.widget.GuidanceStylist
import androidx.leanback.widget.GuidedAction
import com.example.easymovie.R

class NewCreditCard : GuidedStepSupportFragment() {

    companion object {
        private const val ACTION_ID_CARD_NUMBER = 1L
        private const val ACTION_ID_EXPIRY_DATE = 2L
    }

    override fun onCreateGuidance(savedInstanceState: Bundle?): GuidanceStylist.Guidance {
        val context = requireContext()
        val icon = AppCompatResources.getDrawable(context, R.drawable.ic_guidance)

        return GuidanceStylist.Guidance(
            getString(R.string.add_new_card_title),
            getString(R.string.add_new_card_description),
            "",
            icon
        )
    }

    override fun onCreateActions(actions: MutableList<GuidedAction>, savedInstanceState: Bundle?) {
        val cardNumberAction = GuidedAction.Builder(requireContext())
            .id(ACTION_ID_CARD_NUMBER)
            .title(getString(R.string.enter_card_number))
            .descriptionEditable(true)
            .build()
        actions.add(cardNumberAction)

        val expiryDateAction = GuidedAction.Builder(requireContext())
            .id(ACTION_ID_EXPIRY_DATE)
            .title(getString(R.string.select_expiry_date))
            .descriptionEditable(false)
            .build()
        actions.add(expiryDateAction)
    }

    override fun onGuidedActionClicked(action: GuidedAction) {
//        if (ACTION_ID_EXPIRY_DATE == action.id) {
//            val fragment = ExpiryDatePickerFragment()
//            add(childFragmentManager, fragment)
//        }
    }
}