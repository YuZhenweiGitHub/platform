package com.yzw.platform.dto;

/**
 * 带分页数据的结果对象
 */
public class ResultPageVO extends ResultDto {
	private static final long serialVersionUID = -7920448990645099433L;

	private PaginationInfo page;

	public PaginationInfo getPage() {
		return page;
	}

	public void setPage(PaginationInfo page) {
		this.page = page;
	}

}
