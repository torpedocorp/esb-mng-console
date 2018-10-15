package kr.co.bizframe.esb.mng.model;

public class SearchOptions {
	private int pageCnt;
	private int curPage;
	private String f_date;
	private String t_date;
	private String strSearch;
	private String searchKey;
	private int itemCnt;
	private int index;
	private int limit;
	private String fromDate;
	private String toDate;
	private String agentId;
	private String routeId;
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getPageCnt() {
		return pageCnt;
	}

	public void setPageCnt(int pageCnt) {
		this.pageCnt = pageCnt;
	}

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public String getF_date() {
		return f_date;
	}

	public void setF_date(String f_date) {
		this.f_date = f_date;
	}

	public String getT_date() {
		return t_date;
	}

	public void setT_date(String t_date) {
		this.t_date = t_date;
	}

	public String getStrSearch() {
		return strSearch;
	}

	public void setStrSearch(String strSearch) {
		this.strSearch = strSearch;
	}

	public int getItemCnt() {
		return itemCnt;
	}

	public void setItemCnt(int itemCnt) {
		this.itemCnt = itemCnt;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SearchOptions [pageCnt=");
		builder.append(pageCnt);
		builder.append(", curPage=");
		builder.append(curPage);
		builder.append(", f_date=");
		builder.append(f_date);
		builder.append(", t_date=");
		builder.append(t_date);
		builder.append(", strSearch=");
		builder.append(strSearch);
		builder.append(", searchKey=");
		builder.append(searchKey);
		builder.append(", itemCnt=");
		builder.append(itemCnt);
		builder.append(", index=");
		builder.append(index);
		builder.append(", limit=");
		builder.append(limit);
		builder.append(", fromDate=");
		builder.append(fromDate);
		builder.append(", toDate=");
		builder.append(toDate);
		builder.append(", agentId=");
		builder.append(agentId);
		builder.append(", routeId=");
		builder.append(routeId);
		builder.append("]");
		return builder.toString();
	}
}
