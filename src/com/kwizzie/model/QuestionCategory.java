package com.kwizzie.model;

import android.os.Parcel;
import android.os.Parcelable;

public class QuestionCategory  implements Parcelable{

    private String categoryCode;
    private String categoryName;
    
    public QuestionCategory(){
        
    }
    
    public QuestionCategory(String categoryCode, String categoryName){
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
    }
    
    public String getCategoryCode() {
        return categoryCode;
    }
    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }
    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

	@Override
	public int describeContents() {
		return hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(categoryCode);
		dest.writeString(categoryName);
		
	}
}