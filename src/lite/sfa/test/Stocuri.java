/**
 * @author florinb
 *
 */
package lite.sfa.test;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import listeners.AsyncTaskListener;
import listeners.OperatiiArticolListener;
import model.InfoStrings;
import model.OperatiiArticol;
import model.OperatiiArticolFactory;
import model.UserInfo;
import utils.DepartamentAgent;
import utils.UtilsGeneral;
import utils.UtilsUser;
import adapters.CautareArticoleAdapter;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import beans.ArticolDB;
import enums.EnumArticoleDAO;
import enums.EnumDepartExtra;

public class Stocuri extends ListActivity implements AsyncTaskListener,
        OnClickListener, OperatiiArticolListener {

    Button stocBtn;
    String filiala = "", nume = "", cod = "", unitLog = "", numeDepart = "",
            codDepart = "";
    private EditText txtCodArticol;
    String codArticol = "";
    String numeArticol = "";
    String tipAcces;

    private TextView textCodArticol;
    private TextView textNumeArticol, textCmpArticol;

    private static final String METHOD_NAME = "getStocAndroid";
    private NumberFormat nf2;

    ToggleButton tglButton, tglTipArtBtn;

    public SimpleAdapter adapterFiliale;
    private Spinner spinnerFiliale;
    private static ArrayList<HashMap<String, String>> listFiliale = new ArrayList<HashMap<String, String>>();
    private String filialaStoc = "";

    LinearLayout resLayout;
    OperatiiArticol opArticol;

    String selectedDepartamentAgent = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setTheme(R.style.LRTheme);
        setContentView(R.layout.stocuri);

        initSelectionDepartament();

        if (UtilsUser.isAV() || UtilsUser.isKA())
            addSpinnerDepartamente();

        opArticol = OperatiiArticolFactory.createObject("OperatiiArticolImpl",
                this);
        opArticol.setListener(this);

        this.stocBtn = (Button) findViewById(R.id.stocBtn);
        stocBtn.setOnClickListener(this);

        ActionBar actionBar = getActionBar();
        actionBar.setTitle("Stocuri");
        actionBar.setDisplayHomeAsUpEnabled(true);

        nf2 = NumberFormat.getInstance();

        this.tglButton = (ToggleButton) findViewById(R.id.togglebutton);
        this.tglButton.setChecked(true);
        addListenerToggle();

        this.tglTipArtBtn = (ToggleButton) findViewById(R.id.tglTipArt);
        addListenerTglTipArtBtn();

        txtCodArticol = (EditText) findViewById(R.id.txtCodArt);

        textCmpArticol = (TextView) findViewById(R.id.textCmpArticol);
        textCmpArticol.setVisibility(View.INVISIBLE);

        resLayout = (LinearLayout) findViewById(R.id.resLayout);
        resLayout.setVisibility(View.INVISIBLE);

        txtCodArticol.setHint("Introduceti cod articol");

        textNumeArticol = (TextView) findViewById(R.id.textNumeArticol);
        textCodArticol = (TextView) findViewById(R.id.textCodArticol);

        spinnerFiliale = (Spinner) findViewById(R.id.spinFilialaStoc);
        adapterFiliale = new SimpleAdapter(this, listFiliale,
                R.layout.rowlayoutagenti, new String[] { "numeFiliala",
                        "codFiliala" }, new int[] { R.id.textNumeAgent,
                        R.id.textCodAgent });
        spinnerFiliale.setOnItemSelectedListener(new MyOnSelectedFiliala());
        spinnerFiliale.setVisibility(View.INVISIBLE);

        filialaStoc = UserInfo.getInstance().getUnitLog();

        if (UserInfo.getInstance().getTipAcces().equals("9")
                || UserInfo.getInstance().getTipAcces().equals("10")
                || UserInfo.getInstance().getTipAcces().equals("12")
                || UserInfo.getInstance().getTipAcces().equals("14")) // agenti,
        // sd, directori
        {
            // se ofera acces la unit.log. BV90
            if (UserInfo.getInstance().getCodDepart().equals("02")
                    || UserInfo.getInstance().getCodDepart().equals("05")) {
                populateFilialeAgenti();
                spinnerFiliale.setVisibility(View.VISIBLE);

            }

            // agentii si sd-ii din Bucuresti au acces la toate filialele din
            // Buc
            if (UserInfo.getInstance().getUnitLog().substring(0, 2)
                    .equals("BU")) {
                if (UserInfo.getInstance().getCodDepart().equals("03")
                        || UserInfo.getInstance().getCodDepart().equals("04")
                        || UserInfo.getInstance().getCodDepart().equals("06")
                        || UserInfo.getInstance().getCodDepart().equals("07")
                        || UserInfo.getInstance().getCodDepart().equals("08")
                        || UserInfo.getInstance().getCodDepart().equals("09")) {
                    populateFilialeAgentiBuc();
                    spinnerFiliale.setVisibility(View.VISIBLE);

                }
            }

        }

        if (UtilsUser.isSDBUCURESTI()) {
            populateFilialeAgentiBuc();
            spinnerFiliale.setVisibility(View.VISIBLE);
        }

        if (UtilsUser.isANYDV()) // directori
        {
            // se ofera acces filialele din definitie
            populateFilialeDV();
            spinnerFiliale.setVisibility(View.VISIBLE);

        }

    }

    private void addSpinnerDepartamente() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getBaseContext(),
                android.R.layout.simple_spinner_dropdown_item,
                DepartamentAgent.getDepartamenteAgent());

        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.spinner_layout, null);
        final Spinner spinnerView = (Spinner) mCustomView
                .findViewById(R.id.spinnerDep);

        spinnerView.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                    int arg2, long arg3) {
                selectedDepartamentAgent = EnumDepartExtra
                        .getCodDepart(spinnerView.getSelectedItem().toString());
                resLayout.setVisibility(View.INVISIBLE);
                populateListViewArticol(new ArrayList<ArticolDB>());
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spinnerView.setAdapter(adapter);
        getActionBar().setCustomView(mCustomView);
        getActionBar().setDisplayShowCustomEnabled(true);

    }

    private void initSelectionDepartament() {

        selectedDepartamentAgent = UserInfo.getInstance().getCodDepart();

        if (UtilsUser.isCV() || UtilsUser.isDVCV())
            selectedDepartamentAgent = "";

        if (UtilsUser.isKA())
            selectedDepartamentAgent = "00";
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        case android.R.id.home:
            UserInfo.getInstance().setParentScreen("");
            Intent nextScreen = new Intent(getApplicationContext(),
                    MainMenu.class);

            startActivity(nextScreen);

            finish();
            return true;

        }
        return false;
    }

    private void populateFilialeAgentiBuc() {

        listFiliale.clear();

        HashMap<String, String> temp;

        temp = new HashMap<String, String>();
        temp.put("numeFiliala", "BUC. GLINA");
        temp.put("codFiliala", "BU10");
        listFiliale.add(temp);

        temp = new HashMap<String, String>();
        temp.put("numeFiliala", "BUC. MILITARI");
        temp.put("codFiliala", "BU11");
        listFiliale.add(temp);

        temp = new HashMap<String, String>();
        temp.put("numeFiliala", "BUC. OTOPENI");
        temp.put("codFiliala", "BU12");
        listFiliale.add(temp);

        temp = new HashMap<String, String>();
        temp.put("numeFiliala", "BUC. ANDRONACHE");
        temp.put("codFiliala", "BU13");
        listFiliale.add(temp);

        spinnerFiliale.setAdapter(adapterFiliale);

        if (UserInfo.getInstance().getUnitLog().equals("BU10"))
            spinnerFiliale.setSelection(0);

        if (UserInfo.getInstance().getUnitLog().equals("BU11"))
            spinnerFiliale.setSelection(1);

        if (UserInfo.getInstance().getUnitLog().equals("BU12"))
            spinnerFiliale.setSelection(2);

        if (UserInfo.getInstance().getUnitLog().equals("BU13"))
            spinnerFiliale.setSelection(3);

    }

    private void populateFilialeAgenti() {

        listFiliale.clear();

        HashMap<String, String> temp;

        temp = new HashMap<String, String>();
        temp.put("numeFiliala", UserInfo.getInstance().getFiliala());
        temp.put("codFiliala", UserInfo.getInstance().getUnitLog());
        listFiliale.add(temp);

        temp = new HashMap<String, String>();
        temp.put("numeFiliala", "BRASOV CENTRAL");
        temp.put("codFiliala", "BV90");
        listFiliale.add(temp);

        spinnerFiliale.setAdapter(adapterFiliale);

    }

    private void populateFilialeDV() {

        String district = "";
        listFiliale.clear();

        HashMap<String, String> temp;
        String[] tokenLinie = UserInfo.getInstance().getFilialeDV().split(";");

        if (UserInfo.getInstance().getFilialeDV().trim().length() > 0) {

            for (int i = 0; i < tokenLinie.length; i++) {

                temp = new HashMap<String, String>();
                temp.put("numeFiliala", InfoStrings.getNumeUL(tokenLinie[i]));
                temp.put("codFiliala", tokenLinie[i]);

                if (tokenLinie[i].substring(0, 2).equals("BU"))
                    district = "BU";

                listFiliale.add(temp);
            }

            if (!district.equals("")) {
                temp = new HashMap<String, String>();
                temp.put("numeFiliala", "District BUCURESTI");
                temp.put("codFiliala", "BU");
                listFiliale.add(temp);
            }

        } else {
            temp = new HashMap<String, String>();
            temp.put("numeFiliala", "Nedefinit");
            temp.put("codFiliala", "ND10");

            listFiliale.add(temp);
        }
        spinnerFiliale.setAdapter(adapterFiliale);

    }

    // captare evenimente spinner filiale
    public class MyOnSelectedFiliala implements OnItemSelectedListener {

        @SuppressWarnings("unchecked")
        public void onItemSelected(AdapterView<?> parent, View v, int pos,
                long id) {
            HashMap<String, String> map = (HashMap<String, String>) adapterFiliale
                    .getItem(pos);
            filialaStoc = map.get("codFiliala");

            if (!codArticol.equals(""))
                performGetStoc();
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // TODO
        }
    }

    public void addListenerToggle() {
        tglButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (tglButton.isChecked()) {
                    if (tglTipArtBtn.isChecked())
                        txtCodArticol.setHint("Introduceti cod sintetic");
                    else
                        txtCodArticol.setHint("Introduceti cod articol");
                } else {
                    if (tglTipArtBtn.isChecked())
                        txtCodArticol.setHint("Introduceti nume sintetic");
                    else
                        txtCodArticol.setHint("Introduceti nume articol");

                }
            }
        });

    }

    public void addListenerTglTipArtBtn() {
        tglTipArtBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (tglTipArtBtn.isChecked()) {
                    if (!tglButton.isChecked())
                        txtCodArticol.setHint("Introduceti nume sintetic");
                    else
                        txtCodArticol.setHint("Introduceti cod sintetic");
                } else {
                    if (!tglButton.isChecked())
                        txtCodArticol.setHint("Introduceti nume articol");
                    else
                        txtCodArticol.setHint("Introduceti cod articol");

                }
            }
        });

    }

    public void populateListViewArticol(List<ArticolDB> resultList) {

        txtCodArticol.setText("");
        CautareArticoleAdapter adapterArticole = new CautareArticoleAdapter(
                this, resultList);
        setListAdapter(adapterArticole);

    }

    public void addListenerStoc() {
        stocBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    // afisare articole
                    if (txtCodArticol.length() > 0) {
                        try {
                            performGetArticole();

                        } catch (Exception ex) {
                            Log.e("Error", ex.toString());

                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Introduceti cod articol!", Toast.LENGTH_SHORT)
                                .show();
                    }

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.toString(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    protected void performGetArticole() {

        String numeArticol = txtCodArticol.getText().toString().trim();

        if (numeArticol.trim().length() > 0) {
            String tipCautare = "", tipArticol = "";

            if (tglButton.isChecked())
                tipCautare = "C";
            else
                tipCautare = "N";

            if (tglTipArtBtn.isChecked())
                tipArticol = "S";
            else
                tipArticol = "A";

            HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
            params.put("searchString", numeArticol);
            params.put("tipArticol", tipArticol);
            params.put("tipCautare", tipCautare);
            params.put("departament", selectedDepartamentAgent);

            opArticol.getArticoleDistributie(params);
        }

    }

    protected void onListItemClick(ListView l, View v, int position, long id) {

        super.onListItemClick(l, v, position, id);

        ArticolDB articol = (ArticolDB) l.getAdapter().getItem(position);

        numeArticol = articol.getNume();
        codArticol = articol.getCod();

        textNumeArticol.setText(numeArticol);
        textCodArticol.setText(codArticol);

        performGetStoc();

    }

    protected void performGetStoc() {

        try {

            if (codArticol.length() == 8)
                codArticol = "0000000000" + codArticol;

            String showCmp = "0";
            if (UserInfo.getInstance().getTipAcces().equals("12")
                    || UserInfo.getInstance().getTipAcces().equals("14"))// DV,
            // DD
            {
                showCmp = "1";
            }

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("codArt", codArticol);
            params.put("filiala", filialaStoc);
            params.put("showCmp", showCmp);
            params.put("depart", UserInfo.getInstance().getCodDepart());

            AsyncTaskWSCall call = new AsyncTaskWSCall(this, METHOD_NAME,
                    params);
            call.getCallResults();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(),
                    Toast.LENGTH_LONG).show();
        }

    }

    @SuppressLint("ResourceAsColor")
    public void afisStocArt(String stocArt) {

        try {

            resLayout.setVisibility(View.VISIBLE);

            LinearLayout tl = (LinearLayout) findViewById(R.id.ArtStocTable);
            tl.setOrientation(LinearLayout.VERTICAL);
            tl.setGravity(Gravity.RIGHT);

            android.widget.LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    0, LayoutParams.WRAP_CONTENT, 0.01f);

            layoutParams.setMargins(20, 0, 0, 0);

            LinearLayout rowLayout = new LinearLayout(this);
            rowLayout.setGravity(Gravity.LEFT);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);

            tl.removeAllViews();

            TextView labelCant = new TextView(this);
            labelCant.setText("Cant");
            labelCant.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
            labelCant.setTextSize(16);
            labelCant.setGravity(Gravity.LEFT);
            labelCant.setTextColor(this.getResources().getColor(
                    R.color.detColor6));
            labelCant.setLayoutParams(layoutParams);
            rowLayout.addView(labelCant);

            TextView labelUm = new TextView(this);
            labelUm.setText("Um");
            labelUm.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
            labelUm.setTextSize(16);
            labelUm.setGravity(Gravity.LEFT);
            labelUm.setTextColor(this.getResources()
                    .getColor(R.color.detColor6));
            labelUm.setLayoutParams(layoutParams);
            rowLayout.addView(labelUm);

            TextView labelDep = new TextView(this);
            labelDep.setText("Depoz");
            labelDep.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
            labelDep.setTextSize(16);
            labelDep.setGravity(Gravity.LEFT);
            labelDep.setTextColor(this.getResources().getColor(
                    R.color.detColor6));
            labelDep.setLayoutParams(layoutParams);
            rowLayout.addView(labelDep);

            tl.addView(rowLayout);

            nf2.setMinimumFractionDigits(3);
            nf2.setMaximumFractionDigits(3);

            String valoareStoc = "";

            if (!stocArt.equals("-1") && stocArt.length() > 0) {

                String[] tokStocArt = stocArt.split("!");

                String[] tokenMain = tokStocArt[0].split("@@");

                for (int i = 0; i < tokenMain.length; i++) {

                    String[] articol = tokenMain[i].toString().split("#");

                    LinearLayout rowLayoutCh = new LinearLayout(this);

                    if (tokStocArt[2].equals("1")) {
                        valoareStoc = nf2.format(Double.valueOf(articol[0]));
                    }

                    if (tokStocArt[2].equals("0")) {
                        if (Double.parseDouble(articol[0]) > 0)
                            valoareStoc = "In stoc";
                        else
                            valoareStoc = "Fara stoc";
                    }

                    labelCant = new TextView(this);
                    labelCant.setText(valoareStoc);
                    labelCant.setTypeface(Typeface.MONOSPACE);
                    labelCant.setTextSize(16);
                    labelCant.setGravity(Gravity.LEFT);
                    labelCant.setTextColor(this.getResources().getColor(
                            R.color.dropColor2));
                    labelCant.setLayoutParams(layoutParams);
                    rowLayoutCh.addView(labelCant);

                    labelUm = new TextView(this);
                    labelUm.setText(articol[1]);
                    labelUm.setTypeface(Typeface.MONOSPACE);
                    labelUm.setTextSize(16);
                    labelUm.setGravity(Gravity.LEFT);
                    labelUm.setTextColor(this.getResources().getColor(
                            R.color.dropColor2));
                    labelUm.setLayoutParams(layoutParams);
                    rowLayoutCh.addView(labelUm);

                    labelDep = new TextView(this);
                    labelDep.setText(articol[2]);
                    labelDep.setTypeface(Typeface.MONOSPACE);
                    labelDep.setTextSize(16);
                    labelDep.setGravity(Gravity.LEFT);
                    labelDep.setLayoutParams(layoutParams);
                    labelDep.setTextColor(this.getResources().getColor(
                            R.color.dropColor2));
                    rowLayoutCh.addView(labelDep);

                    tl.addView(rowLayoutCh);

                }

                if (UserInfo.getInstance().getTipAcces().equals("12")
                        || UserInfo.getInstance().getTipAcces().equals("14"))// DV,
                // DD
                {
                    textCmpArticol.setVisibility(View.VISIBLE);
                    textCmpArticol.setText("Cmp: " + tokStocArt[1]);
                }

            } else {

                Toast.makeText(getApplicationContext(),
                        "Nu exista informatii.", Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(),
                    Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onBackPressed() {
        UserInfo.getInstance().setParentScreen("");
        Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);

        startActivity(nextScreen);

        finish();
        return;
    }

    public void onTaskComplete(String methodName, Object result) {
        afisStocArt((String) result);

    }

    private void stocBtnListener() {
        try {
            if (txtCodArticol.length() > 0) {
                try {
                    performGetArticole();

                } catch (Exception ex) {
                    Log.e("Error", ex.toString());

                }
            } else {
                Toast.makeText(getApplicationContext(),
                        "Introduceti cod articol!", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.stocBtn:
            stocBtnListener();
            break;
        }

    }

    public void operationComplete(EnumArticoleDAO methodName, Object result) {

        switch (methodName) {
        case GET_ARTICOLE_DISTRIBUTIE:
            populateListViewArticol(opArticol
                    .deserializeArticoleVanzare((String) result));
            break;
        default:
            break;
        }

    }
}