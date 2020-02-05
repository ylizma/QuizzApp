package com.example.quizzapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
        private Button btn;
        private TextView nbrvalue;
        private TextView errortext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn=findViewById(R.id.button);
        nbrvalue=findViewById(R.id.numbervalue);
        errortext=findViewById(R.id.errormsg);

        btn.setOnClickListener(ev->{
            if (nbrvalue.getText().toString().isEmpty() || Integer.parseInt(nbrvalue.getText().toString())<=0 || Integer.parseInt(nbrvalue.getText().toString())>100){
                this.errortext.setText(R.string.errormsg);
            }else{
                int number =Integer.parseInt(this.nbrvalue.getText().toString());
                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                intent.putExtra("choosednumber",number);
                startActivity(intent);
            }

        });


    }
}
