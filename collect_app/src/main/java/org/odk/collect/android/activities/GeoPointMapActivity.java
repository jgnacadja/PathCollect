/*
 * Copyright (C) 2011 University of Washington
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.odk.collect.android.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.odk.collect.android.R;
import org.odk.collect.android.map.GoogleMapFragment;
import org.odk.collect.android.map.MapFragment;
import org.odk.collect.android.map.MapPoint;
import org.odk.collect.android.map.OsmMapFragment;
import org.odk.collect.android.preferences.GeneralKeys;
import org.odk.collect.android.spatial.MapHelper;
import org.odk.collect.android.utilities.GeoPointUtils;
import org.odk.collect.android.utilities.ToastUtils;
import org.odk.collect.android.widgets.GeoPointWidget;
import org.osmdroid.tileprovider.IRegisterReceiver;

import java.text.DecimalFormat;

import timber.log.Timber;

import static org.odk.collect.android.utilities.PermissionUtils.areLocationPermissionsGranted;

/**
 * Allow the user to indicate a location by placing a marker on a map, either
 * by touching a point on the map or by tapping a button to place the marker
 * at the current location (obtained from GPS or other location sensors).
 */
public class GeoPointMapActivity extends BaseGeoMapActivity implements IRegisterReceiver {
    public static final String PREF_VALUE_GOOGLE_MAPS = "google_maps";
    public static final String MAP_CENTER_KEY = "map_center";
    public static final String MAP_ZOOM_KEY = "map_zoom";
    public static final String POINT_KEY = "point";

    public static final String IS_DRAGGED_KEY = "is_dragged";
    public static final String CAPTURE_LOCATION_KEY = "capture_location";
    public static final String FOUND_FIRST_LOCATION_KEY = "found_first_location";
    public static final String SET_CLEAR_KEY = "set_clear";
    public static final String POINT_FROM_INTENT_KEY = "point_from_intent";
    public static final String INTENT_READ_ONLY_KEY = "intent_read_only";
    public static final String INTENT_DRAGGABLE_KEY = "intent_draggable";
    public static final String IS_POINT_LOCKED_KEY = "is_point_locked";

    public static final String PLACE_MARKER_BUTTON_ENABLED_KEY = "place_marker_button_enabled";
    public static final String ZOOM_BUTTON_ENABLED_KEY = "zoom_button_enabled";
    public static final String CLEAR_BUTTON_ENABLED_KEY = "clear_button_enabled";
    public static final String LOCATION_STATUS_VISIBILITY_KEY = "location_status_visibility";
    public static final String LOCATION_INFO_VISIBILITY_KEY = "location_info_visibility";
    public static final String ZOOM_DIALOG_SHOWING_KEY = "zoom_dialog_showing";
    public static final String ZOOM_POINT_BUTTON_ENABLED_KEY = "zoom_point_button";
    public static final String ZOOM_LOCATION_BUTTON_ENABLED_KEY = "zoom_location_button";

    private MapFragment map;
    private int featureId = -1;  // will be a positive featureId once map is ready

    private TextView locationStatus;
    private TextView locationInfo;

    private MapPoint location;
    private ImageButton placeMarkerButton;

    private boolean isDragged;

    private ImageButton zoomButton;
    private AlertDialog zoomDialog;
    private View zoomDialogView;
    private Button zoomPointButton;
    private Button zoomLocationButton;
    private ImageButton clearButton;

    private boolean captureLocation;
    private boolean foundFirstLocation;

    /**
     * True if a tap on the clear button removed an existing marker and
     * no new marker has been placed.
     */
    private boolean setClear;

    /** True if the current point came from the intent. */
    private boolean pointFromIntent;

    /** True if the intent requested for the point to be read-only. */
    private boolean intentReadOnly;

    /** True if the intent requested for the marker to be draggable. */
    private boolean intentDraggable;

    /** While true, the point cannot be moved by dragging or long-pressing. */
    private boolean isPointLocked;

    private Bundle instanceState;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instanceState = savedInstanceState;

        if (!areLocationPermissionsGranted(this)) {
            finish();
            return;
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        try {
            setContentView(R.layout.geopoint_layout);
        } catch (NoClassDefFoundError e) {
            Timber.e(e, "Google maps not accessible due to: %s ", e.getMessage());
            ToastUtils.showShortToast(R.string.google_play_services_error_occured);
            finish();
            return;
        }

        locationStatus = findViewById(R.id.location_status);
        locationInfo = findViewById(R.id.location_info);
        placeMarkerButton = findViewById(R.id.place_marker);
        zoomButton = findViewById(R.id.zoom);

        createMapFragment().addTo(this, R.id.map_container, this::initMap);
    }

    public MapFragment createMapFragment() {
        String mapSdk = getIntent().getStringExtra(GeneralKeys.KEY_MAP_SDK);
        return (mapSdk == null || mapSdk.equals(PREF_VALUE_GOOGLE_MAPS)) ?
            new GoogleMapFragment() : new OsmMapFragment();
    }

    @Override protected void onStart() {
        super.onStart();
        if (map != null) {
            map.setGpsLocationEnabled(true);
        }
    }

    @Override protected void onStop() {
        if (zoomDialog != null && zoomDialog.isShowing()) {
            zoomDialog.dismiss();  // necessary to prevent a memory leak
        }

        // To avoid a memory leak, we have to shut down GPS when the activity
        // quits for good. But if it's only a screen rotation, we don't want to
        // stop/start GPS and make the user wait to get a GPS lock again.
        if (!isChangingConfigurations()) {
            map.setGpsLocationEnabled(false);
        }
        super.onStop();
    }

    @Override protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putParcelable(MAP_CENTER_KEY, map.getCenter());
        state.putDouble(MAP_ZOOM_KEY, map.getZoom());
        state.putParcelable(POINT_KEY, map.getMarkerPoint(featureId));

        // Flags
        state.putBoolean(IS_DRAGGED_KEY, isDragged);
        state.putBoolean(CAPTURE_LOCATION_KEY, captureLocation);
        state.putBoolean(FOUND_FIRST_LOCATION_KEY, foundFirstLocation);
        state.putBoolean(SET_CLEAR_KEY, setClear);
        state.putBoolean(POINT_FROM_INTENT_KEY, pointFromIntent);
        state.putBoolean(INTENT_READ_ONLY_KEY, intentReadOnly);
        state.putBoolean(INTENT_DRAGGABLE_KEY, intentDraggable);
        state.putBoolean(IS_POINT_LOCKED_KEY, isPointLocked);

        // UI state
        state.putBoolean(PLACE_MARKER_BUTTON_ENABLED_KEY, placeMarkerButton.isEnabled());
        state.putBoolean(ZOOM_BUTTON_ENABLED_KEY, zoomButton.isEnabled());
        state.putBoolean(CLEAR_BUTTON_ENABLED_KEY, clearButton.isEnabled());
        state.putInt(LOCATION_STATUS_VISIBILITY_KEY, locationStatus.getVisibility());
        state.putInt(LOCATION_INFO_VISIBILITY_KEY, locationInfo.getVisibility());
        state.putBoolean(ZOOM_DIALOG_SHOWING_KEY, zoomDialog != null && zoomDialog.isShowing());
        state.putBoolean(ZOOM_LOCATION_BUTTON_ENABLED_KEY,
            zoomLocationButton != null && zoomLocationButton.isEnabled());
        state.putBoolean(ZOOM_POINT_BUTTON_ENABLED_KEY,
            zoomPointButton != null && zoomPointButton.isEnabled());
    }

    @Override public void destroy() { }

    public void returnLocation() {
        String result = null;

        if (setClear || (intentReadOnly && featureId == -1)) {
            result = "";
        } else if (isDragged || intentReadOnly || pointFromIntent) {
            Timber.i("IsDragged !!!");
            result = formatResult(map.getMarkerPoint(featureId));
        } else if (location != null) {
            Timber.i("IsNotDragged !!!");
            result = formatResult(location);
        }

        if (result != null) {
            setResult(RESULT_OK, new Intent().putExtra(FormEntryActivity.LOCATION_RESULT, result));
        }
        finish();
    }

    @SuppressLint("MissingPermission") // Permission handled in Constructor
    public void initMap(MapFragment newMapFragment) {
        if (newMapFragment == null) {
            finish();
            return;
        }

        map = newMapFragment;
        map.setDragEndListener(this::onDragEnd);
        map.setLongPressListener(this::onLongPress);

        if (map instanceof GoogleMapFragment) {
            helper = new MapHelper(this, ((GoogleMapFragment) map).getGoogleMap(), selectedLayer);
        } else if (map instanceof OsmMapFragment) {
            helper = new MapHelper(this, ((OsmMapFragment) map).getMapView(), this, selectedLayer);
        }
        helper.setBasemap();

        ImageButton acceptLocation = findViewById(R.id.accept_location);
        acceptLocation.setOnClickListener(v -> returnLocation());

        placeMarkerButton.setEnabled(false);
        placeMarkerButton.setOnClickListener(v -> {
            placeMarker(map.getGpsLocation());
            zoomToMarker(true);
        });

        // Focuses on marked location
        zoomButton.setEnabled(false);
        zoomButton.setOnClickListener(v -> showZoomDialog());

        // Menu Layer Toggle
        ImageButton layers = findViewById(R.id.layer_menu);
        layers.setOnClickListener(v -> helper.showLayersDialog());

        zoomDialogView = getLayoutInflater().inflate(R.layout.geo_zoom_dialog, null);
        zoomLocationButton = zoomDialogView.findViewById(R.id.zoom_location);
        zoomLocationButton.setOnClickListener(v -> {
            map.zoomToPoint(map.getGpsLocation(), true);
            zoomDialog.dismiss();
        });

        zoomPointButton = zoomDialogView.findViewById(R.id.zoom_saved_location);
        zoomPointButton.setOnClickListener(v -> {
            zoomToMarker(true);
            zoomDialog.dismiss();
        });

        clearButton = findViewById(R.id.clear);
        clearButton.setEnabled(false);
        clearButton.setOnClickListener(v -> {
            clear();
            if (map.getGpsLocation() != null) {
                placeMarkerButton.setEnabled(true);
                // locationStatus.setVisibility(View.VISIBLE);
            }
            // placeMarkerButton.setEnabled(true);
            locationInfo.setVisibility(View.VISIBLE);
            locationStatus.setVisibility(View.VISIBLE);
            pointFromIntent = false;
        });

        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            intentDraggable = intent.getBooleanExtra(GeoPointWidget.DRAGGABLE_ONLY, false);
            if (!intentDraggable) {
                // Not Draggable, set text for Map else leave as placement-map text
                locationInfo.setText(getString(R.string.geopoint_no_draggable_instruction));
            }

            intentReadOnly = intent.getBooleanExtra(GeoPointWidget.READ_ONLY, false);
            if (intentReadOnly) {
                captureLocation = true;
                clearButton.setEnabled(false);
            }

            if (intent.hasExtra(GeoPointWidget.LOCATION)) {
                double[] point = intent.getDoubleArrayExtra(GeoPointWidget.LOCATION);

                // If the point is initially set from the intent, the "place marker"
                // button, dragging, and long-pressing are all initially disabled.
                // To enable them, the user must clear the marker and add a new one.
                isPointLocked = true;
                placeMarker(new MapPoint(point[0], point[1], point[2], point[3]));
                placeMarkerButton.setEnabled(false);

                captureLocation = true;
                pointFromIntent = true;
                locationInfo.setVisibility(View.GONE);
                locationStatus.setVisibility(View.GONE);
                zoomButton.setEnabled(true);
                foundFirstLocation = true;
                zoomToMarker(false);
            }
        }

        helper.setBasemap();

        map.setGpsLocationListener(this::onLocationChanged);
        map.setGpsLocationEnabled(true);

        if (instanceState != null) {
            restoreFromInstanceState(instanceState);
        }
    }

    protected void restoreFromInstanceState(Bundle state) {
        isDragged = state.getBoolean(IS_DRAGGED_KEY, false);
        captureLocation = state.getBoolean(CAPTURE_LOCATION_KEY, false);
        foundFirstLocation = state.getBoolean(FOUND_FIRST_LOCATION_KEY, false);
        setClear = state.getBoolean(SET_CLEAR_KEY, false);
        pointFromIntent = state.getBoolean(POINT_FROM_INTENT_KEY, false);
        intentReadOnly = state.getBoolean(INTENT_READ_ONLY_KEY, false);
        intentDraggable = state.getBoolean(INTENT_DRAGGABLE_KEY, false);
        isPointLocked = state.getBoolean(IS_POINT_LOCKED_KEY, false);

        // Restore the marker and dialog after the flags, because they use some of them.
        MapPoint point = state.getParcelable(POINT_KEY);
        if (point != null) {
            placeMarker(point);
        } else {
            clear();
        }

        // Restore the flags again, because placeMarker() and clear() modify some of them.
        isDragged = state.getBoolean(IS_DRAGGED_KEY, false);
        captureLocation = state.getBoolean(CAPTURE_LOCATION_KEY, false);
        foundFirstLocation = state.getBoolean(FOUND_FIRST_LOCATION_KEY, false);
        setClear = state.getBoolean(SET_CLEAR_KEY, false);
        pointFromIntent = state.getBoolean(POINT_FROM_INTENT_KEY, false);
        intentReadOnly = state.getBoolean(INTENT_READ_ONLY_KEY, false);
        intentDraggable = state.getBoolean(INTENT_DRAGGABLE_KEY, false);
        isPointLocked = state.getBoolean(IS_POINT_LOCKED_KEY, false);

        // Restore the rest of the UI state.
        MapPoint mapCenter = state.getParcelable(MAP_CENTER_KEY);
        Double mapZoom = state.getDouble(MAP_ZOOM_KEY);
        if (mapCenter != null && mapZoom != null) {
            map.zoomToPoint(mapCenter, mapZoom, false);
        }

        placeMarkerButton.setEnabled(state.getBoolean(PLACE_MARKER_BUTTON_ENABLED_KEY, false));
        zoomButton.setEnabled(state.getBoolean(ZOOM_BUTTON_ENABLED_KEY, false));
        clearButton.setEnabled(state.getBoolean(CLEAR_BUTTON_ENABLED_KEY, false));

        locationInfo.setVisibility(state.getInt(LOCATION_INFO_VISIBILITY_KEY, View.GONE));
        locationStatus.setVisibility(state.getInt(LOCATION_STATUS_VISIBILITY_KEY, View.GONE));

        if (state.getBoolean(ZOOM_DIALOG_SHOWING_KEY, false)) {
            showZoomDialog();
        }
        if (zoomLocationButton != null) {
            zoomLocationButton.setEnabled(state.getBoolean(ZOOM_LOCATION_BUTTON_ENABLED_KEY));
        }
        if (zoomPointButton != null) {
            zoomPointButton.setEnabled(state.getBoolean(ZOOM_POINT_BUTTON_ENABLED_KEY));
        }
    }

    public void onLocationChanged(MapPoint point) {
        if (setClear) {
            placeMarkerButton.setEnabled(true);
        }

        MapPoint previousLocation = this.location;
        this.location = point;

        if (point != null) {
            Timber.i("onLocationChanged: location = %s", point);

            if (previousLocation != null) {
                enableZoomButtons(true);

                if (!captureLocation && !setClear) {
                    placeMarker(point);
                    placeMarkerButton.setEnabled(true);
                }

                if (!foundFirstLocation) {
                    showZoomDialog();
                    foundFirstLocation = true;
                }

                locationStatus.setText(formatLocationStatus(map.getLocationProvider(), point.sd));
            }

        } else {
            Timber.i("onLocationChanged: null location");
        }
    }

    public String formatResult(MapPoint point) {
        return String.format("%s %s %s %s", point.lat, point.lon, point.alt, point.sd);
    }

    public String formatLocationStatus(String provider, double sd) {
        return getString(
            R.string.location_provider_accuracy,
            GeoPointUtils.capitalizeGps(provider),
            new DecimalFormat("#.##").format(sd)
        );
    }

    public void onDragEnd(int draggedFeatureId) {
        if (draggedFeatureId == featureId) {
            isDragged = true;
            captureLocation = true;
            setClear = false;
            map.setCenter(map.getMarkerPoint(featureId), true);
        }
    }

    public void onLongPress(MapPoint point) {
        if (intentDraggable && !intentReadOnly && !isPointLocked) {
            placeMarker(point);
            enableZoomButtons(true);
            isDragged = true;
        }
    }

    private void enableZoomButtons(boolean shouldEnable) {
        if (zoomButton != null) {
            zoomButton.setEnabled(shouldEnable);
        }
        if (zoomLocationButton != null) {
            zoomLocationButton.setEnabled(shouldEnable);
        }
    }

    public void zoomToMarker(boolean animate) {
        map.zoomToPoint(map.getMarkerPoint(featureId), animate);
    }

    public void showZoomDialog() {
        if (zoomDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.zoom_to_where));
            builder.setView(zoomDialogView)
                    .setNegativeButton(R.string.cancel, (dialog, id) -> dialog.cancel())
                    .setOnCancelListener(dialog -> {
                        dialog.cancel();
                        zoomDialog.dismiss();
                    });
            zoomDialog = builder.create();
        }
        //If feature enable zoom to button else disable
        if (zoomLocationButton != null) {
            if (map.getGpsLocation() != null) {
                zoomLocationButton.setEnabled(true);
                zoomLocationButton.setBackgroundColor(Color.parseColor("#50cccccc"));
                zoomLocationButton.setTextColor(themeUtils.getPrimaryTextColor());
            } else {
                zoomLocationButton.setEnabled(false);
                zoomLocationButton.setBackgroundColor(Color.parseColor("#50e2e2e2"));
                zoomLocationButton.setTextColor(Color.parseColor("#FF979797"));
            }

            if (featureId != -1 && !setClear) {
                zoomPointButton.setEnabled(true);
                zoomPointButton.setBackgroundColor(Color.parseColor("#50cccccc"));
                zoomPointButton.setTextColor(themeUtils.getPrimaryTextColor());
            } else {
                zoomPointButton.setEnabled(false);
                zoomPointButton.setBackgroundColor(Color.parseColor("#50e2e2e2"));
                zoomPointButton.setTextColor(Color.parseColor("#FF979797"));
            }
        }

        zoomDialog.show();
    }

    private void clear() {
        map.clearFeatures();
        featureId = -1;
        clearButton.setEnabled(false);

        isPointLocked = false;
        isDragged = false;
        captureLocation = false;
        setClear = true;
    }

    /** Places the marker and enables the button to remove it. */
    private void placeMarker(MapPoint point) {
        map.clearFeatures();
        featureId = map.addMarker(point, intentDraggable && !intentReadOnly && !isPointLocked);
        clearButton.setEnabled(true);
        captureLocation = true;
        setClear = false;
    }

    public void setCaptureLocation(boolean captureLocation) {
        this.captureLocation = captureLocation;
    }

    @VisibleForTesting public String getLocationStatus() {
        return locationStatus.getText().toString();
    }

    @VisibleForTesting public AlertDialog getZoomDialog() {
        return zoomDialog;
    }

    @VisibleForTesting public MapFragment getMapFragment() {
        return map;
    }
}
