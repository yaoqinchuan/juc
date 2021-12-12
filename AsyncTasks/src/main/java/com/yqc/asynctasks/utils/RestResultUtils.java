package com.yqc.asynctasks.utils;

import com.yqc.asynctasks.common.ResetResult;
import com.yqc.asynctasks.enums.ErrorCodeEnum;

public class RestResultUtils {
    public static  <T> ResetResult<T> success(T data) {
        ResetResult<T> result = new ResetResult<T>();
        result.setData(data);
        result.setErrorCode(ErrorCodeEnum.SuccessCode.getErrorCode());
        result.setErrorMessage(ErrorCodeEnum.SuccessCode.getErrorMessage());
        return result;
    }

    public static  <T> ResetResult<T> failed(String errorCode, String errorMessage) {
        ResetResult<T> result = new ResetResult<T>();
        result.setErrorCode(errorCode);
        result.setErrorMessage(errorMessage);
        return result;
    }
}
