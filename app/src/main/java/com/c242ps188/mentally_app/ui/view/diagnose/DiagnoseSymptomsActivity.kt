package com.c242ps188.mentally_app.ui.view.diagnose

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.C242PS188.mentally_app.R
import com.C242PS188.mentally_app.databinding.ActivityDiagnoseSymptomsBinding
import com.c242ps188.mentally_app.data.local.model.MentalHealthData
import com.c242ps188.mentally_app.data.local.model.MentalHealthRequest
import com.c242ps188.mentally_app.ui.viewmodel.DiagnoseViewModel
import com.c242ps188.mentally_app.ui.viewmodel.UsersViewModel
import com.c242ps188.mentally_app.ui.viewmodel.ViewModelFactory

class DiagnoseSymptomsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiagnoseSymptomsBinding
    private val ageOptions = (10..45).map { it.toString() }
    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(this)
    }
    private val diagnoseViewModel: DiagnoseViewModel by viewModels { factory }
    private val usersViewModel: UsersViewModel by viewModels { factory }
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDiagnoseSymptomsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
        observe()
        setListener()
        updateUI()
    }

    @SuppressLint("SetTextI18n")
    private fun observe() {
        diagnoseViewModel.diagnoseConfidence.observe(this) {
            val confidence = it?.replace("%", "")?.toFloat()

            if (confidence != null) {
                binding.tvSkorResult.text = "${confidence.toInt()}%"
                binding.circularProgressBar.setProgressWithAnimation(confidence, 1000)

                when {
                    confidence < 25 -> {
                        binding.tvResult.text = getString(R.string.normal)
                        binding.tvResultSugestion.text =
                            getString(R.string.normal_result)
                    }

                    confidence in 25.0..29.9 -> {
                        binding.tvResult.text = getString(R.string.low_severity)
                        binding.tvResultSugestion.text =
                            getString(R.string.low_sevirity_result)
                    }

                    confidence in 30.0..59.9 -> {
                        binding.tvResult.text = getString(R.string.moderate_severity)
                        binding.tvResultSugestion.text =
                            getString(R.string.moderete_severity)
                    }

                    confidence in 60.0..79.9 -> {
                        binding.tvResult.text = getString(R.string.high_severity)
                        binding.tvResultSugestion.text =
                            getString(R.string.high_sevirity)
                    }

                    confidence >= 80 -> {
                        binding.tvResult.text = getString(R.string.very_high_severity)
                        binding.tvResultSugestion.text =
                            getString(R.string.very_high_severity_result)
                    }
                }
            }
        }

        usersViewModel.getId.observe(this) { id ->
            id?.let {
                userId = it
            }
        }

        diagnoseViewModel.diagnoseStatus.observe(this) {
            if (it == 200) {
                diagnoseViewModel.incrementProgress()
            }
        }

        diagnoseViewModel.diagnoseMessage.observe(this) {
            if (it != null) {
                showToast(it)
            }
        }

        diagnoseViewModel.isLoading.observe(this){
            showLoading(it)
        }
    }

    private fun updateUI() {
        diagnoseViewModel.diagnoseProgress.observe(this) { progress ->
            when (progress) {

                1 -> {
                    binding.diagnose1.visibility = View.VISIBLE
                    binding.diagnose2.visibility = View.GONE
                    binding.diagnose3.visibility = View.GONE
                    binding.skor.visibility = View.GONE
                    binding.btnFinish.visibility = View.GONE
                    binding.diagnose4.visibility = View.GONE
                    binding.diagnose5.visibility = View.GONE
                    binding.btnNext.visibility = View.VISIBLE
                    binding.btnBackDiagnose.visibility = View.GONE
                    binding.diagnose6.visibility = View.GONE
                    binding.diagnose7.visibility = View.GONE
                    binding.diagnose8.visibility = View.GONE
                    binding.diagnose9.visibility = View.GONE
                }

                2 -> {
                    binding.diagnose3.visibility = View.GONE
                    binding.diagnose4.visibility = View.GONE
                    binding.diagnose5.visibility = View.GONE
                    binding.diagnose2.visibility = View.VISIBLE
                    binding.skor.visibility = View.GONE
                    binding.diagnose1.visibility = View.GONE
                    binding.btnBackDiagnose.visibility = View.VISIBLE
                    binding.diagnose6.visibility = View.GONE
                    binding.btnFinish.visibility = View.GONE
                    binding.diagnose7.visibility = View.GONE
                    binding.diagnose8.visibility = View.GONE
                    binding.diagnose9.visibility = View.GONE
                }

                3 -> {
                    binding.diagnose3.visibility = View.VISIBLE
                    binding.diagnose2.visibility = View.GONE
                    binding.diagnose4.visibility = View.GONE
                    binding.diagnose5.visibility = View.GONE
                    binding.diagnose1.visibility = View.GONE
                    binding.btnBackDiagnose.visibility = View.VISIBLE
                    binding.skor.visibility = View.GONE
                    binding.diagnose6.visibility = View.GONE
                    binding.diagnose7.visibility = View.GONE
                    binding.btnFinish.visibility = View.GONE
                    binding.diagnose8.visibility = View.GONE
                    binding.diagnose9.visibility = View.GONE
                }

                4 -> {
                    binding.diagnose3.visibility = View.GONE
                    binding.diagnose2.visibility = View.GONE
                    binding.diagnose4.visibility = View.VISIBLE
                    binding.skor.visibility = View.GONE
                    binding.diagnose6.visibility = View.GONE
                    binding.btnFinish.visibility = View.GONE
                    binding.diagnose5.visibility = View.GONE
                    binding.diagnose1.visibility = View.GONE
                    binding.btnBackDiagnose.visibility = View.VISIBLE
                    binding.diagnose7.visibility = View.GONE
                    binding.diagnose8.visibility = View.GONE
                    binding.diagnose9.visibility = View.GONE
                }

                5 -> {
                    binding.diagnose3.visibility = View.GONE
                    binding.diagnose2.visibility = View.GONE
                    binding.diagnose4.visibility = View.GONE
                    binding.skor.visibility = View.GONE
                    binding.diagnose5.visibility = View.VISIBLE
                    binding.btnFinish.visibility = View.GONE
                    binding.diagnose6.visibility = View.GONE
                    binding.diagnose1.visibility = View.GONE
                    binding.btnBackDiagnose.visibility = View.VISIBLE
                    binding.diagnose7.visibility = View.GONE
                    binding.diagnose8.visibility = View.GONE
                    binding.diagnose9.visibility = View.GONE
                }

                6 -> {
                    binding.diagnose3.visibility = View.GONE
                    binding.diagnose2.visibility = View.GONE
                    binding.diagnose4.visibility = View.GONE
                    binding.diagnose5.visibility = View.GONE
                    binding.skor.visibility = View.GONE
                    binding.diagnose6.visibility = View.VISIBLE
                    binding.diagnose1.visibility = View.GONE
                    binding.btnBackDiagnose.visibility = View.VISIBLE
                    binding.btnFinish.visibility = View.GONE
                    binding.diagnose7.visibility = View.GONE
                    binding.diagnose8.visibility = View.GONE
                    binding.diagnose9.visibility = View.GONE
                }

                7 -> {
                    binding.diagnose3.visibility = View.GONE
                    binding.diagnose2.visibility = View.GONE
                    binding.diagnose4.visibility = View.GONE
                    binding.diagnose5.visibility = View.GONE
                    binding.skor.visibility = View.GONE
                    binding.diagnose6.visibility = View.GONE
                    binding.diagnose1.visibility = View.GONE
                    binding.diagnose9.visibility = View.GONE
                    binding.btnBackDiagnose.visibility = View.VISIBLE
                    binding.btnFinish.visibility = View.GONE
                    binding.diagnose7.visibility = View.VISIBLE
                    binding.diagnose8.visibility = View.GONE
                }

                8 -> {
                    binding.diagnose3.visibility = View.GONE
                    binding.diagnose2.visibility = View.GONE
                    binding.diagnose4.visibility = View.GONE
                    binding.diagnose5.visibility = View.GONE
                    binding.btnFinish.visibility = View.GONE
                    binding.diagnose6.visibility = View.GONE
                    binding.diagnose1.visibility = View.GONE
                    binding.skor.visibility = View.GONE
                    binding.btnBackDiagnose.visibility = View.VISIBLE
                    binding.diagnose7.visibility = View.GONE
                    binding.diagnose8.visibility = View.VISIBLE
                    binding.diagnose9.visibility = View.GONE
                }

                9 -> {
                    binding.diagnose3.visibility = View.GONE
                    binding.diagnose2.visibility = View.GONE
                    binding.diagnose4.visibility = View.GONE
                    binding.diagnose5.visibility = View.GONE
                    binding.diagnose6.visibility = View.GONE
                    binding.diagnose1.visibility = View.GONE
                    binding.btnFinish.visibility = View.GONE
                    binding.btnBackDiagnose.visibility = View.VISIBLE
                    binding.diagnose7.visibility = View.GONE
                    binding.diagnose8.visibility = View.GONE
                    binding.skor.visibility = View.GONE
                    binding.diagnose9.visibility = View.VISIBLE
                }

                11 -> {
                    binding.diagnose3.visibility = View.GONE
                    binding.diagnose2.visibility = View.GONE
                    binding.diagnose4.visibility = View.GONE
                    binding.diagnose5.visibility = View.GONE
                    binding.diagnose6.visibility = View.GONE
                    binding.diagnose1.visibility = View.GONE
                    binding.btnBackDiagnose.visibility = View.GONE
                    binding.btnNext.visibility = View.GONE
                    binding.diagnose7.visibility = View.GONE
                    binding.diagnose8.visibility = View.GONE
                    binding.btnFinish.visibility = View.VISIBLE
                    binding.diagnose9.visibility = View.GONE
                    binding.skor.visibility = View.VISIBLE
                }

            }
        }

    }


    private fun setListener() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            ageOptions
        )

        binding.btnFinish.setOnClickListener {
            finish()
            diagnoseViewModel.resetProgress()
            diagnoseViewModel.resetDiagnoseMessage()
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerAge.adapter = adapter

        binding.spinnerAge.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                parent?.let {
                    val inputAge = it.getItemAtPosition(position).toString()
                    diagnoseViewModel.age = inputAge.toFloat()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }

        binding.radioGroupGender.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_male -> {
                    diagnoseViewModel.gender = 1
                }

                R.id.radio_female -> {
                    diagnoseViewModel.gender = 0
                }
            }
        }

        // WORK PRESSURE
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_very_low -> {
                    diagnoseViewModel.workPressure = 1F
                }

                R.id.rb_low -> {
                    diagnoseViewModel.workPressure = 2F
                }

                R.id.rb_moderate -> {
                    diagnoseViewModel.workPressure = 3F
                }

                R.id.rb_high -> {
                    diagnoseViewModel.workPressure = 4F
                }

                R.id.rb_very_high -> {
                    diagnoseViewModel.workPressure = 5F
                }
            }
        }

        // WORK SATISFIED
        binding.radioGroup2.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_very_low2 -> {
                    diagnoseViewModel.workSatisfied = 1F
                }

                R.id.rb_low2 -> {
                    diagnoseViewModel.workSatisfied = 2F
                }

                R.id.rb_moderate2 -> {
                    diagnoseViewModel.workSatisfied = 3F
                }

                R.id.rb_high2 -> {
                    diagnoseViewModel.workSatisfied = 4F
                }

                R.id.rb_very_high2 -> {
                    diagnoseViewModel.workSatisfied = 5F
                }
            }
        }

        // STRESS LEVEL
        binding.radioGroup3.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_very_low3 -> {
                    diagnoseViewModel.stressLevel = 1F
                }

                R.id.rb_low3 -> {
                    diagnoseViewModel.stressLevel = 2F
                }

                R.id.rb_moderate3 -> {
                    diagnoseViewModel.stressLevel = 3F
                }

                R.id.rb_high3 -> {
                    diagnoseViewModel.stressLevel = 4F
                }

                R.id.rb_very_high3 -> {
                    diagnoseViewModel.stressLevel = 5F
                }
            }
        }

        // HABITS
        binding.radioGroup4.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_healthy -> {
                    diagnoseViewModel.dietaryHabits = 0F
                }

                R.id.rb_moderate4 -> {
                    diagnoseViewModel.dietaryHabits = 1F
                }

                R.id.rb_unHealthy -> {
                    diagnoseViewModel.dietaryHabits = 2F
                }
            }
        }

        // SLEEP HOURS
        binding.radioGroup5.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_5hours -> {
                    diagnoseViewModel.sleepHours = 0F
                }

                R.id.rb_5_6_hourse -> {
                    diagnoseViewModel.sleepHours = 1F
                }

                R.id.rb_7_8_hourse -> {
                    diagnoseViewModel.sleepHours = 2F
                }

                R.id.rb_8hourse -> {
                    diagnoseViewModel.sleepHours = 3F
                }
            }
        }

        // WORK HOURS
        binding.edWorkHours.addTextChangedListener { editable ->
            val workHours = editable?.toString() ?: ""
            diagnoseViewModel.workHours = workHours.toFloatOrNull() ?: 0f
        }

        // SELF HARM
        binding.radioGroup7.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_yes7 -> {
                    diagnoseViewModel.selfHarm = 1F
                }

                R.id.rb_no7 -> {
                    diagnoseViewModel.selfHarm = 0F
                }
            }
        }

        // HISTORY MENTAL
        binding.radioGroup8.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_yes8 -> {
                    diagnoseViewModel.historyMental = 1F
                }

                R.id.rb_no8 -> {
                    diagnoseViewModel.historyMental = 0F
                }
            }
        }

        binding.btnNext.setOnClickListener {
            if (diagnoseViewModel.diagnoseProgress.value != 11) {
                diagnoseViewModel.incrementProgress()
            }

            var valid = true

            if (diagnoseViewModel.age == null || diagnoseViewModel.gender == null || diagnoseViewModel.selfHarm == null
                || diagnoseViewModel.dietaryHabits == null || diagnoseViewModel.sleepHours == null
                || diagnoseViewModel.workSatisfied == null || diagnoseViewModel.workPressure == null || diagnoseViewModel.historyMental == null
                || diagnoseViewModel.stressLevel == null || diagnoseViewModel.workHours == null
            ) {
                valid = false
            }

            if (diagnoseViewModel.diagnoseProgress.value == 10 && valid) {
                val age = diagnoseViewModel.age
                val gender = diagnoseViewModel.gender
                val workPressure = diagnoseViewModel.workPressure
                val jobSatisfaction = diagnoseViewModel.workSatisfied
                val sleepDuration = diagnoseViewModel.sleepHours
                val dietaryHabits = diagnoseViewModel.dietaryHabits
                val suicidalThoughts = diagnoseViewModel.selfHarm
                val workHours = diagnoseViewModel.workHours
                val financialStress = diagnoseViewModel.stressLevel
                val historyMentalIllness = diagnoseViewModel.historyMental

                val data = MentalHealthData(
                    age,
                    gender,
                    workPressure,
                    jobSatisfaction,
                    sleepDuration,
                    dietaryHabits,
                    suicidalThoughts,
                    workHours,
                    financialStress, historyMentalIllness
                )

                val diagnoseRequest = userId?.let { it1 -> MentalHealthRequest(it1, data) }

                if (diagnoseRequest != null) {
                    diagnoseViewModel.submitMentalHealthData(diagnoseRequest)
                }

            } else if (diagnoseViewModel.diagnoseProgress.value == 10 && !valid) {
                diagnoseViewModel.decrementProgress()
                showToast(getString(R.string.please_fill_in_all_the_data))
            }
        }

        // VALIDATE INPUT WORK HOURS
        binding.edWorkHours.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val input = s?.toString()?.toIntOrNull()
                if (input !in 1..12) {
                    s?.clear()
                    diagnoseViewModel.workHours = null
                    binding.edWorkHours.error =
                        getString(R.string.input_should_be_1_hour_to_12_hours)
                }
            }

        })

        binding.btnBackDiagnose.setOnClickListener {
            diagnoseViewModel.decrementProgress()
        }

        binding.btnBack.setOnClickListener {
            finish()
            diagnoseViewModel.resetDiagnoseMessage()
            diagnoseViewModel.resetProgress()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@DiagnoseSymptomsActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(loading: Boolean) {
        if (loading) {
            binding.diagnose3.visibility = View.GONE
            binding.diagnose2.visibility = View.GONE
            binding.diagnose4.visibility = View.GONE
            binding.diagnose5.visibility = View.GONE
            binding.diagnose6.visibility = View.GONE
            binding.diagnose1.visibility = View.GONE
            binding.btnBackDiagnose.visibility = View.GONE
            binding.btnNext.visibility = View.GONE
            binding.diagnose7.visibility = View.GONE
            binding.diagnose8.visibility = View.GONE
            binding.btnFinish.visibility = View.GONE
            binding.diagnose9.visibility = View.GONE
            binding.skor.visibility = View.GONE
            binding.loading.visibility = View.VISIBLE
        } else {
            binding.diagnose3.visibility = View.GONE
            binding.diagnose2.visibility = View.GONE
            binding.diagnose4.visibility = View.GONE
            binding.diagnose5.visibility = View.GONE
            binding.diagnose6.visibility = View.GONE
            binding.diagnose1.visibility = View.GONE
            binding.btnBackDiagnose.visibility = View.GONE
            binding.btnNext.visibility = View.GONE
            binding.diagnose7.visibility = View.GONE
            binding.diagnose8.visibility = View.GONE
            binding.btnFinish.visibility = View.VISIBLE
            binding.diagnose9.visibility = View.GONE
            binding.skor.visibility = View.VISIBLE
            binding.loading.visibility = View.GONE
        }
    }

//    private fun ageScaling(age: Int, max: Int, min: Int): Float {
//        return (age.toFloat() - min.toFloat()) / (max.toFloat() - min.toFloat())
//    }
}