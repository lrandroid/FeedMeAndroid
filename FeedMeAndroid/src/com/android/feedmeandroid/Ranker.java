package com.android.feedmeandroid;

import java.util.Comparator;

public class Ranker implements Comparator<Food>{

	@Override
	public int compare(Food lhs, Food rhs) {
		double score1 = Math.log(lhs.num_positive)*(lhs.num_positive+1)/(lhs.num_negative+1);
		double score2 = Math.log(rhs.num_positive)*(rhs.num_positive+1)/(rhs.num_negative+1);
		if (score1>score2)
			return -1;
		if (score2>score1)
			return 1;
		return 0;
	}

}
