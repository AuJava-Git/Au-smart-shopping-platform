package cn.fly.smartshopping.DTO;
//这个对象的作用是把两部分数据放在一起返回：
//order：订单基本信息
//items：订单商品明细
import cn.fly.smartshopping.entity.Order;
import cn.fly.smartshopping.entity.OrderItem;

import java.util.List;

public class OrderDetailResponse {

    private Order order;

    private List<OrderItem> items;

    public OrderDetailResponse() {
    }

    public OrderDetailResponse(Order order, List<OrderItem> items) {
        this.order = order;
        this.items = items;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}
