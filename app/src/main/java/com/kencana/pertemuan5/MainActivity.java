package com.kencana.pertemuan5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.kencana.pertemuan5.Util.ServerAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ProdukAdapter.ItemClickListener{

    RecyclerView mRecyclerview;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mManager;
    ArrayList<Produk> mItems;
    Button btnInsert, btnDelete;
    ProgressDialog pd;
    Toast toast;
    String username;
    SharedPreferences sharedpreferences;
    public static final String TAG_USERNAME = "username";
    private ProdukAdapter adapter;
    private ArrayList<Produk> produkArrayList;
    private ArrayList<String> listGambar;
    ViewFlipper v_flipper;
    public double tot=0;
    String dataProduk[] = null;
    String dS[] = null;
    int images[] = {R.drawable.banner, R.drawable.banner1, R.drawable.banner2, R.drawable.banner3,  R.drawable.banner4, R.drawable.banner5};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        v_flipper = findViewById(R.id.v_flipper);

        for (int i =0; i<images.length; i++){
            fliverImages(images[i]);
        }
        for (int image: images) fliverImages(image);

        mRecyclerview = (RecyclerView) findViewById(R.id.recycler_view);
        pd = new ProgressDialog(MainActivity.this);
        mItems = new ArrayList<>();

        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences,Context.MODE_PRIVATE);
        username = getIntent().getStringExtra(TAG_USERNAME);
        toast = Toast.makeText(getApplicationContext(), null,Toast.LENGTH_SHORT);
        mRecyclerview = (RecyclerView)findViewById(R.id.recycler_view);
        pd = new ProgressDialog(MainActivity.this);
        mItems = new ArrayList<>();

        // Membaca Semua Barang
        loadJson();

        //mManager = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false);
        //mRecyclerview.setLayoutManager(mManager);
        mAdapter = new ProdukAdapter(mItems,this);
        adapter= new ProdukAdapter(mItems,this);
        //mRecyclerview.setAdapter(mAdapter);

        //GRID 2 kolom
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);

        //STAGGER 4 KOLOM
        // StaggeredGridLayoutManager llm=new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);

        mRecyclerview.setLayoutManager(layoutManager);

        mRecyclerview.setAdapter(adapter);
        //mAdapter.setClickListener(this);
        adapter.setClickListener(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent intent = new Intent();
        String data;
        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                return true;
            //Intent intent = new Intent(MainActivity.this,TambahActivity.class);
            //startActivity(intent);
            case R.id.action_logout:
                SharedPreferences.Editor editor =
                        sharedpreferences.edit();
                editor.putBoolean(LoginActivity.session_status,
                        false);
                editor.putString(TAG_USERNAME, null);
                editor.commit();
                intent.setClass(MainActivity.this,
                        LoginActivity.class);
                finish();
                startActivity(intent);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public void postTotals()
    {
        TextView txtTot=(TextView) findViewById(R.id.totalPrice);
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        txtTot.setText("Rp. "+decimalFormat.format(tot));
    }

    public void onClick(View view, int position) {
        //final Produk mhs = produkArrayList.get(position);
        final Produk mhs = mItems.get(position);
        switch (view.getId()) {
            case R.id.txt_nama_produk:
                Toast.makeText(this,"okkkkkk" + mhs.getNama() ,Toast.LENGTH_SHORT).show();
                return;
            case R.id.img_card:
                tot=tot+Double.parseDouble(mhs.getHarga());
                mhs.setJumlah(mhs.getJumlah()+1);
                Toast.makeText(this,"gambare....." + mhs.getNama() ,Toast.LENGTH_SHORT).show();
                postTotals();
                return;
            default:
                Toast.makeText(this,"lainnya..... -> " + mhs.getNama()+ " Rp. "+tot ,Toast.LENGTH_SHORT).show();
                break;
        }
    }
    private void loadJson()
    {
        pd.setMessage("Mengambil Data");
        pd.setCancelable(false);
        pd.show();

        JsonArrayRequest reqData = new JsonArrayRequest(Request.Method.POST, ServerAPI.URL_DATA,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        pd.cancel();
                        Log.d("volley","response : " + response.toString());
                        for(int i = 0 ; i < response.length(); i++)
                        {
                            try {
                                JSONObject data = response.getJSONObject(i);
                                Produk md = new Produk();
                                md.setKode(data.getString("kd_brg"));
                                md.setNama(data.getString("nm_brg"));
                                md.setHarga(data.getString("harga"));
                                md.setImg(data.getString("gambar"));
                                mItems.add(md);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        //mAdapter.notifyDataSetChanged();
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.cancel();
                        Log.d("volley", "error : " + error.getMessage());
                    }
                });

        com.kencana.pertemuan5.Util.AppController.getInstance().addToRequestQueue(reqData);
    }
    public void ambildata(){
        listGambar= new ArrayList<String>();

        listGambar.add("indosat1");
        listGambar.add("indosat2");
        listGambar.add("indosat3");
        listGambar.add("indosat4");
        listGambar.add("indosat5");
    }

    public void checkout(View view) {
        if (tot > 0) {
            String[] kode = new String[mItems.size()];
            int[] qty = new int[mItems.size()];
            Intent checkout = new Intent(this,
                    CheckoutActivity.class);
            for (int i = 0; i < mItems.size(); i++) {
                Produk md = mItems.get(i);
                kode[i] = md.getKode();
                qty[i] = md.getJumlah();
            }
            checkout.putExtra("total", tot);
            checkout.putExtra("kode", kode);
            checkout.putExtra("qty", qty);
            checkout.putExtra(TAG_USERNAME, username);
            startActivity(checkout);

        } else {
            Toast.makeText(this,"Belanja dulu...",Toast.LENGTH_SHORT).show();
        }
//        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
//        Toast.makeText(this,"Totol Rp. "+ decimalFormat.format(tot) ,Toast.LENGTH_SHORT).show();
    }
    public  void  fliverImages(int images){
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(images);

        v_flipper.addView(imageView);
        v_flipper.setFlipInterval(5000);
        v_flipper.setAutoStart(true);

        v_flipper.setOutAnimation(this,android.R.anim.slide_out_right);
        v_flipper.setInAnimation(this,android.R.anim.slide_in_left);

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new
                AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Apakah kamu ingin keluar?");
        builder.setPositiveButton("Iya", new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int
                            which) {
                        //if user pressed "yes", then he is allowed to exit from application
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        startActivity(intent);
                        int pid = android.os.Process.myPid();
                        android.os.Process.killProcess(pid);
                    }
                });
        builder.setNegativeButton("Tidak", new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int
                            which) {
                        //if user select "No", just cancel this dialog and continue with app
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
