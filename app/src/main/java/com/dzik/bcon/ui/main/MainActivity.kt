package com.dzik.bcon.ui.main

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.RemoteException
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.dzik.bcon.BconApplication
import com.dzik.bcon.model.BeaconUID
import com.dzik.bcon.ui.main.mvp.MainPresenter
import com.dzik.bcon.ui.main.dagger.DaggerMainActivityComponent
import com.dzik.bcon.ui.main.dagger.MainActivityModule
import com.dzik.bcon.ui.main.mvp.MainView
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import org.altbeacon.beacon.*
import javax.inject.Inject
import org.altbeacon.beacon.Beacon
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), BeaconConsumer, RangeNotifier {

    companion object {
        private const val PERMISSION_REQUEST_COARSE_LOCATION = 1
        private const val TAG = "BEACON"
    }

    @Inject lateinit var presenter: MainPresenter

    @Inject lateinit var view: MainView

    @Inject lateinit var beaconManager: BeaconManager

    private val beaconDetected: BehaviorSubject<List<BeaconUID>> = BehaviorSubject.create()

    fun getBeaconDetected(): Observable<List<BeaconUID>> {
        return Observable.interval(2, TimeUnit.SECONDS)
                .map { listOf(
                        BeaconUID(
                                namespace = "edd1ebeac04e5defa017",
                                instance = "89fac117b149"
                        )
                ) }
//        return beaconDetected.hide()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerMainActivityComponent.builder()
                .mainActivityModule(MainActivityModule(this))
                .bconApplicationComponent(BconApplication.component)
                .build()
                .inject(this)

        setContentView(view)

        // ask for permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                        PERMISSION_REQUEST_COARSE_LOCATION
                )
            }
        }

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.onCreate()
    }

    override fun onPostResume() {
        super.onPostResume()
        beaconManager.bind(this)
    }

    public override fun onPause() {
        super.onPause()
        beaconManager.unbind(this)
    }

    override fun onBeaconServiceConnect() {
        val region = Region("all-beacons-region", null, null, null)
        try {
            beaconManager.startRangingBeaconsInRegion(region)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }

        beaconManager.addRangeNotifier(this)
    }

    override fun didRangeBeaconsInRegion(beacons: Collection<Beacon>, region: Region) {
        beaconDetected.onNext(beacons
                .filter { it.serviceUuid == 0xfeaa && it.beaconTypeCode == 0x00 }
                .sortedBy { it.distance }
                .map { BeaconUID(
                        it.id1.toString().substring(2),
                        it.id2.toString().substring(2))
                }
        )

//        Log.d(TAG, "${beacons.size} beacons")
//        for (beacon in beacons) {
//            if (beacon.serviceUuid == 0xfeaa && beacon.beaconTypeCode == 0x00) {
//                // This is a Eddystone-UID frame
//                val namespaceId = beacon.id1
//                val instanceId = beacon.id2
//                Log.d(TAG, "$instanceId ${beacon.distance}")
//
//                // Do we have telemetry data?
//                if (beacon.extraDataFields.size > 0) {
//                    val telemetryVersion = beacon.extraDataFields[0]
//                    val batteryMilliVolts = beacon.extraDataFields[1]
//                    val pduCount = beacon.extraDataFields[3]
//                    val uptime = beacon.extraDataFields[4]
//
//                    Log.d(TAG, "The above beacon is sending telemetry version " +
//                            telemetryVersion +
//                            ", has been up for : " + uptime + " seconds" +
//                            ", has a battery level of " + batteryMilliVolts + " mV" +
//                            ", and has transmitted " + pduCount + " advertisements.")
//
//                }
//            }
//        }
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_COARSE_LOCATION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "coarse location permission granted")
                } else {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Functionality limited")
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.")
                    builder.setPositiveButton(android.R.string.ok, null)
                    builder.setOnDismissListener(DialogInterface.OnDismissListener { })
                    builder.show()
                }
                return
            }
        }
    }
}
