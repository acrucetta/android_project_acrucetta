package com.example.memorychallenger

import android.app.AlertDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE
import android.widget.Button as Button
import android.widget.TextView as TextView

class MainActivity : AppCompatActivity() {

    internal lateinit var gameScoreTextView: TextView
    internal lateinit var timeLeftTextView: TextView

    internal lateinit var redButton: Button
    internal lateinit var greenButton: Button
    internal lateinit var orangeButton: Button
    internal lateinit var blueButton: Button
    internal lateinit var challengeButton: Button


    internal lateinit var savedPattern: ArrayList<String>
    internal lateinit var triedPattern: ArrayList<String>
    internal var numLastPatterns: Int = 0

    internal var score = 0
    internal var gameStarted = FALSE
    internal var isSettingColors: Boolean = true
    internal var numRounds: Int = 0

    internal lateinit var countDownTimer: CountDownTimer
    internal var initialCountDown: Long = 60000
    internal val countDownInterval: Long = 1000
    internal var timerRunning: Boolean = FALSE

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        const val RED = "RED"
        const val BLUE = "BLUE"
        const val ORANGE = "ORANGE"
        const val GREEN = "GREEN"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreated called. Score is: $score")

        savedPattern = ArrayList()
        triedPattern = ArrayList()
        numLastPatterns = 0

        redButton = findViewById(R.id.redButton)
        greenButton = findViewById(R.id.greenButton)
        orangeButton = findViewById(R.id.orangeButton)
        blueButton = findViewById(R.id.blueButton)
        challengeButton = findViewById(R.id.challengeButton)

        gameScoreTextView = findViewById(R.id.gameScoreTextView)
        timeLeftTextView = findViewById(R.id.timeLeftTextView)

        redButton.setOnClickListener() {
            if (isSettingColors) {
                savedPattern.add(RED)
            } else {
                if (triedMaxPatterns()) {
                    settleRound()
                    return@setOnClickListener
                }
                triedPattern.add(RED)
            }
        }
        greenButton.setOnClickListener() {
            if (isSettingColors) {
                savedPattern.add(GREEN)
            } else {
                if (triedMaxPatterns()) {
                    settleRound()
                    return@setOnClickListener
                }
                triedPattern.add(GREEN)
            }
        }
        orangeButton.setOnClickListener() {
            if (isSettingColors) {
                savedPattern.add(ORANGE)
            } else {
                if (triedMaxPatterns()) {
                    settleRound()
                    return@setOnClickListener
                }
                triedPattern.add(ORANGE)
            }
        }
        blueButton.setOnClickListener() {
            if (isSettingColors) {
                savedPattern.add(BLUE)
            } else {
                if (triedMaxPatterns()) {
                    settleRound()
                    return@setOnClickListener
                }
                triedPattern.add(BLUE)
            }
        }
        challengeButton.setOnClickListener() {
            isSettingColors = FALSE
            val alertDialog: AlertDialog = this.let {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.challengeTitle))
                val commaSeparatedString = savedPattern.joinToString { "\'${it}\'" }
                builder.setMessage("Time to guess the following pattern, press OK when you're " +
                        "done memorizing it $commaSeparatedString")
                builder.apply {
                    setPositiveButton(R.string.ok
                    ) { _, _ ->
                        if (numRounds==0) {
                            countDownTimer.start()
                        }
                    }
                }
                builder.create()
            }
            alertDialog.show()
        }
        resetGame()
    }

    private fun settleRound() {
        val numErrors = compareScores()
        val finalScore = getFinalScore(numErrors)
        savedPattern = ArrayList()
        triedPattern = ArrayList()

        showResults(numErrors)
        incrementScore(finalScore)
        pauseTimer()
        numRounds++
    }

    private fun triedMaxPatterns(): Boolean {
        if (triedPattern.size >= savedPattern.size) {
            return true
        }
        return false
    }

    private fun showResults(numErrors: Int) {
        val resultsDialog: AlertDialog = this.let {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.finalResultTitle))
            builder.setMessage("Game finished, you've made $numErrors mistakes, we're resetting the score " +
                    "for you to try again")
            builder.apply {
                setPositiveButton(R.string.ok
                ) { _, _ ->
                    isSettingColors = TRUE
                }
            }
            builder.create()
        }
        resultsDialog.show()
    }

    private fun getFinalScore(numErrors: Int): Int {
        val sizeTried = triedPattern.size
        return sizeTried-numErrors
    }

    private fun compareScores(): Int {
        var numErrors = 0
        savedPattern.forEachIndexed { index, item ->
            if (item!=triedPattern[index]) {
                numErrors++
            }
        }
        return numErrors
    }

    private fun resetGame() {
        score = 0
        gameScoreTextView.text = getString(R.string.yourScore, score)
        startTimer()
        gameStarted = false
    }

    private fun startTimer() {
        val initialTimeLeft = initialCountDown / 1000
        timeLeftTextView.text = getString(R.string.timeLeft, initialTimeLeft)
        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                val timeLeft = millisUntilFinished / 1000
                initialCountDown = timeLeft*1000
                timeLeftTextView.text = getString(R.string.timeLeft, timeLeft)
            }
            override fun onFinish() {
                endGame()
            }
        }
        timerRunning = TRUE
    }

    private fun pauseTimer() {
        if (timerRunning) {
            countDownTimer.cancel()
            timerRunning = FALSE
        } else {
            startTimer()
            countDownTimer.start()
        }
    }

    private fun incrementScore(incrementAmount: Int) {
        if (!gameStarted) {
            startGame()
        }
        score += incrementAmount
        val newScore = getString(R.string.yourScore,score)
        gameScoreTextView.text = newScore
    }

    private fun startGame() {
        countDownTimer.start()
        gameStarted = true
    }

    private fun endGame() {
        Toast.makeText(this, getString(R.string.gameOverMessage,score),Toast.LENGTH_LONG).show()
        resetGame()
    }
}
