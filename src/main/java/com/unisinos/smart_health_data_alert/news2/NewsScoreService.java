package com.unisinos.smart_health_data_alert.news2;

import com.unisinos.smart_health_data_alert.vital_sign.model.VitalSign;
import com.unisinos.smart_health_data_alert.vital_sign.model.VitalSignType;

public class NewsScoreService {
	
	public static final int NORMAL_SCORE = 0;
	public static final int SINGLE_SCORE_LIMIT = 3;
	public static final int MULTI_SCORE_LIMIT = 5;
	
	public static int calculateNewsScore(VitalSign vitalSign) {
		if (vitalSign.getType() == VitalSignType.TEMPERATURE) {
			return getTemperatureScore(Integer.valueOf(vitalSign.getValue()));
		}
		//TODO fazer para todos os tipos de sinal vital
		return 0;
	}
	
	//TODO valores absurdos devem jogar exception
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

}
