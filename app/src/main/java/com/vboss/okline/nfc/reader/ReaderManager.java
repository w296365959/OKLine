/* NFCard is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

NFCard is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Wget.  If not, see <http://www.gnu.org/licenses/>.

Additional permission under GNU GPL version 3 section 7 */

package com.vboss.okline.nfc.reader;

import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.NfcF;
import android.os.AsyncTask;


import com.vboss.okline.nfc.Application;
import com.vboss.okline.nfc.SPEC;
import com.vboss.okline.nfc.Util;
import com.vboss.okline.nfc.bean.NfcCard;
import com.vboss.okline.nfc.reader.pboc.StandardPboc;
import com.vboss.okline.ui.app.apppool.apppooltype.Card;
import com.vboss.okline.ui.contact.c2cPart.C2CwaitingActivity;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.ui.user.Utils;

import java.util.Collection;

public final class ReaderManager extends AsyncTask<Tag, SPEC.EVENT, NfcCard> {

    private ReaderListener realListener;

    public ReaderManager() {
        Utils.showLog(TAG,"ReaderManager构造方法");
    }

    private ReaderManager(ReaderListener listener) {
        Utils.showLog(TAG,"ReaderManager构造方法 1");
        realListener = listener;
    }
//    private ReaderCompleteListener realListener;

    public static void readCard(Tag tag, ReaderListener listener) {
        new ReaderManager(listener).execute(tag);
    }

    @Override
    protected NfcCard doInBackground(Tag... detectedTag) {
        return readCard(detectedTag[0]);
    }

    @Override
    protected void onProgressUpdate(SPEC.EVENT... events) {
        if (realListener != null)
            realListener.onReadEvent(events[0]);
    }

    private static final String TAG = "ReaderManager";

    @Override
    protected void onPostExecute(NfcCard card) {
        Utils.showLog(TAG,"读卡结果："+card != null ? card.toString() : null);
        C2CwaitingActivity.getCard(card);
        if (realListener != null)
            realListener.onReadEvent(SPEC.EVENT.FINISHED, card);
        Collection<Application> applications = card.getApplications();
        System.out.println("  size  ---  " + applications.size());
        Utils.showLog(TAG,"  size  ---  " + applications.size());
        for (Application app : applications) {
            System.out.print(" SERIAL " + app.getStringProperty(SPEC.PROP.SERIAL));
            Utils.showLog(TAG," SERIAL " + app.getStringProperty(SPEC.PROP.SERIAL));
        }
        if (card != null && realListener != null) {
            realListener.onReadCard(card);
        }
    }

    public NfcCard readCard(Tag tag) {

        final NfcCard card = new NfcCard();
        try {

            publishProgress(SPEC.EVENT.READING);

            card.setProperty(SPEC.PROP.ID, Util.toHexString(tag.getId()));

            final IsoDep isodep = IsoDep.get(tag);
            if (isodep != null)
                StandardPboc.readCard(isodep, card);

            final NfcF nfcf = NfcF.get(tag);
            if (nfcf != null)
                FelicaReader.readCard(nfcf, card);

            publishProgress(SPEC.EVENT.IDLE);

        } catch (Exception e) {
            card.setProperty(SPEC.PROP.EXCEPTION, e);
            publishProgress(SPEC.EVENT.ERROR);
        }

        return card;
    }
}
