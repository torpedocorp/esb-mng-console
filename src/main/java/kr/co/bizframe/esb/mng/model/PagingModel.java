package kr.co.bizframe.esb.mng.model;

import java.util.ArrayList;
import java.util.List;

public class PagingModel<T> {

	private long count;

	private List<T> list = new ArrayList<T>();

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public List<T> getModels() {
		return list;
	}

	public void addModel(T model) {
		this.list.add(model);
	}

	public void setModels(List<T> list) {
		this.list.addAll(list);
	}

}
