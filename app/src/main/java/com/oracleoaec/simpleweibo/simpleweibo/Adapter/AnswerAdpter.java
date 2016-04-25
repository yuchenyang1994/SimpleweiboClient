package com.oracleoaec.simpleweibo.simpleweibo.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oracleoaec.simpleweibo.simpleweibo.R;
import com.oracleoaec.simpleweibo.simpleweibo.Utils.CommonAdapter;
import com.oracleoaec.simpleweibo.simpleweibo.Utils.ViewHolder;
import com.oracleoaec.simpleweibo.simpleweibo.enity.Answers;


import java.util.List;

/**
 * Created by ycy on 16-4-15.
 */
public class AnswerAdpter<T> extends CommonAdapter<T> {
    public AnswerAdpter(Context context, List<T> mDatas) {
        super(context, mDatas);
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        ViewHolder viewHolder = ViewHolder.get(context,view,viewGroup, R.layout.getanswe_layout,i);
        Answers answers = (Answers) mdatas.get(i);
        TextView username = viewHolder.getView(R.id.tv_answer_username);
        TextView answer_content = viewHolder.getView(R.id.tv_answwer_content);
        TextView answer_time = viewHolder.getView(R.id.tv_answer_time);
        username.setText(answers.getUser_name());
        answer_content.setText(answers.getContent());
        answer_time.setText(answers.getAnswertime());
        return viewHolder.getConvertView();
    }
}
