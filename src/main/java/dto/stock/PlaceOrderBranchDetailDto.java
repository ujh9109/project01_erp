package dto.stock;

public class PlaceOrderBranchDetailDto {
	private int detail_id;
	private int order_id;
	private String branch_id;
	private int inventory_id;
	private String product;
	private int current_quantity;
	private int request_quantity;
	private String approval_status;
	private String manager;
	
	
	
	
	public String getBranch_id() {
		return branch_id;
	}
	public void setBranch_id(String branch_id) {
		this.branch_id = branch_id;
	}
	public int getInventory_id() {
		return inventory_id;
	}
	public void setInventory_id(int inventory_id) {
		this.inventory_id = inventory_id;
	}
	public int getOrder_id() {
		return order_id;
	}
	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}
	public int getDetail_id() {
		return detail_id;
	}
	public void setDetail_id(int detail_id) {
		this.detail_id = detail_id;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public int getCurrent_quantity() {
		return current_quantity;
	}
	public void setCurrent_quantity(int current_quantity) {
		this.current_quantity = current_quantity;
	}
	public int getRequest_quantity() {
		return request_quantity;
	}
	public void setRequest_quantity(int request_quantity) {
		this.request_quantity = request_quantity;
	}
	public String getApproval_status() {
		return approval_status;
	}
	public void setApproval_status(String approval_status) {
		this.approval_status = approval_status;
	}
	public String getManager() {
		return manager;
	}
	public void setManager(String manager) {
		this.manager = manager;
	}
}
