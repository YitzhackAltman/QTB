package org.example.User;

public abstract class Calc{

    public static double calculatePresentAnsweredUsers(int Amount_Answered_Users, int Amount_Total_Users) {
        return ((double) Amount_Answered_Users / (double)Amount_Total_Users) * 100;
    }

    public static double calculatePresentEachQuestion(int count, int total_answers) {
        return 0;
    }
}
