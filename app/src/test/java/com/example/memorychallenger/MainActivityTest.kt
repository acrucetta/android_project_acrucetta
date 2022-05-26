package com.example.memorychallenger

import android.app.AlertDialog
import android.os.Bundle
import android.widget.TextView
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import android.widget.Button
import org.apache.tools.ant.taskdefs.condition.IsFalse

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun startsWithZeroInitialScore() {
        val systemUnderTest = setUpTestableActivity()
        val scoreTextView = systemUnderTest.findViewById(R.id.gameScoreTextView) as TextView

//        Then
        assertEquals("Your Score: 0", scoreTextView.text)
    }

    @Test
    fun settingOffWhenChallengePressed() {
        // Given
        val systemUnderTest = setUpTestableActivity()
        val challengeButton = systemUnderTest.findViewById(R.id.challengeButton) as Button
        var isSettingStatus = systemUnderTest.isSettingColors

//        When
        challengeButton.performClick()
        isSettingStatus = systemUnderTest.isSettingColors

//        Then
        assertEquals(false, isSettingStatus)
    }

    private fun setUpTestableActivity(): MainActivity {
        val controller = Robolectric.buildActivity(MainActivity::class.java)
            .create()
            .resume()
            .visible()
        val systemUnderTest = controller.get()
        return systemUnderTest
    }
}