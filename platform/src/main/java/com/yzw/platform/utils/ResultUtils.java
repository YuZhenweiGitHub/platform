package com.yzw.platform.utils;

import com.yzw.platform.dto.PaginationData;
import com.yzw.platform.dto.PaginationInfo;
import com.yzw.platform.dto.ResultDto;
import com.yzw.platform.dto.ResultPageVO;
import com.yzw.platform.enums.ResultMessageEnum;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * 消息结果工具类
 */
@Slf4j
public class ResultUtils {

	public static ResultDto buildMessageByEnum(ResultMessageEnum messageEnum, Object... params) {
		ResultDto result = new ResultDto();
		if (null != messageEnum) {
			result.setCode(messageEnum.getCode().intValue());
			result.setMessage(MessageFormat.format(messageEnum.getMessage(), params));
		} else {
			result.setCode(ResultMessageEnum.SUCCESS.getCode());
			result.setMessage(ResultMessageEnum.SUCCESS.getMessage());
		}
		return result;
	}

	public static ResultDto buildMessageByEnum(ResultMessageEnum messageEnum) {
		ResultDto result = new ResultDto();
		if (null != messageEnum) {
			result.setCode(messageEnum.getCode().intValue());
			result.setMessage(messageEnum.getMessage());
		} else {
			result.setCode(ResultMessageEnum.SUCCESS.getCode());
			result.setMessage(ResultMessageEnum.SUCCESS.getMessage());
		}
		return result;
	}

	/**
	 * 构建执行正确的消息返回结果
	 * @param messageEnum
	 * @return
	 */
	public static ResultDto buildMessageByEnumAndData(ResultMessageEnum messageEnum, Object data) {
		ResultDto result = new ResultDto();
		if (null != messageEnum) {
			result.setCode(messageEnum.getCode().intValue());
			result.setMessage(messageEnum.getMessage());
		} else {
			result.setCode(ResultMessageEnum.SUCCESS.getCode());
			result.setMessage(ResultMessageEnum.SUCCESS.getMessage());
		}
		if (null != data) {
			result.setData(data);
		}
		return result;
	}

	/**
	 * 构建执行正确的消息返回结果-默认消息
	 */
	public static ResultDto buildSuccessMessage() {
		return buildMessageByEnum(ResultMessageEnum.SUCCESS);
	}

	public static ResultDto buildErrorCustomMessage(Integer code, String message) {
		ResultDto result = new ResultDto();
		result.setCode(code);
		result.setMessage(message);
		return result;
	}

	/**
	 * 构建执行正确的消息返回结果-默认消息
	 */
	public static ResultDto buildSuccessData(Object data) {
		return buildMessageByEnumAndData(ResultMessageEnum.SUCCESS, data);
	}

	/**
	 * 构建执行正确的数据返回结果-带分页数据
	 * @param data
	 * @param pageInfo
	 * @return
	 */
	public static ResultDto buildSuccessData(Object data, PaginationInfo pageInfo) {
		ResultPageVO vo = new ResultPageVO();
		vo.setCode(ResultMessageEnum.SUCCESS.getCode());
		vo.setData(data);
		vo.setPage(pageInfo);
		return vo;
	}

	/**
	 * 构建空分页返回结果集
	 * 
	 * @param clazz
	 * @param pageId
	 * @param pageSize
	 * @return
	 */
	public static <T> ResultDto buildSuccessEmptyPageData(Class<T> clazz, Integer pageId, Integer pageSize) {
		PaginationData<T> page = new PaginationData<T>(new ArrayList<T>(), pageId, pageSize, 0, 0);
		return ResultUtils.buildSuccessData(page.getPageData(), page.getPageInfo());
	}

	/**
	 * 构建执行异常的数据返回结果
	 * @param data
	 * @return
	 */
	public static ResultDto buildErrorData(Object data) {
		return buildMessageByEnumAndData(ResultMessageEnum.SYSTEM_ERROR, data);
	}

	/**
	 * 构建系统异常的消息返回结果
	 * @return
	 */
	public static ResultDto buildErrorMessage() {
		return buildMessageByEnum(ResultMessageEnum.SYSTEM_ERROR);
	}

}
