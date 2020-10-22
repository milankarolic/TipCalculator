package learning.mkarolic.tipcalculator

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import learning.mkarolic.tipcalculator.databinding.ActivityMainBinding

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initializing % of tip on SEEKBAR and detecting changes
        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvTipPercentage.text = "${seekBarTip.progress}%"
        updateTipDesc(INITIAL_TIP_PERCENT)

        seekBarTip.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChaged $progress")
                tvTipPercentage.text = "$progress%"
                updateTipDesc(progress)
                computeTipAndTotal()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        //edit text changes

        etBase.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                Log.i(
                    TAG,
                    "Edit text has been changed! $s"
                ) // $s is value that user has been typing
                computeTipAndTotal()
            }
        })


    }

    private fun updateTipDesc(tipPercent: Int) {
        val tipDescription: String
        when (tipPercent) {
            in 0..9 -> tipDescription = "Poor"
            in 10..14 -> tipDescription = "Acceptable"
            in 15..19 -> tipDescription = "Good"
            in 20..24 -> tipDescription = "Great!"
            else -> tipDescription = "Amazing!!!"
        }
        tvTipDesc.text = tipDescription
        val color = ArgbEvaluator().evaluate(
            tipPercent.toFloat() / seekBarTip.max,
            ContextCompat.getColor(this, R.color.colorWorstTip),
            ContextCompat.getColor(this, R.color.colorBestTip)
        ) as Int

        tvTipDesc.setTextColor(color)

    }

    private fun computeTipAndTotal() {
        // protecting empty string to convert do double!
        if (etBase.text.toString().isEmpty()) {
            tvTipAmount.text = ""
            tvTotalAmount.text = ""
            return
        }
        //Get the value of base and tip percentage
        val baseAmount = etBase.text.toString().toDouble()
        val tipPercent = seekBarTip.progress
        val tipAmount = baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipAmount

        tvTipAmount.text = "$${"%.2f".format(tipAmount)}" // returns 2 decimal points only
        tvTotalAmount.text = "$${"%.2f".format(totalAmount)}"

    }
}