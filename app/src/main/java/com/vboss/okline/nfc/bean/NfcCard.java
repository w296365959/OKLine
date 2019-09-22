package com.vboss.okline.nfc.bean;

import com.vboss.okline.nfc.Application;
import com.vboss.okline.nfc.SPEC;
import com.vboss.okline.ui.app.apppool.apppooltype.Card;

import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/4/11 09:55
 * Desc :
 */

public class NfcCard extends Application{
    public static final Card EMPTY = new Card();

    private final LinkedHashMap<Object, Application> applications;

    public NfcCard() {
        applications = new LinkedHashMap<Object, Application>(2);
    }

    public Exception getReadingException() {
        return (Exception) getProperty(SPEC.PROP.EXCEPTION);
    }

    public boolean hasReadingException() {
        return hasProperty(SPEC.PROP.EXCEPTION);
    }

    public final boolean isUnknownCard() {
        return applicationCount() == 0;
    }

    public final int applicationCount() {
        return applications.size();
    }

    public final Collection<Application> getApplications() {
        return applications.values();
    }

    public final void addApplication(Application app) {
        if (app != null) {
            Object id = app.getProperty(SPEC.PROP.ID);
            if (id != null && !applications.containsKey(id))
                applications.put(id, app);
        }
    }

    @Override
    public String toString() {
        return "NfcCard{" +
                "applications=" + applications +
                '}';
    }
}
