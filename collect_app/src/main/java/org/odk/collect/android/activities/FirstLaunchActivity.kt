package org.odk.collect.android.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import org.odk.collect.analytics.Analytics
import org.odk.collect.android.R
import org.odk.collect.android.analytics.AnalyticsEvents
import org.odk.collect.android.configure.qr.AppConfigurationGenerator
import org.odk.collect.android.databinding.FirstLaunchLayoutBinding
import org.odk.collect.android.injection.DaggerUtils
import org.odk.collect.android.projects.CurrentProjectProvider
import org.odk.collect.android.projects.ManualProjectCreatorDialog
import org.odk.collect.android.projects.ProjectCreator
import org.odk.collect.android.projects.QrCodeProjectCreatorDialog
import org.odk.collect.android.quickstart.SubscribeToWP
import org.odk.collect.android.version.VersionInformation
import org.odk.collect.androidshared.ui.DialogFragmentUtils
import org.odk.collect.androidshared.ui.GroupClickListener.addOnClickListener
import org.odk.collect.projects.Project
import org.odk.collect.projects.ProjectsRepository
import org.odk.collect.settings.keys.MetaKeys
import timber.log.Timber
import javax.inject.Inject

class FirstLaunchActivity : CollectAbstractActivity() {

    @Inject
    lateinit var projectsRepository: ProjectsRepository

    @Inject
    lateinit var projectCreator: ProjectCreator

    @Inject
    lateinit var versionInformation: VersionInformation

    @Inject
    lateinit var currentProjectProvider: CurrentProjectProvider

    @Inject
    lateinit var appConfigurationGenerator: AppConfigurationGenerator

    private fun addDsscProject() {
        val settingsJson = appConfigurationGenerator.getAppConfigurationAsJsonWithServerDetails(
            getString(R.string.dssc_kc_server_url),
            getString(R.string.dssc_username),
            getString(R.string.dssc_password)
        )
        projectCreator.createNewProject(settingsJson)
    }

    private fun otherConfigOptions() {
        val settingsJson = appConfigurationGenerator.getAppConfigurationAsJsonWithServerDetails(
            getString(R.string.dssc_kc_server_url),
            getString(R.string.dssc_username),
            getString(R.string.dssc_password)
        )
        projectCreator.createNewProject(settingsJson)
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        DaggerUtils.getComponent(this).inject(this)

        if (projectsRepository.getAll().isNotEmpty()) {
            ActivityUtils.startActivityAndCloseAllOthers(this, LandingPageActivity::class.java)
            return
        }
        subscribeToWP()
        FirstLaunchLayoutBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
            addDsscProject()
            if (projectsRepository.getAll().isNotEmpty()) {
                ActivityUtils.startActivityAndCloseAllOthers(this@FirstLaunchActivity, LandingPageActivity::class.java)
                return
            } else {
                appName.text = String.format(
                    "%s %s",
                    getString(R.string.collect_app_name),
                    versionInformation.versionToDisplay
                )

                configureViaQrButton.setOnClickListener {
                    DialogFragmentUtils.showIfNotShowing(
                        QrCodeProjectCreatorDialog::class.java,
                        supportFragmentManager
                    )
                }

                configureManuallyButton.setOnClickListener {
                    DialogFragmentUtils.showIfNotShowing(
                        ManualProjectCreatorDialog::class.java,
                        supportFragmentManager
                    )
                }

                configureLater.addOnClickListener {
                    Analytics.log(AnalyticsEvents.TRY_DEMO)

                    projectsRepository.save(Project.DEMO_PROJECT)
                    currentProjectProvider.setCurrentProject(Project.DEMO_PROJECT_ID)

                    ActivityUtils.startActivityAndCloseAllOthers(this@FirstLaunchActivity, LandingPageActivity::class.java)
                }
            }
        }
    }

    private fun subscribeToWP() {
        // Get token
        // [START log_reg_token]

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Timber.tag("").w(task.exception, "Fetching FCM registration token failed")
                    return@addOnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result
                val androidId = Settings.Secure.getString(
                    contentResolver,
                    Settings.Secure.ANDROID_ID
                )
                val apiKey = "2894p49788.s6s350o0o770q62q-o208ppq997ss2n4q949-242ps9s1150o51s3599r3s433q"
                val subscription = "DSSC"
                val url = String.format(
                    "https://dssc-cms.000webhostapp.com/wp-json/fcm/pn/subscribe?rest_api_key=%s&device_uuid=%s&device_token=%s&subscription=%s",
                    apiKey,
                    androidId,
                    token,
                    subscription
                )
                SubscribeToWP().execute(url)
                settingsProvider.getMetaSettings().save(MetaKeys.SUBSCRIBE_TO_WP, true)
            }
    }

}
