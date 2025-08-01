package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import dto.StockRequestDto;
import util.DbcpBean;

public class StockRequestDao {
    // 1. INSERT
    public boolean insertRequest(StockRequestDto dto) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int rowCount = 0;
        try {
            conn = new DbcpBean().getConn();
            String sql = """
                INSERT INTO stock_request 
                (order_id, branch_num, branch_id, inventory_id, product, current_quantity,
                 request_quantity, status, requestedat, updatedat, isPlaceOrder)
                VALUES (stock_request_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, SYSDATE, SYSDATE, ?, ?)
            """;
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, dto.getBranchNum());           // FK (branch_stock의 PK)
            pstmt.setString(2, dto.getBranchId());
            pstmt.setInt(3, dto.getInventoryId());
            pstmt.setString(4, dto.getProduct());
            pstmt.setInt(5, dto.getCurrentQuantity());
            pstmt.setInt(6, dto.getRequestQuantity());
            pstmt.setString(7, dto.getStatus());
            pstmt.setString(8, dto.getIsPlaceOrder());
            
            rowCount = pstmt.executeUpdate();
        } catch(Exception e) { e.printStackTrace(); }
        finally {
            try { if(pstmt!=null) pstmt.close(); } catch(Exception e){}
            try { if(conn!=null) conn.close(); } catch(Exception e){}
        }
        return rowCount > 0;
    }

    // 2. 단일건 SELECT (order_id로)
    public StockRequestDto selectByOrderId(int orderId) {
        StockRequestDto dto = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = new DbcpBean().getConn();
            String sql = "SELECT * FROM stock_request WHERE order_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, orderId);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                dto = new StockRequestDto();
                dto.setOrderId(rs.getInt("order_id"));
                dto.setBranchNum(rs.getInt("branch_num"));
                dto.setBranchId(rs.getString("branch_id"));
                dto.setInventoryId(rs.getInt("inventory_id"));
                dto.setProduct(rs.getString("product"));
                dto.setCurrentQuantity(rs.getInt("current_quantity"));
                dto.setRequestQuantity(rs.getInt("request_quantity"));
                dto.setStatus(rs.getString("status"));
                dto.setRequestedAt(rs.getDate("requestedat"));
                dto.setUpdatedAt(rs.getDate("updatedat"));
                dto.setIsPlaceOrder(rs.getString("isPlaceOrder"));
                
            }
        } catch(Exception e) { e.printStackTrace(); }
        finally {
            try { if(rs!=null) rs.close(); } catch(Exception e){}
            try { if(pstmt!=null) pstmt.close(); } catch(Exception e){}
            try { if(conn!=null) conn.close(); } catch(Exception e){}
        }
        return dto;
    }

    // 3. 전체/지점별 SELECT
    public List<StockRequestDto> selectAllByBranch(String branchId) {
        List<StockRequestDto> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = new DbcpBean().getConn();
            String sql = """
                SELECT * FROM stock_request
                WHERE branch_id = ?
                ORDER BY requestedat DESC
            """;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, branchId);
            rs = pstmt.executeQuery();
            while(rs.next()) {
                StockRequestDto dto = new StockRequestDto();
                dto.setOrderId(rs.getInt("order_id"));
                dto.setBranchNum(rs.getInt("branch_num"));
                dto.setBranchId(rs.getString("branch_id"));
                dto.setInventoryId(rs.getInt("inventory_id"));
                dto.setProduct(rs.getString("product"));
                dto.setCurrentQuantity(rs.getInt("current_quantity"));
                dto.setRequestQuantity(rs.getInt("request_quantity"));
                dto.setStatus(rs.getString("status"));
                dto.setRequestedAt(rs.getDate("requestedat"));
                dto.setUpdatedAt(rs.getDate("updatedat"));
                dto.setIsPlaceOrder(rs.getString("isPlaceOrder"));
                
                list.add(dto);
            }
        } catch(Exception e) { e.printStackTrace(); }
        finally {
            try { if(rs!=null) rs.close(); } catch(Exception e){}
            try { if(pstmt!=null) pstmt.close(); } catch(Exception e){}
            try { if(conn!=null) conn.close(); } catch(Exception e){}
        }
        return list;
    }

    // 4. UPDATE (주로 요청수량, 상품명만 바꿈. 필요시 파라미터/필드 더 추가 가능)
    public boolean updateRequest(int orderId, String product, int requestQuantity) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int rowCount = 0;
        try {
            conn = new DbcpBean().getConn();
            String sql = """
                UPDATE stock_request 
                   SET product=?, request_quantity=?, updatedat=SYSDATE 
                 WHERE order_id=?
            """;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, product);
            pstmt.setInt(2, requestQuantity);
            pstmt.setInt(3, orderId);
            rowCount = pstmt.executeUpdate();
        } catch(Exception e) { e.printStackTrace(); }
        finally {
            try { if(pstmt!=null) pstmt.close(); } catch(Exception e){}
            try { if(conn!=null) conn.close(); } catch(Exception e){}
        }
        return rowCount > 0;
    }

    // 5. DELETE
    public boolean deleteRequest(int orderId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int rowCount = 0;
        try {
            conn = new DbcpBean().getConn();
            String sql = "DELETE FROM stock_request WHERE order_id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, orderId);
            rowCount = pstmt.executeUpdate();
        } catch(Exception e) { e.printStackTrace(); }
        finally {
            try { if(pstmt!=null) pstmt.close(); } catch(Exception e){}
            try { if(conn!=null) conn.close(); } catch(Exception e){}
        }
        return rowCount > 0;
    }
    
    public List<StockRequestDto> selectAll() {
        List<StockRequestDto> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = new DbcpBean().getConn();

            String sql = """
                SELECT order_id, branch_num, branch_id, inventory_id, 
                    product, current_quantity, request_quantity, status, 
                    requestedat, updatedat, isPlaceOrder
                FROM stock_request
                ORDER BY order_id DESC
                """;

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                StockRequestDto dto = new StockRequestDto();
                dto.setOrderId(rs.getInt("order_id"));
                dto.setBranchNum(rs.getInt("branch_num"));
                dto.setBranchId(rs.getString("branch_id"));
                dto.setInventoryId(rs.getInt("inventory_id"));
                dto.setProduct(rs.getString("product"));
                dto.setCurrentQuantity(rs.getInt("current_quantity"));
                dto.setRequestQuantity(rs.getInt("request_quantity"));
                dto.setStatus(rs.getString("status"));
                dto.setRequestedAt(rs.getDate("requestedat"));
                dto.setUpdatedAt(rs.getDate("updatedat"));
                dto.setIsPlaceOrder(rs.getString("isPlaceOrder"));
                

                list.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {}
        }

        return list;
    }
    
    public String getProductByNum(int orderId) {
        String product = null;
        String sql = """
            SELECT product
            FROM stock_request
            WHERE order_id = ?
        """;
        try (Connection conn = new DbcpBean().getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    product = rs.getString("product");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return product;
    }

    public int getQuantityByNum(int num) throws SQLException {
        String sql = """
            SELECT request_quantity
            FROM stock_request
            WHERE order_id = ?
        """;
        try (Connection conn = new DbcpBean().getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, num);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt("request_quantity") : 0;
        }
    }

    public String getBranchIdByNum(int num) throws SQLException {
        String sql = """
            SELECT branch_id
            FROM stock_request
            WHERE order_id = ?
        """;
        try (Connection conn = new DbcpBean().getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, num);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString("branch_id") : null;
        }
    }

    public int getInventoryIdByNum(int num) throws SQLException {
        String sql = """
            SELECT inventory_id
            FROM stock_request
            WHERE order_id = ?
        """;
        try (Connection conn = new DbcpBean().getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, num);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt("inventory_id") : 0;
        }
    }

    public void updateApproval(int num, String status) throws SQLException {
        String sql = """
            UPDATE stock_request
            SET approval = ?
            WHERE order_id = ?
        """;
        try (Connection conn = new DbcpBean().getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, num);
            ps.executeUpdate();
        }
    }

    public void updatePlaceOrder(int num, boolean isPlaceOrder) throws SQLException {
        String sql = """
            UPDATE stock_request
            SET is_placeorder = ?
            WHERE order_id = ?
        """;
        try (Connection conn = new DbcpBean().getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, isPlaceOrder ? "승인" : "대기");
            ps.setInt(2, num);
            ps.executeUpdate();
        }
    }

    public void updateDate(int num) throws SQLException {
        String sql = """
            UPDATE stock_request
            SET updatedAt = SYSDATE
            WHERE order_id = ?
        """;
        try (Connection conn = new DbcpBean().getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, num);
            ps.executeUpdate();
        }
    }

    public void update(int num, String isPlaceOrder) throws SQLException {
        String sql = """
            UPDATE stock_request
            SET is_placeorder = ?
            WHERE order_id = ?
        """;
        try (Connection conn = new DbcpBean().getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, isPlaceOrder);
            ps.setInt(2, num);
            ps.executeUpdate();
        }
    }
    
    public void updateStatus(int num, String status) throws SQLException {
        String sql = """
            UPDATE stock_request
            SET status = ?, updatedAt = SYSDATE
            WHERE order_id  = ?
        """;

        try (Connection conn = new DbcpBean().getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, num);
            ps.executeUpdate();
        }
    }
    
    public void updateIsPlaceOrder(int num, String value) {
        String sql = "UPDATE stock_request SET isplaceorder = ? WHERE order_id = ?";
        try (Connection conn = new DbcpBean().getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, value);
            ps.setInt(2, num);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int getNumByDetailId(int detailId) {
        String sql = "SELECT request_num FROM stock_request WHERE detail_id = ?";
        try (Connection conn = new DbcpBean().getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, detailId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("request_num");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // 존재하지 않을 경우
    }

    /**
     * 승인 수량 감소 (예: 반려 처리 시 기존 승인 수량 복원)
     */
    public void decreaseCurrentQuantity(int requestNum, int qty) {
        updateCurrentQuantity(requestNum, -qty);
    }

    /**
     * 승인 수량 증가 (예: 반려 → 승인 전환 시)
     */
    public void increaseCurrentQuantity(int requestNum, int qty) {
        updateCurrentQuantity(requestNum, qty);
    }

    /**
     * 승인 수량 조정 (예: 승인 상태에서 수량 변경 시)
     */
    public void adjustCurrentQuantity(int requestNum, int diffQty) {
        updateCurrentQuantity(requestNum, diffQty);
    }

    /**
     * 내부 메소드: current_quantity 값 ±변경
     */
    private void updateCurrentQuantity(int requestNum, int qtyDiff) {
        String sql = "UPDATE stock_request SET current_quantity = current_quantity + ? WHERE request_num = ?";
        try (Connection conn = new DbcpBean().getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, qtyDiff);
            pstmt.setInt(2, requestNum);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

