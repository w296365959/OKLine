package com.vboss.okline.ui.contact.ContactDetail;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.helper.FragmentToolbar;
import com.vboss.okline.ui.card.widget.LogoView;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.utils.FileSizeUtils;
import com.vboss.okline.utils.TextUtils;
import com.vboss.okline.utils.ToastUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.vboss.okline.utils.FileSizeUtils.SIZETYPE_KB;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/5/3 10:33
 * Desc :
 */

public class SendEmailActivity extends BaseActivity {
    protected static final int REQUEST_CODE_LOCAL = 3;
    private static final String TAG = "SendEmailActivity";
    @BindView(R.id.toolbar_contact)
    FragmentToolbar toolbar;
    @BindView(R.id.lv_choose_document)
    ListView lvChooseDocument;
    @BindView(R.id.receiver)
    TextView receiver;
    @BindView(R.id.receiver_button)
    TextView receiverButton;
    @BindView(R.id.main_object)
    TextView mainObject;
    @BindView(R.id.accessory_button)
    TextView accessoryButton;
    @BindView(R.id.et_recipients)
    EditText etRecipients;

    //假数据
    @BindView(R.id.turn_back)
    LinearLayout turnBack;
    List<AccessaryBean> list = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_email_activity);
        ButterKnife.bind(this);
        initToolbar();
        //add by linzhangbin 默认弹出软键盘 start
        etRecipients.setFocusable(true);
        etRecipients.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) etRecipients.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(etRecipients, 0);
        //add by linzhangbin 默认弹出软键盘 end
    }

    private void initToolbar() {
        toolbar.setActionTitle(getResources().getString(R.string.title_new_email));
        toolbar.setNavigationVisible(View.VISIBLE);
        toolbar.setActionMenuVisible(View.VISIBLE);
        toolbar.setActionMenuIcon(R.mipmap.text_send);
        toolbar.setOnNavigationClickListener(new FragmentToolbar.OnNavigationClickListener() {
            @Override
            public void onNavigation(View v) {
                TextUtils.showOrHideSoftIM(etRecipients,false);
                finish();
            }
        });
        toolbar.setOnActionMenuClickListener(new FragmentToolbar.OnActionMenuClickListener() {
            @Override
            public void onActionMenu(View v) {
                ToastUtil.show(SendEmailActivity.this, "发送邮件");
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LOCAL) {
            if (data != null) {
                Uri selectedImage = data.getData();
                if (selectedImage != null) {
//                    sendPicByUri(selectedImage);
//                    File file = new File(selectedImage);
//                    FileSizeUtils.getFileOrFilesSize(,)
                    String fileByUri = getFileByUri(selectedImage);
                    File file = new File(fileByUri);
                    //文件大小
                    long length = file.length();
                    //图片
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    double fileOrFilesSize = FileSizeUtils.getFileOrFilesSize(fileByUri, SIZETYPE_KB);
                    String name = file.getName();
                    AccessaryBean accessaryBean = new AccessaryBean();
                    turnBack.setVisibility(View.VISIBLE);

                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * send image
     *
     * @param selectedImage
     */
    protected void sendPicByUri(Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {
                Toast toast = Toast.makeText(this, com.hyphenate.easeui.R.string.cant_find_pictures, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
//            sendImageMessage(picturePath);
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                Toast toast = Toast.makeText(this, com.hyphenate.easeui.R.string.cant_find_pictures, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;

            }
//            sendImageMessage(file.getAbsolutePath());
        }

    }


    /**
     * select local image
     */
    protected void selectPicFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_LOCAL);
    }

    /**
     * send file
     *
     * @param uri
     */
    protected String getFileByUri(Uri uri) {
        String filePath = null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;

            try {
                cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            filePath = uri.getPath();
        }
        return filePath;
    }

    @OnClick({R.id.receiver_button, R.id.accessory_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.receiver_button:
                //跳秀秀页面

                break;
            case R.id.accessory_button:
                selectPicFromLocal();

                break;
        }
    }

    public class EmailAdapter extends BaseAdapter {
        List<AccessaryBean> mList;

        public EmailAdapter(List<AccessaryBean> list) {
            if (null != list && list.size() > 0) {
                this.mList = list;
            }
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int i) {
            return mList.get(i);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {


            return null;
        }
    }
}
