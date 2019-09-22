package com.vboss.okline.ui.contact.TransferAccounts;

import android.content.Intent;
import android.util.Log;

import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.base.OKLineApp;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.TransferProtocol;
import com.vboss.okline.data.entities.User;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: yuan shaoyu  <br/>
 * Email : yuer@okline.cn <br/>
 * Date  : 2017/5/8 <br/>
 * summary  :在这里描述Class的主要功能
 */

public class TransHelper {
    private static final String TAG = "TransHelper";

    private static TransHelper instance;
    UserRepository userRepository;
    int requestTipsId;
    int receiveTipsId;
    String friendolNo;

    private TransHelper() {
        userRepository = UserRepository.getInstance(OKLineApp.context);
    }

    public static TransHelper getInstance() {
        if (instance == null) {
            synchronized (TransHelper.class) {
                if (instance == null) {
                    instance = new TransHelper();
                }
            }
        }
        return instance;
    }

    /**
     * 发起方主动发起转账请求（点击对方提供账号）
     */

    //TODO  传参： 1、自己的卡号 2、自己欧乐号 olNo 3、对方欧乐号 friendOlNo， 4 tipsType 0
    //TODO  返回： tipsId 数据库主键
    public void startTransRequest(String cardNo, String friendOlNo) {
        friendolNo = friendOlNo;
        Log.i(TAG,"friendolNo : " +friendolNo);
        Log.i(TAG,"请求对方提供账户*****************************");
        userRepository.setTransferTips(cardNo, friendOlNo, null, -1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<Integer>(TAG){
                    @Override
                    public void onNext(Integer integer) {
                        super.onNext(integer);
                        requestTipsId = integer;
                        Log.i(TAG,"对方提供账户请求成功***************************** integer: " +integer);
                    }
                });
    }

    /**
     * 接受方查询是否有转账请求（查询转账请求）
     * Integer:tipsId
     */

    //TODO  传参：接收方自己的欧乐号 friendOlNo  ； tipsType 1
    //TODO  返回： tipsId 数据库主键
    public void receiveTransRequest(String friendOlNo) {
        userRepository.queryTransferTips(friendOlNo,-1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<TransferProtocol>(TAG){
                    @Override
                    public void onNext(TransferProtocol transferProtocol) {
                        super.onNext(transferProtocol);
                        Log.i(TAG,"接受方查询是否有转账请求*****************************");
                        receiveTipsId = transferProtocol.getTipsId();
                        Log.i(TAG,"receiveTipsId: "+receiveTipsId);
                        if (receiveTipsId != 0) {
//                            Intent intent = new Intent(OKLineApp.context, SelectAccountActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            OKLineApp.context.startActivity(intent);
                        }
                    }
                });
    }

    /**
     * 发起方查询到对方提供的银行卡信息
     */

    //TODO  传参：tipsId  ；tipsType = 0
    //TODO  返回：tipsId ； returnState 发起方 0， 接受方 1
    public void getBankInfo() {

        userRepository.queryTransferTips(friendolNo,requestTipsId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<TransferProtocol>(TAG){
                    @Override
                    public void onNext(TransferProtocol transferProtocol) {
                        super.onNext(transferProtocol);
                        // TODO: 2017/5/9  如果对方提供了卡号，需要通知用户
                        if (transferProtocol.getReturnState() == 1) {
                            Log.i(TAG,"对方提供了银行卡********************");

                        }else {
                            Log.i(TAG,"没有收到对方的银行卡**********************");
                        }
                    }
                });
    }

    /**
     * 接受方提供银行卡信息
     */
    //TODO  传参：tipsId  ；tipsType = 1； friendCardNo  接受方提供的卡号
    //TODO  返回：tipsId ；
    public void offerBankInfo(String friendCardNo) {
        userRepository.setTransferTips(null, friendolNo, friendCardNo, receiveTipsId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<Integer>(TAG){
                    @Override
                    public void onNext(Integer integer) {
                        super.onNext(integer);
                    }
                });
    }
}
