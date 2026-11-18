package cn.fly.smartshopping.DTO;

public class ReviewEligibilityResponse {
    private boolean loggedIn;
    private boolean purchased;
    private boolean canReview;
    private String message;

    public ReviewEligibilityResponse() {}

    public ReviewEligibilityResponse(boolean loggedIn, boolean purchased, boolean canReview, String message) {
        this.loggedIn = loggedIn;
        this.purchased = purchased;
        this.canReview = canReview;
        this.message = message;
    }

    public boolean isLoggedIn() { return loggedIn; }
    public void setLoggedIn(boolean loggedIn) { this.loggedIn = loggedIn; }
    public boolean isPurchased() { return purchased; }
    public void setPurchased(boolean purchased) { this.purchased = purchased; }
    public boolean isCanReview() { return canReview; }
    public void setCanReview(boolean canReview) { this.canReview = canReview; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
