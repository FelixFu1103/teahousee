package com.cmpe277.onlinemilktea.Interface;

import com.cmpe277.onlinemilktea.Model.CommentModel;

import java.util.List;

public interface ICommentCallbackListener {
    void onCommentLoadSuccess(List<CommentModel> commentModels);
    void onCommentLoadFailed(String message);
}
