package com.vboss.okline.ui.user.files;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.data.model.CloudFileModel;
import com.vboss.okline.ui.card.widget.LogoView;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.view.widget.OKCardView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyFilesActivity extends BaseActivity implements MyFilesContract.View {

    public static final String FILE_TYPE = "file_type";
    public static final int FILE_TYPE_DOCUMENT = 1;
    public static final int FILE_TYPE_SMS= 2;
    public static final int FILE_TYPE_MAIL= 3;
    @BindView(R.id.action_back)
    ImageButton actionBack;
    @BindView(R.id.iv_ocard_state)
    LogoView ivOcardState;
    @BindView(R.id.okcard_view)
    OKCardView okcardView;
    @BindView(R.id.action_back_layout)
    RelativeLayout actionBackLayout;
    @BindView(R.id.sdv_logo)
    SimpleDraweeView sdvLogo;
    @BindView(R.id.action_title)
    TextView actionTitle;
    @BindView(R.id.action_menu_button)
    ImageButton actionMenuButton;
    @BindView(R.id.action_menu_layout)
    RelativeLayout actionMenuLayout;
    @BindView(R.id.rv_items)
    RecyclerView rvItems;
    private int fileType;
    private MyFilesPresenter myFilesPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_files);
        ButterKnife.bind(this);
        myFilesPresenter = new MyFilesPresenter(MyFilesActivity.this);
        actionBackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fileType = getIntent().getIntExtra(FILE_TYPE, FILE_TYPE_DOCUMENT);
        switch (fileType) {
            case FILE_TYPE_DOCUMENT:
                actionTitle.setText("我的文件");
                myFilesPresenter.getDocumentList(new CloudFileFetchListener() {
                    @Override
                    public void onFetch(List<CloudFileModel> files) {
                        showFiles(files);
                    }

                    @Override
                    public void onFail(String message) {
                        Utils.customToast(MyFilesActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case FILE_TYPE_SMS:
                actionTitle.setText("我的短信");
                break;
            case FILE_TYPE_MAIL:
                actionTitle.setText("我的邮件");
                break;
        }

    }

    @Override
    public void showFiles(List<CloudFileModel> fileModels) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyFilesActivity.this);
        rvItems.setLayoutManager(linearLayoutManager);
        rvItems.setAdapter(new FileAdapter(fileModels, MyFilesActivity.this));
    }
}

class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder>{

    private final Context context;
    private List<CloudFileModel> fileInfos;

    public FileAdapter(List<CloudFileModel> fileInfos, Context context) {
        this.fileInfos = fileInfos;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(),R.layout.item_my_files_doc,null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CloudFileModel fileInfo = fileInfos.get(position);
        holder.sdv_pic.setImageURI(fileInfo.getIcon());
        holder.tv_date.setText(fileInfo.getDate());
        holder.tv_format.setText(fileInfo.getFormat()+context.getString(R.string.blank_3)+fileInfo.getSize());
        holder.tv_name.setText(fileInfo.getTitle());
    }

    @Override
    public int getItemCount() {
        return fileInfos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        SimpleDraweeView sdv_pic;
        TextView tv_name;
        TextView tv_format;
        TextView tv_date;

        public ViewHolder(View itemView) {
            super(itemView);
            sdv_pic = (SimpleDraweeView) itemView.findViewById(R.id.sdv_pic);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_format = (TextView) itemView.findViewById(R.id.tv_format);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
        }
    }
}
