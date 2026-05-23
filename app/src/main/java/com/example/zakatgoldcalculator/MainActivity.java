package com.example.zakatgoldcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.WindowCompat;

public class MainActivity extends AppCompatActivity {

    EditText etWeight, etGoldValue;
    RadioButton rbKeep, rbWear;
    Button btnCalculate, btnReset;
    TextView tvTotalGoldValue, tvZakatPayable, tvTotalZakat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etWeight = findViewById(R.id.etWeight);
        etGoldValue = findViewById(R.id.etGoldValue);

        rbKeep = findViewById(R.id.rbKeep);
        rbWear = findViewById(R.id.rbWear);

        btnCalculate = findViewById(R.id.btnCalculate);
        btnReset = findViewById(R.id.btnReset);

        tvTotalGoldValue = findViewById(R.id.tvTotalGoldValue);
        tvZakatPayable = findViewById(R.id.tvZakatPayable);
        tvTotalZakat = findViewById(R.id.tvTotalZakat);

        btnCalculate.setOnClickListener(v -> calculateZakat());
        btnReset.setOnClickListener(v -> resetForm());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuShare) {
            String shareText = getString(R.string.app_name);

            String weightText = etWeight.getText().toString().trim();

            if (!weightText.isEmpty()) {
                shareText += "\n\n" + tvTotalGoldValue.getText()
                        + "\n" + tvZakatPayable.getText()
                        + "\n" + tvTotalZakat.getText();
            }

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share)));
            return true;
        }

        if (item.getItemId() == R.id.menuAbout) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void calculateZakat() {

        String weightText = etWeight.getText().toString().trim();
        String valueText = etGoldValue.getText().toString().trim();

        if (weightText.isEmpty()) {
            etWeight.setError("Please enter gold weight");
            etWeight.requestFocus();
            return;
        }

        if (valueText.isEmpty()) {
            etGoldValue.setError("Please enter current gold value");
            etGoldValue.requestFocus();
            return;
        }

        double weight = Double.parseDouble(weightText);
        double goldValue = Double.parseDouble(valueText);

        double uruf;

        if (rbKeep.isChecked()) {
            uruf = 85;
        } else {
            uruf = 200;
        }

        // Gold weight minus uruf
        double goldWeightMinusUruf = weight - uruf;

        // For zakat calculation, negative value becomes 0
        double taxableWeight = goldWeightMinusUruf;

        if (taxableWeight < 0) {
            taxableWeight = 0;
        }

        double zakatPayable = taxableWeight * goldValue;

        double totalZakat = zakatPayable * 0.025;

        tvTotalGoldValue.setText(
                "💜  Gold Weight minus Uruf             "
                        + String.format("%.2f", goldWeightMinusUruf) + " g");

        tvZakatPayable.setText(
                "⚖️  Zakat Payable Value                RM "
                        + String.format("%.2f", zakatPayable));

        tvTotalZakat.setText(
                "✅  Total Zakat (2.5%)                  \nRM "
                        + String.format("%.2f", totalZakat));
    }

    private void resetForm() {

        etWeight.setText("");
        etGoldValue.setText("");

        rbKeep.setChecked(true);

        tvTotalGoldValue.setText(
                "💜  Gold Weight minus Uruf             0.00 g");

        tvZakatPayable.setText(
                "⚖️  Zakat Payable Value                RM 0.00");

        tvTotalZakat.setText(
                "✅  Total Zakat (2.5%)                  RM 0.00");

        etWeight.requestFocus();
    }
}
