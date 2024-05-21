package br.com.ifra;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

import br.com.ifra.enums.IdiomaEnum;
import br.com.ifra.utils.MessageUtil;

public class ConfigurationActivity extends AppCompatActivity {

    private Spinner spinner_idiomas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_configuration);

        spinner_idiomas = findViewById(R.id.spinner_idioma);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.lista_idiomas, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinner_idiomas.setAdapter(arrayAdapter);
        spinner_idiomas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                IdiomaEnum idioma = IdiomaEnum.fromInt(position);
                if(idioma != null){
                    selecionarIdioma(idioma.name());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void selecionarIdioma(String idioma) {
        Locale locale = new Locale(idioma);
        Locale.setDefault(locale);

        Resources resources = this.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        this.createConfigurationContext(configuration);

        MessageUtil.setLocale(locale);
    }

}