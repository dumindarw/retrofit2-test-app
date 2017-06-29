package com.loits.insurance.cm.models;

/*
 * This Class will create and handle the policy object and 
 * It contains Getters and Setters and Basic validations for setters    
 */

public class Claim {

	private long id;
	private int intimationNo;
	private int claimNo;
	private int inspType;
	private String policyNo;
	private String vehicleNo;
	private String location;
	private String contactNo;
	private String callerName;
	private String yom;
	private double sumInsured;
	private double excessAmt;
	private String compFacility;
	private String siteOffer;
	private String inspDate;
	private double acr;
	//private double assMileage;
	private String callCenterRemark;
	private String assessorRemark;
	private String respondDate;
	private String user;
	private String cDate;
	private String status;
	/*private String SyncDate;*/
	private double preAccValue;
	private double baldTirePenalty;
	private double underInsPenalty;
	private String inspectionRemarks;
	private String specialRemarks;
	private double proffesionalFee;
	private double miles;
	private double rate;
	private double telephone;
	private int copies;
	private double price;
	private double photoCharges;
	private double otherCharges;
	private double totalCharges;
	private String reason;
	private String policeReport;
	private String investigateClaim;
	private double payableAmount;
	private double onsiteOfferAmount;
	private String repairCompleted;
	private String salvageReceived;
	private String settlementMethod;
	private int imagesCount;
    private String informDate;
    private String SyncDate;
	private String username;
	private int seqNo;

	public static final String KEY_FACILITY		= "comp_facility";
	public static final String KEY_SITE_OFFER	= "site_offer";
	public static final String KEY_INSP_DATE	= "insp_date";
	public static final String KEY_ACR			= "acr";
	//public static final String KEY_CLAIM_MIL	= "claim_mileage";
	public static final String KEY_ASS_MIL		= "assessor_mileage";
	public static final String KEY_CC_REM		= "call_center_remark";
	public static final String KEY_ASS_REM		= "assessor_remark";
	public static final String KEY_R_DATE 		= "respond_date";
	public static final String KEY_USER 		= "user";
	public static final String KEY_C_DATE 		= "c_date";
	public static final String KEY_STATUS 		= "status";
	public static final String KEY_SYNC_DATE 	= "sync_date";

	public static final String KEY_PRE_ACC_VAL  		= "pre_acc_val";
	public static final String KEY_BALD_TIRE_PENALTY 	= "bald_tire_penalty";
	public static final String KEY_UNDER_INS_PENALTY 	= "under_ins_penalty";
	public static final String KEY_INSP_REMARKS  		= "insp_remarks";
	public static final String KEY_SP_REMARKS			= "sp_remarks";
	public static final String KEY_PROFF_FEE 			= "proff_fee";
	public static final String KEY_MILES  				= "miles";
	public static final String KEY_RATE  				= "rate";
	public static final String KEY_TELEPHONE  			= "telephone";
	public static final String KEY_COPIES  				= "copies";
	public static final String KEY_PRICE  				= "price";
	public static final String KEY_PHOTO_CHARGES  		= "photo_charges";
	public static final String KEY_OTHER_CHARGES  		= "other_charges";
	public static final String KEY_TOTAL_CHARGES  		= "total_charges";
	public static final String KEY_REASON  				= "reason";
	public static final String KEY_POLICE_REPORT  		= "police_report";
	public static final String KEY_INVESTIGATE_CLAIM  	= "investigate_claim";
	public static final String KEY_PAYABLE_AMOUNT       = "payable_amont";
	public static final String KEY_ONSITE_OFFER_AMOUNT	= "onsite_offer_amount";
	public static final String KEY_REPAIR_COMPLETED		= "repair_completed";
	public static final String KEY_SALVAGE_RECEIVED		= "salvage_received";
	public static final String KEY_SETTLEMENT_METHOD	= "settlement_method";

	public Claim(int intimation_no, int inspType, String policyNo, String vehicleNo,
					   String location, String contactNo, String callerName, String yom,
					   double sumInsured, double excessAmt, String compFacility,
					   String siteOffer, String callCenterRemark, String status,
					   String informDate, String respondDate, int seqNo) {
		super();
		this.intimationNo = intimation_no;
		this.claimNo = 0;
		this.inspType = inspType;
		this.policyNo = policyNo;
		this.vehicleNo = vehicleNo;
		this.location = location;
		this.contactNo = contactNo;
		this.callerName = callerName;
		this.yom = yom;
		this.sumInsured = sumInsured;
		this.excessAmt = excessAmt;
		this.compFacility = compFacility;
		this.siteOffer = siteOffer;
		this.callCenterRemark = callCenterRemark;
		this.status =status;
		this.cDate =  informDate;
		this.respondDate = respondDate;
		this.seqNo = seqNo;
	}

	/*
	 * Constructor 1
	 */

	public Claim(long id, int intimation_no, int inspType,
				 String policyNo, String vehicleNo, String location,
				 String contactNo, String callerName, String yom, double sumInsured,
				 double excessAmt, String compFacility, String siteOffer,
				 String callCenterRemark, String assessorRemark, String respondDate,
				 String user, double preAccValue, double baldTirePenalty, double underInsPenalty,
				 String inspectionRemarks,
				 String specialRemarks,
				 double proffesionalFee,
				 double miles,
				 double rate,
				 /*double assMileage,*/
				 double telephone,
				 int copies,
				 double price,
				 double photoCharges,
				 double otherCharges,
				 double totalCharges,
				 String reason,
				 String policeReport,
				 String investigateClaim,
				 double payableAmount,
				 double onsiteOfferAmount,
				 String repairCompleted,
				 String salvageReceived,
				 String settlementMethod,
				 int seqNo) {
		super();
		this.id = id;
		this.intimationNo = intimation_no;
		this.claimNo = 0;
		this.inspType = inspType;
		this.policyNo = policyNo;
		this.vehicleNo = vehicleNo;
		this.location = location;
		this.contactNo = contactNo;
		this.callerName = callerName;
		this.yom = yom;
		this.sumInsured = sumInsured;
		this.excessAmt = excessAmt;
		this.compFacility = compFacility;
		this.siteOffer = siteOffer;
		this.inspDate = inspDate;
		this.acr = acr;
		//this.claimMileage = claimMilage;
		this.callCenterRemark = callCenterRemark;
		this.assessorRemark = assessorRemark;
		this.respondDate = respondDate;
		this.user = user;
		this.cDate = DateTimeHelper.now();
		this.status = "A";
		this.preAccValue = preAccValue;
		this.baldTirePenalty = baldTirePenalty;
		this.underInsPenalty = underInsPenalty;
		this.inspectionRemarks = inspectionRemarks;
		this.specialRemarks = specialRemarks;
		this.proffesionalFee = proffesionalFee;
		this.miles = miles;
		this.rate = rate;
		//this.assMileage = assMileage;
		this.telephone = telephone;
		this.copies = copies;
		this.price = price;
		this.photoCharges = photoCharges;
		this.otherCharges = otherCharges;
		this.totalCharges = totalCharges;
		this.reason = reason;
		this.policeReport = policeReport;
		this.investigateClaim = investigateClaim;
		this.payableAmount = payableAmount;
		this.onsiteOfferAmount = onsiteOfferAmount;
		this.repairCompleted = repairCompleted;
		this.salvageReceived = salvageReceived;
		this.settlementMethod = settlementMethod;
		this.seqNo = seqNo;
	}

	/*
	 * Constructor 2
	 */
	//23

	public Claim(long id, int intimation_no, int inspType,
				 String policyNo, String vehicleNo, String location,
				 String contactNo, String callerName, String yom, double sumInsured,
				 double excessAmt, String compFacility, String siteOffer, String inspDate, double acr,
				 String callCenterRemark, String assessorRemark, String status, String respondDate,
                 String cDate,
				 double preAccValue, double baldTirePenalty, double underInsPenalty,
				 String inspectionRemarks,
				 String specialRemarks,
				 double proffesionalFee,
				 double miles, double rate,
				 double telephone,
				 int copies,
				 double price,
				 double photoCharges,
				 double otherCharges,
				 double totalCharges,
				 String reason,
				 String policeReport,
				 String investigateClaim,
				 double payableAmount,
				 double onsiteOfferAmount,
				 String repairCompleted,
				 String salvageReceived,
				 String settlementMethod,
				 int noOfImages,
				 String username,
				 int seqNo) {
			super();
			this.id = id;
			this.intimationNo = intimation_no;
			this.claimNo = 0;
			this.inspType = inspType;
			this.policyNo = policyNo;
			this.vehicleNo = vehicleNo;
			this.location = location;
			this.contactNo = contactNo;
			this.callerName = callerName;
			this.yom = yom;
			this.sumInsured = sumInsured;
			this.excessAmt = excessAmt;
			this.compFacility = compFacility;
			this.siteOffer = siteOffer;
			this.inspDate = inspDate;
			this.acr = acr;
			//this.claimMileage = claimMileage;
			this.callCenterRemark = callCenterRemark;
			this.assessorRemark = assessorRemark;
			this.respondDate = respondDate;
			this.user = user;
			this.cDate = cDate;
			this.status = status;
			this.preAccValue = preAccValue;
			this.baldTirePenalty = baldTirePenalty;
			this.underInsPenalty = underInsPenalty;
			this.inspectionRemarks = inspectionRemarks;
			this.specialRemarks = specialRemarks;
			this.proffesionalFee = proffesionalFee;
			this.miles = miles;
			this.rate = rate;
			/*this.assMileage = assMileage;*/
			this.telephone = telephone;
			this.copies = copies;
			this.price = price;
			this.photoCharges = photoCharges;
			this.otherCharges = otherCharges;
			this.totalCharges = totalCharges;
			this.reason = reason;
			this.policeReport = policeReport;
			this.investigateClaim = investigateClaim;
			this.payableAmount = payableAmount;
			this.onsiteOfferAmount = onsiteOfferAmount;
			this.repairCompleted = repairCompleted;
			this.salvageReceived = salvageReceived;
			this.settlementMethod = settlementMethod;
			this.imagesCount = noOfImages;
			this.username = username;
			this.seqNo = seqNo;
	}

	/*
	 * Constructor 3
	 */


	public Claim(int intimation_no, int inspType, String policyNo,
                 String vehicleNo, String location, String contactNo,
                 String callerName, String yom, double sumInsured, double excessAmt,
                 String compFacility, String siteOffer, String callCenterRemark,
                 String respondDate, int seqNo) {
		super();
		this.id = 0;
		this.intimationNo = intimation_no;
		this.claimNo = 0;
		this.inspType = inspType;
		this.policyNo = policyNo;
		this.vehicleNo = vehicleNo;
		this.location = location;
		this.contactNo = contactNo;
		this.callerName = callerName;
		this.yom = yom;
		this.sumInsured = sumInsured;
		this.excessAmt = excessAmt;
		this.compFacility = compFacility;
		this.siteOffer = siteOffer;
		this.inspDate = null;
		this.acr = -1;
		/*this.assMileage = -1;*/
		this.callCenterRemark = callCenterRemark;
		this.assessorRemark = null;
		this.respondDate = respondDate;
		this.cDate = DateTimeHelper.now();
		this.status = "A"; // New
		this.preAccValue = -1;
		this.baldTirePenalty = -1;
		this.underInsPenalty = -1;
		this.proffesionalFee = -1;
		this.miles = -1;
		this.rate = -1;
		//this.assMileage = -1;
		this.copies = -1;
		this.price = -1;
		this.photoCharges = -1;
		this.otherCharges = -1;
		this.totalCharges = -1;
		this.payableAmount = -1;
		this.onsiteOfferAmount = -1;
		this.salvageReceived = "";
		this.repairCompleted = "";
		this.policeReport = "";
		this.investigateClaim = "";
		this.settlementMethod = "";
		this.seqNo = seqNo;

	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(int intimation_no) {
		this.intimationNo = intimation_no;
	}

	public int getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(int claimNo) {
		this.claimNo = claimNo;
	}

	public int getInspType() {
		return inspType;
	}

	public void setInspType(int inspType) {
		this.inspType = inspType;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getCallerName() {
		return callerName;
	}

	public void setCallerName(String callerName) {
		this.callerName = callerName;
	}

	public String getYom() {
		return yom;
	}

	public void setYom(String yom) {
		this.yom = yom;
	}

	public double getSumInsured() {
		return sumInsured;
	}

	public void setSumInsured(double sumInsured) {
		this.sumInsured = sumInsured;
	}

	public double getExcessAmt() {
		return excessAmt;
	}

	public void setExcessAmt(double excessAmt) {
		this.excessAmt = excessAmt;
	}

	public String getCompFacility() {
		return compFacility;
	}

	public void setCompFacility(String compFacility) {
		this.compFacility = compFacility;
	}

	public String getSiteOffer() {
		return siteOffer;
	}

	public void setSiteOffer(String siteOffer) {
		this.siteOffer = siteOffer;
	}

	public String getInspDate() {
		return inspDate;
	}

	public void setInspDate(String inspDate) {
		this.inspDate = inspDate;
	}

	public double getAcr() {
		return acr;
	}

	public void setAcr(double acr) {
		this.acr = acr;
	}

	/*public double getAssMileage() {
		return assMileage;
	}

	public void setAssMileage(double assMileage) {
		this.assMileage = assMileage;
	}*/

	public String getCallCenterRemark() {
		return callCenterRemark;
	}

	public void setCallCenterRemark(String callCenterRemark) {
		this.callCenterRemark = callCenterRemark;
	}

	public String getAssessorRemark() {
		return assessorRemark;
	}

	public void setAssessorRemark(String assessorRemark) {
		this.assessorRemark = assessorRemark;
	}

	public String getRespondDate() {
		return respondDate;
	}

	public void setRespondDate(String respondDate) {
		this.respondDate = respondDate;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getcDate() {
		return cDate;
	}

	public void setcDate(String cDate) {
		this.cDate = cDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSyncDate() {
		return SyncDate;
	}

	public void setSyncDate(String syncDate) {
		SyncDate = syncDate;
	}

	public double getPreAccValue() {
		return preAccValue;
	}

	public void setPreAccValue(double preAccValue) {
		this.preAccValue = preAccValue;
	}

	public double getBaldTirePenalty() {
		return baldTirePenalty;
	}

	public void setBaldTirePenalty(double baldTirePenalty) {
		this.baldTirePenalty = baldTirePenalty;
	}

	public double getUnderInsPenalty() {
		return underInsPenalty;
	}

	public void setUnderInsPenalty(double underInsPenalty) {
		this.underInsPenalty = underInsPenalty;
	}

	public String getInspectionRemarks() {
		return inspectionRemarks;
	}

	public void setInspectionRemarks(String inspectionRemarks) {
		this.inspectionRemarks = inspectionRemarks;
	}

	public String getSpecialRemarks() {
		return specialRemarks;
	}

	public void setSpecialRemarks(String specialRemarks) {
		this.specialRemarks = specialRemarks;
	}

	public double getProffesionalFee() {
		return proffesionalFee;
	}

	public void setProffesionalFee(double proffesionalFee) {
		this.proffesionalFee = proffesionalFee;
	}

	public double getMiles() {
		return miles;
	}

	public void setMiles(double miles) {
		this.miles = miles;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public double getTelephone() {
		return telephone;
	}

	public void setTelephone(double telephone) {
		this.telephone = telephone;
	}

	public int getCopies() {
		return copies;
	}

	public void setCopies(int copies) {
		this.copies = copies;
	}

	/*public double getClaimMileage() {
		return claimMileage;
	}

	public void setClaimMileage(double claimMileage) {
		this.claimMileage = claimMileage;
	}*/

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getPhotoCharges() {
		return photoCharges;
	}

	public void setPhotoCharges(double photoCharges) {
		this.photoCharges = photoCharges;
	}

	public double getOtherCharges() {
		return otherCharges;
	}

	public void setOtherCharges(double otherCharges) {
		this.otherCharges = otherCharges;
	}

	public double getTotalCharges() {
		return totalCharges;
	}

	public void setTotalCharges(double totalCharges) {
		this.totalCharges = totalCharges;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getPoliceReport() {
		return policeReport;
	}

	public void setPoliceReport(String policeReport) {
		this.policeReport = policeReport;
	}

	public String getInvestigateClaim() {
		return investigateClaim;
	}

	public void setInvestigateClaim(String investigateClaim) {
		this.investigateClaim = investigateClaim;
	}

	public double getPayableAmount() {
		return payableAmount;
	}

	public void setPayableAmount(double payableAmount) {
		this.payableAmount = payableAmount;
	}

	public double getOnsiteOfferAmount() {
		return onsiteOfferAmount;
	}

	public void setOnsiteOfferAmount(double onsiteOfferAmount) {
		this.onsiteOfferAmount = onsiteOfferAmount;
	}

	public String getRepairCompleted() {
		return repairCompleted;
	}

	public void setRepairCompleted(String repairCompleted) {
		this.repairCompleted = repairCompleted;
	}

	public String getSalvageReceived() {
		return salvageReceived;
	}

	public void setSalvageReceived(String salvageReceived) {
		this.salvageReceived = salvageReceived;
	}

	public String getSettlementMethod() {
		return settlementMethod;
	}

	public void setSettlementMethod(String settlementMethod) {
		this.settlementMethod = settlementMethod;
	}

	public int getImagesCount() {
		return imagesCount;
	}

	public void setImagesCount(int imagesCount) {
		this.imagesCount = imagesCount;
	}

    public String getInformDate() {
        return informDate;
    }

    public void setInformDate(String informDate) {
        this.informDate = informDate;
    }

	public int getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}
}
