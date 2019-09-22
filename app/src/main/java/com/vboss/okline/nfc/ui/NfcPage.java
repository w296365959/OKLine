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

package com.vboss.okline.nfc.ui;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.vboss.okline.R;
import com.vboss.okline.base.OKLineApp;
import com.vboss.okline.nfc.SPEC;
import com.vboss.okline.nfc.bean.NfcCard;
import com.vboss.okline.nfc.reader.ReaderListener;
import com.vboss.okline.ui.app.apppool.apppooltype.Card;
import com.vboss.okline.ui.user.Utils;


public final class NfcPage implements ReaderListener {
	private static final String TAG = "READCARD_ACTION";
	private static final String RET = "READCARD_RESULT";
	private static final String STA = "READCARD_STATUS";

	private final Activity activity;

	public NfcPage(Activity activity) {
		this.activity = activity;
	}

	public static boolean isSendByMe(Intent intent) {
		return intent != null && TAG.equals(intent.getAction());
	}

	public static boolean isNormalInfo(Intent intent) {
		return intent != null && intent.hasExtra(STA);
	}

	private static final String tag = "NfcPage";
	@Override
	public void onReadEvent(SPEC.EVENT event, Object... objs) {
		Utils.showLog(tag,"event = [" + event + "], objs = [" + objs + "]");
		if (event == SPEC.EVENT.IDLE) {
			showProgressBar();
		} else if (event == SPEC.EVENT.FINISHED) {
			Utils.showLog(tag,"读卡结束");
			hideProgressBar();

			final NfcCard card;
			if (objs != null && objs.length > 0)
				card = (NfcCard) objs[0];
			else
				card = null;

			activity.setIntent(buildResult(card));
		}
	}

	@Override
	public void onReadCard(NfcCard card) {
		Utils.showLog(TAG,"NfcPage onReadCard:"+card.toString());
	}

	private Intent buildResult(NfcCard card) {
		Utils.showLog(tag,"NfcPage.buildResult + card = [" + card + "]");
		final Intent ret = new Intent(TAG);

		if (card != null && !card.hasReadingException()) {
			Utils.showLog(tag,"对象为空 或者 读取异常");
			if (card.isUnknownCard()) {
				Utils.showLog(tag,"对象为空 或者 读取异常     未知卡");
				ret.putExtra(RET, OKLineApp
						.getStringResource(R.string.info_nfc_unknown));
			} else {
//				ret.putExtra(RET, card.toHtml());
				ret.putExtra(STA, 1);
				Utils.showLog(tag,"对象为空 或者 读取异常     不是未知卡：状态1");
			}
		} else {
			Utils.showLog(tag,"读取成功！");
			ret.putExtra(RET,
					OKLineApp.getStringResource(R.string.info_nfc_error));
		}
		Utils.showLog(tag,"最终结果：setResult:"+ret.toString());
		return ret;
	}

	private void showProgressBar() {
		/*Dialog d = progressBar;

		if (d == null) {
			d = new Dialog(activity, R.style.progressBar);
			d.setCancelable(false);
			d.setContentView(R.layout.progress);
			progressBar = d;
		}

		if (!d.isShowing())
			d.show();*/
	}

	private void hideProgressBar() {
		/*final Dialog d = progressBar;
		if (d != null && d.isShowing())
			d.cancel();*/
	}

//	private Dialog progressBar;
}
