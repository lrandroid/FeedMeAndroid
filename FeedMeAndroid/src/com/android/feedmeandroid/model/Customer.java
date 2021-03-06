package com.android.feedmeandroid.model;

import java.util.Map;

import com.android.feedmeandroid.exception.StripeException;
import com.android.feedmeandroid.net.APIResource;

public class Customer extends APIResource {
	Long created;
	String id;
	Boolean livemode;
	Boolean deleted;
	String description;
	Card activeCard;
	String email;
	String plan;
	Long trialEnd;
	Discount discount;
	NextRecurringCharge nextRecurringCharge;
	Subscription subscription;
	Boolean delinquent;
	Integer accountBalance;

	public Long getCreated() {
		return created;
	}

	public void setCreated(Long created) {
		this.created = created;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getLivemode() {
		return livemode;
	}

	public void setLivemode(Boolean livemode) {
		this.livemode = livemode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Card getActiveCard() {
		return activeCard;
	}

	public void setActiveCard(Card activeCard) {
		this.activeCard = activeCard;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public Long getTrialEnd() {
		return trialEnd;
	}

	public void setTrialEnd(Long trialEnd) {
		this.trialEnd = trialEnd;
	}

	public Discount getDiscount() {
		return discount;
	}

	public void setDiscount(Discount discount) {
		this.discount = discount;
	}

	public NextRecurringCharge getNextRecurringCharge() {
		return nextRecurringCharge;
	}

	public void setNextRecurringCharge(NextRecurringCharge nextRecurringCharge) {
		this.nextRecurringCharge = nextRecurringCharge;
	}

	public Subscription getSubscription() {
		return subscription;
	}

	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public Boolean getDelinquent() {
		return delinquent;
	}

	public void setDelinquent(Boolean delinquent) {
		this.delinquent = delinquent;
	}
	
	public Integer getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(Integer accountBalance) {
		this.accountBalance = accountBalance;
	}

	public static Customer create(Map<String, Object> params) throws StripeException {
		return request(RequestMethod.POST, classURL(Customer.class), params, Customer.class);
	}

	public static Customer retrieve(String id) throws StripeException {
		return request(RequestMethod.GET, instanceURL(Customer.class, id), null, Customer.class);
	}
	
	public static CustomerCollection all(Map<String, Object> params) throws StripeException {
		return request(RequestMethod.GET, classURL(Customer.class), params, CustomerCollection.class);
	}
	
	public Customer update(Map<String, Object> params) throws StripeException {
		return request(RequestMethod.POST, instanceURL(Customer.class, this.id), params, Customer.class);
	}
	
	public DeletedCustomer delete() throws StripeException { 
		return request(RequestMethod.DELETE, instanceURL(Customer.class, this.id), null, DeletedCustomer.class);
	}
	
	public Subscription updateSubscription(Map<String, Object> params) throws StripeException {
		return request(RequestMethod.POST,
				String.format("%s/subscription", instanceURL(Customer.class, this.id)),
				params, Subscription.class);
	}
	
	public Subscription cancelSubscription() throws StripeException {
		return cancelSubscription(null);
	}
	
	public Subscription cancelSubscription(Map<String, Object> params) throws StripeException {
		return request(RequestMethod.DELETE,
				String.format("%s/subscription", instanceURL(Customer.class, this.id)),
				params, Subscription.class);
	}

	public void deleteDiscount() throws StripeException {
		request(RequestMethod.DELETE,
			String.format("%s/discount", instanceURL(Customer.class, this.id)),
			null, Discount.class);
	}
}
