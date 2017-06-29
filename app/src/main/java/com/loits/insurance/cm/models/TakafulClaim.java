package com.loits.insurance.cm.models;

/*
 * This Class will create and handle the policy object and 
 * It contains Getters and Setters and Basic validations for setters    
 */

public class TakafulClaim {

	double assessor_mileage;
    String	policy_no;
	String	respond_date;
	String	reason;
	String	assessor_remark;
	String	location;
	String	police_report;
	double	other_charges;
	double	rate;
	double	onsite_offer_amount;
	double	seq_no;
	double	payable_amont;
	String	settlement_method;
	double	bald_tire_penalty;
	double	sum_insured;
	String	caller_name;
	String	repair_completed;
	double	excess_amt;
	int	total_images;
	String	yom;
	String	status;
	String	contact_no;
	String	sp_remarks;
	String	call_center_remark;
	double photo_charges;
	double proff_fee;
	String	salvage_received;
	String	investigate_claim;
	double	miles;
	String	c_date;
	double	pre_acc_val;
	String	insp_date;
	int	intimation_no;
	int	copies;
	double price;
	int	insp_type;
	double under_ins_penalty;
	String insp_remarks;
	String vehicle_no;
	double telephone;
	String user;
	double total_charges;
	double acr;

    public TakafulClaim(double excess_amt, double assessor_mileage, String policy_no, String respond_date, String reason, String assessor_remark, String location, String police_report, double other_charges, double rate, double onsite_offer_amount, double seq_no,
						double payable_amont, String settlement_method, double bald_tire_penalty, double sum_insured, String caller_name, String repair_completed, int total_images, String yom, String status, String contact_no, String sp_remarks, String call_center_remark, double photo_charges, double proff_fee, String salvage_received, String investigate_claim, double miles, String c_date, double pre_acc_val, String insp_date, int intimation_no, int copies, double price, int insp_type, double under_ins_penalty, String insp_remarks, String vehicle_no, double telephone, String user, double total_charges, double acr) {
        this.excess_amt = excess_amt;
        this.assessor_mileage = assessor_mileage;
        this.policy_no = policy_no;
        this.respond_date = respond_date;
        this.reason = reason;
        this.assessor_remark = assessor_remark;
        this.location = location;
        this.police_report = police_report;
        this.other_charges = other_charges;
        this.rate = rate;
        this.onsite_offer_amount = onsite_offer_amount;
        this.seq_no = seq_no;
        this.payable_amont = payable_amont;
        this.settlement_method = settlement_method;
        this.bald_tire_penalty = bald_tire_penalty;
        this.sum_insured = sum_insured;
        this.caller_name = caller_name;
        this.repair_completed = repair_completed;
        this.total_images = total_images;
        this.yom = yom;
        this.status = status;
        this.contact_no = contact_no;
        this.sp_remarks = sp_remarks;
        this.call_center_remark = call_center_remark;
        this.photo_charges = photo_charges;
        this.proff_fee = proff_fee;
        this.salvage_received = salvage_received;
        this.investigate_claim = investigate_claim;
        this.miles = miles;
        this.c_date = c_date;
        this.pre_acc_val = pre_acc_val;
        this.insp_date = insp_date;
        this.intimation_no = intimation_no;
        this.copies = copies;
        this.price = price;
        this.insp_type = insp_type;
        this.under_ins_penalty = under_ins_penalty;
        this.insp_remarks = insp_remarks;
        this.vehicle_no = vehicle_no;
        this.telephone = telephone;
        this.user = user;
        this.total_charges = total_charges;
        this.acr = acr;
    }

	public String getRepair_completed() {
		return repair_completed;
	}

	public void setRepair_completed(String repair_completed) {
		this.repair_completed = repair_completed;
	}

	public double getAssessor_mileage() {
		return assessor_mileage;
	}

	public void setAssessor_mileage(double assessor_mileage) {
		this.assessor_mileage = assessor_mileage;
	}

	public String getPolicy_no() {
		return policy_no;
	}

	public void setPolicy_no(String policy_no) {
		this.policy_no = policy_no;
	}

	public String getRespond_date() {
		return respond_date;
	}

	public void setRespond_date(String respond_date) {
		this.respond_date = respond_date;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getAssessor_remark() {
		return assessor_remark;
	}

	public void setAssessor_remark(String assessor_remark) {
		this.assessor_remark = assessor_remark;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getPolice_report() {
		return police_report;
	}

	public void setPolice_report(String police_report) {
		this.police_report = police_report;
	}

	public double getOther_charges() {
		return other_charges;
	}

	public void setOther_charges(double other_charges) {
		this.other_charges = other_charges;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public double getOnsite_offer_amount() {
		return onsite_offer_amount;
	}

	public void setOnsite_offer_amount(double onsite_offer_amount) {
		this.onsite_offer_amount = onsite_offer_amount;
	}

	public double getSeq_no() {
		return seq_no;
	}

	public void setSeq_no(double seq_no) {
		this.seq_no = seq_no;
	}

	public double getPayable_amont() {
		return payable_amont;
	}

	public void setPayable_amont(double payable_amont) {
		this.payable_amont = payable_amont;
	}

	public String getSettlement_method() {
		return settlement_method;
	}

	public void setSettlement_method(String settlement_method) {
		this.settlement_method = settlement_method;
	}

	public double getBald_tire_penalty() {
		return bald_tire_penalty;
	}

	public void setBald_tire_penalty(double bald_tire_penalty) {
		this.bald_tire_penalty = bald_tire_penalty;
	}

	public double getSum_insured() {
		return sum_insured;
	}

	public void setSum_insured(double sum_insured) {
		this.sum_insured = sum_insured;
	}

	public String getCaller_name() {
		return caller_name;
	}

	public void setCaller_name(String caller_name) {
		this.caller_name = caller_name;
	}

	public double getExcess_amt() {
		return excess_amt;
	}

	public void setExcess_amt(double excess_amt) {
		this.excess_amt = excess_amt;
	}

	public int getTotal_images() {
		return total_images;
	}

	public void setTotal_images(int total_images) {
		this.total_images = total_images;
	}

	public String getYom() {
		return yom;
	}

	public void setYom(String yom) {
		this.yom = yom;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getContact_no() {
		return contact_no;
	}

	public void setContact_no(String contact_no) {
		this.contact_no = contact_no;
	}

	public String getSp_remarks() {
		return sp_remarks;
	}

	public void setSp_remarks(String sp_remarks) {
		this.sp_remarks = sp_remarks;
	}

	public String getCall_center_remark() {
		return call_center_remark;
	}

	public void setCall_center_remark(String call_center_remark) {
		this.call_center_remark = call_center_remark;
	}

	public double getPhoto_charges() {
		return photo_charges;
	}

	public void setPhoto_charges(double photo_charges) {
		this.photo_charges = photo_charges;
	}

	public double getProff_fee() {
		return proff_fee;
	}

	public void setProff_fee(double proff_fee) {
		this.proff_fee = proff_fee;
	}

	public String getSalvage_received() {
		return salvage_received;
	}

	public void setSalvage_received(String salvage_received) {
		this.salvage_received = salvage_received;
	}

	public String getInvestigate_claim() {
		return investigate_claim;
	}

	public void setInvestigate_claim(String investigate_claim) {
		this.investigate_claim = investigate_claim;
	}

	public double getMiles() {
		return miles;
	}

	public void setMiles(double miles) {
		this.miles = miles;
	}

	public String getC_date() {
		return c_date;
	}

	public void setC_date(String c_date) {
		this.c_date = c_date;
	}

	public double getPre_acc_val() {
		return pre_acc_val;
	}

	public void setPre_acc_val(double pre_acc_val) {
		this.pre_acc_val = pre_acc_val;
	}

	public String getInsp_date() {
		return insp_date;
	}

	public void setInsp_date(String insp_date) {
		this.insp_date = insp_date;
	}

	public int getIntimation_no() {
		return intimation_no;
	}

	public void setIntimation_no(int intimation_no) {
		this.intimation_no = intimation_no;
	}

	public int getCopies() {
		return copies;
	}

	public void setCopies(int copies) {
		this.copies = copies;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getInsp_type() {
		return insp_type;
	}

	public void setInsp_type(int insp_type) {
		this.insp_type = insp_type;
	}

	public double getUnder_ins_penalty() {
		return under_ins_penalty;
	}

	public void setUnder_ins_penalty(double under_ins_penalty) {
		this.under_ins_penalty = under_ins_penalty;
	}

	public String getInsp_remarks() {
		return insp_remarks;
	}

	public void setInsp_remarks(String insp_remarks) {
		this.insp_remarks = insp_remarks;
	}

	public String getVehicle_no() {
		return vehicle_no;
	}

	public void setVehicle_no(String vehicle_no) {
		this.vehicle_no = vehicle_no;
	}

	public double getTelephone() {
		return telephone;
	}

	public void setTelephone(double telephone) {
		this.telephone = telephone;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public double getTotal_charges() {
		return total_charges;
	}

	public void setTotal_charges(double total_charges) {
		this.total_charges = total_charges;
	}

	public double getAcr() {
		return acr;
	}

	public void setAcr(double acr) {
		this.acr = acr;
	}

}
