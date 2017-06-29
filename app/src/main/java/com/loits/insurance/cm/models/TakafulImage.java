package com.loits.insurance.cm.models;

/*
 * This Class will create and handle the policy object and 
 * It contains Getters and Setters and Basic validations for setters    
 */

public class TakafulImage {

	int intimation_no;
    int inspection_type;
	int seq_no;
	String img_date;
	int image_no;
	String image;
	double longitude;
	double latitude;

    public TakafulImage(int intimation_no, int inspection_type, int seq_no, String img_date, int image_no, String image, double longitude, double latitude) {
        this.intimation_no = intimation_no;
        this.inspection_type = inspection_type;
        this.seq_no = seq_no;
        this.img_date = img_date;
        this.image_no = image_no;
        this.image = image;
        this.longitude = longitude;
        this.latitude = latitude;
    }

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getIntimation_no() {
		return intimation_no;
	}

	public void setIntimation_no(int intimation_no) {
		this.intimation_no = intimation_no;
	}

	public int getInspection_type() {
		return inspection_type;
	}

	public void setInspection_type(int inspection_type) {
		this.inspection_type = inspection_type;
	}

	public int getSeq_no() {
		return seq_no;
	}

	public void setSeq_no(int seq_no) {
		this.seq_no = seq_no;
	}

	public String getImg_date() {
		return img_date;
	}

	public void setImg_date(String img_date) {
		this.img_date = img_date;
	}

	public int getImage_no() {
		return image_no;
	}

	public void setImage_no(int image_no) {
		this.image_no = image_no;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
}
