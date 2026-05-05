package com.alibaba.sdk.android.openaccount.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.OpenAccountService;
import com.alibaba.sdk.android.openaccount.model.User;
import com.alibaba.sdk.android.openaccount.model.UserContract;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class SqliteUtil extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "offlineAccounts";
    private static final int DATABASE_VERSION = 4;
    private static final String KEY_EMAIL = "_email";
    private static final String KEY_ID = "_hash";
    private static final String KEY_LOGIN_ID = "_loginid";
    private static final String KEY_MOBILE = "_mobile";
    private static final String KEY_NICK = "_nick";
    private static final String KEY_USERID = "_userid";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS accounts1";
    private static final String TABLE_ACCOUTNS = "accounts1";

    public boolean dropTable() {
        return true;
    }

    public SqliteUtil(Context context) {
        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 4);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        try {
            sQLiteDatabase.execSQL("CREATE TABLE accounts1(_hash TEXT , _mobile TEXT , _userid TEXT , _nick TEXT, _email TEXT , _loginid TEXT )");
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        try {
            sQLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
            onCreate(sQLiteDatabase);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public boolean addAccount(UserContract userContract) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_ID, userContract.getHash());
            contentValues.put(KEY_MOBILE, userContract.getMobile());
            contentValues.put(KEY_USERID, userContract.getUserid());
            contentValues.put(KEY_NICK, userContract.getNick());
            contentValues.put(KEY_EMAIL, userContract.getEmail());
            contentValues.put(KEY_LOGIN_ID, userContract.getLoginId());
            writableDatabase.insert(TABLE_ACCOUTNS, null, contentValues);
        } catch (Throwable th) {
            writableDatabase.close();
            throw th;
        }
        writableDatabase.close();
        return true;
    }

    public UserContract getAccount(String str) {
        Cursor cursorQuery = getReadableDatabase().query(TABLE_ACCOUTNS, new String[]{KEY_ID, KEY_MOBILE, KEY_USERID, KEY_NICK, KEY_EMAIL, KEY_LOGIN_ID}, "_hash=?", new String[]{str}, null, null, null, null);
        if (cursorQuery == null || !cursorQuery.moveToFirst()) {
            return null;
        }
        try {
            return new UserContract(str, cursorQuery.getString(1), cursorQuery.getString(2), cursorQuery.getString(3), cursorQuery.getString(4), cursorQuery.getString(5));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean deleteAccountByUserId(String str) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        try {
            writableDatabase.delete(TABLE_ACCOUTNS, "_userid = ?", new String[]{str});
        } catch (Exception unused) {
        } catch (Throwable th) {
            writableDatabase.close();
            throw th;
        }
        writableDatabase.close();
        return true;
    }

    public List<UserContract> getAllAccounts() {
        ArrayList arrayList = new ArrayList();
        try {
            Cursor cursorRawQuery = getWritableDatabase().rawQuery("SELECT * FROM  accounts1", null);
            if (cursorRawQuery.moveToFirst()) {
                do {
                    arrayList.add(new UserContract(cursorRawQuery.getString(0), cursorRawQuery.getString(1), cursorRawQuery.getString(2), cursorRawQuery.getString(3), cursorRawQuery.getString(4), cursorRawQuery.getString(5)));
                } while (cursorRawQuery.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public void saveToSqlite(String str, String str2) {
        OpenAccountService openAccountService = (OpenAccountService) OpenAccountSDK.getService(OpenAccountService.class);
        if (openAccountService != null) {
            User user = openAccountService.getSession().getUser();
            UserContract userContract = new UserContract(Md5Utils.getSHA256(str + "&" + str2), user.mobile, user.id, user.nick, user.email, user.loginId);
            if (!TextUtils.isEmpty(userContract.getUserid())) {
                deleteAccountByUserId(userContract.getUserid());
            }
            addAccount(userContract);
            if (!TextUtils.isEmpty(user.mobile) && !user.mobile.equals(str)) {
                userContract.setHash(Md5Utils.getSHA256(user.mobile + "&" + str2));
                addAccount(userContract);
            }
            if (!TextUtils.isEmpty(user.email) && !user.email.equals(str)) {
                userContract.setHash(Md5Utils.getSHA256(user.email + "&" + str2));
                addAccount(userContract);
            }
            if (TextUtils.isEmpty(user.nick) || user.nick.equals(str)) {
                return;
            }
            userContract.setHash(Md5Utils.getSHA256(user.nick + "&" + str2));
            addAccount(userContract);
        }
    }

    public void saveByUser(String str, User user) {
        if (user == null || TextUtils.isEmpty(str)) {
            return;
        }
        UserContract userContract = new UserContract(str, user.mobile, user.id, user.nick, user.email, user.loginId);
        if (!TextUtils.isEmpty(userContract.getUserid())) {
            deleteAccountByUserId(userContract.getUserid());
        }
        addAccount(userContract);
    }
}
