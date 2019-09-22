package com.vboss.okline;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.vboss.okline.data.AppRepository;
import com.vboss.okline.data.entities.AppEntity;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/4/20 <br/>
 * Summary : 在这里描述Class的主要功能
 */

@RunWith(AndroidJUnit4.class)
public class DataModuleInterfaceTest {

    @Test
    public void getPendingUpdateApp() {
        Context appContext = InstrumentationRegistry.getTargetContext();

        AppRepository repository = AppRepository.getInstance(appContext);
        List<AppEntity> list = repository.getPendingUpdateApp(appContext).toBlocking().single();
        System.out.println(list);
    }

}
