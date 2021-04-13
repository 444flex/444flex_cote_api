package com.flex.api.model;

public class Solution {
	public int[] solution(int[] lottos, int[] win_nums) {
		int[] answer = new int[2];
        int count = 0;
        int zeroCount = 0;
        
        for (int i=0; i<lottos.length; i++) {
        	for (int win_num : win_nums) {
        		if (lottos[i] == 0) {
        			zeroCount++;
        			break;
        		}
        		if (lottos[i] == win_num) {
        			count++;
        		}
        	}
        }
        if (count < 2) answer[1] = 6;
        else answer[1] = 7 - count;
        if (count == 0 && zeroCount == 0) answer[0] = 6;
        else answer[0] = 7 - count - zeroCount;
        
        return answer;
	}
	
}
