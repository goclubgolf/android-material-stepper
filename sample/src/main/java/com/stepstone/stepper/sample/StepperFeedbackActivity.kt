/*
Copyright 2017 StepStone Services

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.stepstone.stepper.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.internal.feedback.StepperFeedbackType
import com.stepstone.stepper.sample.adapter.StepperFeedbackFragmentStepAdapter

import butterknife.BindView
import butterknife.ButterKnife

class StepperFeedbackActivity : AppCompatActivity() {

    companion object {

        private const val CURRENT_STEP_POSITION_KEY = "position"
    }

    @BindView(R.id.stepperLayout)
    lateinit var mStepperLayout: StepperLayout

    private var mMenu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Stepper sample"

        setContentView(R.layout.activity_stepper_feedback)
        ButterKnife.bind(this)
        val startingStepPosition = savedInstanceState?.getInt(CURRENT_STEP_POSITION_KEY) ?: 0
        mStepperLayout.setAdapter(StepperFeedbackFragmentStepAdapter(supportFragmentManager, this), startingStepPosition)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(CURRENT_STEP_POSITION_KEY, mStepperLayout.currentStepPosition)
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        val currentStepPosition = mStepperLayout.currentStepPosition
        if (currentStepPosition > 0) {
            //do nothing when operation is in progress
            if (!mStepperLayout.isInProgress) {
                mStepperLayout.onBackClicked()
            }
        } else {
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_stepper_feedback, menu)
        mMenu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId

        if (!mStepperLayout.isInProgress && (itemId == R.id.menu_feedback_content || itemId == R.id.menu_feedback_tabs || itemId == R.id.menu_feedback_nav)) {
            toggleItem(item)
            val tabsEnabled = mMenu?.findItem(R.id.menu_feedback_tabs)?.isChecked
            val contentEnabled = mMenu?.findItem(R.id.menu_feedback_content)?.isChecked
            val disablingBottomNavigationEnabled = mMenu?.findItem(R.id.menu_feedback_nav)?.isChecked

            var feedbackMask = 0
            if (tabsEnabled == true) {
                feedbackMask = feedbackMask or StepperFeedbackType.TABS
            }
            if (contentEnabled == true) {
                feedbackMask = feedbackMask or StepperFeedbackType.CONTENT
            }
            if (disablingBottomNavigationEnabled == true) {
                feedbackMask = feedbackMask or StepperFeedbackType.DISABLED_BOTTOM_NAVIGATION
            }
            if (feedbackMask == 0) {
                feedbackMask = StepperFeedbackType.NONE
            }
            mStepperLayout.setFeedbackType(feedbackMask)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun toggleItem(item: MenuItem) {
        item.isChecked = !item.isChecked
    }

}
