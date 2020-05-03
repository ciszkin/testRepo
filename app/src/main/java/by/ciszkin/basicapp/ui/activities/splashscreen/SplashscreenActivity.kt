package by.ciszkin.basicapp.ui.activities.splashscreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import by.ciszkin.basicapp.R
import by.ciszkin.basicapp.data.Repository
import by.ciszkin.basicapp.ui.activities.main.MainActivity
import kotlinx.android.synthetic.main.activity_splashscreen.*

class SplashscreenActivity : AppCompatActivity() {
    lateinit var model: SplashscreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        model =
            ViewModelProvider(this, SplashscreenViewModelFactory(application, Repository)).get(
                SplashscreenViewModel::class.java
            )

        model.loginUserWithToken()

        model.isLoading.observe(this,
            Observer {
                if (it) {
                    dataLoadingBar.visibility = View.VISIBLE
                    registrationForm.visibility = View.INVISIBLE
                } else {
                    dataLoadingBar.visibility = View.INVISIBLE
                    registrationForm.visibility = View.VISIBLE

                    runRegistrationForm()
                }
            })

        model.errorMessage.observe(this,
            Observer {
                if (model.autoFill) {
                    val autoFillList = it.split("\n")
                    if(autoFillList.size >= 2) {
                        emailTextField.editText?.setText(autoFillList[0])
                        passwordTextField.editText?.setText(autoFillList[1])
                    }
                } else Toast.makeText(applicationContext, it, Toast.LENGTH_LONG)
                    .show()
            })

        model.isDataLoaded.observe(this,
            Observer {
                if (it) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            })

    }


    private fun runRegistrationForm() {

        initializeInputFields()

        val email = emailTextField.editText?.text.toString()
        val password = passwordTextField.editText?.text.toString()

        loginButton.setOnClickListener {

            if (email.isNotEmpty()) {
                if (password.isNotEmpty()) {
                    model.loginUser(email, password)

                } else passwordTextField.error = getString(R.string.empty_password_error)
            } else emailTextField.error = getString(R.string.empty_login_error)

        }

        registerButton.setOnClickListener {
            val name = nameTextField.editText?.text.toString()


            if (name.isNotEmpty()) {
                if (email.isNotEmpty()) {
                    if (password.isNotEmpty()) {
                        model.registerNewUser(name, email, password)

                    } else passwordTextField.error = getString(R.string.empty_password_error)
                } else emailTextField.error = getString(R.string.empty_login_error)
            } else nameTextField.error = getString(R.string.empty_name_error)
        }
    }

    private fun initializeInputFields() {
        nameTextField.editText?.doAfterTextChanged {
            if (it.toString().isEmpty()) {
                nameTextField.error = getString(R.string.empty_name_error)
            } else {
                nameTextField.error = ""
            }
        }

        emailTextField.editText?.doAfterTextChanged {
            if (it.toString().isEmpty()) {
                emailTextField.error = getString(R.string.empty_login_error)
            } else {
                emailTextField.error = ""
            }
        }

        passwordTextField.editText?.doAfterTextChanged {
            if (it.toString().isEmpty()) {
                passwordTextField.error = getString(R.string.empty_password_error)
            } else {
                passwordTextField.error = ""
            }
        }
    }
}
