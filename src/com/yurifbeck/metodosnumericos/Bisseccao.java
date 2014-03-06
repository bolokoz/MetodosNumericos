package com.yurifbeck.metodosnumericos;

import java.text.DecimalFormat;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Bisseccao extends ListActivity {

	EditText primeiroTermo, intervaloMenor, intervaloMaior, erro, iteracoes;
	Button calcular, reset;
	TextView invisivelTexto;
	ListView list;
	TableRow invisivelLinha;
	double a, b, c, error, x, raiz, fa, fb, fc;
	int i, j;
	boolean margem = true;
	String ocorrencia = "";
	double[] a_array;
	double[] b_array;
	double[] c_array;
	double[] fc_array;

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
		invisivelLinha = (TableRow) findViewById(R.id.tableRowInvisivel);
		invisivelTexto = (TextView) findViewById(R.id.textViewInvisivel);
	}

	public void calcular(View v) {

		/*
		 * x = double.parsedouble(primeiroTermo.getText().toString()); a =
		 * double.parsedouble(intervaloMenor.getText().toString()); b =
		 * double.parsedouble(intervaloMaior.getText().toString()); error =
		 * double.parsedouble(erro.getText().toString()); i =
		 * Integer.parseInt(iteracoes.getText().toString());
		 */

		x = 1f;
		a = -2.0f;
		b = 1f;
		error = 0.001f;
		i = 15;

		a_array = new double[i];
		b_array = new double[i];
		c_array = new double[i];
		fc_array = new double[i];

		fa = x * a; // funcao de a
		fb = x * b; // funcao de b
		j = i; // aux
		i = 0; // aux

		do {
			// Primeiro passo, achar f(a) e f(b) e garantir que f(a).f(b) < 0

			if (fa == 0) {
				raiz = a;
				ocorrencia = "Intervalo menor é raiz";
				invisivelTexto.setVisibility(View.VISIBLE);
				invisivelTexto.setText(ocorrencia);
				break;
			} else if (fb == 0) {
				raiz = b;
				ocorrencia = "Intervalo maior é raiz";
				invisivelTexto.setVisibility(View.VISIBLE);
				invisivelTexto.setText(ocorrencia);
				break;
			} else if ((fa > 0 && fb > 0) || (fa < 0 && fb < 0)) {
				ocorrencia = "Não há raiz ou há mais de uma no intervalo";
				invisivelTexto.setVisibility(View.VISIBLE);
				invisivelTexto.setText(ocorrencia);
				break;
			} else {

				c = (a + b) / 2;
				fc = x * c;

				a_array[i] = a;
				b_array[i] = b;
				c_array[i] = c;
				fc_array[i] = fc;

				if (fc * fa < 0) {

					b = c;
					i++;

				} else {

					a = c;
					i++;
				}

				fc = (fc <= 0.0F) ? 0.0F - fc : fc; // valor absoluto de funcao
													// de c

				if (fc < error) {
					margem = false;
				}

			}
		} while ((i != j) && margem);

		if (ocorrencia.isEmpty()) {

			Toast toast = Toast.makeText(getApplicationContext(),
					Double.toString(fc_array[2]), Toast.LENGTH_LONG);
			toast.show();

			setListAdapter(new BisseccaoAdapter(a_array, b_array, c_array,
					fc_array));
			/*
			 * double[] a_array = a_string.split(" ");
			 * 
			 * ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
			 * android.R.layout.simple_list_item_1, resultados);
			 * setListAdapter(adapter);
			 */

		} else {

			Toast toast = Toast.makeText(getApplicationContext(), ocorrencia,
					Toast.LENGTH_LONG);
			toast.show();
		}
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

	class BisseccaoAdapter extends BaseAdapter {

		double[] a2, b2, c2, fc2;

		BisseccaoAdapter() {
			a2 = null;
			b2 = null;
			c2 = null;
			fc2 = null;
		}

		public BisseccaoAdapter(double[] a_array, double[] b_array,
				double[] c_array, double[] fc_array) {

			a2 = a_array;
			b2 = b_array;
			c2 = c_array;
			fc2 = fc_array;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = getLayoutInflater();
			View row;
			row = inflater.inflate(R.layout.linha_listview, parent, false);
			TextView at, bt, ct, fct, i;
			i = (TextView) row.findViewById(R.id.lista_i);
			at = (TextView) row.findViewById(R.id.lista_a);
			bt = (TextView) row.findViewById(R.id.lista_b);
			ct = (TextView) row.findViewById(R.id.lista_c);
			fct = (TextView) row.findViewById(R.id.lista_fc);

			i.setText(Integer.toString(position + 1));
			at.setText(new DecimalFormat("#.#####").format(a2[position]));
			bt.setText(new DecimalFormat("#.#####").format(b2[position]));
			ct.setText(new DecimalFormat("#.#####").format(c2[position]));
			fct.setText(new DecimalFormat("#.########").format(fc2[position]));

			return (row);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return a2.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
	}

}
