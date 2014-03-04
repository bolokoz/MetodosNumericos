package com.yurifbeck.metodosnumericos;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class Bisseccao extends ListActivity{
	
	EditText primeiroTermo, intervaloMenor, intervaloMaior, erro, iteracoes;
	Button calcular, reset;
	ListView list;
	String[] resultados;
	float a, b, error;
	int i,j;
	boolean r;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.metodobisseccao);
		/*	
		primeiroTermo = (EditText) findViewById(R.id.primeiroTermoEditText);
		intervaloMaior = (EditText) findViewById(R.id.intervaloMaiorEditText);
		intervaloMenor = (EditText) findViewById(R.id.intervaloMenorEditText);
		erro = (EditText) findViewById(R.id.erroEditText);
		iteracoes = (EditText) findViewById(R.id.iteracoesEditText);
		calcular = (Button) findViewById(R.id.calcularButton);
		reset = (Button) findViewById(R.id.resetButton);
		
		
		calcular.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	a = Float.parseFloat(intervaloMenor.getText().toString());
        		b = Float.parseFloat(intervaloMaior.getText().toString());
        		error = Float.parseFloat(erro.getText().toString());
        		i = Integer.parseInt(iteracoes.getText().toString());
            }
        });
		
		reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	intervaloMenor.setText("");
        		intervaloMaior.setText("");
        		erro.setText("");
        		iteracoes.setText("");
            }
        });
		
	
		for(j=0;j<i;j++){
			
			
			
		}
		
		do{
			
			
		}while(r);
		
		
		
		
		
		
		
		
		
		
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, resultados);
            setListAdapter(adapter);
		
		*/
	}
	
	

}
