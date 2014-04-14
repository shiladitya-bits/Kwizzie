package com.kwizzie.model;

import android.os.Parcel;
import android.os.Parcelable;

public class QuestionCategory  implements Parcelable{

    private String categoryCode;
    private String categoryName;
    
    public QuestionCategory(){ }
    
    public QuestionCategory(String categoryCode, String categoryName){
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
    }
    
    public QuestionCategory(Parcel source) {
    	this.categoryCode = source.readString();
    	this.categoryName = source.readString();
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
	public static final Parcelable.Creator<QuestionCategory> CREATOR = new Parcelable.Creator<QuestionCategory>() {

		@Override
		public QuestionCategory createFromParcel(Parcel source) {
			return new QuestionCategory(source);
		}

		@Override
		public QuestionCategory[] newArray(int size) {
			return new QuestionCategory[size];
		}
	};
	
}