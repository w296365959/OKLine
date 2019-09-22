package com.vboss.okline.data.remote;

import com.vboss.okline.data.LogUtils;
import com.vboss.okline.data.model.ExpressModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/7/5 <br/>
 * Summary : 在这里描述Class的主要功能
 */
public class RemoteExpressDataSourceTest {
    private RemoteExpressDataSource dataSource;

    @Before
    public void setUp() throws Exception {
        dataSource = new RemoteExpressDataSource();
    }

    @After
    public void tearDown() throws Exception {
        dataSource = null;
    }

    @Test
    public void getExpressList() throws Exception {
        List<ExpressModel> expressModels = dataSource.getExpressList(ExpressModel.ALL).toBlocking().single();
        LogUtils.println(expressModels);
    }

    @Test
    public void getExpressDetail() throws Exception {
        ExpressModel model = dataSource.getExpressDetail("6").toBlocking().single();
        LogUtils.println("Model : " + model);
    }

    @Test
    public void getCompanyList() throws Exception {
        List<ExpressModel.Company> companies = dataSource.getCompanyList().toBlocking().single();
        LogUtils.println("Companies : " + companies);
    }

    @Test
    public void getLogistics() throws Exception {
        List<ExpressModel.Logistics> logisticses = dataSource.getLogistics("6").toBlocking().single();
        LogUtils.println(logisticses);
    }

    @Test
    public void buildExpress() throws Exception {
        String expressId = dataSource.buildExpress("OLHZ510020000000001021", "2", "dfa", "dfadf", "dfsaf", "1345").toBlocking().single();
        LogUtils.println(expressId);
    }

    @Test
    public void confirmAddress() throws Exception {
        String expressId = dataSource.confirmAddress("OLHZ510020000000001021", "10", "杭州使得房间啊；的房间啊").toBlocking().single();
        LogUtils.println("ExpressId : " + expressId);
    }

    @Test
    public void notifyExpress() throws Exception {
        boolean success = dataSource.notifyExpress("10").toBlocking().single();
        LogUtils.println(success);
    }

}