package com.minchainx.permission.check;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.os.Build;
import android.os.Environment;
import android.provider.CalendarContract;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.provider.VoicemailContract;
import androidx.annotation.RequiresApi;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import com.minchainx.permission.util.Permission;

import java.io.File;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by jimmy on 2018/8/1.
 * <p>
 * use for request permission only for Android 5.0(API>=21)
 */

public class StrictChecker implements PermissionChecker {

    public StrictChecker() {
    }

    @Override
    public boolean hasPermission(Context context, String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return true;
        try {
            switch (permission) {
                case Permission.READ_CALENDAR:
                    return checkReadCalendar(context);
                case Permission.WRITE_CALENDAR:
                    return checkWriteCalendar(context);
                case Permission.CAMERA:
                    return checkCamera(context);
                case Permission.READ_CONTACTS:
                    return checkReadContacts(context);
                case Permission.WRITE_CONTACTS:
                    return checkWriteContacts(context);
                case Permission.GET_ACCOUNTS:
                    return true;
                case Permission.ACCESS_COARSE_LOCATION:
                    return checkCoarseLocation(context);
                case Permission.ACCESS_FINE_LOCATION:
                    return checkFineLocation(context);
                case Permission.RECORD_AUDIO:
                    return checkRecordAudio(context);
                case Permission.READ_PHONE_STATE:
                    return checkReadPhoneState(context);
                case Permission.CALL_PHONE:
                    return true;
                case Permission.READ_CALL_LOG:
                    return checkReadCallLog(context);
                case Permission.WRITE_CALL_LOG:
                    return checkWriteCallLog(context);
                case Permission.ADD_VOICEMAIL:
                    return checkAddVoicemail(context);
                case Permission.USE_SIP:
                    return checkSip(context);
                case Permission.PROCESS_OUTGOING_CALLS:
                    return true;
                case Permission.BODY_SENSORS:
                    return checkSensors(context);
                case Permission.SEND_SMS:
                case Permission.RECEIVE_MMS:
                    return true;
                case Permission.READ_SMS:
                    return checkReadSms(context);
                case Permission.RECEIVE_WAP_PUSH:
                case Permission.RECEIVE_SMS:
                    return true;
                case Permission.READ_EXTERNAL_STORAGE:
                    return checkReadStorage();
                case Permission.WRITE_EXTERNAL_STORAGE:
                    return checkWriteStorage();
            }
        } catch (Throwable e) {
            return false;
        }
        return true;
    }

    /**
     * test read calendar
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("MissingPermission")
    private boolean checkReadCalendar(Context context) throws Throwable {
        ContentResolver resolver = context.getContentResolver();
        String[] projection = new String[]{CalendarContract.Calendars._ID, CalendarContract.Calendars.NAME};
        Cursor cursor = resolver.query(CalendarContract.Calendars.CONTENT_URI, projection, null, null, null);
        if (cursor != null) {
            try {
                readCursor(cursor);
            } finally {
                cursor.close();
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * test write calendar
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private boolean checkWriteCalendar(Context context) throws Throwable {
        final String NAME = "PERMISSION";
        final String ACCOUNT = "permission@gmail.com";
        ContentResolver resolver = context.getContentResolver();
        try {
            TimeZone timeZone = TimeZone.getDefault();
            ContentValues value = new ContentValues();
            value.put(CalendarContract.Calendars.NAME, NAME);
            value.put(CalendarContract.Calendars.ACCOUNT_NAME, ACCOUNT);
            value.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
            value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, NAME);
            value.put(CalendarContract.Calendars.VISIBLE, 1);
            value.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE);
            value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
            value.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
            value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
            value.put(CalendarContract.Calendars.OWNER_ACCOUNT, NAME);
            value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0);

            Uri insertUri = CalendarContract.Calendars.CONTENT_URI.buildUpon()
                    .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                    .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, NAME)
                    .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL)
                    .build();
            Uri resourceUri = resolver.insert(insertUri, value);
            return ContentUris.parseId(resourceUri) > 0;
        } finally {
            Uri deleteUri = CalendarContract.Calendars.CONTENT_URI.buildUpon().build();
            resolver.delete(deleteUri, CalendarContract.Calendars.ACCOUNT_NAME + "=?", new String[]{ACCOUNT});
        }
    }

    /**
     * test camera
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private boolean checkCamera(Context context) throws Throwable {
        SurfaceView surfaceView = new SurfaceView(context);
        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(CALLBACK);

        Camera camera = null;
        try {
            camera = Camera.open();
            Camera.Parameters parameters = camera.getParameters();
            camera.setParameters(parameters);
            camera.setPreviewDisplay(holder);
            camera.setPreviewCallback(PREVIEW_CALLBACK);
            camera.startPreview();
            return true;
        } catch (Throwable e) {
            PackageManager packageManager = context.getPackageManager();
            return !packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA);
        } finally {
            if (camera != null) {
                camera.stopPreview();
                camera.setPreviewDisplay(null);
                camera.setPreviewCallback(null);
                camera.release();
            }
        }
    }

    private static final Camera.PreviewCallback PREVIEW_CALLBACK = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
        }
    };

    private static final SurfaceHolder.Callback CALLBACK = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
        }
    };

    /**
     * test read contacts
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private boolean checkReadContacts(Context context) throws Throwable {
        ContentResolver resolver = context.getContentResolver();
        String[] projection = new String[]{ContactsContract.Data._ID, ContactsContract.Data.DATA1};
        Cursor cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null);
        if (cursor != null) {
            try {
                readCursor(cursor);
            } finally {
                cursor.close();
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * test write contacts
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private boolean checkWriteContacts(Context context) throws Throwable {
        final String DISPLAY_NAME = "PERMISSION";
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.Data.CONTENT_URI,
                new String[]{ContactsContract.Data.RAW_CONTACT_ID},
                ContactsContract.Data.MIMETYPE + "=? and " + ContactsContract.Data.DATA1 + "=?",
                new String[]{ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE, DISPLAY_NAME},
                null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                long rawContactId = cursor.getLong(0);
                cursor.close();
                return update(resolver, DISPLAY_NAME, rawContactId);
            } else {
                cursor.close();
                return insert(resolver, DISPLAY_NAME);
            }
        }
        return false;
    }

    private boolean insert(ContentResolver resolver, String DISPLAY_NAME) {
        ContentValues values = new ContentValues();
        Uri rawContractUri = resolver.insert(ContactsContract.RawContacts.CONTENT_URI, values);
        long rawContactId = ContentUris.parseId(rawContractUri);

        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.DATA1, DISPLAY_NAME);
        values.put(ContactsContract.Data.DATA2, DISPLAY_NAME);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        Uri dataUri = resolver.insert(ContactsContract.Data.CONTENT_URI, values);
        return ContentUris.parseId(dataUri) > 0;
    }

    private void delete(ContentResolver resolver, long contactId, long dataId) {
        resolver.delete(ContactsContract.RawContacts.CONTENT_URI, ContactsContract.RawContacts._ID + "=?", new String[]{Long.toString(contactId)});
        resolver.delete(ContactsContract.Data.CONTENT_URI, ContactsContract.Data._ID + "=?", new String[]{Long.toString(dataId)});
    }

    private boolean update(ContentResolver resolver, String DISPLAY_NAME, long rawContactId) {
        ContentValues values = new ContentValues();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.DATA1, DISPLAY_NAME);
        values.put(ContactsContract.Data.DATA2, DISPLAY_NAME);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        Uri dataUri = resolver.insert(ContactsContract.Data.CONTENT_URI, values);
        return ContentUris.parseId(dataUri) > 0;
    }

    /**
     * test coarse location
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private boolean checkCoarseLocation(Context context) throws Throwable {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        boolean networkProvider = providers.contains(LocationManager.NETWORK_PROVIDER);
        if (networkProvider) {
            return true;
        }
        PackageManager packageManager = context.getPackageManager();
        boolean networkHardware = packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_NETWORK);
        return !networkHardware || !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    /**
     * test fine location
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private boolean checkFineLocation(Context context) throws Throwable {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        boolean gpsProvider = providers.contains(LocationManager.GPS_PROVIDER);
        boolean passiveProvider = providers.contains(LocationManager.PASSIVE_PROVIDER);
        if (gpsProvider || passiveProvider) {
            return true;
        }
        PackageManager packageManager = context.getPackageManager();
        boolean gpsHardware = packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
        return !gpsHardware || !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * test record audio
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private boolean checkRecordAudio(Context context) throws Throwable {
        File mTempFile = null;
        MediaRecorder mediaRecorder = new MediaRecorder();

        try {
            mTempFile = File.createTempFile("permission", "test");

            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(mTempFile.getAbsolutePath());
            mediaRecorder.prepare();
            mediaRecorder.start();
            return true;
        } catch (Throwable e) {
            PackageManager packageManager = context.getPackageManager();
            return !packageManager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
        } finally {
            try {
                mediaRecorder.stop();
            } catch (Exception ignored) {
            }
            try {
                mediaRecorder.release();
            } catch (Exception ignored) {
            }
            if (mTempFile != null && mTempFile.exists()) {
                mTempFile.delete();
            }
        }
    }

    /**
     * test record audio
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("MissingPermission")
    private boolean checkReadPhoneState(Context context) throws Throwable {
        PackageManager packageManager = context.getPackageManager();
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) return true;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE
                || !TextUtils.isEmpty(telephonyManager.getDeviceId())
                || !TextUtils.isEmpty(telephonyManager.getSubscriberId());
    }

    /**
     * test read call log
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("MissingPermission")
    private boolean checkReadCallLog(Context context) throws Throwable {
        String[] projection = new String[]{CallLog.Calls._ID, CallLog.Calls.NUMBER, CallLog.Calls.TYPE};
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(CallLog.Calls.CONTENT_URI, projection, null, null, null);
        if (cursor != null) {
            try {
                readCursor(cursor);
            } finally {
                cursor.close();
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * test write call log
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("MissingPermission")
    private boolean checkWriteCallLog(Context context) throws Throwable {
        ContentResolver resolver = context.getContentResolver();
        try {
            ContentValues content = new ContentValues();
            content.put(CallLog.Calls.TYPE, CallLog.Calls.INCOMING_TYPE);
            content.put(CallLog.Calls.NUMBER, "1");
            content.put(CallLog.Calls.DATE, 20080808);
            content.put(CallLog.Calls.NEW, "0");
            Uri resourceUri = resolver.insert(CallLog.Calls.CONTENT_URI, content);
            return ContentUris.parseId(resourceUri) > 0;
        } finally {
            resolver.delete(CallLog.Calls.CONTENT_URI, CallLog.Calls.NUMBER + "=?", new String[]{"1"});
        }
    }

    /**
     * test add voice
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private boolean checkAddVoicemail(Context context) throws Throwable {
        ContentResolver resolver = context.getContentResolver();
        try {
            Uri mBaseUri = VoicemailContract.Voicemails.CONTENT_URI;
            ContentValues contentValues = new ContentValues();
            contentValues.put(VoicemailContract.Voicemails.DATE, System.currentTimeMillis());
            contentValues.put(VoicemailContract.Voicemails.NUMBER, "1");
            contentValues.put(VoicemailContract.Voicemails.DURATION, 1);
            contentValues.put(VoicemailContract.Voicemails.SOURCE_PACKAGE, "permission");
            contentValues.put(VoicemailContract.Voicemails.SOURCE_DATA, "permission");
            contentValues.put(VoicemailContract.Voicemails.IS_READ, 0);
            Uri newVoicemailUri = resolver.insert(mBaseUri, contentValues);
            long id = ContentUris.parseId(newVoicemailUri);
            int count = resolver.delete(mBaseUri, VoicemailContract.Voicemails._ID + "=?", new String[]{Long.toString(id)});
            return count > 0;
        } catch (Exception e) {
            String message = e.getMessage();
            if (!TextUtils.isEmpty(message)) {
                message = message.toLowerCase();
                return !message.contains("add_voicemail");
            }
            return false;
        }
    }

    /**
     * test sip
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private boolean checkSip(Context context) throws Throwable {
        if (!SipManager.isApiSupported(context)) {
            return true;
        }
        SipManager manager = SipManager.newInstance(context);
        if (manager == null) {
            return true;
        }
        SipProfile.Builder builder = new SipProfile.Builder("Permission", "127.0.0.1");
        builder.setPassword("password");
        SipProfile profile = builder.build();
        manager.open(profile);
        manager.close(profile.getUriString());
        return true;
    }

    /**
     * test sensors
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private boolean checkSensors(Context context) throws Throwable {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        try {
            Sensor heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
            sensorManager.registerListener(SENSOR_EVENT_LISTENER, heartRateSensor, 3);
            sensorManager.unregisterListener(SENSOR_EVENT_LISTENER, heartRateSensor);
        } catch (Throwable e) {
            PackageManager packageManager = context.getPackageManager();
            return !packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_HEART_RATE);
        }
        return true;
    }

    private static final SensorEventListener SENSOR_EVENT_LISTENER = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    /**
     * test read sms
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private boolean checkReadSms(Context context) throws Throwable {
        String[] projection = new String[]{Telephony.Sms._ID, Telephony.Sms.ADDRESS, Telephony.Sms.PERSON, Telephony.Sms.BODY};
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(Telephony.Sms.CONTENT_URI, projection, null, null, null);
        if (cursor != null) {
            try {
                readCursor(cursor);
            } finally {
                cursor.close();
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * test read storage
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private boolean checkReadStorage() throws Throwable {
        File directory = Environment.getExternalStorageDirectory();
        if (directory.exists() && directory.canRead()) {
            long modified = directory.lastModified();
            String[] pathList = directory.list();
            return modified > 0 && pathList != null;
        }
        return false;
    }

    /**
     * test write storage
     */
    private static boolean checkWriteStorage() throws Throwable {
        File directory = Environment.getExternalStorageDirectory();
        if (!directory.exists() || !directory.canWrite())
            return false;
        File parent = new File(directory, "Android");
        if (parent.exists() && parent.isFile())
            if (!parent.delete())
                return false;
        if (!parent.exists())
            if (!parent.mkdirs())
                return false;
        File file = new File(parent, "ANDROID.PERMISSION.TEST");
        if (file.exists())
            return file.delete();
        else
            return file.createNewFile();
    }

    private void readCursor(Cursor cursor) {
        int count = cursor.getCount();
        if (count > 0) {
            cursor.moveToFirst();
            int type = cursor.getType(0);
            switch (type) {
                case Cursor.FIELD_TYPE_BLOB:
                case Cursor.FIELD_TYPE_NULL:
                    break;
                case Cursor.FIELD_TYPE_INTEGER:
                case Cursor.FIELD_TYPE_FLOAT:
                case Cursor.FIELD_TYPE_STRING:
                default: {
                    cursor.getString(0);
                    break;
                }
            }
        }
    }

}
