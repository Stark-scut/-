package Pojo;

import java.util.List;

public class Page {
    private int pageNumber;   // 起始页
    private int pageSize;     // 每页条数
    private int totalCount;   // 商品总数
    private int totalPage;    // 总页数

    private List<Object> list;   // 存储商品

    public void SetPageSizeAndTotalCount(int pageSize,int totalCount)
    {
        this.pageSize=pageSize;
        this.totalCount=totalCount;
        totalPage= (int)Math.ceil((double)totalCount/pageSize);
    }
    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<Object> getList() {
        return list;
    }

    public void setList(List<Object> list) {
        this.list = list;
    }
}
