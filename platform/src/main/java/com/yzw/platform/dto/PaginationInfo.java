package com.yzw.platform.dto;

import java.io.Serializable;

public class PaginationInfo implements Serializable {

	private static final long serialVersionUID = -8768267915482517000L;

	private int currentPage = 1;
	private int pageSize = 10;
	private int totalCount;
	private int totalPage;

	public PaginationInfo(final int currentPage, final int pageSize, final int totalPage, final int totalCount) {
		super();
		this.currentPage = currentPage;
		this.pageSize = pageSize;
		this.totalCount = totalCount;
		this.totalPage = totalPage;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public int getTotalPage() {
		return totalPage;
	}

	@Override
	public String toString() {
		return "PaginationInfo [currentPage=" + currentPage + ", pageSize=" + pageSize + ", totalCount=" + totalCount + ", totalPage=" + totalPage + "]";
	}
}
