package com.goshu.hongbien;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.goshu.hongbien.model.Post;

public class FragmentContentView extends RelativeLayout {

    // internal components
    private RelativeLayout header;
    private ImageView imageContent;
    private ImageButton avaContent;
    private Button displayName;
    private Button timeStamp;
    private TextView messageContent;
    private Button btnLike;
    private Button btnComment;
    private Button btnShare;

    public FragmentContentView(Context context) {
        super(context);
        initControl(context);
    }

    private void initControl(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.fragment_content, this);

        // layout is inflated, assign local variables to components
        header = (RelativeLayout) findViewById(R.id.fragment_content);
        imageContent = (ImageView) findViewById(R.id.image_content);
        avaContent = (ImageButton) findViewById(R.id.ava_content);
        displayName = (Button) findViewById(R.id.display_name);
        timeStamp = (Button) findViewById(R.id.time_stamp);
        messageContent = (TextView) findViewById(R.id.message_content);
        btnLike = (Button) findViewById(R.id.btn_like);
        btnLike = (Button) findViewById(R.id.btn_comment);
        btnLike = (Button) findViewById(R.id.btn_share);
    }

    public void updateFragmentView(Post post) {
        messageContent.setText(post.getContent());
        displayName.setText(post.getDisplayName());
    }
}
