package moco.schleppo.fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import moco.schleppo.R;


public class ProfilFragment extends Fragment {

    Button btnEdit;
    View rootView;
    static final int REQUEST_CODE = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_profil, container, false);
        if(UserManagement.isAnonymousUser) {
            Intent intent = new Intent(getActivity(), UserManagement.class);
            startActivityForResult(intent, REQUEST_CODE);
        } else {
            setTextFieldsAndListener(rootView);
        }

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==Activity.RESULT_OK) {
            setTextFieldsAndListener(rootView);
        } else if(resultCode== Activity.RESULT_CANCELED) {
            FragmentManager fm = getFragmentManager();
            fm.popBackStack();
        }
    }

    private void setTextFieldsAndListener (View rootView) {
        ParseUser user = UserManagement.parseUser;

        EditText tfName = (EditText) rootView.findViewById(R.id.tfName);
        EditText tfUsername = (EditText) rootView.findViewById(R.id.tfUsername);
        EditText tfVorname = (EditText) rootView.findViewById(R.id.tfVorname);
        EditText tfEmail = (EditText) rootView.findViewById(R.id.tfEmail);
        EditText tfKennzeichen = (EditText) rootView.findViewById(R.id.tfKennzeichen);

        if(UserManagement.parseUser!=null) {
            tfName.setText(user.getString("name"));
            tfVorname.setText(user.getString("forename"));
            tfUsername.setText(user.getUsername());
            tfEmail.setText(user.getEmail());
            tfKennzeichen.setText(user.getString("licenseNumber"));
        }

        tfName.addTextChangedListener(textWatcher);
        tfVorname.addTextChangedListener(textWatcher);
        tfUsername.addTextChangedListener(textWatcher);
        tfEmail.addTextChangedListener(textWatcher);
        tfKennzeichen.addTextChangedListener(textWatcher);



        btnEdit = (Button) rootView.findViewById(R.id.btnSpeichern);
        btnEdit.setEnabled(false);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            btnEdit.setEnabled(false);
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            btnEdit.setEnabled(true);
            btnEdit.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText tfName = (EditText) rootView.findViewById(R.id.tfName);
                    EditText tfUsername = (EditText) rootView.findViewById(R.id.tfUsername);
                    EditText tfVorname = (EditText) rootView.findViewById(R.id.tfVorname);
                    EditText tfEmail = (EditText) rootView.findViewById(R.id.tfEmail);
                    EditText tfKennzeichen = (EditText) rootView.findViewById(R.id.tfKennzeichen);

                    UserManagement.parseUser.setEmail(tfEmail.getText().toString().trim());
                    UserManagement.parseUser.setUsername(tfUsername.getText().toString().trim());
                    UserManagement.parseUser.put("name", tfName.getText().toString().trim());
                    UserManagement.parseUser.put("forename", tfVorname.getText().toString().trim());
                    UserManagement.parseUser.put("licenseNumber", tfKennzeichen.getText().toString().trim().toUpperCase());

                    UserManagement.parseUser.saveInBackground();
                    getFragmentManager().popBackStack();
                }
            });
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}
