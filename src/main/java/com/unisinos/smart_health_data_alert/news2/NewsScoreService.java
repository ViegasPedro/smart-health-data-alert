package com.unisinos.smart_health_data_alert.news2;

import com.unisinos.smart_health_data_alert.vital_sign.model.InvalidVitalSignValueException;
import com.unisinos.smart_health_data_alert.vital_sign.model.VitalSign;

public class NewsScoreService {
	
	public static final int NORMAL_SCORE = 0;
	public static final int SINGLE_SCORE_LIMIT = 3;
	public static final int MULTI_SCORE_LIMIT = 5;
		
	public static int calculateNewsScore(VitalSign vitalSign) {
	    switch (vitalSign.getType()) {
	        case TEMPERATURE:
	            return getTemperatureScore(Double.valueOf(vitalSign.getValue()));
	        case HEARTRATE:
	            return getHeartRateScore(Integer.valueOf(vitalSign.getValue()));
	        case BLOODPRESSURE:
	            return getBloodPressureScore(Integer.valueOf(vitalSign.getValue()));
	        case OXYGEN:
	            return getOxygenScore(vitalSign.getValue());
	        case CONSCIOUSNESS:
	            return getConsciousnessScore(vitalSign.getValue());
	        case RESPIRATIONRATE:
	            return getRespirationRateScore(Integer.valueOf(vitalSign.getValue()));
	        case SPO2:
	            return getSpo2Score(Integer.valueOf(vitalSign.getValue()));
	        default:
	            throw new InvalidVitalSignValueException();
	    }
	}
	
	private static int getTemperatureScore(double temperature) {
		if (temperature <= 35.0) {
            return 3;
        } else if (temperature <= 36.0) {
            return 1;
        } else if (temperature <= 38.0) {
            return 0;
        } else if (temperature <= 39.0) {
            return 1;
        } else {
            return 2;
        }
    }

	private static int getHeartRateScore(int heartRate) {
		if (heartRate <= 40) {
            return 3;
        } else if (heartRate <= 50) {
            return 1;
        } else if (heartRate <= 90) {
            return 0;
        } else if (heartRate <= 110) {
            return 1;
        } else if (heartRate <= 130) {
            return 2;
        } else {
            return 3;
        }
    }

	private static int getBloodPressureScore(int systolicBP) {
        if (systolicBP <= 90) {
            return 3;
        } else if (systolicBP <= 100) {
            return 2;
        } else if (systolicBP <= 110) {
            return 1;
        } else if (systolicBP <= 219) {
            return 0;
        } else {
            return 3;
        }
    }
	
	private static int getOxygenScore(String oxygen) {
        if (oxygen.equalsIgnoreCase("air"))
        	return 0;
        if (oxygen.equalsIgnoreCase("oxygen"))
        	return 2;
        throw new InvalidVitalSignValueException();
    }
	
	private static int getConsciousnessScore(String oxygen) {
        if (oxygen.equalsIgnoreCase("alert"))
        	return 0;
        if (oxygen.equalsIgnoreCase("cvpu"))
        	return 3;
        throw new InvalidVitalSignValueException();
    }
	
	private static int getRespirationRateScore(int respirationRate) {
		if (respirationRate <= 8) 
			return 3;
		if (respirationRate >= 9 || respirationRate <= 11) 
			return 1;
		if (respirationRate >= 12 || respirationRate <= 20)
			return 0;
		if (respirationRate >= 21 || respirationRate <= 24)
			return 2;
		if (respirationRate <= 25) 
			return 3;
		throw new InvalidVitalSignValueException();
	}
	
	private static int getSpo2Score(int spo2) {
		if (spo2 <= 91) 
			return 3;
		if (spo2 == 92 || spo2 == 93) 
			return 2;
		if (spo2 == 94 || spo2 == 95) 
			return 1;
		if (spo2 >= 96)
			return 0;
		throw new InvalidVitalSignValueException();
	}

}
