package com.yurifbeck.metodosnumericos;

import java.text.DecimalFormat;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import org.nfunk.jep.JEP;

import android.support.v4.app.NavUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class Bisseccao extends ListActivity {

	private EditText primeiroTermo, intervaloMenor, intervaloMaior, erro,
			iteracoes;
	private Button calcular, reset;
	private TextView invisivelTexto;
	private ListView list;
	private TableRow invisivelLinha;
	private double a, b, c, error, x, raiz, fa, fb, fc;
	private int i, j;
	private boolean margem;
	private String ocorrencia = "";
	private double[] a_array;
	private double[] b_array;
	private double[] c_array;
	private double[] fc_array;
	private JEP myParser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.metodobisseccao);

		// Configura ActionBar
		ActionBar actionBar = getActionBar();
		actionBar.setSubtitle("Bissecção");

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		// XML para java
		primeiroTermo = (EditText) findViewById(R.id.primeiroTermoEditText);
		intervaloMaior = (EditText) findViewById(R.id.intervaloMaiorEditText);
		intervaloMenor = (EditText) findViewById(R.id.intervaloMenorEditText);
		erro = (EditText) findViewById(R.id.erroEditText);
		iteracoes = (EditText) findViewById(R.id.iteracoesEditText);
		calcular = (Button) findViewById(R.id.calcularButton);
		reset = (Button) findViewById(R.id.resetButton);
		invisivelLinha = (TableRow) findViewById(R.id.tableRowInvisivel);
		invisivelTexto = (TextView) findViewById(R.id.textViewInvisivel);

		// Fazer com que no último EditText ao pressionar 'enter' ser igual a
		// apertar o botao
		iteracoes.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == (EditorInfo.IME_ACTION_DONE)) {
					calcular(v);
				}
				return false;

			}
		});

	}

	// Menu no actionBar
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.bisseccao, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// actionBar
		case R.id.action_condicao:

			new AlertDialog.Builder(this)
					.setTitle("Condições")
					.setMessage("Deve exister apenas uma raiz no intervalo \n")
					.setNeutralButton(android.R.string.yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// continue with delete
								}
							}).show();
			break;

		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
			return true;
		}

		return true;
	}

	// Inicializar variaveis
	// Preparacoes para poder repetir o procedimento apos a primeira vez
	public void resetar() {

		margem = true;
		ocorrencia = "";
		raiz = Double.NaN;

		invisivelLinha.setVisibility(View.GONE);
		invisivelTexto.setVisibility(View.GONE);

	}

	// Verificar se os campos nao estao vazios
	// Obter dados dos EditTexts
	public boolean isEmptyAndGet() {

		if (primeiroTermo.getText().toString().isEmpty()
				|| intervaloMaior.getText().toString().isEmpty()
				|| intervaloMenor.getText().toString().isEmpty()
				|| iteracoes.getText().toString().isEmpty()) {

			return true;
		} else {

			return false;
		}
	}

	// Chamado ao apertar o botao
	public void calcular(View v) {

		if (!isEmptyAndGet()) {

			// Obter os valores dos EditTexts
			double a = Double.valueOf((intervaloMenor.getText().toString()));
			double b = Double.valueOf(intervaloMaior.getText().toString());
			double error = Double.parseDouble(erro.getText().toString());
			int i = Integer.parseInt((iteracoes.getText().toString()));

			// Reiniciar variaveis
			resetar();

			myParser = new org.nfunk.jep.JEP();
			a_array = new double[i];
			b_array = new double[i];
			c_array = new double[i];
			fc_array = new double[i];

			myParser.setImplicitMul(true);
			myParser.setTraverse(true);
			myParser.initFunTab();
			myParser.initSymTab();
			myParser.addStandardConstants();
			myParser.addStandardFunctions();
			myParser.addVariable("x", a);
			myParser.parseExpression(primeiroTermo.getText().toString());

			if (!myParser.hasError()) {
				double fa = myParser.getValue();
				myParser.setVarValue("x", b);
				double fb = myParser.getValue();
				j = i; // aux
				i = 0; // aux

				do {
					// Verificar por possiveis erros
					// Primeiro passo, achar f(a) e f(b) e garantir que
					// f(a).f(b) < 0
					if (fa == 0) {
						raiz = a;
						ocorrencia = "Intervalo menor é raiz";
						break;

					} else if (fb == 0) {
						raiz = b;
						ocorrencia = "Intervalo maior é raiz";
						break;

					} else if (fa * fb > 0) {
						ocorrencia = "f(a).f(b)>0\nNão há raiz ou há mais de uma no intervalo ou delta = 0";
						break;

					} else {

						// Caso tudo ocorra bem, fazer o metodo
						c = (a + b) / 2;
						myParser.setVarValue("x", c);
						fc = myParser.getValue();

						// Se c for raiz no meio do caminho
						if (fc == 0) {
							raiz = c;
							ocorrencia = "Raiz encontrada. c = "
									+ new DecimalFormat("#.#########")
											.format(raiz);
							break;
						} else {

							// Inserir resultados nos vetores
							a_array[i] = a;
							b_array[i] = b;
							c_array[i] = c;
							fc_array[i] = fc;

							// Decidir qual vai ser o intervalo
							if (fc * fa < 0) {

								b = c;
								i++;

							} else {

								a = c;
								i++;
							}

							// Verificar se o valor já esta dentro do erro
							// pedido
							fc = (fc <= 0.0F) ? 0.0F - fc : fc; // valor
																// absoluto de
							// funcao
							// de c

							if (fc < error) {
								margem = false;
							}

						}
					}
					// Repetir até iteracoes ou estar dentro do erro
				} while ((i != j) && margem);

				// Caso nenhum erro ocorra durante o loop
				if (ocorrencia.isEmpty() && Double.isNaN(raiz)) {

					// Mostrar linhas para visualizar o resultado
					invisivelLinha.setVisibility(View.VISIBLE);
					setListAdapter(new BisseccaoAdapter(a_array, b_array,
							c_array, fc_array));

				} else { // Caso ocorra erros

					// Mostrar TextView com ocorrencia
					invisivelTexto.setVisibility(View.VISIBLE);

					// Erro se alguma raiz for encontrada
					if (ocorrencia.isEmpty()) {
						invisivelTexto.setText(ocorrencia);
						Toast toasty = Toast.makeText(
								getApplicationContext(),
								ocorrencia
										+ "\n Raiz = "
										+ new DecimalFormat("#.#########")
												.format(raiz),
								Toast.LENGTH_LONG);
						toasty.show();
					} else { // Erro se nenhuma raiz for encontrada
						invisivelTexto.setText(ocorrencia);
						Toast toasty = Toast.makeText(getApplicationContext(),
								ocorrencia, Toast.LENGTH_LONG);
						toasty.show();
					}

				}
			} else {
				invisivelTexto.setVisibility(View.VISIBLE);
				invisivelTexto.setText(myParser.getErrorInfo());
			}
		} else { // Caso campos nao estajam preenchidos
			ocorrencia = "Cheque os campos";
			Toast toast = Toast.makeText(getApplicationContext(), ocorrencia,
					Toast.LENGTH_LONG);
			toast.show();
			ocorrencia = "";
		}
	}

	public void resetar(View v) {

		double xValue;

		org.nfunk.jep.JEP myParser = new org.nfunk.jep.JEP();
		myParser.addStandardConstants();
		myParser.addStandardFunctions();
		myParser.setTraverse(true);
		myParser.addVariable("x", 15.0);
		myParser.parseExpression("x+1");

		double result = myParser.getValue();

		Toast toast = Toast.makeText(getApplicationContext(),
				Double.toString(result), Toast.LENGTH_LONG);
		toast.show();

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
			at.setText(new DecimalFormat("#.#########").format(a2[position]));
			bt.setText(new DecimalFormat("#.#########").format(b2[position]));
			ct.setText(new DecimalFormat("#.#########").format(c2[position]));
			fct.setText(new DecimalFormat("#.##########").format(fc2[position]));

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
