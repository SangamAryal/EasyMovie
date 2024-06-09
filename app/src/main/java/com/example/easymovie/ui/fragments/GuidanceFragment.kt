package com.example.easymovie.ui.fragments

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.leanback.app.GuidedStepSupportFragment
import androidx.leanback.widget.GuidanceStylist
import androidx.leanback.widget.GuidedAction
import com.example.easymovie.R

class GuidanceFragment : GuidedStepSupportFragment() {

    companion object {
        private const val ACTION_ID_POSITIVE = 1L
        private const val ACTION_ID_NEGATIVE = 2L
    }

    override fun onCreateGuidance(savedInstanceState: Bundle?): GuidanceStylist.Guidance {
        val context = requireContext()
        val icon = AppCompatResources.getDrawable(context, R.drawable.ic_guidance)

        return GuidanceStylist.Guidance(
            getString(R.string.dialog_example_title),
            getString(R.string.dialog_example_description),
            "",
            icon
        )
    }

    override fun onCreateActions(actions: MutableList<GuidedAction>, savedInstanceState: Bundle?) {
        val positiveAction = GuidedAction.Builder(requireContext())
            .id(ACTION_ID_POSITIVE)
            .title(getString(R.string.dialog_example_button_positive))
            .build()
        actions.add(positiveAction)

        val negativeAction = GuidedAction.Builder(requireContext())
            .id(ACTION_ID_NEGATIVE)
            .title(getString(R.string.dialog_example_button_negative))
            .build()
        actions.add(negativeAction)
    }

    override fun onGuidedActionClicked(action: GuidedAction) {
        if (ACTION_ID_POSITIVE == action.id) {
            Toast.makeText(
                activity, R.string.dialog_example_button_toast_positive_clicked,
                Toast.LENGTH_SHORT
            ).show()
        } else if (ACTION_ID_NEGATIVE == action.id) {
            Toast.makeText(
                activity, R.string.dialog_example_button_toast_negative_clicked,
                Toast.LENGTH_SHORT
            ).show()
        }
        activity?.finish()
    }
}