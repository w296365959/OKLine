package com.vboss.okline.data.local;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import com.vboss.okline.data.entities.AppEntity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/4/19 <br/>
 * Summary : 在这里描述Class的主要功能
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
public class AppLocalDataSourceTest {

    private AppLocalDataSource local;

    @Before
    public void setUp() throws Exception {
        if (local == null) {
            local = AppLocalDataSource.getInstance(InstrumentationRegistry.getTargetContext());
        }
    }

    @Test
    public void getPendingUpdateApp() throws Exception {
        List<AppEntity> list = local.getPendingUpdateApp(InstrumentationRegistry.getTargetContext()).toBlocking().single();
        System.out.println(list);
    }

}