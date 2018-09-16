package br.com.marginais.controller.util;

import java.util.List;

public abstract class PaginationHelper<T> {

	private long count;
	private long totalOfPages;

	private int pageIndex;
	protected int begin;
	protected int end;

	private List<T> items;

	public PaginationHelper(long count, int page, int pageSize) {
		this.count = count;
		this.pageIndex = page - 1;

		if (count > 0 && page > 0) {
			totalOfPages = count / pageSize;
			if ((int) (long) count % pageSize > 0) {
				totalOfPages++;
			}

			begin = pageIndex * pageSize;
			end = begin + pageSize - 1;

			items = isValidPage() ? getData() : null;
		} else {
			totalOfPages = 1;

			begin = 0;
			end = 0;
		}
	}

	protected abstract List<T> getData();

	public List<T> getItems() {
		return items;
	}

	public boolean isValidPage() {
		return count >= 0 && pageIndex >= 0 && begin >= 0 && (begin < count || count == 0);
	}

	public boolean getHasPrev() {
		return isValidPage() && pageIndex > 0;
	}

	public boolean getHasNext() {
		return isValidPage() && pageIndex < totalOfPages - 1;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public long getTotalOfPages() {
		return totalOfPages;
	}

}
