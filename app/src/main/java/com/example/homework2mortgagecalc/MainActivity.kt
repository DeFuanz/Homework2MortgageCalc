package com.example.homework2mortgagecalc

import android.icu.text.NumberFormat
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homework2mortgagecalc.ui.theme.Homework2MortgageCalcTheme
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Homework2MortgageCalcTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    CalculatorLayout()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorLayout(modifier: Modifier = Modifier) {
    var amountInput by remember { mutableStateOf("") }
    var interestInput by remember { mutableStateOf("") }
    var yearsInput by remember { mutableStateOf("") }

    val amount = amountInput.toIntOrNull() ?: 0
    val interest = (interestInput.toDoubleOrNull() ?: 0.0) / 100 / 12
    val years = (yearsInput.toIntOrNull() ?: 0) * 12

    val monthlyPayment = calculateMortgage(amount, interest, years)

    Column(
        modifier
            .fillMaxSize()
            .padding(40.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.app_name),
            modifier
                .padding(bottom = 16.dp)
                .align(alignment = Alignment.Start)
        )
        ReusableTextFields(
            value = amountInput,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            leadingIcon = R.drawable.moneysign,
            onValueChange = { amountInput = it },
            title = stringResource(R.string.loan_amount),
            modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        ReusableTextFields(
            value = interestInput,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            leadingIcon = R.drawable.percent,
            onValueChange = { interestInput = it },
            title = stringResource(R.string.interest_rate),
            modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        ReusableTextFields(
            value = yearsInput,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            leadingIcon = R.drawable.calendar,
            onValueChange = { yearsInput = it },
            title = stringResource(R.string.number_of_years),
            modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        Row {
            Text(text = "Monthly Payment: ", fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Text(text = monthlyPayment, fontWeight = FontWeight.Bold, fontSize = 22.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReusableTextFields(
    value: String,
    keyboardOptions: KeyboardOptions,
    @DrawableRes leadingIcon: Int,
    onValueChange: (String) -> Unit,
    title: String,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        leadingIcon = { Icon(painter = painterResource(id = leadingIcon), null)},
        onValueChange = onValueChange,
        label = { Text(title) },
        singleLine = true,
        keyboardOptions = keyboardOptions,
        modifier = modifier
    )
}

private fun calculateMortgage(p: Int, r: Double, n: Int): String {
    var mortgage = p * ((r * (1.0 + r).pow(n)) / ((1.0 + r).pow(n) - 1))
    if (mortgage.isNaN() || mortgage.isInfinite()) mortgage = 0.0
    return NumberFormat.getCurrencyInstance().format(mortgage)
}

