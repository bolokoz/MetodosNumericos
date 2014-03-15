package com.yurifbeck.metodosnumericos;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
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
import android.widget.ArrayAdapter;
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
					.setMessage(">Deve exister apenas uma raiz no intervalo \n" +
							">Função deve ser contínua no intervalo")
					.setNeutralButton(android.R.string.yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// continue with delete
								}
							}).show();
			break;

		case android.R.id.home:
			NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
			return true;

		case R.id.action_funcoes:

			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			String funcao = "";
			dialog.setTitle("Lista de funções");
			dialog.setItems(R.array.lista_de_funcoes, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	                   Toast toast = Toast.makeText(getApplicationContext(), Integer.toString(which), Toast.LENGTH_SHORT);
	                   toast.show();
	            	   
	               }
	        });
			dialog.show();

			break;
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
			ArrayList<Double> a_array = new ArrayList<Double>();
			ArrayList<Double> b_array = new ArrayList<Double>();
			ArrayList<Double> c_array = new ArrayList<Double>();
			ArrayList<Double> fc_array = new ArrayList<Double>();

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
							a_array.add(a);
							b_array.add(b);
							c_array.add(c);
							fc_array.add(fc);


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
		
		String teste = "x^3-9x+3";
		String int_a = "1";
		String int_b = "3";
		String erro_ = "0.0001";
		String it = "25";
		
		primeiroTermo.setText(teste);
		intervaloMenor.setText(int_a);
		intervaloMaior.setText(int_b);
		this.erro.setText(erro_);
		iteracoes.setText(it);
		calcular(v);

	}

	static class ViewHolderItem {
	    TextView i, at, bt, ct,fct;
	}
	
	
	
	class BisseccaoAdapter extends BaseAdapter {
		
		double[] a2,b2,c2,fc2;

		public BisseccaoAdapter(ArrayList<Double> a_array, ArrayList<Double> b_array,
				ArrayList<Double> c_array, ArrayList<Double> fc_array) {

			a2 = new double[a_array.size()];
			b2 = new double[b_array.size()];
			c2 = new double[c_array.size()];
			fc2 = new double[fc_array.size()];
			
			for(i=0; i < a2.length ; i++){
				a2[i] = a_array.get(i);
				b2[i] = b_array.get(i);
				c2[i] = c_array.get(i);
				fc2[i] = fc_array.get(i);
			}


		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolderItem viewHolder;
			if(convertView==null){

		        // inflate the layout
		        LayoutInflater inflater = getLayoutInflater();
		        convertView = inflater.inflate(R.layout.linha_listview, parent, false);

		        // well set up the ViewHolder
		        viewHolder = new ViewHolderItem();
		        viewHolder.i = (TextView) convertView.findViewById(R.id.lista_i);
		        viewHolder.at = (TextView) convertView.findViewById(R.id.lista_a);
		        viewHolder.bt= (TextView) convertView.findViewById(R.id.lista_b);
		        viewHolder.ct = (TextView) convertView.findViewById(R.id.lista_c);
		        viewHolder.fct = (TextView) convertView.findViewById(R.id.lista_fc);

		        // store the holder with the view.
		        convertView.setTag(viewHolder);

		    }else{
		        // we've just avoided calling findViewById() on resource everytime
		        // just use the viewHolder
		        viewHolder = (ViewHolderItem) convertView.getTag();
		    }

		        // inflate the layout
		        LayoutInflater inflater = getLayoutInflater();
		        convertView = inflater.inflate(R.layout.linha_listview, parent, false);
		    // assign values if the object is not null
		    if(a2.length != 0) {
		        // get the TextView from the ViewHolder and then set the text (item name) and tag (item ID) values
		        viewHolder.i.setText(Integer.toString(position + 1));
		        viewHolder.at.setText(new DecimalFormat("#.#######").format(a2[position]));
		        viewHolder.bt.setText(new DecimalFormat("#.#######").format(b2[position]));
		        viewHolder.ct.setText(new DecimalFormat("#.########").format(c2[position]));
		        viewHolder.fct.setText(new DecimalFormat(".#######E0").format(fc2[position]));
		    }

		        // well set up the ViewHolder
		        viewHolder = new ViewHolderItem();
		        viewHolder.i = (TextView) convertView.findViewById(R.id.lista_i);
		    return convertView;
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
				return 0;
			}

	}

}
