package gr.clink.nopandroidclient.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import gr.clink.nopandroidclient.R;
import gr.clink.nopandroidclient.model.UserInformation;
import gr.clink.nopandroidclient.other.Globals;
import gr.clink.nopandroidclient.other.JSONParser;


public class RegisterFragment extends Fragment {

    private UserRegisterTask mAuthTask = null;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mVerifyPasswordView;
    private View mProgressView;
    private View mLoginFormView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);

        mEmailView = (EditText) rootView.findViewById(R.id.email);
        mPasswordView = (EditText) rootView.findViewById(R.id.password);
        mVerifyPasswordView = (EditText) rootView.findViewById(R.id.verify_password);

        Button mEmailRegisterButton = (Button) rootView.findViewById(R.id.email_register_button);
        mEmailRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        mLoginFormView = rootView.findViewById(R.id.login_form);
        mProgressView = rootView.findViewById(R.id.login_progress);


        return rootView;
    }


    /**
     * Attempts to  register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    //TODO: Needs improvements
    private void attemptRegister() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserRegisterTask();
            mAuthTask.execute(email, password);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 1;
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }



    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    class UserRegisterTask extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();
        private static final String LOGIN_URL = Globals.HOST + Globals.POST_CUSTOMER_LOGIN;
        private static final String RESPONSE_CODE = "responseCode";
        private static final String RESULT = "result";
        private static final String CUSTOMER = "customer";
        private static final String MESSAGE = "message";


        @Override
        protected JSONObject doInBackground(String... args) {
            try {

                HashMap<String, String> params = new HashMap<>();
                params.put("username", args[0]);
                params.put("password", args[1]);

                Log.d("request", "starting");

                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);

                if (json != null) {
                    Log.d("JSON result", json.toString());

                    return json;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(final JSONObject json) {
            mAuthTask = null;
            showProgress(false);


            UserInformation.CustomerLoginResults loginResult;
            if (json != null) {
                try {
                    int responseCode = json.getInt(RESPONSE_CODE);
                    loginResult = UserInformation.CustomerLoginResults.typeFromInt(responseCode);
                    JSONObject result = json.getJSONObject(RESULT);
                    if (loginResult == UserInformation.CustomerLoginResults.Successful) {
                        Toast.makeText(getActivity(), loginResult.toString(),
                                Toast.LENGTH_LONG).show();
                        UserInformation.getInstance().setCustomerFrom(result.getJSONObject(CUSTOMER));
                        getActivity().finish();
                    } else {
                        String message = result.getString(MESSAGE);
                        Toast.makeText(getActivity(), message,
                                Toast.LENGTH_LONG).show();
                        mPasswordView.setError(message);
                        mPasswordView.requestFocus();
                    }





                } catch (JSONException e) {
                    Toast.makeText(getActivity(), R.string.login_no_internet_connection,
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(getActivity(), R.string.login_no_internet_connection,
                        Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
