package kr.co.bizframe.esb.mng.utils;

import static kr.co.bizframe.esb.mng.utils.Strings.trim;

import kr.co.bizframe.esb.mng.model.SearchOptions;

public class PagingUtil {

	public static void getPageArgs(SearchOptions request) throws Throwable {

		int curPage = request.getCurPage();
		int pageCnt = request.getPageCnt();
		int itemCnt = request.getItemCnt();
		curPage = curPage == 0 ? 1 : curPage;
		pageCnt = pageCnt == 0 ? 5 : pageCnt;
		itemCnt = itemCnt == 0 ? 10 : itemCnt;
		request.setIndex((curPage - 1) * itemCnt);
		request.setLimit(itemCnt);

		String today = TimeUtil.getCurrentDateTime("yyyy-MM-dd");
		if (Strings.trim(request.getF_date()) != null) {
			String strFromDate = trim(request.getF_date(), today);
			request.setFromDate(strFromDate);
		}

		if (Strings.trim(request.getT_date()) != null) {
			String strToDate = trim(request.getT_date(), today);
			request.setToDate(strToDate);
		}
	}

}
