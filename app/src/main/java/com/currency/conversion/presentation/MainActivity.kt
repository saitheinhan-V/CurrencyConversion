package com.currency.conversion.presentation

import android.R
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.currency.conversion.databinding.ActivityMainBinding
import com.currency.conversion.domain.model.Currency
import com.currency.conversion.domain.model.Rate
import com.currency.conversion.domain.vo.CurrencyListVo
import com.currency.conversion.domain.vo.CurrencyRateVo
import com.currency.conversion.presentation.adapter.RateAdapter
import com.currency.conversion.presentation.event.MainEvent
import com.currency.conversion.presentation.state.Status
import com.currency.conversion.presentation.utils.LoadingDialog
import com.currency.conversion.presentation.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), RateAdapter.ItemClickListener {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()
    private lateinit var currencyList: MutableList<Currency>
    private lateinit var rateList: MutableList<Rate>
    private lateinit var currencyNameList: MutableList<String>
    private lateinit var manager: LinearLayoutManager
    private lateinit var rateAdapter: RateAdapter
    private var sourceAmount: Double? = 1.0

    private lateinit var loadingDialog: LoadingDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)

        loadingDialog = LoadingDialog(this)
        currencyList = arrayListOf()
        rateList = arrayListOf()
        currencyNameList = arrayListOf()
        manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {
            observeEvent()
        }

        setUpRV()

        //fetch CurrencyList from API
        viewModel.fetchCurrencyList()
        observeCurrencyList()

        //currency amount change listener //edit text
        binding.edtCurrencyAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(text: Editable?) {
                sourceAmount = text.toString().trim().toDoubleOrNull() ?: 0.0
                Log.i("update", "Update => $sourceAmount ${rateList.size} $rateList")
                rateAdapter.updateAmount(amount = sourceAmount!!)
            }

        })

        binding.edtCurrencyAmount.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                true
            } else {
                false
            }
        }

        setContentView(binding.root)
    }

    private fun setUpRV() {
        rateAdapter = RateAdapter(rateList = rateList, sourceAmount!!, this)
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = rateAdapter
    }


    private fun observeCurrencyList() {
        lifecycleScope.launch {
            viewModel.currencyListState.collect {
                when (it.status) {
                    Status.SUCCESS -> {
                        Log.i("data", "Data -> ${it.data}")
                        loadingDialog.hideDialog()

                        currencyNameList.clear()
                        val currencyList = it.data!!
                        if (currencyList.isNotEmpty()) {
                            for (j in currencyList.indices) {
                                currencyNameList.add("${currencyList[j].key} (${currencyList[j].name})")
                            }

                            setUpSpinner()
                            if (currencyNameList.size != 0) {
                                binding.spCurrency.setSelection(0)
                            }
                        }

                    }

                    Status.ERROR -> {
                        loadingDialog.hideDialog()
                    }

                    Status.LOADING -> {
                        loadingDialog.showDialog("")
                    }
                }
            }
        }
    }

    private fun setUpSpinner() {
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, currencyNameList)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spCurrency.adapter = adapter

        binding.spCurrency.dropDownVerticalOffset = binding.spCurrency.height
        binding.spCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                hideKeyboard()
                loadingDialog.showDialog("")
                val selectedItem = currencyNameList[position].substring(0,3)
                Toast.makeText(
                    this@MainActivity,
                    "Selected item: $selectedItem",
                    Toast.LENGTH_SHORT
                ).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    viewModel.fetchCurrencyRate(selectedItem)
                }, 1000)

                //update rate depend on source // "USD","AED", etc...
                observeRate()

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

    }

    private fun observeRate() {
        lifecycleScope.launch {
            viewModel.currencyRateState.collect {
                when (it.status) {
                    Status.SUCCESS -> {
                        loadingDialog.hideDialog()

                        rateList.clear()
                        rateList = it.data!!

                        if (rateList.isNotEmpty()) {
                            Log.i("rate", "Rate => Size ${rateList.size} $rateList")

                            rateAdapter.updateData(newRates = rateList.toList())
                        }
                    }

                    Status.ERROR -> {
                        loadingDialog.hideDialog()
                    }

                    Status.LOADING -> {
                        loadingDialog.showDialog("")
                    }
                }
            }
        }
    }

    private suspend fun observeEvent() {
        viewModel.uiEvent.collectLatest {
            when (it) {
                is MainEvent.showSnack -> {
                    Snackbar.make(binding.root, it.message, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onRateItemClick(data: Rate) {
        //click
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

}