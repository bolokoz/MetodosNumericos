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

	EditText primeiroTermo, primeiroTermoElevado, segundoTermo,
			segundoTermoElevado, termoIndependente, intervaloMenor,
			intervaloMaior, erro, iteracoes;
	Button calcular, reset;
	TextView invisivelTexto, funcaoResultante;
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
		primeiroTermoElevado = (EditText) findViewById(R.id.primeiroTermoElevadoEditText);
		segundoTermo = (EditText) findViewById(R.id.SegundoTermoEditText);
		segundoTermoElevado = (EditText) findViewById(R.id.segundoTermoElevadoEditText);
		termoIndependente = (EditText) findViewById(R.id.termoIndependenteEditText);
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

		if (!isEmpty()) {

			double termo1 = Double
					.valueOf((primeiroTermo.getText().toString()));
			double termo1e = Double.valueOf(primeiroTermoElevado.getText()
					.toString());
			double termo2 = Double.valueOf(segundoTermo.getText()
					.toString());
			double termo2e = Double.valueOf(segundoTermoElevado.getText()
					.toString());
			double termoInddy = Double.valueOf(termoIndependente.getText()
					.toString());

			double a = Double.valueOf((intervaloMenor.getText().toString()));
			double b = Double.valueOf(intervaloMaior.getText().toString());
			double error = Double.parseDouble(erro.getText().toString());
			int i = Integer.parseInt((iteracoes.getText().toString()));

			a_array = new double[i];
			b_array = new double[i];
			c_array = new double[i];
			fc_array = new double[i];

			fa = Math.pow((termo1 * a), termo1e)
					+ Math.pow((termo2 * a), termo2e) + termoInddy;
			fb = Math.pow((termo1 * b), termo1e)
					+ Math.pow((termo2 * b), termo2e) + termoInddy;
			j = i; // aux
			i = 0; // aux

			do {
				// Primeiro passo, achar f(a) e f(b) e garantir que f(a).f(b) <
				// 0

				if (fa == 0) {
					raiz = a;
					ocorrencia = "Intervalo menor � raiz";

					break;
				} else if (fb == 0) {
					raiz = b;
					ocorrencia = "Intervalo maior � raiz";

					break;
				} else if (fa*fb> 0) {
					ocorrencia = "N�o h� raiz ou h� mais de uma no intervalo";

					break;
				} else {

					c = (a + b) / 2;
					fc = Math.pow((termo1 * c), termo1e)
							+ Math.pow((termo2 * c), termo2e) + termoInddy;

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

					fc = (fc <= 0.0F) ? 0.0F - fc : fc; // valor absoluto de
														// funcao
														// de c

					if (fc < error) {
						margem = false;
					}

				}
			} while ((i != j) && margem);

			if (ocorrencia.isEmpty()) {

				margem = true;
				invisivelLinha.setVisibility(View.VISIBLE);

				setListAdapter(new BisseccaoAdapter(a_array, b_array, c_array,
						fc_array));

			} else {
				invisivelLinha.setVisibility(View.GONE);
				invisivelTexto.setVisibility(View.VISIBLE);
				invisivelTexto.setText(ocorrencia);
				Toast toast = Toast.makeText(getApplicationContext(),
						ocorrencia, Toast.LENGTH_LONG);
				toast.show();
			}
		}else{
			ocorrencia = "Cheque os campos";
			Toast toast = Toast.makeText(getApplicationContext(),
					ocorrencia, Toast.LENGTH_LONG);
			toast.show();
		}
	}

	public void resetar(View v) {

		primeiroTermo.setText("");
		intervaloMenor.setText("");
		intervaloMaior.setText("");
		erro.setText("");
		iteracoes.setText("");

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
				ocorrencia = "Intervalo menor � raiz";

				break;
			} else if (fb == 0) {
				raiz = b;
				ocorrencia = "Intervalo maior � raiz";

				break;
			} else if ((fa > 0 && fb > 0) || (fa < 0 && fb < 0)) {
				ocorrencia = "N�o h� raiz ou h� mais de uma no intervalo";

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

			margem = true;
			invisivelLinha.setVisibility(View.VISIBLE);

			setListAdapter(new BisseccaoAdapter(a_array, b_array, c_array,
					fc_array));

		} else {
			invisivelLinha.setVisibility(View.GONE);
			invisivelTexto.setVisibility(View.VISIBLE);
			invisivelTexto.setText(ocorrencia);
			Toast toast = Toast.makeText(getApplicationContext(), ocorrencia,
					Toast.LENGTH_LONG);
			toast.show();
		}

	}

	public boolean isEmpty() {

		if (primeiroTermo.getText().toString().isEmpty()
				|| primeiroTermoElevado.getText().toString().isEmpty()
				|| segundoTermo.getText().toString().isEmpty()
				|| segundoTermoElevado.getText().toString().isEmpty()
				|| intervaloMaior.getText().toString().isEmpty()
				|| intervaloMenor.getText().toString().isEmpty()
				|| iteracoes.getText().toString().isEmpty()) {

			return true;
		} else
			return false;
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

			for (i = 0; i < a2.length; i++) {
				if (a2[i] == 0) {
					break;
				}

			}

			return i;
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
