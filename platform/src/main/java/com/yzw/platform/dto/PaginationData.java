package com.yzw.platform.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PaginationData<T> implements Serializable {

	private static final long serialVersionUID = 4242254700977686119L;

	private List<T> pageData;
	private final PaginationInfo pageInfo;



	/**
	 * 数据原样拼接分页数据
	 * 
	 * @param data
	 * @param pageId
	 * @param pageSize
	 * @param totalPages
	 * @param totalCount
	 */
	public PaginationData(final List<T> data, final int pageId, final int pageSize, final int totalPages, final int totalCount) {
		pageData = Collections.unmodifiableList(data);
		pageInfo = new PaginationInfo(pageId, pageSize, totalPages, totalCount);
	}

	/**
	 * 数据原样拼接分页数据
	 * 
	 * @param pageData
	 * @param pageInfo
	 */
	public PaginationData(final List<T> pageData, final PaginationInfo pageInfo) {
		this.pageData = pageData;
		this.pageInfo = pageInfo;
	}

	/**
	 * 截取数据,拼接分页数据
	 * 
	 * @param list
	 * @param pageId
	 * @param pageSize
	 */
	public PaginationData(List<T> list, final int pageId, final int pageSize) {
		int begin = (pageId - 1) * pageSize;
		int end = pageId * pageSize;
		if (begin < 0) {
			begin = 0;
		}
		if (begin > list.size()) {
			this.pageData = Collections.unmodifiableList(new ArrayList<T>());
			this.pageInfo = new PaginationInfo(pageId, pageSize, 0, 0);
		} else {
			if (end > list.size()) {
				end = list.size();
			}
			int totalPages = ((list.size() - 1 + pageSize) / pageSize);
			int totalCount = list.size();
			list = list.subList(begin, end);
			// 截取数据
			this.pageData = Collections.unmodifiableList(list);
			this.pageInfo = new PaginationInfo(pageId, pageSize, totalPages, totalCount);
		}
	}

	public List<T> getPageData() {
		return pageData;
	}

	public void setPageData(List<T> pageData) {
		this.pageData = pageData;
	}

	public final PaginationInfo getPageInfo() {
		return pageInfo;
	}

	@Override
	public String toString() {
		return "PaginationData [pageData=" + pageData + ", pageInfo=" + pageInfo + "]";
	}

}
