package com.loits.insurance.cm.models;

/*
 * This Class will create and handle the Image object and 
 * It contains Getters and Setters and Basic validations for setters    
 */

public class Image {
	
	private long id;
	private int intim_no;
	private int seq_no;
	private String image_date;
	private String myimage;
	private int img_no;
	private double  logitude;
	private double  latitude;
	private String	status;
	private int inspection_type;
	private String username;

	/*
	 * This variable keep the allowed maximum number of images per claim	
	 */
	//public static final int NO_OF_IMAGES = 15;
	public static final int NO_OF_IMAGES = 25;
	public static final int MIN_NO_OF_IMAGES = 1;
	
	/*
	 * This variable keep the Compression Quality of the bitmap	
	 * 0 	Minimum
	 * 100	Maximum
	 */
	public static final int COMPRESS_QUALITY = 50;
	public static final int VIEW_QUALITY = 50;

	public Image(String image){
		this.myimage = image;
	}
		
	/*
	 * Constructor 1
	 */
	
	public Image(int intim_no, int seq_no, String image, String image_date, int img_no,
			double logitude, double latitude, int inspection_type) {
		this.intim_no = intim_no ;
		this.seq_no = seq_no;
		this.image_date=image_date;
		this.myimage=image;
		this.img_no=img_no;
		this.logitude=logitude;
		this.latitude=latitude;
		this.status="A";
        this.inspection_type=inspection_type;
	}

	
	public Image(long id, int intim_no, int seq_no, String image, String image_date, int img_no,
			double logitude, double latitude, int inspection_type, String userName) {
		this.id = id;
		this.setIntim_no(intim_no) ;
		this.setSeq_no(seq_no);
		this.setImage_date(image_date);
		this.setMyimage(image);
		this.setImg_no(img_no);
		this.setLogitude(logitude);
		this.setLatitude(latitude);
		this.setStatus("A");
        this.setInspection_type(inspection_type);
		this.username = userName;
	}
	
	
	/*
	 * Getters
	 */
	public int getImg_no() {
		return img_no;
	}
	public double getLogitude() {
		return logitude;
	}
	public double getLatitude() {
		return latitude;
	}

	public String getMyimage() {
		return myimage;
	}
	public long getId() {
		return id;
	}
	
	public int getIntim_no() {
		return intim_no;
	}
	
	public int getSeq_no() {
		return seq_no;
	}
	public String getImage_date() {
		return image_date;
	}

	
	/*
	 * Setters
	 */

	public void setImg_no(int img_no) {
		this.img_no = img_no;
	}
	public void setLogitude(double logitude) {
		this.logitude = logitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	
	public void setMyimage(String myimage) {
		
		this.myimage = myimage;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public void setIntim_no(int intim_no) {
		this.intim_no = intim_no;
	}


	public void setSeq_no(int seq_no) {
		this.seq_no = seq_no;
	}
	public void setImage_date(String image_date) {
		this.image_date = image_date;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}

	public int getInspection_type() {
		return inspection_type;
	}

	public void setInspection_type(int inspection_type) {
		this.inspection_type = inspection_type;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
