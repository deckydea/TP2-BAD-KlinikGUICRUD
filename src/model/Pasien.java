package model;

import java.util.Date;

public class Pasien {
	int id;
    String name;
    String address;
    String nik;
    Date dateOfBirth;
   
	public Pasien(int id, String name, String address, String nik, Date dateOfBirth) {
		this.id = id;
		this.name = name;
        this.address = address;
        this.nik = nik;
        this.dateOfBirth = dateOfBirth;
	}
	
	public Pasien(String name, String address, String nik, Date dateOfBirth) {
		this.name = name;
        this.address = address;
        this.nik = nik;
        this.dateOfBirth = dateOfBirth;
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
        return name;
    }
    
    public String getAddress() {
        return address;
    }
    
    public String getNik() {
        return nik;
    }
    
    public Date getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public void setNik(String nik) {
        this.nik = nik;
    }
    
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
