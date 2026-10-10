package com.hanh.gmsprotect.lite;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

public final class GmsProtector {
    private static final String TAG = "GmsProtector";

    public static final String GMS_PACKAGE = "com.google.android.gms";

    public enum Result { VERIFIED, GMS_MISSING, FAILED }

    private GmsProtector() {}

    public static Result protect(Context context) {
        return protect(context, null);
    }

    public static Result protect(Context context, Runnable recoveryFinished) {
        Context app = context.getApplicationContext();
        Log.i(TAG, "protect START package=" + app.getPackageName());

        try {
            ApplicationInfo info;
            if (android.os.Build.VERSION.SDK_INT >= 33) {
                info = app.getPackageManager().getApplicationInfo(
                        GMS_PACKAGE,
                        PackageManager.ApplicationInfoFlags.of(0L)
                );
            } else {
                info = app.getPackageManager().getApplicationInfo(GMS_PACKAGE, 0);
            }
            if (info.uid <= 0) {
                Log.w(TAG, "protect STOP GMS_MISSING uid=" + info.uid);
                runCompletion(recoveryFinished);
                return Result.GMS_MISSING;
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG, "protect STOP GMS_MISSING");
            runCompletion(recoveryFinished);
            return Result.GMS_MISSING;
        } catch (Throwable t) {
            // Package visibility is declared in the manifest; continue best-effort.
            Log.w(TAG, "GMS package probe failed; continuing error="
                    + t.getClass().getSimpleName() + ":" + String.valueOf(t.getMessage()));
        }

        // IMPORTANT: MILLET is the primary gate against HyperOS' special GMS
        // quick-freeze path. Do this first and do not report the protection as
        // successful when WRITE_SETTINGS was granted but the setting could not
        // actually be written/verified.
        MilletNoRestrict.Result milletResult = MilletNoRestrict.ensureGms(app);
        boolean milletOk = milletResult == MilletNoRestrict.Result.ADDED
                || milletResult == MilletNoRestrict.Result.ALREADY_PRESENT;

        Log.i(TAG, "MILLET result=" + milletResult
                + " canWrite=" + MilletNoRestrict.canWrite(app)
                + " containsGms=" + MilletNoRestrict.containsGms(app));

        if (!milletOk) {
            Log.e(TAG, "protect FAILED because MILLET is not active result=" + milletResult);
            runCompletion(recoveryFinished);
            return Result.FAILED;
        }

        try {
            // Experimental binding layer kept unchanged.
            GmsBindingProbe.ensureBound(app);

            // MILLET is verified above. FCM recovery is an independent,
            // best-effort operation and must not control the protection result.
            try {
                FcmReconnect.recoverIfDisconnected(app, recoveryFinished);
            } catch (Throwable t) {
                Log.w(TAG, "FCM recovery failed error="
                        + t.getClass().getSimpleName() + ":" + String.valueOf(t.getMessage()));
                runCompletion(recoveryFinished);
            }

            Log.i(TAG, "protect DONE result=VERIFIED (MILLET verified)");
            return Result.VERIFIED;
        } catch (Throwable t) {
            Log.e(TAG, "protect FAILED after MILLET error="
                    + t.getClass().getSimpleName() + ":" + String.valueOf(t.getMessage()), t);
            runCompletion(recoveryFinished);
            return Result.FAILED;
        }
    }

    private static void runCompletion(Runnable completion) {
        if (completion == null) return;
        try {
            completion.run();
        } catch (Throwable ignored) {
        }
    }
}
