package com.yurifbeck.metodosnumericos;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity {
	
	ListView list;
	String[] metodos;

	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
         metodos = new String[] { "Bissecção" };
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, metodos);
            setListAdapter(adapter);
    }
    
    


    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		switch (position){
		
		case 0:
			Intent intent = new Intent(this, Bisseccao.class);
			Toast toast = Toast.makeText(getApplicationContext(), "Metodo escolhido: " + metodos[position], Toast.LENGTH_LONG);
			toast.show();
			startActivity(intent);
			break;
		
		}
		
	}




	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
