package br.com.ifra;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button btConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //changeLanguage(, Idiomas.Portugues);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btConfig = findViewById(R.id.bt_config);
        btConfig.setOnClickListener(view -> {
            Intent navegar_config = new Intent(this, ConfigurationActivity.class);
            startActivity(navegar_config);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    /*
    private void changeLanguage(Activity activity, Idiomas idioma) {
        Locale locale = new Locale(idioma.getValue()[0], idioma.getValue()[1]);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);

        MessageUtil.setLocale(locale);

        // Recriar a Activity para aplicar a nova configuração de linguagem
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }*/
}