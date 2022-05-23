package com.example.memorychallenger

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
        val controller = Robolectric.buildActivity(MainActivity::class.java)
        val systemUnderTest = controller.get()
        val scoreTextView = systemUnderTest.findViewById(R.id.gameScoreTextView) as TextView
//        Then
        assertEquals("Your Score: 0", scoreTextView.text)
    }

    @Test
    fun increasesScore_whenButtonPressed() {
        val controller = Robolectric.buildActivity(MainActivity::class.java)
        val systemUnderTest = controller.get()
        val scoreTextView = systemUnderTest.findViewById(R.id.gameScoreTextView) as TextView
        val tapMeButton = systemUnderTest.findViewById(R.id.tapMeButton) as Button

//        When
        tapMeButton.performClick()

//        Then
        assertEquals("Your Score: 1", scoreTextView.text)
    }

    @Test
    fun gameRetainsScore_whenDeviceRotates() {

        var controller = Robolectric.buildActivity(MainActivity::class.java)
            .create()
            .resume()
            .visible()
        var systemUnderTest = controller.get()
        var scoreTextView = systemUnderTest.findViewById(R.id.gameScoreTextView) as TextView
        val tapMeButton = systemUnderTest.findViewById(R.id.tapMeButton) as Button

        tapMeButton.performClick()
        tapMeButton.performClick()
        tapMeButton.performClick()

        assertEquals("Your Score: 3",scoreTextView.text)

        // Rotate the device
        val bundle = Bundle()

        controller
            .saveInstanceState(bundle)
            .pause()
            .stop()
            .destroy()

        controller = Robolectric.buildActivity(MainActivity::class.java)
            .create(bundle)
            .start()
            .restoreInstanceState(bundle)
            .resume()
            .visible()
        val recreatedSystemUnderTest = controller.get();
        scoreTextView = recreatedSystemUnderTest.findViewById(R.id.gameScoreTextView) as TextView
        assertEquals("Your Score: 3",scoreTextView.text)
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