package com.yurifbeck.metodosnumericos;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class Bisseccao extends ListActivity {

	EditText primeiroTermo, intervaloMenor, intervaloMaior, erro, iteracoes;
	Button calcular, reset;
	ListView list;
	float a, b, c, error, x, raiz, fa, fb, fc;
	int i, j;
	boolean margem = true;
	String ocorrencia, a_string = "", b_string = "", c_string = "", fc_string = "";
	float[] a_array = new float[i];
	float[] b_array = new float[i];
	float[] c_array = new float[i];
	float[] resultados = new float[i];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.metodobisseccao);

		primeiroTermo = (EditText) findViewById(R.id.primeiroTermoEditText);
		intervaloMaior = (EditText) findViewById(R.id.intervaloMaiorEditText);
		intervaloMenor = (EditText) findViewById(R.id.intervaloMenorEditText);
		erro = (EditText) findViewById(R.id.erroEditText);
		iteracoes = (EditText) findViewById(R.id.iteracoesEditText);
		calcular = (Button) findViewById(R.id.calcularButton);
		reset = (Button) findViewById(R.id.resetButton);
	}

	public void calcular(View v) {

		x = Float.parseFloat(primeiroTermo.getText().toString());
		a = Float.parseFloat(intervaloMenor.getText().toString());
		b = Float.parseFloat(intervaloMaior.getText().toString());
		error = Float.parseFloat(erro.getText().toString());
		i = Integer.parseInt(iteracoes.getText().toString());

		do {
			// Primeiro passo, achar f(a) e f(b) e garantir que f(a).f(b) < 0
			fa = x * a; 	//funcao de a
			fb = x * b; 	//funcao de b
			j = i;      	//aux
			i = 1;			//aux
			if (fa == 0) {
				raiz = a;
				ocorrencia = "Intervalo menor é raiz";
				break;
			} else if (fb == 0) {
				raiz = b;
				ocorrencia = "Intervalo maior é raiz";
				break;
			} else if ((fa > 0 && fb > 0) || (fa < 0 && fb < 0)) {
				ocorrencia = "Não há raiz ou há mais de uma no intervalo";
				break;
			} else {
				
				c = (a + b) / 2;
				fc = x * c;
				
				a_string = a_string + " " + Float.toString(a); 
				b_string = b_string + " " + Float.toString(b);
				c_string = c_string + " " + Float.toString(c);
				fc_string = fc_string + " " + Float.toString(fc);

				if (fc * fa < 0) {

					b = c;
					i++;

				} else {

					a = c;
					i++;
				}

				fc = (fc <= 0.0F) ? 0.0F - fc : fc;  //valor absoluto de funcao de c
				
				if (fc < error) {
					margem = false;
				}

			}
		} while ((i != j+1) && margem);
		
		Toast toast = Toast.makeText(getApplicationContext(), Float.toString(fc), Toast.LENGTH_LONG);
		toast.show();
		/*
		float[] a_array = a_string.split(" "); 
		
		 * ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		 * android.R.layout.simple_list_item_1, resultados);
		 * setListAdapter(adapter);
		 */
		
	}

	public void resetar(View v) {

		primeiroTermo.setText("");
		intervaloMenor.setText("");
		intervaloMaior.setText("");
		erro.setText("");
		iteracoes.setText("");

	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bisseccao, menu);
		return true;
	}

}
