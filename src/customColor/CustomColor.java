package customColor;

import java.awt.Color;

public enum CustomColor {
	NavyBlue(new Color(0x303841)),GreyishBlue(new Color(0x47555E)),LightBlue(new Color(0x7AA5D2)),Grey(new Color(0xEEEEEE));
	
	public Color color;
	CustomColor(Color c){
		this.color = c;
	}
	
	public Color returnColor(){
		return color;
	}
}
