package cn.fly.smartshopping.common;

public final class OrderStatus {
    public static final int PENDING_PAYMENT = 1;
    public static final int PAID = 2;
    public static final int CANCELLED = 3;

    private OrderStatus() {}

    public static boolean isValid(Integer status) {
        return status != null && (status == PENDING_PAYMENT || status == PAID || status == CANCELLED);
    }
}
