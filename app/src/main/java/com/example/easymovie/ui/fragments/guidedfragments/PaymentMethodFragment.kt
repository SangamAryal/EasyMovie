package com.example.easymovie.ui.fragments.guidedfragments

import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import androidx.leanback.app.GuidedStepSupportFragment
import androidx.leanback.widget.GuidanceStylist
import androidx.leanback.widget.GuidedAction
import com.example.easymovie.R

class PaymentMethodFragment : GuidedStepSupportFragment() {

    companion object {
        private const val ACTION_ID_SELECT_CARD = 1L
        private const val ACTION_ID_ADD_NEW_CARD = 2L

        // Define unique IDs for each card
        private const val ACTION_ID_CARD_1 = 3L
        private const val ACTION_ID_CARD_2 = 4L
    }

    override fun onCreateGuidance(savedInstanceState: Bundle?): GuidanceStylist.Guidance {
        val context = requireContext()
        val icon = AppCompatResources.getDrawable(context, R.drawable.ic_guidance)
        val title = getString(R.string.payment_method_title)

        val description = getString(R.string.payment_method_description)
        return GuidanceStylist.Guidance(title, description, "", icon)
    }

    override fun onCreateActions(actions: MutableList<GuidedAction>, savedInstanceState: Bundle?) {
        val checkSetId = 1L
        val selectCardAction = GuidedAction.Builder(context).id(ACTION_ID_SELECT_CARD)
            .title(getString(R.string.select_card))
            .description(getString(R.string.select_card_description)).subActions(
                listOf(
                    GuidedAction.Builder(context).id(ACTION_ID_CARD_1).title("Card 1")
                        .checkSetId(checkSetId.toInt()).build(),
                    GuidedAction.Builder(context).id(ACTION_ID_CARD_2).title("Card 2")
                        .checkSetId(checkSetId.toInt()).build(),
                    GuidedAction.Builder(context).id(ACTION_ID_ADD_NEW_CARD)
                        .title(getString(R.string.add_new_card)).build()
                )
            ).build()
        actions.add(selectCardAction)
    }

    override fun onSubGuidedActionClicked(action: GuidedAction): Boolean {
        when (action.id) {
            ACTION_ID_CARD_1, ACTION_ID_CARD_2 -> {
                parentFragmentManager.popBackStack()

            }
//            ACTION_ID_ADD_NEW_CARD -> {
//                val fragment = NewCreditCardFragment() // Replace with your fragment class
//                GuidedStepSupportFragment.add(parentFragmentManager, fragment)
//            }
//            // Handle other subaction clicks if necessary
        }
        return true
    }

}
